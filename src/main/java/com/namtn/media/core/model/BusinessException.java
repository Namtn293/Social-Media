package com.namtn.media.core.model;

import com.namtn.media.core.auth.enumration.ExceptionEnum;

public class BusinessException extends Exception {
    private String message;
    private String errorCode;
    private String messageId;

    public BusinessException(ExceptionEnum error) {
        this.errorCode = error.errorCode();
        this.messageId = error.errorMessageId();
        this.message = error.errorMessageId();
        logError();
    }

    public BusinessException(ExceptionEnum error, String message) {
        this(error);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message != null ? message : "Error Code: " + errorCode + ", Message ID: " + messageId;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private void logError() {
        System.out.println("Logging error - Code: " + errorCode + ", Message: " + messageId);
    }
}
