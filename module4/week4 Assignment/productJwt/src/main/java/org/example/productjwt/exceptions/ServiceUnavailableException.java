package org.example.productjwt.exceptions;

/**
 * Thrown when an external service or dependency is unavailable or unreachable.
 * Returns HTTP 503 Service Unavailable
 */
public class ServiceUnavailableException extends RuntimeException {

    private final String serviceName;
    private final String reason;

    public ServiceUnavailableException(String serviceName, String reason) {
        super(String.format("Service '%s' is currently unavailable: %s", serviceName, reason));
        this.serviceName = serviceName;
        this.reason = reason;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getReason() {
        return reason;
    }
}

