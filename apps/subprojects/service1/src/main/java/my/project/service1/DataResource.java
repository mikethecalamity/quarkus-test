package my.project.service1;

import java.util.List;
import java.util.Objects;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import my.project.service1.jpa.DataManager;
import my.project.service1.jpa.MyData;

@Path("data")
public class DataResource {

    @Inject
    DataManager dataManager;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Long> getLatest(@QueryParam("time") final Long time) {
        return dataManager.getAll(Objects.requireNonNullElse(time, Long.MAX_VALUE), time);
    }
    
    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void add(@QueryParam("id") final int id, final MyData data) {
        dataManager.addData(id, data);
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<MyData> get(@QueryParam("id") final int id, @PathParam("value") final int value) {
        return dataManager.getData(id, value);
    }
}
