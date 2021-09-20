package my.project.service3;

import java.util.Optional;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import io.smallrye.config.ConfigMapping;

@Dependent
public class Service3Handler {

    private final String value0;
    private final String value1;
    private final String value2;

    @Inject
    public Service3Handler(@ConfigMapping(prefix = "app.config") final Optional<Config> config) {
        if (config.isPresent()) {
            this.value0 = Boolean.toString(config.get().enabled());
            this.value1 = config.get().name();
            this.value2 = Integer.toString(config.get().num());
        }
        else {
            this.value0 = "value0";
            this.value1 = "value1";
            this.value2 = "value2";
        }
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
    interface Config {
        boolean enabled();
        String name();
        int num();
    }
}
