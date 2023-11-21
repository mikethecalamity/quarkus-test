package my.project.service3;

import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;

@Dependent
public class Service3Handler {

    private final String value1;
    private final String value2;

    @Inject
    public Service3Handler() {
        this.value1 = "none1";
        this.value2 = "none2";
    }

    public String getValue1() {
        return this.value1;
    }

    public String getValue2() {
        return this.value2;
    }
}
