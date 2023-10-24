package my.project.providers.jwt;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.interceptor.InterceptorBinding;

/**
 * Enables the propagation of a security context to child threads. This is
 * necessary because standard CDI does not propagate the request context.
 */
@Inherited
@InterceptorBinding
@Target({ ElementType.TYPE, ElementType.METHOD, ElementType.FIELD, ElementType.CONSTRUCTOR })
@Retention(value = RetentionPolicy.RUNTIME)
public @interface SecurityContextAware {
}
