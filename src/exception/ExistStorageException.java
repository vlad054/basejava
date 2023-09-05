package exception;

public class ExistStorageException extends RuntimeException {
    public ExistStorageException(String uuid) {
        super("Resume " + uuid + " already exist");
    }
}
