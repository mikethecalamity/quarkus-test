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

@Path("/hello")
public class PluginResource {

    @Inject @Any
    private Instance<Plugin> plugins;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public List<String> hello() {
        return plugins.stream().map(Plugin::getName).collect(Collectors.toList());
    }
}
