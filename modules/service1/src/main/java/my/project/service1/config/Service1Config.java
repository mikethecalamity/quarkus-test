package my.project.service1.config;

import io.smallrye.config.ConfigMapping;

@ConfigMapping
public class Service1Config {

    private String name;

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }
}
