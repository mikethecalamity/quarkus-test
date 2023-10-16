package my.project.service4;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("test")
public class Service4Resource {

    @GET
    @Path("value")
    @Produces(MediaType.TEXT_PLAIN)
    public String value() {
        return "value";
    }
}
