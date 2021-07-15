package my.project.service1;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.config.inject.ConfigProperty;

@Path("/test")
public class PluginResource {

    @ConfigProperty(name = "app.other")
    String other;

    @GET
    @Path("other")
    @Produces(MediaType.TEXT_PLAIN)
    public String other() {
        return this.other;
    }
}
