package my.project.providers.jwt;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import jakarta.enterprise.context.Dependent;

/**
 * Manages the security context for the current thread. Also allows for the
 * association of a specific object with a context. This is necessary when
 * contexts need to be propagated to child threads.
 *
 */
@Dependent
public class SecurityContextManager {

    // Initializer is important for out-of-order mappings to work.
    private static final ThreadLocal<JwtSecurityContext> thread =
        ThreadLocal.withInitial(() -> new JwtSecurityContext(null, null));

    // This mapping allows us to propagate contexts to any thread by mapping object references directly to that context
    // A context can be mapped with put(o) and retrieved with get(o)
    private static final ConcurrentMap<Object, JwtSecurityContext> mappings = new ConcurrentHashMap<>();

    /**
     * @return The context associated with the current thread (if there is one)
     */
    public static JwtSecurityContext getContext() {
        return thread.get();
    }

    /**
     * Set the current security context
     *
     * @param context the authenticated context
     */
    public static void set(final JwtSecurityContext context) {
        if (context == null) {
            return;
        }

        // Replace all mappings with the new context. Only 1 context is allowed to be active at a time
        // Also not that a mapped context can never be null. If there is no set context, getContext will
        // create an empty ref. This allows us to start tracking the object and map real context later.
        // This is necessary because sometimes objects are mapped during construction before the context has
        // been extracted from a message.
        JwtSecurityContext old = getContext();
        thread.set(context);
        mappings.keySet().forEach(k -> mappings.replace(k, old, context));
    }

    /**
     * Create a mapping between an object and context that can be retrieved from another thread
     *
     * @param obj the object
     * @return the context
     */
    JwtSecurityContext put(final Object obj) {
        return mappings.put(obj, getContext());
    }

    /**
     * Get the context associated with the object
     *
     * @param obj the object
     * @return the context
     */
    JwtSecurityContext get(final Object obj) {
        return mappings.getOrDefault(obj, getContext());
    }

    /**
     * Remove the mapping
     *
     * @param obj the object
     * @return the context
     */
    JwtSecurityContext remove(final Object obj) {
        return mappings.remove(obj);
    }

}
