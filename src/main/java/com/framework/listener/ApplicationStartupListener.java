package com.framework.listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.framework.annotation.Controller;
import com.framework.core.AnnotationChecker;
import com.framework.core.PackageScanner;
import com.framework.core.RouteHandler;
import com.framework.core.RouteKey;
import com.framework.core.RouteResolver;
import com.framework.core.SpringContextHolder;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

public class ApplicationStartupListener implements ServletContextListener {

    private AnnotationConfigApplicationContext springContext;

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        ServletContext context = sce.getServletContext();
        String packageControllers = context.getInitParameter("controllerPackage");

        Map<RouteKey, RouteHandler> routeTable = new HashMap<>();

        try {
            RouteResolver.remplir(packageControllers, routeTable);

            List<Class<?>> classesDuPackage = PackageScanner.scanPackage(packageControllers);
            List<String> listController = new ArrayList<>();
            for (Class<?> classe : classesDuPackage) {
                if (AnnotationChecker.surClasse(classe, Controller.class)) {
                    listController.add(classe.getName());
                }
            }
            context.setAttribute("listController", listController);

        } catch (Exception e) {
            throw new RuntimeException("Demarrage annule : " + e.getMessage(), e);
        }

        context.setAttribute("routeTable", routeTable);

        demarrerSpring(context);

        System.out.println("=== Application demarree (via listener) ===");
        System.out.println("Package scanne  : " + packageControllers);
        System.out.println("Routes chargees : " + routeTable.size());

        for (Map.Entry<RouteKey, RouteHandler> entree : routeTable.entrySet()) {
            System.out.println(" -> " + entree.getKey() + " => " + entree.getValue());
        }
    }

    private void demarrerSpring(ServletContext context) {

        String configClassName = context.getInitParameter("springConfigClass");

        if (configClassName == null || configClassName.isEmpty()) {
            System.out.println("Aucun springConfigClass fourni : Spring n'est pas demarre");
            return;
        }

        try {
            Class<?> configClass = Class.forName(configClassName);
            springContext = new AnnotationConfigApplicationContext(configClass);
            SpringContextHolder.set(springContext);

            System.out.println("Contexte Spring demarre avec : " + configClassName);

        } catch (Exception e) {
            throw new RuntimeException("Impossible de demarrer le contexte Spring", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (springContext != null) {
            springContext.close();
        }
    }
}