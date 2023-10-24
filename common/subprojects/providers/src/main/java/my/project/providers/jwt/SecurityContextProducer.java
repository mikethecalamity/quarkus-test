package my.project.providers.jwt;

import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.Disposes;
import jakarta.enterprise.inject.Produces;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

/**
 * CDI producer which provides the {@link JwtSecurityContext} for the current
 * {@link RequestScoped} context. This class observes events for {@link Jws}
 * {@link Claims} to provide credentials for a context. Any CDI bean can simply
 * inject via :
 *
 * <pre>
 * <code>@Inject JwtSecurityContext context
 * </code>
 * </pre>
 *
 */
public class SecurityContextProducer {

    /**
     * Producer method to enable injection
     *
     * @return The context associated with the current thread (if there is one)
     */
    @RequestScoped
    @Produces
    public JwtSecurityContext produceContext() {
        JwtSecurityContext securityContext = SecurityContextManager.getContext();
        // Request scoped producer cannot return null - create "empty" security context
        return securityContext != null ? securityContext : new JwtSecurityContext(null, null);
    }

    /**
     * Disposes the security context
     *
     * @param context The context associated with the current thread (if there is one)
     */
    public void dispose(@Disposes final JwtSecurityContext context) {
        if (context == SecurityContextManager.getContext()) {
            SecurityContextManager.set(null);
        }
    }

}
