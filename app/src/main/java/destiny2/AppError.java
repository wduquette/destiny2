package destiny2;

/**
 * Application errors (e.g., input errors) to be reported to the
 * user.
 */
class AppError extends RuntimeException {
    AppError(String message) {
        super(message);
    }
}
