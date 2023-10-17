package my.project.service1;

import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.smallrye.config.ConfigMapping;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
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
