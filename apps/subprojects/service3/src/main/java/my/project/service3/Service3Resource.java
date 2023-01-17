package my.project.service3;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("test")
public class Service3Resource {

    @Inject
    Service3Handler handler;

    @GET
    @Path("value0")
    @Produces(MediaType.TEXT_PLAIN)
    public String value0() {
        return this.handler.getValue0().toString();
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
