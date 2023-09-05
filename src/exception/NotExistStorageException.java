package exception;

public class NotExistStorageException extends RuntimeException {
    public NotExistStorageException(String uuid) {
        super("Resume " + uuid + " not exist");
    }
}
