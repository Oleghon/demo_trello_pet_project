package com.spd.trello.security.extrafilter;

import com.spd.trello.domain.enums.Permission;
import com.spd.trello.domain.resources.Member;
import com.spd.trello.domain.resources.User;
import com.spd.trello.domain.resources.WorkSpace;
import com.spd.trello.exception.SecurityAccessException;
import com.spd.trello.repository_jpa.MemberRepository;
import com.spd.trello.repository_jpa.UserRepository;
import com.spd.trello.repository_jpa.WorkspaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

@Component
public class WorkspaceChecker {

    private UserRepository userRepository;
    private MemberRepository memberRepository;
    private WorkspaceRepository workspaceRepository;
    private Pattern regex = Pattern.compile("(/\\w+[/a-z]/)");

    @Autowired
    public WorkspaceChecker(UserRepository userRepository, MemberRepository memberRepository, WorkspaceRepository workspaceRepository) {
        this.userRepository = userRepository;
        this.memberRepository = memberRepository;
        this.workspaceRepository = workspaceRepository;
    }

    public boolean checkAuthority(HttpServletRequest request) throws IOException {
        String requestURI = request.getRequestURI();
        User user = getAuthorizedUser();
        boolean flag = false;
        String method = request.getMethod();
        switch (method) {
            case "GET":
                if (!regex.matcher(requestURI).find()) {
                    flag = true;
                } else
                    flag = readWorkspaceAccessCheck(getIdFromRequest(request), user);
                break;
            case "PUT":
                flag = checkMembership(getIdFromRequest(request), user, Permission.UPDATE);
                break;
            case "DELETE":
                flag = checkMembership(getIdFromRequest(request), user, Permission.WRITE);
                break;
            case "POST":
                flag = true;
                break;
        }
        return flag;
    }

    private boolean checkMembership(String request, User user, Permission permission) {
        Member member = findMemberByUserIdAndWorkspaceId(request, user.getId());
        return member.getRole().getPermissions().contains(permission);
    }

    private boolean readWorkspaceAccessCheck(String requestId, User user) {
        boolean flag = false;
        WorkSpace workSpace = workspaceRepository.findById(UUID.fromString(requestId)).get();
        switch (workSpace.getVisibility()) {
            case PUBLIC:
                flag = true;
                break;
            case PRIVATE:
                flag = findMemberByUserIdAndWorkspaceId(requestId, user.getId()) != null;
                break;
        }
        return flag;
    }

    private String getIdFromRequest(HttpServletRequest request) {
        return request.getRequestURI().replaceAll(regex.pattern(), "");
    }

    private User getAuthorizedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userByEmail = userRepository.findUserByEmail(authentication.getName()).get();
        return userByEmail;
    }

    private Member findMemberByUserIdAndWorkspaceId(String workSpaceId, UUID userId) {
        List<Member> byUserIdAndWorkspacesExists = memberRepository.findByUserIdAndWorkspacesExists(userId, UUID.fromString(workSpaceId));
        if (!byUserIdAndWorkspacesExists.isEmpty())
            return byUserIdAndWorkspacesExists.get(0);
        throw new SecurityAccessException("Member not exist");
    }
}
