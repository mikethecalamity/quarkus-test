package my.project.service3.config;

import io.smallrye.config.ConfigMapping;

@ConfigMapping
public interface Service3Config {
    String name();
    int num();
}
