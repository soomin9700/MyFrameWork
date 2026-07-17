package com.framework.core;

import java.util.Objects;

public class RouteKey {

    private final String url;
    private final String httpMethod;

    public RouteKey(String url, String httpMethod) {
        this.url = url;
        this.httpMethod = (httpMethod == null || httpMethod.isEmpty()) ? "GET" : httpMethod.toUpperCase();
    }

    public String getUrl() {
        return url;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof RouteKey)) {
            return false;
        }
        RouteKey autre = (RouteKey) obj;
        return url.equals(autre.url) && httpMethod.equals(autre.httpMethod);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, httpMethod);
    }

    @Override
    public String toString() {
        return httpMethod + " " + url;
    }
}