package com.tubes.pbw;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tubes.pbw.user.User;

import jakarta.servlet.http.HttpSession;
import java.util.Arrays;

@Aspect
@Component
public class AuthorizationAspect {

    @Autowired
    private HttpSession session;
    
    @Before("@annotation(requiredRole)")
    public void checkAuthorization(JoinPoint joinPoint, RequiredRole requiredRole) throws Throwable {

        if (session == null) {
            throw new SecurityException("Session is not available.");
        }

        // String username = (String) session.getAttribute("username");
        // String role = (String) session.getAttribute("role");

        User user = (User) session.getAttribute("user");

        if (user == null) {
            throw new SecurityException("User is not logged in.");
        }

        String role = user.getRole();

        if ("*".equals(requiredRole.value()[0])) {
            return;
        }

        if (role == null || !Arrays.asList(requiredRole.value()).contains(role)) {
            throw new SecurityException("User does not have the required role.");
        }

    }

}
