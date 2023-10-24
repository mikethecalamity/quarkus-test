package my.project.providers.jwt;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.interceptor.InterceptorBinding;

/**
 * Interceptor binding to determine security protocol being used.
 *
 * @author jhaas
 *
 */
@InterceptorBinding
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface SecurityContextInjector {
    /**
     * JMS Protocol
     */
    public static String JMS = "JMS";

    /**
     * @return Security protocol
     */
    String protocol() default "";
}
