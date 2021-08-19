package my.project.service4;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import my.project.lib2.Handler2;

@Path("test")
public class Service4Resource {

    @Inject
    Handler2 handler;

    @GET
    @Path("value")
    @Produces(MediaType.TEXT_PLAIN)
    public String value() {
        return this.handler.getValue();
    }
}
