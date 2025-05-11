package com.namtn.media.core.util;

public class ResponseUtil {
    public static <T> SuccessResponse<T> ok(T data){
        SuccessResponse ret = new SuccessResponse(data);
        ret.setStatus(true);
        ret.setMessage("success");
        return ret;
    }

    public static <T> SuccessResponse<T> ok(T data,String message){
        SuccessResponse ret = new SuccessResponse(data);
        ret.setStatus(true);
        ret.setMessage(message);
        return ret;
    }
}
