package com.gepardec.bookshop.persistence.listener;

// TODO 3: use it!
public class UsernameHolder {

    private static final ThreadLocal<String> USER = new ThreadLocal<>();

    private UsernameHolder() {
        // nop
    }

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
