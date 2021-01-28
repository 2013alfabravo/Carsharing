package carsharing.model;

public class DAOException extends RuntimeException {
    public DAOException(String message) {
        super(message);
    }

    public DAOException(Throwable cause) {
        super(cause);
    }

    DAOException(String message, Throwable cause) {
        super(message, cause);
    }
}
