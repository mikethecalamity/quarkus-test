package my.project.service4;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("test")
public class Service4Resource {

    @GET
    @Path("value")
    @Produces(MediaType.TEXT_PLAIN)
    public String value() {
        return "value";
    }
}
