package com.framework.listener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.framework.annotation.Controller;
import com.framework.core.AnnotationChecker;
import com.framework.core.PackageScanner;
import com.framework.core.RouteHandler;
import com.framework.core.RouteKey;
import com.framework.core.RouteResolver;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

public class ApplicationStartupListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        ServletContext context = sce.getServletContext();
        String packageControllers = context.getInitParameter("controllerPackage");

        Map<RouteKey, RouteHandler> routeTable = new HashMap<>();

        try {
            RouteResolver.remplir(packageControllers, routeTable);

            List<Class<?>> classesDuPackage = PackageScanner.scanPackage(packageControllers);
            List<String> listController = new java.util.ArrayList<>();
            for (Class<?> classe : classesDuPackage) {
                if (AnnotationChecker.surClasse(classe, Controller.class)) {
                    listController.add(classe.getName());
                }
            }
            context.setAttribute("listController", listController);

        } catch (Exception e) {
            //  empecher l'application de demarrer si une route est en double
            throw new RuntimeException("Demarrage annule : " + e.getMessage(), e);
        }

        context.setAttribute("routeTable", routeTable);

        System.out.println("=== Application demarree (via listener) ===");
        System.out.println("Package scanne  : " + packageControllers);
        System.out.println("Routes chargees : " + routeTable.size());

        for (Map.Entry<RouteKey, RouteHandler> entree : routeTable.entrySet()) {
            System.out.println(" -> " + entree.getKey() + " => " + entree.getValue());
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // rien a liberer pour l'instant
    }
}