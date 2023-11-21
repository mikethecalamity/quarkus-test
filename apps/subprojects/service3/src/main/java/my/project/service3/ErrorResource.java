package my.project.service3;

import java.util.List;

import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("error")
public class ErrorResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Long> getError() {
        throw new BadRequestException();
    }

    @DELETE
    public void getVoid() {
        
    }
}
