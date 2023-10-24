package my.project.providers.jwt;

import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.ws.rs.NotAuthorizedException;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.MarkerManager;

/**
 * A Jax-Rs filter that requires JWT authentication for a given request. Method calls annotated with
 * {@link JsonWebToken} will be intercepted and rejected if a request can not be authenticated. The
 * filter MUST be registered on the application's {@link Application} implementation. Currently only
 * one secret is loaded so only RSA or HMAC can be resolved.
 *
 */
@JsonWebToken
@Provider
@Priority(Priorities.AUTHENTICATION)
public class JwtRequestFilter implements ContainerRequestFilter {

    private static final Logger LOGGER = LogManager.getLogger(JwtRequestFilter.class);

    private static final String AUTH = "auth";
    // Standard scheme for token authentication
    private static final String SCHEME = "Bearer";

    @Inject
    private JwtAuthenticator authenticator;

    @Context
    private ResourceInfo info;

    @Override
    public void filter(final ContainerRequestContext requestContext) {
        LOGGER.debug("Request received {} : {}", "path", requestContext.getUriInfo().getAbsolutePath());

        if (authenticator.isDisabled()) {
            return;
        }
        String jws = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        if (StringUtils.isEmpty(jws)) {

            if (requestContext.getCookies() == null || requestContext.getCookies().get(AUTH) == null) {
                throw new NotAuthorizedException("Request received with no Authorization header.",
                        Response.status(Response.Status.UNAUTHORIZED));
            }
            jws = requestContext.getCookies().get(AUTH).getValue();
            if (StringUtils.isEmpty(jws)) {
                throw new NotAuthorizedException("Request received with no Authorization header.",
                        Response.status(Response.Status.UNAUTHORIZED));
            }
        }
        else {
            jws = jws.substring(SCHEME.length()).trim();
        }

        // Roles declared on the method supersede the class
        JsonWebToken annotation = info.getResourceMethod().getDeclaredAnnotation(JsonWebToken.class) != null
                ? info.getResourceMethod().getDeclaredAnnotation(JsonWebToken.class)
                : info.getResourceClass().getDeclaredAnnotation(JsonWebToken.class);

        JwtSecurityContext context = authenticator.authenticateJws(jws, annotation.value());

        LOGGER.trace(MarkerManager.getMarker("indexed"),
                Json.createObjectBuilder().add("http.url", requestContext.getUriInfo().getAbsolutePath().toString())
                        .add("security.context", context.toString()).add("msg", "Request authenticated").build()
                        .toString());

        requestContext.setSecurityContext(context);
    }
}