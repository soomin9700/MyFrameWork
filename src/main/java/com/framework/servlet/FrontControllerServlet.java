package com.framework.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.framework.annotation.Controller;
import com.framework.core.AnnotationChecker;
import com.framework.core.PackageScanner;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class FrontControllerServlet extends HttpServlet {

    private List<String> listController = new ArrayList<>();

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

            System.out.println("=== Initialisation du FrontController ===");
            System.out.println("Package scanne : " + packageControllers);
            System.out.println("Controllers trouves : " + listController.size());

            for (String nomController : listController) {
                System.out.println(" -> " + nomController);
            }

        } catch (Exception e) {
            throw new ServletException("Impossible d'initialiser la liste des controllers", e);
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

        String uri = request.getRequestURI();

        response.getWriter().println("FrontController OK");
        response.getWriter().println("URL appelée : " + uri);
        response.getWriter().println("Nombre de controllers detectes : " + listController.size());

        for (String nomController : listController) {
            response.getWriter().println(" - " + nomController);
        }
    }
}