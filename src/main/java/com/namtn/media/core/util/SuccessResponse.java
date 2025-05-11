package com.namtn.media.core.util;


import java.util.Objects;

public class SuccessResponse<T> extends ResponseBase {
    private T data;
    public SuccessResponse(T data) {
        this.data = data;
    }
    public SuccessResponse(Boolean status, String message, T data) {
        super(status, message);
        this.data = data;
    }
    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        SuccessResponse<?> that = (SuccessResponse<?>) o;

        return Objects.equals(data, that.data);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (data != null ? data.hashCode() : 0);
        return result;
    }
}

