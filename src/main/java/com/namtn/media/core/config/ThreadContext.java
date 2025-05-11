package com.namtn.media.core.config;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author tuananh
 * @version 0.0.1
 * @date 15/07/2024
 */
public class ThreadContext {
    private static final ThreadLocal<UserDetails> userHolder = new ThreadLocal<>();

    public static UserDetails getCurrentUser() {
        return userHolder.get();
    }

    public static void setCurrentUser(UserDetails user) {
        userHolder.set(user);
    }
}
