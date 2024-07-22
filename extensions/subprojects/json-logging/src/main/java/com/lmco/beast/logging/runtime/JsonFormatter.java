package my.project.extension.logging.runtime;

import java.io.Writer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.jboss.logmanager.ExtLogRecord;

import my.project.extension.logging.runtime.JsonLogConfig.AdditionalFieldConfig;

//CHECKSTYLE:OFF: JavadocVariable
//CHECKSTYLE:OFF: MissingJavadocMethod
//CHECKSTYLE:OFF: MissingJavadocType
public class JsonFormatter extends org.jboss.logmanager.formatters.JsonFormatter {

    private Set<String> excludedKeys;
    private Map<String, AdditionalFieldConfig> additionalFields;
    private Map<String, String> mdcKeyOverrides;

    /**
     * Creates a new JSON formatter.
     */
    public JsonFormatter() {
        super();
        this.excludedKeys = new HashSet<>();
        this.additionalFields = new HashMap<>();
        this.mdcKeyOverrides = new HashMap<>();
    }

    /**
     * Creates a new JSON formatter.
     *
     * @param keyOverrides a string representation of a map to override keys
     *
     *                     "@see
     *                     org.jboss.logmanager.ext.PropertyValues#stringToEnumMap(Class,
     *                     String)"
     */
    public JsonFormatter(final String keyOverrides) {
        super(keyOverrides);
        this.excludedKeys = new HashSet<>();
        this.additionalFields = new HashMap<>();
        this.mdcKeyOverrides = new HashMap<>();
    }

    /**
     * Creates a new JSON formatter.
     *
     * @param keyOverrides     a string representation of a map to override keys
     *
     *                         "@see
     *                         org.jboss.logmanager.ext.PropertyValues#stringToEnumMap(Class,
     *                         String)"
     * @param excludedKeys     a list of keys to be excluded when writing the output
     * @param additionalFields additionalFields to be added to the output
     */
    public JsonFormatter(final String keyOverrides, final Set<String> excludedKeys,
            final Map<String, AdditionalFieldConfig> additionalFields) {
        super(keyOverrides);
        this.excludedKeys = excludedKeys;
        this.additionalFields = additionalFields;
        this.mdcKeyOverrides = new HashMap<>();
    }

    public Set<String> getExcludedKeys() {
        return this.excludedKeys;
    }

    public void setExcludedKeys(final Set<String> excludedKeys) {
        this.excludedKeys = excludedKeys;
    }

    public Map<String, AdditionalFieldConfig> getAdditionalFields() {
        return this.additionalFields;
    }

    public void setAdditionalFields(final Map<String, AdditionalFieldConfig> additionalFields) {
        this.additionalFields = additionalFields;
    }

    public Map<String, String> getMdcKeyOverrides() {
        return mdcKeyOverrides;
    }

    public void setMdcKeyOverrides(final Map<String, String> mdcKeyOverrides) {
        this.mdcKeyOverrides = mdcKeyOverrides;
    }

    @Override
    protected Generator createGenerator(final Writer writer) {
        Generator superGenerator = super.createGenerator(writer);
        return new FormatterJsonGenerator(superGenerator, this.excludedKeys, this.mdcKeyOverrides);
    }

    @Override
    protected void after(final Generator generator, final ExtLogRecord record) throws Exception {
        for (var entry : this.additionalFields.entrySet()) {
            switch (entry.getValue().type) {
                case STRING:
                    generator.add(entry.getKey(), entry.getValue().value);
                    break;
                case INT:
                    generator.add(entry.getKey(), Integer.valueOf(entry.getValue().value));
                    break;
                case LONG:
                    generator.add(entry.getKey(), Long.valueOf(entry.getValue().value));
                    break;
                default:
            }
        }
        if (record != null && record.getParameters() != null) {
            for (var entry : record.getParameters()) {
                if (entry instanceof LogField) {
                    LogField f = (LogField) entry;
                    generator.add(f.key(), f.value());
                }
                else if (entry instanceof LogFieldString) {
                    LogFieldString f = (LogFieldString) entry;
                    generator.add(f.key(), f.value());
                }
            }
        }
    }

    private static class FormatterJsonGenerator implements Generator {
        private final Generator generator;
        private final Set<String> excludedKeys;
        private final Map<String, String> mdcKeyOverrides;

        private FormatterJsonGenerator(final Generator generator, final Set<String> excludedKeys,
                final Map<String, String> mdcKeyOverrides) {
            this.generator = generator;
            this.excludedKeys = excludedKeys;
            this.mdcKeyOverrides = mdcKeyOverrides;
        }

        @Override
        public Generator begin() throws Exception {
            generator.begin();
            return this;
        }

        @Override
        public Generator add(final String key, final int value) throws Exception {
            if (!excludedKeys.contains(key)) {
                generator.add(key, value);
            }
            return this;
        }

        @Override
        public Generator add(final String key, final long value) throws Exception {
            if (!excludedKeys.contains(key)) {
                generator.add(key, value);
            }
            return this;
        }

        @Override
        @SuppressWarnings("unchecked")
        public Generator add(final String key, final Map<String, ?> value) throws Exception {
            if ("mdc".equals(key)) {
                for (var entry : mdcKeyOverrides.entrySet()) {
                    Optional<Object> result = findValue(entry.getKey(), value);
                    if (result.isPresent()) {
                        Object val = result.get();
                        if (val instanceof String) {
                            generator.add(entry.getValue(), (String) val);
                        }
                        else if (val instanceof Integer) {
                            generator.add(entry.getValue(), (Integer) val);
                        }
                        else if (val instanceof Long) {
                            generator.add(entry.getValue(), (Long) val);
                        }
                        else if (val instanceof Map) {
                            generator.add(entry.getValue(), (Map<String, ?>) val);
                        }
                    }
                }
            }
            if (!excludedKeys.contains(key)) {
                generator.add(key, value);
            }
            return this;
        }

        @Override
        public Generator add(final String key, final String value) throws Exception {
            if (!excludedKeys.contains(key)) {
                generator.add(key, value);
            }
            return this;
        }

        @Override
        public Generator startObject(final String key) throws Exception {
            generator.startObject(key);
            return this;
        }

        @Override
        public Generator endObject() throws Exception {
            generator.endObject();
            return this;
        }

        @Override
        public Generator startArray(final String key) throws Exception {
            generator.startArray(key);
            return this;
        }

        @Override
        public Generator endArray() throws Exception {
            generator.endArray();
            return this;
        }

        @Override
        public Generator end() throws Exception {
            generator.end();
            return this;
        }

        private static Optional<Object> findValue(final String key, final Map<String, ?> map) {
            if (!map.containsKey(key) && key.contains(".")) {
                var keys = key.split(".");
                Object curr = map;
                for (String k : keys) {
                    if (curr instanceof Map && ((Map) curr).containsKey(k)) {
                        curr = ((Map) curr).get(k);
                    }
                    else {
                        return Optional.empty();
                    }
                }
                return Optional.of(curr);
            }
            else {
                return Optional.ofNullable(map.get(key));
            }
        }
    }
}
//CHECKSTYLE:ON: JavadocVariable
//CHECKSTYLE:ON: MissingJavadocMethod
//CHECKSTYLE:ON: MissingJavadocType
