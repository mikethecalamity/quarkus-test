package my.project.providers.jwt;

import java.io.IOException;

import jakarta.enterprise.context.Dependent;
import jakarta.ws.rs.client.ClientRequestContext;
import jakarta.ws.rs.client.ClientRequestFilter;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.ext.Provider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Provider
@Dependent
public class JwtClientFilter implements ClientRequestFilter {
    private static final Logger LOGGER = LogManager.getLogger(JwtClientFilter.class);

    @Override
    public void filter(final ClientRequestContext requestContext) throws IOException {
        JwtSecurityContext user = SecurityContextManager.getContext();

        if (user.getClaims() != null) {
            LOGGER.trace(() -> "Outgoing http request for : " + user);
            requestContext.getHeaders().putSingle(HttpHeaders.AUTHORIZATION, "Bearer " + user.getToken());
        }
        else {
            LOGGER.trace(() -> "Outgoing http request with no associated user");
        }

    }

}