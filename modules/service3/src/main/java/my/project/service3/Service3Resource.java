package my.project.service3;

import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.smallrye.config.ConfigMapping;
import my.project.service3.config.Service3Config;
import my.project.service3.config.Service3Configs;

@Path("test")
public class Service3Resource {

    @ConfigMapping(prefix = "app.test")
    Service3Configs config;

    @GET
    @Path("names")
    @Produces(MediaType.TEXT_PLAIN)
    public List<String> names() {
        return this.config.items().stream().map(Service3Config::name).collect(Collectors.toList());
    }
}
