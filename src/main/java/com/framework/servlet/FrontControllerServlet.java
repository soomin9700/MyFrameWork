package com.framework.servlet;

import java.io.IOException;
import java.util.Map;

import com.framework.core.MethodInvoker;
import com.framework.core.ModelAndView;
import com.framework.core.RouteHandler;
import com.framework.core.RouteKey;
import com.framework.core.ViewResolver;

import jakarta.servlet.RequestDispatcher;
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

        ServletContext context = getServletContext();
        Map<RouteKey, RouteHandler> routeTable = (Map<RouteKey, RouteHandler>) context.getAttribute("routeTable");

        if (routeTable == null) {
            throw new ServletException("routeTable non trouvee dans le context : le listener a-t-il bien tourne ?");
        }

        lireMethodeEtClasse(request, response, routeTable);
    }

    private void lireMethodeEtClasse(HttpServletRequest request, HttpServletResponse response,
            Map<RouteKey, RouteHandler> routeTable) throws ServletException, IOException {

        String url = request.getRequestURI().substring(request.getContextPath().length());
        RouteKey cleDemandee = new RouteKey(url, request.getMethod());

        RouteHandler handler = routeTable.get(cleDemandee);

        if (handler == null) {
            response.setContentType("text/plain");
            response.getWriter().println("Aucune route ne correspond a : " + cleDemandee);
            response.getWriter().println("Routes disponibles :");
            for (RouteKey routeDisponible : routeTable.keySet()) {
                response.getWriter().println(" - " + routeDisponible);
            }
            return;
        }

        Object resultat;
        try {
            resultat = MethodInvoker.invoquer(handler);
        } catch (Exception e) {
            throw new ServletException("Erreur lors de l'invocation de " + handler, e);
        }

        // ModelAndView 
        if (resultat instanceof ModelAndView) {
            traiterModelAndView(request, response, (ModelAndView) resultat);
            return;
        }

        response.setContentType("text/plain");
        response.getWriter().println("Route executee : " + cleDemandee);
        response.getWriter().println("Controller     : " + handler.getControllerClassName());
        response.getWriter().println("Methode        : " + handler.getMethodName());
        response.getWriter().println("Resultat brut  : " + resultat);
    }

    private void traiterModelAndView(HttpServletRequest request, HttpServletResponse response, ModelAndView mv)
            throws ServletException, IOException {

        ServletContext context = getServletContext();
        String prefixe = context.getInitParameter("viewPrefix");
        String suffixe = context.getInitParameter("viewSuffix");

        String cheminVue = ViewResolver.resoudreChemin(prefixe, mv.getVue(), suffixe);

        if (cheminVue == null) {
            throw new ServletException("ModelAndView sans vue definie");
        }

        for (Map.Entry<String, Object> entree : mv.getDonnees().entrySet()) {
            request.setAttribute(entree.getKey(), entree.getValue());
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher(cheminVue);
        dispatcher.forward(request, response);
    }
}