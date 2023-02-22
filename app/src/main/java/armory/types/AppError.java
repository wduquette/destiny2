package armory.types;

/**
 * Application errors (e.g., input errors) to be reported to the
 * user.
 */
public class AppError extends RuntimeException {
    public AppError(String message) {
        super(message);
    }
}
