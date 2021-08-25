package my.project.service3;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithName;
import my.project.service3.config.Service3Config;

@Dependent
public class Service3Handler {

    private final String value1;
    private final String value2;

    @Inject
    public Service3Handler(final Config config) {
        this.value1 = String.format("%s-%s", config.myConfig().name(), config.myConfig().num());
        this.value2 = String.format("%s-%s", config.theirConfig().name(), config.theirConfig().num());
    }

    public String getValue1() {
        return this.value1;
    }

    public String getValue2() {
        return this.value2;
    }

    @ConfigMapping(prefix = "app.config")
    private interface Config {

        @WithName("my-config")
        Service3Config myConfig();

        @WithName("their-config")
        Service3Config theirConfig();
    }
}
