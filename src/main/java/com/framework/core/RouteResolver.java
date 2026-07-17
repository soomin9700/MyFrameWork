package com.framework.core;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import com.framework.annotation.Controller;
import com.framework.annotation.UrlMapping;

public class RouteResolver {

    public static void remplir(String packageControllers, Map<RouteKey, RouteHandler> table) throws Exception {

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
                System.out.println("Methode trouvee : " + methode.getName());
                UrlMapping infoUrl = methode.getAnnotation(UrlMapping.class);
                RouteKey cle = new RouteKey(infoUrl.value(), infoUrl.method());

                if (table.containsKey(cle)) {
                    throw new Exception("Url deja prise : " + cle);
                }

                // Retourne une table url -> RouteHandler.
                table.put(cle, new RouteHandler(classe.getName(), methode.getName()));
            }
        }
    }
}