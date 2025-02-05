package my.project.service3;

import java.util.List;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import my.project.plugin.Plugin;

@Path("test")
public class Service3Resource {
    
    @ConfigProperty(name = "app.list")
    List<String> myList;
    
    @ConfigProperty(name = "app.order-a")
    List<String> order1;
    
    //Class<? extends Plugin>
    @ConfigProperty(name = "app.order-b")
    List<String> order2;
    
    @Inject
    Instance<Plugin> plugins;

    @Inject
    Service3Handler handler;

    @GET
    @Path("list")
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> list() {
        return this.myList;
    }
    
    @GET
    @Path("order1")
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> order1() {
        return order1;
    }
    
    @GET
    @Path("order2")
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> order2() {
        return order2;
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
