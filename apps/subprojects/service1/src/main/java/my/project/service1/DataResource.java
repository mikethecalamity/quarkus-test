package my.project.service1;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import my.project.service1.jpa.DataManager;

@Path("data")
public class DataResource {

    @Inject
    DataManager dataManager;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Long> getLatest(@QueryParam("time") final Long time) {
        return dataManager.getAll(Objects.requireNonNullElse(time, Long.MAX_VALUE));
    }
}
