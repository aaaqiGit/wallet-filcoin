package com.eth.filecoin.admin;

import java.util.Objects;

/**
 * @author: aqi
 * @Date: 2021/5/13 12:06
 * @Description:
 */
public class AuthenContext {
    private static ThreadLocal<AuthenRequest> AuthenContext = new ThreadLocal();

    public static AuthenRequest get() {
        if (Objects.isNull(AuthenContext.get())) {
            AuthenContext.set(new AuthenRequest());
        }
        return AuthenContext.get();
    }

    public static void set(AuthenRequest authenRequest) {
        AuthenContext.set(authenRequest);
    }

    public static void clean() {
        AuthenContext.remove();
    }
}