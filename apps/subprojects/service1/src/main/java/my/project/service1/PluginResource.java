package my.project.service1;

import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.smallrye.config.ConfigMapping;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import my.project.plugin.Plugin;

@Path("plugin")
public class PluginResource {

    @ConfigProperty(name = "app.my-list")
    List<String> myList;

    @Inject
    Service1Config config;

    @Inject
    @Any
    Instance<Plugin> plugins;

    @GET
    @Path("list")
    @Produces(MediaType.TEXT_PLAIN)
    public List<String> test() {
        return this.myList;
    }

    @GET
    @Path("name")
    @Produces(MediaType.TEXT_PLAIN)
    public String name() {
        return this.config.name();
    }

    @GET
    @Path("plugins")
    @Produces(MediaType.TEXT_PLAIN)
    public List<String> plugins() {
        return this.plugins.stream().map(Plugin::getName).collect(Collectors.toList());
    }

    @ConfigMapping(prefix = "app.config")
    interface Service1Config {
        String name();
        int num();
    }
}
