package my.project.providers;

import java.lang.reflect.Parameter;

import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.ext.Provider;

/**
 * Verify all {@link NotNull} rest query parameters are populated with a value
 * If there is a query parameter that is annotated with {@link NotNull} but a value has not been provided in the request
 * a {@link Status#BAD_REQUEST} will be returned with the name of the query parameter that was null
 */
@Provider
public class RequiredParameterFilter implements ContainerRequestFilter {

    @Context
    ResourceInfo resourceInfo;

    @Override
    public void filter(final ContainerRequestContext requestContext) {
        StringBuilder sb = new StringBuilder();
        for (final Parameter parameter : resourceInfo.getResourceMethod().getParameters()) {
            if (parameter.isAnnotationPresent(QueryParam.class) && parameter.isAnnotationPresent(NotNull.class)) {
                final QueryParam queryAnnotation = parameter.getAnnotation(QueryParam.class);
                if (!requestContext.getUriInfo().getQueryParameters().containsKey(queryAnnotation.value())) {
                    sb.append(String.format("'%s' is a required query parameter for the rest endpoint.",
                                    queryAnnotation.value()));
                }
            }
        }

        if (sb.length() > 0) {
            throw new BadRequestException(sb.toString());
        }
    }
}