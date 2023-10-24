package my.project.providers.schema;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.function.Function;

import jakarta.inject.Inject;
import jakarta.json.JsonException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.ext.Provider;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jakarta.rs.json.JacksonJsonProvider;
import com.google.json.JsonSanitizer;

/**
 * An extension providing extra processing to JAX-RS JSON inputs/outputs. This provider with sanitize
 * the input stream, check for valid schema, and return the expected POJO created by Jackson.
 *
 * @see JacksonJsonProvider
 * @see JsonSanitizer
 * @see JsonSchemaValidator
 */
@Provider
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class JsonSchemaProvider extends JacksonJsonProvider {

    private static final Logger LOGGER = LogManager.getLogger(JsonSchemaProvider.class);

    private static final Charset CHARSET = Charset.defaultCharset();

    private static final Function<String, String> SANITIZE = JsonSanitizer::sanitize;

    @Inject
    ObjectMapper mapper;

    @Inject
    JsonSchemaValidator validator;

    @Override
    public Object readFrom(final Class<Object> type, final Type genericType, final Annotation[] annotations,
            final MediaType mediaType, final MultivaluedMap<String, String> httpHeaders, final InputStream entityStream)
            throws IOException {
        LOGGER.trace("Providing json schema validation to JAX-RS resource.");
        
        try {
            final String json = IOUtils.toString(entityStream, CHARSET);
            final String sanitizedJson = SANITIZE.apply(json);
            validator.validate(sanitizedJson, type);
            return mapper.readValue(sanitizedJson, type);
        }
        catch (IOException e) {
            throw new JsonException("Error reading json from stream as value: " + type.getSimpleName(), e);
        }
    }

    @Override
    public void writeTo(final Object value, final Class<?> type, final Type genericType, final Annotation[] annotations,
            final MediaType mediaType, final MultivaluedMap<String, Object> httpHeaders,
            final OutputStream entityStream) throws IOException {
        LOGGER.trace("Providing json schema validation to JAX-RS resource.");

        try {
            final String json = mapper.writeValueAsString(value);
            final String sanitizedJson = SANITIZE.apply(json);
            validator.validate(sanitizedJson, value.getClass());
            IOUtils.write(sanitizedJson, entityStream, CHARSET);
        }
        catch (IOException e) {
            throw new JsonException("Error writing json to stream", e);
        }
    }
}