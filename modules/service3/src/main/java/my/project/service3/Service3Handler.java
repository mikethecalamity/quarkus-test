package my.project.service3;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import io.smallrye.config.ConfigMapping;
import my.project.service3.config.Service3Config;

@Dependent
public class Service3Handler {

    private final String value1;
    private final String value2;

    @Inject
    public Service3Handler(@ConfigMapping(prefix = "app.data1") final Service3Config config1,
            @ConfigMapping(prefix = "app.data2") final Service3Config config2) {
        this.value1 = String.format("%s-%s", config1.name(), config1.num());
        this.value2 = String.format("%s-%s", config2.name(), config2.num());
    }

    public String getValue1() {
        return this.value1;
    }

    public String getValue2() {
        return this.value2;
    }
}
