package com.carlinker.configuration;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class CorsFilter implements Filter {
    private final String clientAppurl = "http://localhost:5173";

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) res;
        HttpServletRequest request = (HttpServletRequest) req;
        Map<String, String> map = new HashMap<>();
        String originHeader = request.getHeader("Origin");

        // Check if the origin is allowed (case-insensitive check)
        if (originHeader != null && originHeader.toLowerCase().startsWith(clientAppurl.toLowerCase())) {
            response.setHeader("Access-Control-Allow-Origin", originHeader);
        }

        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PUT");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "*");

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            // Allow the preflight request by responding with the necessary headers
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            chain.doFilter(req, res);
        }
    }

    @Override
    public void init(FilterConfig filterConfig) {
        // Initialization code if needed
    }

    @Override
    public void destroy() {
        // Cleanup code if needed
    }
}
