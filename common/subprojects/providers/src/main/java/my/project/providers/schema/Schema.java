package my.project.providers.schema;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to declare the JSON Schema file this class will be validated against. This ensures
 * Jackson dto models and the corresponding JSON Schema stay *NSYNC like its 1999.
 *
 * <p>The schema provider implementation will attempt to load the annotation value from the classpath.
 * During deserialization, the provider will compare the schema to the target Jackson data model
 * class. The below example would shows a resource endpoint that accepts a class annotation with a
 * schema. This request will only succeed if the JSON request is valid per the schema and can be
 * deserialized as MyObject
 *
 * <pre>
 * <code>
 * &#64;POST
 * &#64;Produces(MediaType.APPLICATION_JSON)
 * &#64;Consumes(MediaType.APPLICATION_JSON)
 * public Response makeRequest(final MyObject obj) {
 *     return someResponse;
 * }
 *
 * &#64;Schema("http://url.com/my-object-schema.json")
 * public class MyObject {
 * ...
 * }
 *
 * {
 *     "$id": "http://url.com/my-object-schema.json",
 *     ...
 * }
 * </code>
 * </pre>
 *
 * @see JsonSchemaProvider
 * @see <a href="https://json-schema.org">https://json-schema.org/</a>
 *
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Schema {

    /**
     * @return the id of the schema
     */
    public String value();

    /**
     * @return whether schema validation should be ignored for the class
     */
    public boolean ignore() default false;

}