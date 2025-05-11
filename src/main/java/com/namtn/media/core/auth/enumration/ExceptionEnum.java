package com.namtn.media.core.auth.enumration;

import com.namtn.media.core.model.BusinessError;

public enum ExceptionEnum implements BusinessError {

    // Common
    SERVER_ERROR("500", "server.error"),
    INVALID_INPUT("400", "invalid.input"),

    // Enum
    INVALID_REPORT_TYPE("500", "invalid.report.type"),

    // Auth
    UNAUTHORIZED("403", "unauthorized"),
    EMAIL_ALREADY_EXIST("422", "email.already.exist"),
    INCORRECT_PASSWORD("500", "incorrect.password"),
    EMAIL_NOT_FOUND("500", "email.not.found"),
    UNVERIFIED_ACCOUNT("500", "unverified.account"),
    ACCOUNT_LOCKED("500", "account.is.locked"),

    // User
    NOT_PERMISSION("500", "not.permission"),
    INVALID_USER_STATUS("500", "invalid.user.status"),
    USER_NOT_FOUND("500", "user.not.found"),
    USER_STATUS_UNCHANGED("500", "user.status.unchanged"),

    // Content
    FILE_NOT_FOUND("500", "file.not.found"),
    FILE_TOO_LARGE("500", "file.too.large"),

    // Post
    POST_NOT_FOUND("500", "post.not.found"),
    CONTENT_NOT_FOUND("500", "content.not.found"),

    // Comment
    COMMENT_NOT_FOUND("500", "comment.not.found"),

    // Report
    REPORT_ALREADY_EXIST("500", "already.report"),
    REPORT_NOT_FOUND("500", "report.not.found"),
    INVALID_REPORT_ACTION("500", "invalid.report.action"),

    // Chat
    INVALID_CHAT_TYPE("500", "invalid.chat.type"),
    NOT_ENOUGH_USER("500", "not.enough.user"),
    CHAT_NOT_FOUND("500", "chat.not.found"),
    COMMENT_IS_DELETED("500", "comment.is.deleted"),
    NOT_EMPTY("500","not.empty"),
    ;

    private String code;
    private String message;

    ExceptionEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String errorCode() {
        return code;
    }

    @Override
    public String errorMessageId() {
        return message;
    }
}