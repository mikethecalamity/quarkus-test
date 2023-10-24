package my.project.providers.jwt;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import jakarta.enterprise.inject.Vetoed;
import jakarta.ws.rs.core.SecurityContext;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

/**
 * A user that can be authenticated. Instances should be injected via cdi when needed and are
 * produced by {@link SecurityContextManager}. This class is @Vetoed because it is produced somewhere else
 */
@Vetoed
public class JwtSecurityContext implements SecurityContext {

    private static final String ROLES = "roles";
    private Jws<Claims> claim;
    private String token;

    /**
     * No-arg constructor for weld proxy
     */
    public JwtSecurityContext() {
    }

    /**
     * Create a security context
     * @param claim the authenticated claims for the token
     * @param token the original token
     */
    public JwtSecurityContext(final Jws<Claims> claim, final String token) {
        this.claim = claim;
        this.token = token;
    }

    /**
     * @return json web token fields
     */
    public Jws<Claims> getClaims() {
        return this.claim;
    }

    /**
     * Convenience method for {@link Jws#getSignature()}
     * @return the signature
     */
    public String getSignature() {
        return claim.getSignature();
    }

    /**
     * @return the original json web token for this context
     */
    public String getToken() {
        return this.token;
    }

    @Override
    public Principal getUserPrincipal() {
        return () -> claim.getBody().getSubject();
    }

    @Override
    public boolean isUserInRole(final String role) {
        @SuppressWarnings("unchecked")
        List<String> roles = (ArrayList<String>) claim.getBody().get(ROLES);
        return Stream.ofNullable(roles).flatMap(List::stream).anyMatch(role::equalsIgnoreCase);
    }

    @Override
    public boolean isSecure() {
        // Probably don't care about this
        return true;
    }

    @Override
    public String getAuthenticationScheme() {
        return "Bearer";
    }


    @Override
    public String toString() {
        return claim.getBody().toString();
    }

}
