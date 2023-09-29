package exception;

public class StorageException extends RuntimeException {

    private final String uuid;

    public StorageException(String mes, String uuid) {
        super(mes);
        this.uuid = uuid;
    }

    public StorageException(String mes, String uuid, Exception e) {
        super(mes, e);
        this.uuid = uuid;
    }

    public String getUuid() {
        return this.uuid;
    }

}
