package my.project.service5;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import io.smallrye.config.ConfigMapping;

@Dependent
public class Service5Handler {

    private final String value0;
    private final String value1;
    private final String value2;

    @Inject
    public Service5Handler(@ConfigMapping(prefix = "app.config") final Config config) {
        this.value0 = Boolean.toString(config.enabled());
        this.value1 = config.name();
        this.value2 = Integer.toString(config.num());
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
     * Configuration
     */
    interface Config {
        boolean enabled();
        String name();
        int num();
    }
}
