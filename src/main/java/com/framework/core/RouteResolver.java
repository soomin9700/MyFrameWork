package com.framework.core;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.framework.annotation.Controller;
import com.framework.annotation.UrlMapping;

public class RouteResolver {

    public static Map<String, RouteHandler> resoudre(String packageControllers) throws Exception {
        Map<String, RouteHandler> table = new HashMap<>();

        List<Class<?>> classes = PackageScanner.scanPackage(packageControllers);

        //  Parcourt le package donne, retient les classes annotees @Controller,
        for (Class<?> classe : classes) {

            if (!AnnotationChecker.surClasse(classe, Controller.class)) {
                continue;
            }

            for (Method methode : classe.getDeclaredMethods()) {

                if (!AnnotationChecker.surMethode(methode, UrlMapping.class)) {
                    continue;
                }

                // Pour chacune, les methodes annotees @UrlMapping

                UrlMapping infoUrl = methode.getAnnotation(UrlMapping.class);
                String url = infoUrl.value();

                if (table.containsKey(url)) {
                    throw new Exception("Cette url est deja associee a une methode : " + url);
                }

                // Retourne une table url -> RouteHandler.
                table.put(url, new RouteHandler(classe.getName(), methode.getName()));
            }
        }

        return table;
    }
}