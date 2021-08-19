package my.project.lib2;

import javax.enterprise.context.Dependent;

import org.eclipse.microprofile.config.inject.ConfigProperty;

@Dependent
public class Handler1 {

    @ConfigProperty(name = "app.handler1.config")
    String value;

    public String getValue() {
        return this.value;
    }
}