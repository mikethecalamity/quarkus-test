package my.project.service3;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.smallrye.config.ConfigMapping;

@Dependent
public class Service3Handler {

    private final String value0;
    private final String value1;
    private final String value2;

    @Inject
    public Service3Handler(@ConfigProperty(name = "app.other") final String value0, final Config config) {
        this.value0 = value0;
        this.value1 = String.format("%s-%s", config.myConfig().name(), config.myConfig().num());
        this.value2 = String.format("%s-%s", config.theirConfig().name(), config.theirConfig().num());
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

    @ConfigMapping(prefix = "app.config")
    interface Config {
        InnerConfig myConfig();
        InnerConfig theirConfig();
    }

    interface InnerConfig {
        String name();
        int num();
    }
}
