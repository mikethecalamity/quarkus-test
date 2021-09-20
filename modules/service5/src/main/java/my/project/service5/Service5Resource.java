package my.project.service5;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("test")
public class Service5Resource {

    @Inject
    Service5Handler handler;

    @GET
    @Path("value0")
    @Produces(MediaType.TEXT_PLAIN)
    public String value0() {
        return this.handler.getValue0();
    }

    @GET
    @Path("value1")
    @Produces(MediaType.TEXT_PLAIN)
    public String value1() {
        return this.handler.getValue1();
    }

    @GET
    @Path("value2")
    @Produces(MediaType.TEXT_PLAIN)
    public String value2() {
        return this.handler.getValue2();
    }
}
