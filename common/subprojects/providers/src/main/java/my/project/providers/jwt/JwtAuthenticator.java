package my.project.providers.jwt;

import java.util.function.Consumer;
import java.util.stream.Stream;

import jakarta.enterprise.context.Dependent;
import jakarta.ws.rs.NotAuthorizedException;
import jakarta.ws.rs.core.Response;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.jackson.io.JacksonDeserializer;

/**
 * Responsible for authenticating a Json Web Token (JWT) and activating a current security context.
 */
@Dependent
public class JwtAuthenticator {

    /** The strategy for activating the context */
    private static final Consumer<JwtSecurityContext> DEFAULT_CONTEXT_STRATEGY = SecurityContextManager::set;

    private static final Logger LOGGER = LogManager.getLogger(JwtAuthenticator.class);

    private static final JwtParser DEFAULT_PARSER = Jwts.parserBuilder()
            .deserializeJsonWith(new JacksonDeserializer<>(new ObjectMapper()))
            .setSigningKeyResolver(new DefaultKeyResolver())
            .build();

    private Consumer<JwtSecurityContext> contextLoader = DEFAULT_CONTEXT_STRATEGY;
    private JwtParser parser = DEFAULT_PARSER;


    @ConfigProperty(name = "auth.disabled", defaultValue = "false")
    boolean disabled;

    /**
     * Builder method for setting the disabled flag to true for unit tests
     * @return this, for fluent building
     */
    JwtAuthenticator setEnabled() {
        this.disabled = false;
        return this;
    }

    /**
     * Builder method for setting the security context loading strategy to be used
     *
     * @param loader the loader method
     * @return this, for fluent building.
     */
    JwtAuthenticator setContextLoaderStrategy(final Consumer<JwtSecurityContext> loader) {
        this.contextLoader = loader;
        return this;
    }

    /**
     * Builder method for setting the parser for unit tests
     *
     * @param parser the parser
     * @return this, for fluent building
     */
    JwtAuthenticator setParser(final JwtParser parser) {
        this.parser = parser;
        return this;
    }

    /**
     * Authenticates the user into the current security context.
     * @param jws the JSON Web Signature (JWS) being authenticated
     * @param allowedRoles the roles to authorize the user against
     * @return the active security context
     */
    public JwtSecurityContext authenticateJws(final String jws, final String... allowedRoles) {

        if (isDisabled()) {
            LOGGER.debug("Skipping authentication.");
            return SecurityContextManager.getContext();
        }

        Jws<Claims> claim = parser.parseClaimsJws(jws);

        JwtSecurityContext context = new JwtSecurityContext(claim, jws);

        // Don't care about roles if they are not set. Seems ok for now
        boolean allowed = ArrayUtils.isEmpty(allowedRoles) ? true
                : Stream.of(allowedRoles).anyMatch(context::isUserInRole);

        if (!allowed) {
            LOGGER.warn("Request received without sufficient role.");
            throw new NotAuthorizedException("Insufficient role.", Response.status(Response.Status.UNAUTHORIZED));
        }

        LOGGER.info("Request authenticated : {}", claim.getBody().toString());

        contextLoader.accept(context);
        return context;
    }

    /**
     * Determines a special case for if authentication is disabled.
     *
     * <p>
     * Default library behavior is authentication should be enabled unless explicitly disabled.
     * </p>
     *
     * @return true is authentication is disabled
     */
    public boolean isDisabled() {
        return disabled;
    }

}
