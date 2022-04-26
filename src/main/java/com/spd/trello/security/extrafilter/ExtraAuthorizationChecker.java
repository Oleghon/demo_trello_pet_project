package com.spd.trello.security.extrafilter;

import com.spd.trello.domain.Domain;

import javax.servlet.http.HttpServletRequest;

public interface ExtraAuthorizationChecker <D extends Domain>{

    void checkAuthority(HttpServletRequest request);


}
