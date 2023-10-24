package my.project.providers.jwt;

import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.interceptor.AroundConstruct;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Associates an object instance with the current context. This is
 * necessary in cases where the context cannot be retrieved from a
 * {@link java.lang.ThreadLocal} safely, either because we are in a new or
 * re-used thread.
 *
 */
@SecurityContextAware
@Interceptor
@Priority(Interceptor.Priority.APPLICATION)
public class SecurityContextInterceptor {

    private static Logger LOGGER = LogManager.getLogger(SecurityContextInterceptor.class);

    @Inject
    SecurityContextManager contextManager;

    /**
     * Intercepts construction calls and registers the object with the context manager
     *
     * @param context the invocation context
     * @throws Exception required declaration
     */
    @AroundConstruct
    public void aroundConstruct(final InvocationContext context) throws Exception {
        context.proceed();
        LOGGER.trace("Tracking security context for {}", context.getTarget());
        contextManager.put(context.getTarget());
    }

    /**
     * Intercepts method invocations annotated with {@link SecurityContextInterceptor}
     *
     * @param context the invocation context
     * @return the proceeded context
     * @throws Exception required declaration
     */
    @AroundInvoke
    public Object executeInvoke(final InvocationContext context) throws Exception {
        LOGGER.trace("Loading new security context for {}", context.getTarget());
        SecurityContextManager.set(contextManager.get(context.getTarget()));
        try {
            return context.proceed();
        }
        finally {
            contextManager.remove(context.getTarget());
        }
    }
}
