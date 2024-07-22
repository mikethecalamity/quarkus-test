package my.project.extension.logging.runtime;

import java.util.Map;

import jakarta.json.Json;
import jakarta.json.JsonObject;

//CHECKSTYLE:OFF: MissingJavadocMethod
//CHECKSTYLE:OFF: MissingJavadocType
public record LogField(String key, JsonObject value) {
    public LogField(final String key, final Map<String, ?> value) {
        this(key, Json.createObjectBuilder(value).build());
    }
}
//CHECKSTYLE:ON: MissingJavadocMethod
//CHECKSTYLE:ON: MissingJavadocType
