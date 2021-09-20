package my.project.service3;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;

@Dependent
public class Service3Handler {

    private final String value0;
    private final String value1;
    private final String value2;

    @Inject
    public Service3Handler(final JmsConfig config) {
        this.value0 = Boolean.toString(config.ignoreAnnotatedListeners());
        this.value1 = config.sessions().stream().flatMap(Collection::stream).findFirst().map(JmsSessionConfig::name).map(Optional::get).orElse("none1");
        this.value2 = config.listeners().stream().flatMap(Collection::stream).findFirst().map(JmsListenerConfig::session).orElse("none2");
    }

    public String getValue0() {
        return this.value0;
    }

    public String getValue1() {
        return this.value1;
    }

    public String getValue2() {
        return this.value2;
    }

    /**
     * Configuration for JMS
     */
    @ConfigMapping(prefix = "app.jms")
    interface JmsConfig {

        /**
         * @return boolean indicating if listeners that are annotated and not in the configuration should be ignored
         */
        @WithDefault("true")
        boolean ignoreAnnotatedListeners();

        /**
         * @return the JMS sessions
         */
        Optional<List<JmsSessionConfig>> sessions();

        /**
         * @return the JMS listeners
         */
        Optional<List<JmsListenerConfig>> listeners();
    }

    /**
     * Configuration for JMS session
     */
    interface JmsSessionConfig {

        /**
         * @return the session name
         */
        Optional<String> name();

        /**
         * @return the session URI
         */
        Optional<String> uri();

        /**
         * @return the session username
         */
        Optional<String> username();

        /**
         * @return the session password
         */
        Optional<String> password();

        /**
         * @return the session acknowledgement option
         */
        Optional<Integer> ackOption();

        /**
         * @return the session connection factory fully qualified class name
         */
        Optional<String> connectionFactory();
    }

    /**
     * Configuration for JMS listener
     */
    interface JmsListenerConfig {

        /**
         * @return flag indicating if this listener is disabled or not
         */
        @WithDefault("false")
        boolean disabled();

        /**
         * @return flag indicating if annotations should be ignored for this listener
         */
        @WithDefault("true")
        boolean ignoreAnnotated();

        /**
         * @return the listener fully qualified class name
         */
        String listenerClass();

        /**
         * @return the listener session name
         */
        String session();

        /**
         * @return the listener pool size
         */
        Optional<Integer> poolSize();

        /**
         * @return the listener flag indicating if is asynchronous
         */
        Optional<Boolean> async();

        /**
         * @return the listener queues
         */
        Optional<List<String>> queues();

        /**
         * @return the listener topics
         */
        Optional<List<String>> topics();
    }


}
