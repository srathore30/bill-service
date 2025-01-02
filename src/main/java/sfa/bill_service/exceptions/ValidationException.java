package sfa.bill_service.exceptions;

public class ValidationException extends BaseException {
    public ValidationException(int errorCode, String errorMessage) {
        super(errorCode, errorMessage);
    }

    @Override
    public int getErrorCode() {
        return this.errorCode;
    }

    @Override
    public String getErrorMessage() {
        return this.errorMessage;
    }

}