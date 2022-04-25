package com.spd.trello.security;

import com.spd.trello.exception.SecurityAccessException;
import com.spd.trello.security.extrafilter.WorkspaceChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomFilter extends GenericFilterBean {

    private final WorkspaceChecker checker;

    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;

    @Autowired
    public CustomFilter(WorkspaceChecker checker, HandlerExceptionResolver resolver) {
        this.checker = checker;
        this.resolver = resolver;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        try {
            if (httpServletRequest.getRequestURI().contains("workspaces")) {
                if (!checker.checkAuthority(httpServletRequest))
                    throw new SecurityAccessException("Not have enough access rights");
            }
            chain.doFilter(request, response);
        } catch (Exception e) {
            resolver.resolveException(httpServletRequest, (HttpServletResponse) response, null, e);
        }
    }
}
