package my.project.providers.jwt;

import jakarta.jms.JMSException;
import jakarta.jms.Message;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.microprofile.config.ConfigProvider;

/**
 * This class provides utility functions for JMS authentication when CDI is not available
 */
public class JmsAuthUtility {

    private static final Logger LOGGER = LogManager.getLogger(JmsAuthUtility.class);
    private static final Boolean DISABLED =
            ConfigProvider.getConfig().getOptionalValue("auth.jms-disabled", boolean.class).orElse(false);
    private static final String TOKEN_KEY =
            ConfigProvider.getConfig().getOptionalValue("auth.jms-token-key", String.class).orElse("bearerToken");

    /**
     * Set token on {@link Message} and return the message with token
     *
     * @param message message to add token to
     * @return {@link Message} with token property set
     */
    public static Message setBearerToken(final Message message) {
        JwtSecurityContext jws = SecurityContextManager.getContext();
        try {
            if (jws != null && jws.getToken() != null) {
                message.setStringProperty(getTokenKey(), jws.getToken());
            }
        }
        catch (JMSException e) {
            LOGGER.error("Failed to write bearerToken string property to jms message", e);
        }
        return message;
    }

    /**
     * Whether authentication disabled for JMS protocols
     * @return true if authentication is disabled for JMS
     */
    public static boolean isDisabled() {
        return DISABLED;
    }

    /**
     * Retrieve the property key for the authentication token
     *
     * @return the message property key
     */
    public static String getTokenKey() {
        return TOKEN_KEY;
    }
}
