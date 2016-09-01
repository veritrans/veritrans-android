package id.co.veritrans.sdk.coreflow.callback.exception;

/**
 * Created by ziahaqi on 8/31/16.
 */
public class BaseError extends Throwable{
    protected String ErrorType;

    public BaseError(String detailMessage, String errorType) {
        super(detailMessage);
        ErrorType = errorType;
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

    public String getErrorType() {
        return ErrorType;
    }
}
