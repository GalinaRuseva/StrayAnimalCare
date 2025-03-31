package app.exception;

public class NoFileSelectedForUploadException extends RuntimeException{
    public NoFileSelectedForUploadException() {
    }

    public NoFileSelectedForUploadException(String message) {
        super(message);
    }
}
