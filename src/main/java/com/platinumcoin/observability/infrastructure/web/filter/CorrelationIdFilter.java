package com.platinumcoin.observability.infrastructure.web.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
public class CorrelationIdFilter extends OncePerRequestFilter {
    private static final String HDR = "x-correlation-id";
    private static final String MDC_KEY = "correlationId";

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {
        String cid = req.getHeader(HDR);
        if (cid == null || cid.isBlank()) cid = UUID.randomUUID().toString();
        MDC.put(MDC_KEY, cid);
        res.setHeader(HDR, cid);
        try { chain.doFilter(req, res); } finally { MDC.remove(MDC_KEY); }
    }
}