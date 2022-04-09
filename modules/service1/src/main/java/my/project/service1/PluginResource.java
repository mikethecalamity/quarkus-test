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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.smallrye.config.ConfigMapping;
import my.project.plugin.Plugin;

@Path("test")
public class PluginResource {

    private static final Logger LOGGER = LogManager.getLogger();

    @Inject
    @Any
    Instance<Plugin> plugins;

    @Inject
    Service1Config config;

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

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("TEST");
        }

        return this.plugins.stream().map(Plugin::getName).collect(Collectors.toList());
    }

    @ConfigMapping(prefix = "app.config")
    interface Service1Config {
        String name();
        int num();
    }
}
