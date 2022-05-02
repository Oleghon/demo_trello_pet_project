package com.spd.trello.security.extrafilter;

import com.spd.trello.configuration.UserContextHolder;
import com.spd.trello.security.extrafilter.wrapper.BufferedServletRequestWrapper;
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

    private static final String FILTER_APPLIED = "__spring_security_customFilter_filter_Applied";

    @Autowired
    public CustomFilter(AccessRightsCheckerFactory checker) {
        this.checker = checker;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletRequest wrappedRequest = new BufferedServletRequestWrapper(httpServletRequest);
        try {
            if (request.getAttribute(FILTER_APPLIED) == null) {
                checker.checkAccess(wrappedRequest);
                request.setAttribute(FILTER_APPLIED, true);
                chain.doFilter(wrappedRequest, response);
            } else {
                UserContextHolder.cleanUserContext(SecurityContextHolder.getContext().getAuthentication().getName());
                chain.doFilter(wrappedRequest, response);
            }
        } catch (Exception e) {
            request.setAttribute(FILTER_APPLIED, false);
            this.resolver.resolveException(httpServletRequest, (HttpServletResponse) response, null, e);
        }
    }
}
