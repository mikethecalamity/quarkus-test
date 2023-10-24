package my.project.providers;

import jakarta.inject.Inject;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.ext.ContextResolver;
import jakarta.ws.rs.ext.Provider;

import com.fasterxml.jackson.databind.ObjectMapper;

@Provider
@Produces(MediaType.APPLICATION_JSON)
public class JacksonObjectMapperContextResolver implements ContextResolver<ObjectMapper> {

    @Inject
    private ObjectMapper mapper;

    @Override
    public ObjectMapper getContext(final Class<?> type) {
        return mapper;
    }
}