package edu.sunyulster.genie.auth.utils;

import java.util.Date;

import com.ibm.websphere.security.jwt.Claims;
import com.ibm.websphere.security.jwt.InvalidBuilderException;
import com.ibm.websphere.security.jwt.InvalidClaimException;
import com.ibm.websphere.security.jwt.JwtBuilder;
import com.ibm.websphere.security.jwt.JwtException;

import jakarta.ws.rs.core.Cookie;
import jakarta.ws.rs.core.NewCookie;

public class AuthUtils {
    public static String buildJwt(String userId, String[] roles) throws JwtException {
        try {
            return JwtBuilder.create("jwtBuilderConfig")
                    .claim(Claims.SUBJECT, userId)
                    .claim("upn", userId)
                    .claim("groups", roles)
                    .claim("aud", "wishlist-app")
                    .buildJwt()
                    .compact();
        } catch (JwtException | InvalidClaimException | InvalidBuilderException e) {
            throw new JwtException("Error building JWT: " + e.getMessage());
        }
    }

    public static NewCookie createCookie(String sessionId, int ttl, Date expirationDate) {
        // create cookie with session Id
        // Cookie cookie = new Cookie("auth-session", sessionId, "/api", "127.0.0.1");
        Cookie cookie = new Cookie("auth-session", sessionId);
        NewCookie newCookie = new NewCookie(cookie, 
            "Session cookie for auth-server", 
            ttl, 
            expirationDate, 
            false, 
            false);
            System.out.println(newCookie);
        return newCookie;
    }
}
