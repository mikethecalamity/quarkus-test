package my.project.providers.jwt;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import jakarta.inject.Inject;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;
import jakarta.jms.JMSException;
import jakarta.jms.Message;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Injects the current security context into message method parameter
 */
@SecurityContextInjector(protocol = SecurityContextInjector.JMS)
@Interceptor
public class JmsSecurityContextInjector {

    private static final Logger LOGGER = LogManager.getLogger(JmsSecurityContextInjector.class);

    @Inject
    private JwtSecurityContext jws;

    /**
     * Intercepts method invocations annotated with
     * {@link JmsSecurityContextInjector}
     *
     * @param context the invocation context
     * @return the proceeded context
     * @throws Exception required declaration
     */
    @AroundInvoke
    public Object executeInvoke(final InvocationContext context) throws Exception {
        Object[] methodParameters = context.getParameters();

        List<Message> messages = Stream.of(methodParameters)
                .filter(Message.class::isInstance)
                .map(Message.class::cast)
                .collect(Collectors.toList());

        if (messages.isEmpty()) {
            throw new IllegalStateException("No instance of jakarta.jms.Message was found in the method parameters."
                    + " Did you mean to use this annotation?");
        }

        messages.forEach(m -> {
            try {
                if (jws != null && jws.getToken() != null) {
                    m.setStringProperty(JmsAuthUtility.getTokenKey(), jws.getToken());
                }
            }
            catch (JMSException e) {
                LOGGER.error("Failed to write bearerToken string property to jms message", e);
            }
        });

        return context.proceed();
    }
}
