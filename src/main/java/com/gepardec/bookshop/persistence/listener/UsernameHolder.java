package com.gepardec.bookshop.persistence.listener;

public class UsernameHolder {

    private UsernameHolder() {
        // nop
    }

    private static final ThreadLocal<String> USER = new ThreadLocal<>();

    public static void set(String username) {
        USER.set(username);
    }

    public static String get() {
        return USER.get();
    }

    public static void clear() {
        USER.remove();
    }
}
