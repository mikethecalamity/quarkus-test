package my.project.extension.logging.runtime;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.jboss.logmanager.formatters.StructuredFormatter;

import io.quarkus.runtime.annotations.ConfigDocMapKey;
import io.quarkus.runtime.annotations.ConfigDocSection;
import io.quarkus.runtime.annotations.ConfigGroup;
import io.quarkus.runtime.annotations.ConfigItem;
import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;
import io.quarkus.runtime.annotations.StaticInitSafe;

//CHECKSTYLE:OFF: JavadocVariable
//CHECKSTYLE:OFF: MissingJavadocType
/**
 * Configuration for JSON log formatting.
 */
@StaticInitSafe
@ConfigRoot(phase = ConfigPhase.RUN_TIME, name = "log")
public class JsonLogConfig {

    /**
     * Console logging.
     */
    @ConfigDocSection
    @ConfigItem(name = "console.json")
    public JsonConfig consoleJson;

    /**
     * File logging.
     */
    @ConfigDocSection
    @ConfigItem(name = "file.json")
    public JsonConfig fileJson;

    /**
     * Syslog logging.
     */
    @ConfigDocSection
    @ConfigItem(name = "syslog.json")
    public JsonConfig syslogJson;

    @ConfigGroup
    public static class AdditionalFieldConfig {
        /**
         * Additional field value.
         */
        @ConfigItem
        public String value;

        /**
         * Additional field type specification. Supported types: {@code string},
         * {@code int}, and {@code long}. String is the default if not specified.
         */
        @ConfigItem(defaultValue = "string")
        public Type type;

        /**
         * Type enum
         */
        public enum Type {
            STRING, INT, LONG,
        }
    }

    @ConfigGroup
    public static class JsonConfig {
        /**
         * Determine whether to enable the JSON console formatting extension, which
         * disables "normal" console formatting.
         */
        @ConfigItem(name = ConfigItem.PARENT, defaultValue = "true")
        public boolean enable;
        /**
         * Enable "pretty printing" of the JSON record. Note that some JSON parsers will
         * fail to read the pretty printed output.
         */
        @ConfigItem
        public boolean prettyPrint;
        /**
         * The date format to use. The special string "default" indicates that the
         * default format should be used.
         */
        @ConfigItem(defaultValue = "default")
        public String dateFormat;
        /**
         * The special end-of-record delimiter to be used. By default, newline is used.
         */
        @ConfigItem
        public Optional<String> recordDelimiter;
        /**
         * The zone ID to use. The special string "default" indicates that the default
         * zone should be used.
         */
        @ConfigItem(defaultValue = "default")
        public String zoneId;
        /**
         * The exception output type to specify.
         */
        @ConfigItem(defaultValue = "detailed")
        public StructuredFormatter.ExceptionOutputType exceptionOutputType;
        /**
         * Enable printing of more details in the log.
         *
         * <p>Printing the details can be expensive as the values are retrieved from the
         * caller. The details include the source class name, source file name, source
         * method name, and source line number.
         */
        @ConfigItem
        public boolean printDetails;
        /**
         * Override keys with custom values. Omitting this value indicates that no key
         * overrides will be applied.
         */
        @ConfigItem
        public Optional<String> keyOverrides;
        /**
         * Keys to be excluded from the JSON output.
         */
        @ConfigItem
        public Optional<Set<String>> excludedKeys;

        /**
         * Additional fields to be appended in the JSON logs.
         */
        @ConfigItem
        @ConfigDocMapKey("field-name")
        public Map<String, AdditionalFieldConfig> additionalField;

        /**
         * Custom functionality added to the quarkus logging base functionality
         */
        @ConfigDocSection
        @ConfigItem()
        public ExtendedConfig extended;
    }


    @ConfigGroup
    public static class ExtendedConfig {
        /**
         * Override keys with custom values. Omitting this value indicates that no key
         * overrides will be applied.
         */
        @ConfigItem
        public Optional<String> mdcKeyOverrides;

        /**
         * Log level for http logging
         */
        @ConfigItem(defaultValue = "info")
        public String http;

        /**
         * Log level for amqp logging
         */
        @ConfigItem(defaultValue = "info")
        public String amqp;

        /**
         * Log level for amqp body logging
         */
        @ConfigItem(defaultValue = "debug")
        public String amqpBody;

        /**
         * Log level for db logging
         */
        @ConfigItem(defaultValue = "debug")
        public String db;

        /**
         * Include telemetry context in log statements
         */
        @ConfigItem(defaultValue = "true")
        public boolean telemetry;

        /**
         * If middleware logging is enabled
         */
        @ConfigItem(defaultValue = "true")
        public boolean middlewareEnabled;
    }
}
//CHECKSTYLE:OFF: JavadocVariable
//CHECKSTYLE:OFF: MissingJavadocType
