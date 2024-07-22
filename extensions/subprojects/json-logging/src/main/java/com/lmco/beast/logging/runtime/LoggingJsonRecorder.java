package my.project.extension.logging.runtime;

import java.util.Optional;
import java.util.logging.Formatter;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import my.project.extension.logging.runtime.JsonLogConfig.JsonConfig;

import io.quarkus.runtime.RuntimeValue;
import io.quarkus.runtime.annotations.Recorder;

//CHECKSTYLE:OFF: MissingJavadocMethod
//CHECKSTYLE:OFF: MissingJavadocType
@Recorder
public class LoggingJsonRecorder {

    public RuntimeValue<Optional<Formatter>> initializeConsoleJsonLogging(final JsonLogConfig config) {
        return getFormatter(config.consoleJson);
    }

    public RuntimeValue<Optional<Formatter>> initializeFileJsonLogging(final JsonLogConfig config) {
        return getFormatter(config.fileJson);
    }

    public RuntimeValue<Optional<Formatter>> initializeSyslogJsonLogging(final JsonLogConfig config) {
        return getFormatter(config.syslogJson);
    }

    private RuntimeValue<Optional<Formatter>> getFormatter(final JsonConfig config) {
        if (!config.enable) {
            return new RuntimeValue<>(Optional.empty());
        }

        final JsonFormatter formatter = config.keyOverrides.map(ko -> new JsonFormatter(ko))
                .orElse(new JsonFormatter());
        config.excludedKeys.ifPresent(ek -> formatter.setExcludedKeys(ek));
        Optional.ofNullable(config.additionalField).ifPresent(af -> formatter.setAdditionalFields(af));
        config.extended.mdcKeyOverrides
                .ifPresent(keys -> formatter.setMdcKeyOverrides(Stream.of(keys.split(","))
                        .map(s -> s.trim().split("="))
                        .collect(Collectors.toMap(arr -> arr[0], arr -> arr[1]))));

        formatter.setPrettyPrint(config.prettyPrint);
        final String dateFormat = config.dateFormat;
        if (!dateFormat.equals("default")) {
            formatter.setDateFormat(dateFormat);
        }
        formatter.setExceptionOutputType(config.exceptionOutputType);
        formatter.setPrintDetails(config.printDetails);
        config.recordDelimiter.ifPresent(formatter::setRecordDelimiter);
        final String zoneId = config.zoneId;
        if (!zoneId.equals("default")) {
            formatter.setZoneId(zoneId);
        }
        return new RuntimeValue<>(Optional.of(formatter));
    }
}
//CHECKSTYLE:ON: MissingJavadocMethod
//CHECKSTYLE:ON: MissingJavadocType
