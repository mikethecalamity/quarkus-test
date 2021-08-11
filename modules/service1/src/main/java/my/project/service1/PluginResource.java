package my.project.service1;

import java.util.List;
import java.util.stream.Collectors;

import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import my.project.plugin.Plugin;

@Path("test")
public class PluginResource {

    @Inject
    @Any
    Instance<Plugin> plugins;

    @GET
    @Path("plugins")
    @Produces(MediaType.TEXT_PLAIN)
    public List<String> plugins() {
        return this.plugins.stream().map(Plugin::getName).collect(Collectors.toList());
    }
}
