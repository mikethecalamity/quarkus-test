package my.project.service4;

import java.util.List;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.NoContentException;

@Path("test")
public class Service4Resource {
    
    @RestClient
    Service3ErrorClient service3Client;

    @GET
    @Path("value")
    @Produces(MediaType.TEXT_PLAIN)
    public String value() {
        return "value";
    }
    
    @GET
    @Path("client")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Long> client() {
        return service3Client.getError();
    }
    
    @GET
    @Path("client2")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Long> client2() {
        service3Client.getVoid();
        return List.of(1L, 2L, 3L);
    }
    
    @GET
    @Path("content")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Long> content() {
        return null;
    }
    
    @GET
    @Path("void")
    public void getVoid() {
        
    }
    
    @GET
    @Path("bool")
    public boolean bool() {
        return true;
    }
    
    @Path("error")
    @RegisterRestClient(configKey = "service3")
    interface Service3ErrorClient {
        
        @GET
        @Produces(MediaType.APPLICATION_JSON)
        List<Long> getError();
        
        @DELETE
        void getVoid();
    }
}
