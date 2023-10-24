package my.project.providers.jwt;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import jakarta.ws.rs.NameBinding;

/**
 * Annotates a class or method to require Json Web Token (JWT) authentication
 * and authorization. This is commonly enforced by a JavaEE interceptor or
 * decorator implemented for a specific protocol framework such as JAX-RS
 * (Jersery, RestEasy, etc) or JMS. Once auth is complete, JWTs are loaded into
 * a JwtSecurityContext for future reference.
 *
 * @see JwtRequestFilter
 * @see JwtClientFilter
 * @see JwtSecurityContext
 */
@NameBinding
@Retention(RUNTIME)
@Target({TYPE, METHOD, FIELD})
public @interface JsonWebToken {

    /**
     * @return The role this method allows
     */
    String[] value() default {};

}