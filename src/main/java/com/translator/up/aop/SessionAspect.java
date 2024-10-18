package com.translator.up.aop;

import com.translator.up.entity.UserEntity;
import com.translator.up.exception.user.SessionNotFoundException;
import jakarta.servlet.http.HttpSession;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class SessionAspect {

    @Autowired
    private HttpSession session;

    @Before("@annotation(SessionRequired)")
    public void checkSession() throws Throwable {
        UserEntity user = (UserEntity) session.getAttribute("user");
        if (user == null) {
            throw new SessionNotFoundException("Login required");
        }
    }
}
