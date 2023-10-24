package my.project.providers;

import java.io.Serializable;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.function.Predicate;
import java.util.List;

import jakarta.json.JsonException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.core.Response.StatusType;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParseException;

@Provider
public class RestExceptionMapper implements ExceptionMapper<Throwable> {

    public static final Predicate<Throwable> BAD_REQUEST_EXCEPTIONS = t -> t instanceof DateTimeParseException //
            || t instanceof IllegalArgumentException //
            || t instanceof JsonException //
            || t instanceof JsonParseException;

    private static final Logger LOGGER = LogManager.getLogger(RestExceptionMapper.class);

    @Context
    private HttpServletRequest httpRequest;

    @Context
    private HttpServletResponse httpResponse;

    @Context
    private ResourceInfo info;

    @Context
    private UriInfo uriInfo;

    @Override
    public Response toResponse(final Throwable exception) {

        LOGGER.throwing(exception);

        Throwable rootCause = getRootCause(exception);

        if (exception instanceof WebApplicationException) {
            WebApplicationException webAppException = (WebApplicationException) exception;

            // Create a new message if there is a root cause
            if (webAppException.getCause() != null) {
                StatusType status = webAppException.getResponse().getStatusInfo();
                return buildResponse(status, rootCause);
            }

            return webAppException.getResponse();
        }
        // If the exception is a defined bad request exception
        else if (BAD_REQUEST_EXCEPTIONS.test(rootCause)) {
            return buildResponse(Status.BAD_REQUEST, rootCause,
                    "The request could not be understood by the server due to malformed request.");
        }
        else {
            return buildResponse(Status.INTERNAL_SERVER_ERROR, rootCause);
        }
    }

    private Response buildResponse(final StatusType status, final Throwable cause, final String... recommendations) {
        Class<?> resource = info.getResourceClass();
        String resourceClass = resource == null ? null : resource.getSimpleName();
        String resourcePath = httpRequest.getRequestURI();

        RestExceptionResponseDTO resp = new RestExceptionResponseDTO(resourceClass, resourcePath,
                cause.getClass().getCanonicalName(), cause.getMessage());
        resp.setDetails(List.of(ExceptionUtils.getStackTrace(cause)));

        if (recommendations != null && recommendations.length > 0) {
            resp.setRecommendations(List.of(recommendations));
        }

        return Response.status(status).type(MediaType.APPLICATION_JSON).entity(resp).build();
    }

    private static Throwable getRootCause(final Throwable throwable) {
        if (throwable.getCause() != null) {
            return getRootCause(throwable.getCause());
        }
        return throwable;
    }

    public static class RestExceptionResponseDTO implements Serializable {
        private static final long serialVersionUID = 1L;

        private final String service;
        private final String endpoint;
        private final String type;
        private final String message;

        private List<String> recommendations = new ArrayList<>();
        private List<String> details = new ArrayList<>();

        public RestExceptionResponseDTO(final String service, final String endpoint, final String type,
                final String message) {
            this.service = service;
            this.endpoint = endpoint;
            this.type = type;
            this.message = message;
        }

        /**
         * @return the service name
         */
        public String getService() {
            return service;
        }

        /**
         * @return the REST endpoint
         */
        public String getEndpoint() {
            return endpoint;
        }

        /**
         * @return the error type
         */
        public String getType() {
            return type;
        }

        /**
         * @return the error message
         */
        @JsonProperty("title") // TODO: find out where this needs to be changed
        public String getMessage() {
            return message;
        }

        /**
         * @return the recommendations to handle the exception
         */
        public List<String> getRecommendations() {
            return recommendations;
        }

        /**
         * @param recommendations the recommendations to handle the exception
         */
        public void setRecommendations(final List<String> recommendations) {
            this.recommendations = recommendations;
        }

        /**
         * @return the exception details (likely the stacktrace)
         */
        public List<String> getDetails() {
            return details;
        }

        /**
         * @param details the exception details (likely the stacktrace)
         */
        public void setDetails(final List<String> details) {
            this.details = details;
        }
    }
}