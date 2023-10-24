package my.project.providers.schema;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonException;
import jakarta.json.JsonObject;
import jakarta.ws.rs.core.UriBuilder;

import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import com.networknt.schema.uri.ClasspathURLFetcher;
import com.networknt.schema.uri.URIFetcher;

/**
 * JSON schema validator using <a href="https://github.com/networknt/json-schema-validator">networknt/json-schema-validator</a>
 *
 * <p>
 * Supports <a href="https://json-schema.org/draft/2019-09/release-notes.html">JSON Schema draft-2019-09</a>
 * </p>
 */
@Dependent
public class JsonSchemaValidator {

    private static final Logger LOGGER = LogManager.getLogger(JsonSchemaValidator.class);

    /**
     * JSON schema validator
     */
    private static final JsonSchemaFactory VALIDATOR;

    /**
     * Initialize the schema validator with an overridden {@link URIFetcher}
     */
    static {
        final JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V201909);

        // The validator attempts to follow the URLs of the schema IDs for references, this will fetch
        // the schemas from the resources instead
        final URIFetcher uriFactory = new ResourceURIFetcher();
        VALIDATOR = JsonSchemaFactory.builder(factory)
                .uriFetcher(uriFactory, ResourceURIFetcher.SUPPORTED_SCHEMES).build();
    }

    private final ObjectMapper mapper;

    /**
     * CDI constructor
     * @param mapper the {@link ObjectMapper}
     */
    @Inject
    public JsonSchemaValidator(final ObjectMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * Get the schema identifier from the {@link Schema} annotation, if it exists
     * @param clazz the class to get the schema from
     * @return the schema identifier
     */
    private Optional<String> getSchemaId(final Class<?> clazz) {
        if (clazz.isAnnotationPresent(Schema.class)) {
            final Schema schema = clazz.getAnnotation(Schema.class);
            if (schema != null && !schema.ignore()) {
                return Optional.of(schema.value());
            }
        }
        else {
            LOGGER.debug("No schema specified for class {}, JSON validation bypassed.", clazz.getSimpleName());
        }
        return Optional.empty();
    }

    /**
     * Validates a given string of JSON against a schema associated to a class
     *
     * <p>
     * This validates against the schema of the given class as if it was an array of that schema type.
     * </p>
     *
     * <p>
     * If the given class does not specify a schema via the annotation {@link Schema}, validation will be skipped.
     * </p>
     *
     * @param json the json string to validate
     * @param clazz the class of the schema to validate against
     * @throws JsonException when the json is invalid
     */
    public void validateList(final String json, final Class<?> clazz) {
        getSchemaId(clazz).ifPresent(s -> validateList(s, json));
    }

    /**
     * Validates a given string of JSON against a schema
     *
     * <p>
     * This validates against the given schema as if it was an array of that schema type.
     * </p>
     *
     * @param schemaId the id of the schema to validate against
     * @param json the json string to validate
     * @throws JsonException when the json is invalid
     */
    public void validateList(final String schemaId, final String json) {
        try {
            // Build a schema representing an array of the given schema
            final JsonObject schemaJson = Json.createObjectBuilder() //
                    .add("type", "array") //
                    .add("items", Json.createObjectBuilder().add("$ref", schemaId)) //
                    .build();

            // Create the schema object to validate against
            final JsonSchema schema = VALIDATOR.getSchema(schemaJson.toString());

            // Validate against the schema
            validate(schemaId, schema, json);
        }
        catch (Exception e) {
            throw new JsonException("Error validating JSON", e);
        }
    }

    /**
     * Validates a given string of JSON against a schema associated to a class
     *
     * <p>
     * If the given class does not specify a schema via the annotation {@link Schema}, validation will be skipped.
     * </p>
     *
     * @param json the json string to validate
     * @param clazz the class of the schema to validate against
     * @throws JsonException when the json is invalid
     */
    public void validate(final String json, final Class<?> clazz) {
        getSchemaId(clazz).ifPresent(s -> validate(s, json));
    }

    /**
     * Validates a given string of JSON against a schema
     *
     * @param schemaId the id of the schema to validate against
     * @param json the json string to validate
     * @throws JsonException when the json is invalid
     */
    public void validate(final String schemaId, final String json) {
        try {
            // Create the schema object to validate against
            final URI schemaUri = new URI(schemaId);
            final JsonSchema schema = VALIDATOR.getSchema(schemaUri);

            // Validate against the schema
            validate(schemaId, schema, json);
        }
        catch (Exception e) {
            throw new JsonException("Error validating JSON", e);
        }
    }

    /**
     * Validates the given string of JSON against the schema
     *
     * @param schemaName the name of the schema
     * @param schema the schema to validate against
     * @param json the json string to validate
     * @throws JsonProcessingException exception
     * @throws JsonMappingException exception
     * @throws JsonException when the json is invalid
     */
    private void validate(final String schemaName, final JsonSchema schema, final String json)
            throws JsonMappingException, JsonProcessingException {
        final JsonNode jsonNode = mapper.readTree(json);
        final Set<ValidationMessage> errors = schema.validate(jsonNode);
        if (!errors.isEmpty()) {
            final StringBuilder sb = new StringBuilder().append("JSON invalid for schema \"").append(schemaName)
                    .append("\"").append(System.lineSeparator());
            for (ValidationMessage message : errors) {
                sb.append("\t").append(message.toString()).append(System.lineSeparator());
            }
            throw new JsonException(sb.toString());
        }
    }

    /**
     * {@link URIFetcher} to get the schemas from resources.
     */
    private static class ResourceURIFetcher implements URIFetcher {

        private static final List<String> SUPPORTED_SCHEMES = List.of("http", "https");

        @Override
        public InputStream fetch(final URI uri) throws IOException {
            final URIFetcher resourceFetcher = new ClasspathURLFetcher();
            final String filename = FilenameUtils.getName(uri.getPath());
            final URI resource = UriBuilder.fromPath(filename).scheme("resource").build();
            return resourceFetcher.fetch(resource);
        }
    }
}