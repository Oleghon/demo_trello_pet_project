package com.spd.trello.security;

import com.spd.trello.exception.SecurityAccessException;
import com.spd.trello.security.extrafilter.AccessRightsCheckerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;
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

    private AccessRightsCheckerFactory checker;
    @Autowired
    @Qualifier(value = "handlerExceptionResolver")
    private HandlerExceptionResolver resolver;

    @Autowired
    public CustomFilter(AccessRightsCheckerFactory checker) {
        this.checker = checker;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        try {
            if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
                checker.checkAccess(httpServletRequest);
            } else throw new SecurityAccessException();
        } catch (Exception e) {
            this.resolver.resolveException(httpServletRequest, (HttpServletResponse) response, null, e);
        }
        chain.doFilter(request, response);
    }
}
