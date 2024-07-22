package my.project.extension.logging.deployment;

import my.project.extension.logging.runtime.JsonLogConfig;
import my.project.extension.logging.runtime.LoggingJsonRecorder;

import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.annotations.ExecutionTime;
import io.quarkus.deployment.annotations.Record;
import io.quarkus.deployment.builditem.LogConsoleFormatBuildItem;
import io.quarkus.deployment.builditem.LogFileFormatBuildItem;
import io.quarkus.deployment.builditem.LogSyslogFormatBuildItem;

/**
 * Build components for logging in JSON
 */
public final class LoggingJsonProcessor {

    /**
     * Set up the console formatter at build time
     * @param recorder the {@link LoggingJsonRecorder}
     * @param config the {@link JsonLogConfig}
     * @return the {@link LogConsoleFormatBuildItem}
     */
    @BuildStep
    @Record(ExecutionTime.RUNTIME_INIT)
    public LogConsoleFormatBuildItem setUpConsoleFormatter(final LoggingJsonRecorder recorder,
            final JsonLogConfig config) {
        return new LogConsoleFormatBuildItem(recorder.initializeConsoleJsonLogging(config));
    }

    /**
     * Set up the file formatter at build time
     * @param recorder the {@link LoggingJsonRecorder}
     * @param config the {@link JsonLogConfig}
     * @return the {@link LogFileFormatBuildItem}
     */
    @BuildStep
    @Record(ExecutionTime.RUNTIME_INIT)
    public LogFileFormatBuildItem setUpFileFormatter(final LoggingJsonRecorder recorder, final JsonLogConfig config) {
        return new LogFileFormatBuildItem(recorder.initializeFileJsonLogging(config));
    }

    /**
     * Set up the syslog formatter at build time
     * @param recorder the {@link LoggingJsonRecorder}
     * @param config the {@link JsonLogConfig}
     * @return the {@link LogSyslogFormatBuildItem}
     */
    @BuildStep
    @Record(ExecutionTime.RUNTIME_INIT)
    public LogSyslogFormatBuildItem setUpSyslogFormatter(final LoggingJsonRecorder recorder,
            final JsonLogConfig config) {
        return new LogSyslogFormatBuildItem(recorder.initializeSyslogJsonLogging(config));
    }
}
