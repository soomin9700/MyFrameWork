package com.framework.core;

public class RouteHandler {

    private final String controllerClassName;
    private final String methodName;

    public RouteHandler(String controllerClassName, String methodName) {
        this.controllerClassName = controllerClassName;
        this.methodName = methodName;
    }

    public String getControllerClassName() {
        return controllerClassName;
    }

    public String getMethodName() {
        return methodName;
    }

    @Override
    public String toString() {
        return controllerClassName + "#" + methodName;
    }
}