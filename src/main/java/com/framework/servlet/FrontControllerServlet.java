package com.framework.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.framework.annotation.Controller;
import com.framework.core.AnnotationChecker;
import com.framework.core.PackageScanner;
import com.framework.core.RouteHandler;
import com.framework.core.RouteResolver;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class FrontControllerServlet extends HttpServlet {

    private List<String> listController = new ArrayList<>();
    private Map<String, RouteHandler> routeTable;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        String packageControllers = config.getInitParameter("controllerPackage");

        try {
            List<Class<?>> classesDuPackage = PackageScanner.scanPackage(packageControllers);

            for (Class<?> classe : classesDuPackage) {
                if (AnnotationChecker.surClasse(classe, Controller.class)) {
                    listController.add(classe.getName());
                }
            }

            routeTable = RouteResolver.resoudre(packageControllers);

            System.out.println("=== Initialisation du FrontController ===");
            System.out.println("Package scanne : " + packageControllers);
            System.out.println("Controllers trouves : " + listController.size());
            System.out.println("Routes trouvees : " + routeTable.size());

            for (Map.Entry<String, RouteHandler> entree : routeTable.entrySet()) {
                System.out.println(" -> " + entree.getKey() + " => " + entree.getValue());
            }

        } catch (Exception e) {
            throw new ServletException("Impossible d'initialiser le FrontController", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("text/plain");

        String url = request.getRequestURI().substring(request.getContextPath().length());

        RouteHandler handler = routeTable.get(url);

        if (handler == null) {
            response.getWriter().println("Aucune route ne correspond a : " + url);
            response.getWriter().println("Routes disponibles :");
            for (String routeDisponible : routeTable.keySet()) {
                response.getWriter().println(" - " + routeDisponible);
            }
            return;
        }

        response.getWriter().println("URL demandee   : " + url);
        response.getWriter().println("Controller     : " + handler.getControllerClassName());
        response.getWriter().println("Methode        : " + handler.getMethodName());
    }
}