package com.framework.servlet;

import java.io.IOException;
import java.util.Map;

import com.framework.core.MethodInvoker;
import com.framework.core.RouteHandler;
import com.framework.core.RouteKey;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class FrontControllerServlet extends HttpServlet {

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

    @SuppressWarnings("unchecked")
    private void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/plain");

        ServletContext context = getServletContext();
        Map<RouteKey, RouteHandler> routeTable = (Map<RouteKey, RouteHandler>) context.getAttribute("routeTable");

        if (routeTable == null) {
            throw new ServletException("routeTable non trouvee dans le context : le listener a-t-il bien tourne ?");
        }

        lireMethodeEtClasse(request, response, routeTable);
    }

    /**
     * Recherche la route correspondant a la requete dans la map fournie,
     * invoque la methode trouvee, et ecrit le resultat dans la reponse.
     */
    private void lireMethodeEtClasse(HttpServletRequest request, HttpServletResponse response,
            Map<RouteKey, RouteHandler> routeTable) throws ServletException, IOException {

        String url = request.getRequestURI().substring(request.getContextPath().length());
        RouteKey cleDemandee = new RouteKey(url, request.getMethod());

        RouteHandler handler = routeTable.get(cleDemandee);

        if (handler == null) {
            response.getWriter().println("Aucune route ne correspond a : " + cleDemandee);
            response.getWriter().println("Routes disponibles :");
            for (RouteKey routeDisponible : routeTable.keySet()) {
                response.getWriter().println(" - " + routeDisponible);
            }
            return;
        }

        try {
            Object resultat = MethodInvoker.invoquer(handler);

            response.getWriter().println("Route executee : " + cleDemandee);
            response.getWriter().println("Controller     : " + handler.getControllerClassName());
            response.getWriter().println("Methode        : " + handler.getMethodName());
            response.getWriter().println("Resultat       : " + resultat);

        } catch (Exception e) {
            throw new ServletException("Erreur lors de l'invocation de " + handler, e);
        }
    }
}