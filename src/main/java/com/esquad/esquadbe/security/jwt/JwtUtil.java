package com.esquad.esquadbe.security.jwt;

import lombok.experimental.UtilityClass;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;



@UtilityClass
public class JwtUtil {

    public long getLoginId(final Authentication authentication) throws AccessDeniedException {
        checkAuth(authentication);

        return Long.parseLong(authentication.getPrincipal().toString());
    }

    private void checkAuth(final Authentication authentication) throws AccessDeniedException {
        if(authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("로그인 정보가 존재하지 않습니다.");
        }
    }
}
