package com.framework.core;

import org.springframework.context.ApplicationContext;

public class SpringContextHolder {

    private static ApplicationContext context;

    public static void set(ApplicationContext ctx) {
        context = ctx;
    }

    public static <T> T getBean(Class<T> type) {
        if (context == null) {
            throw new IllegalStateException("Le contexte Spring n'a pas ete initialise");
        }
        return context.getBean(type);
    }
}