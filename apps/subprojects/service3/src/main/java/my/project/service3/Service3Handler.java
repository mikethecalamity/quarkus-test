package my.project.service3;

import java.util.List;
import java.util.Map;

import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;

import io.smallrye.config.ConfigMapping;

@Dependent
public class Service3Handler {

    private final Map<String, String> value0;
    private final String value1;
    private final String value2;

    @Inject
    public Service3Handler(final MapConfig config) {
        this.value0 = config.mapConfigs().get(0);
        this.value1 = "none1";
        this.value2 = "none2";
    }

    public Map<String, String> getValue0() {
        return this.value0;
    }

    public String getValue1() {
        return this.value1;
    }

    public String getValue2() {
        return this.value2;
    }

    @ConfigMapping(prefix = "app.config")
    interface MapConfig {
        List<Map<String, String>> mapConfigs();
    }
}
