package com.spd.trello.security.extrafilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class AccessRightsCheckerFactory {
    private CardChecker cardChecker;
    private WorkspaceChecker workspaceChecker;
    private BoardChecker boardChecker;
    private CardListChecker cardListChecker;

    @Autowired
    public AccessRightsCheckerFactory(CardChecker cardChecker, WorkspaceChecker workspaceChecker, BoardChecker boardChecker, CardListChecker cardListChecker) {
        this.cardChecker = cardChecker;
        this.workspaceChecker = workspaceChecker;
        this.boardChecker = boardChecker;
        this.cardListChecker = cardListChecker;
    }

    public void checkAccess(HttpServletRequest request) {
        String uri = request.getRequestURI();
        if (uri.contains("workspaces"))
            workspaceChecker.checkAuthority(request);
        if (uri.contains("boards"))
            boardChecker.checkAuthority(request);
        if (uri.contains("cards"))
            cardChecker.checkAuthority(request);
        if (uri.contains("cardlists"))
            cardListChecker.checkAuthority(request);

    }
}
