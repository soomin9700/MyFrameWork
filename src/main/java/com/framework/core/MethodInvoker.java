package com.framework.core;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MethodInvoker {

    public static Object invoquer(RouteHandler handler) throws Exception {

        Class<?> classeControleur = Class.forName(handler.getControllerClassName());
        // Instancie le controller
        Object instanceControleur = classeControleur.getDeclaredConstructor().newInstance();
        Method methode = classeControleur.getDeclaredMethod(handler.getMethodName());

        try {
            // Retourne ce que renvoie la methode
            return methode.invoke(instanceControleur);
        } catch (InvocationTargetException e) {
            throw new Exception("Erreur dans la methode " + handler + " : " + e.getCause(), e.getCause());
        }
    }
}