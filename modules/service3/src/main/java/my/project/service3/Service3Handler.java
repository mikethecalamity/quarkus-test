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

    @ConfigMapping(prefix = "app.jms")
    interface JmsConfig {

        @WithDefault("true")
        boolean ignoreAnnotatedListeners();

        Optional<List<JmsSessionConfig>> sessions();
        Optional<List<JmsListenerConfig>> listeners();
    }

    interface JmsSessionConfig {
        Optional<String> name();
        Optional<String> uri();
        Optional<String> username();
        Optional<String> password();
        Optional<Integer> ackOption();
        Optional<String> connectionFactory();
    }

    interface JmsListenerConfig {

        @WithDefault("false")
        boolean disabled();

        @WithDefault("true")
        boolean ignoreAnnotated();

        String listenerClass();
        String session();
        Optional<Integer> poolSize();
        Optional<Boolean> async();
        Optional<List<String>> queues();
        Optional<List<String>> topics();
    }
}
