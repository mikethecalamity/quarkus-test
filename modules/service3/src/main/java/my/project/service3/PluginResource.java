package my.project.service3;

import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.smallrye.config.ConfigMapping;
import my.project.service3.config.Service3Config;

@Path("/test")
public class PluginResource {

    @ConfigProperty(name = "app.other")
    String other;

    @ConfigMapping(prefix = "app.items")
    List<Service3Config> config;

    @GET
    @Path("names")
    @Produces(MediaType.TEXT_PLAIN)
    public List<String> names() {
        return this.config.stream().map(Service3Config::name).collect(Collectors.toList());
    }

    @GET
    @Path("other")
    @Produces(MediaType.TEXT_PLAIN)
    public String other() {
        return this.other;
    }
}
