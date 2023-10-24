package my.project.providers.jwt;

import jakarta.annotation.Priority;
import jakarta.decorator.Decorator;
import jakarta.decorator.Delegate;
import jakarta.enterprise.inject.Any;
import jakarta.inject.Inject;
import jakarta.interceptor.Interceptor;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.MessageListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A decorator for Message Listeners which requires Json Web Token (JWT)
 * authentication for the context to continue.
 *
 */
@Decorator
@JsonWebToken
@Priority(Interceptor.Priority.APPLICATION)
public class JmsAuthListener implements MessageListener {

    private static final Logger LOGGER = LogManager.getLogger(JmsAuthListener.class);

    @Inject
    private JwtAuthenticator authenticator;

    @Inject
    @Delegate
    @Any
    private MessageListener listener;

    @Override
    public void onMessage(final Message message) {

        if (authenticator.isDisabled() || JmsAuthUtility.isDisabled()) {
            listener.onMessage(message);
            return;
        }

        String jws = null;
        try {
            jws = message.getStringProperty(JmsAuthUtility.getTokenKey());
        }
        catch (JMSException e) {
            LOGGER.error("Failed to retrieve token.", e);
        }

        // Check if we were handed a proxy, then access the JsonWebToken annotation value
        // Unsure if this check works on non-weld CDI implementations
        JsonWebToken jwt = listener.getClass().isSynthetic()
                ? listener.getClass().getSuperclass().getAnnotation(JsonWebToken.class)
                : listener.getClass().getAnnotation(JsonWebToken.class);
        if (jwt == null) {
            // Listener does not have JsonWebToken annotation -- do not authenticate
            listener.onMessage(message);
            return;
        }
        String[] roles = jwt.value();

        try {
            authenticator.authenticateJws(jws, roles);
        }
        catch (Exception e) {
            LOGGER.error("Failed to authenticate JSON Web signature.", e);
            handleAuthFailure(message);
            return;
        }
        listener.onMessage(message);
    }

    /**
     * Acknowledge message so consumer does not keep picking it up.
     *
     * @param message message to ack
     */
    private void handleAuthFailure(final Message message) {
        try {
            message.acknowledge();
        }
        catch (JMSException e) {
            LOGGER.error("Failed to acknowledge message", e);
        }
    }
}
