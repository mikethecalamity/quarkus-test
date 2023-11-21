package my.project.service3;

import java.util.List;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import org.eclipse.microprofile.config.inject.ConfigProperty;

@Path("test")
public class Service3Resource {

    @ConfigProperty(name = "app.list")
    List<String> myList;

    @Inject
    Service3Handler handler;

    @GET
    @Path("list")
    @Produces(MediaType.TEXT_PLAIN)
    public List<String> test() {
        return this.myList;
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
