package com.gridsig.audits.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class RequestResponseInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(RequestResponseInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Log the incoming request details (you can include headers, body, etc.)
        logger.info("Request: {} {} - Headers: {}", request.getMethod(), request.getRequestURI(), request.getHeaderNames());
        return true;  // continue with the request handling
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // Log the response status
        logger.info("Response: {} {} - Status: {}", request.getMethod(), request.getRequestURI(), response.getStatus());
    }
}
