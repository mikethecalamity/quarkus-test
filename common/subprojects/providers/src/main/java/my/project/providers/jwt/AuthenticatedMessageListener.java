package my.project.providers.jwt;

import jakarta.inject.Inject;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.MessageListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * An abstract class implementing the Message Listeners, and goes on to authenticate any included
 * Json Web Tokens (JWT)s on the provided message
 *
 */
public abstract class AuthenticatedMessageListener implements MessageListener {

    private static final Logger LOGGER = LogManager.getLogger(AuthenticatedMessageListener.class);

    @Inject
    private JwtAuthenticator authenticator;

    /**
     * Main handoff to sub-classes for further processing of a {@link Message}
     *
     * @param message to process. Guaranteed to be non-null
     */
    protected abstract void onAuthenticated(final Message message);

    @Override
    public void onMessage(final Message message) {

        if (authenticator.isDisabled() || JmsAuthUtility.isDisabled()) {
            onAuthenticated(message);
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
        JsonWebToken jwt = getClass().isSynthetic() ? getClass().getSuperclass().getAnnotation(JsonWebToken.class)
                : getClass().getAnnotation(JsonWebToken.class);
        if (jwt == null) {
            // Listener does not have JsonWebToken annotation -- do not authenticate
            String msg = String.format("MessageListener %s is missing %s annotation. Will not process messages.",
                    getClass().getSimpleName(), JsonWebToken.class.getName());
            LOGGER.error(msg);
            handleAuthFailure(message, new NullPointerException(msg));
            return;
        }
        String[] roles = jwt.value();

        try {
            authenticator.authenticateJws(jws, roles);
        }
        catch (Exception e) {
            LOGGER.error("Failed to authenticate JSON Web signature.", e);
            handleAuthFailure(message, e);
            return;
        }

        try {
            onAuthenticated(message);
        }
        catch (Exception e) {
            LOGGER.throwing(e);
            throw e;
        }
    }

    /**
     * Acknowledge message so consumer does not keep picking it up.
     *
     * @param message message to ack
     * @param e the exception that caused the auth failure (so a subclass may handle it differently)
     */
    protected void handleAuthFailure(final Message message, @SuppressWarnings("unused") final Exception e) {
        try {
            message.acknowledge();
        }
        catch (JMSException jmsEx) {
            LOGGER.error("Failed to acknowledge message", jmsEx);
        }
    }
}
