package sfa.bill_service.exceptions;

public class BusinessServiceException extends BaseException {
    public BusinessServiceException(int errorCode, String errorMessage) {
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
