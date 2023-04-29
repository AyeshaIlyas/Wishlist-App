package edu.sunyulster.genie.auth.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.Random;

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

    public static String hashPassword(String p) throws NoSuchAlgorithmException {
        // generate random salt - recommend to be >= length of hash
        final int HASH_LEN = 256;
        Random random = new Random();
        byte[] salt = new byte[HASH_LEN];
        random.nextBytes(salt);
        // convert salt to base64 string
        String saltString = toBase64(salt);

        // hash password + salt using sha256
        return getSaltedHash(p, saltString);
    }

    public static boolean verifyPassword(String plainText, String saltedHash) throws NoSuchAlgorithmException {
        String salt = saltedHash.split("\\.")[1];
        String hash = getSaltedHash(plainText, salt);
        System.out.println(hash);
        System.out.println(hash);
        System.out.println(hash.equals(saltedHash));
        return hash.equals(saltedHash);
    }

    private static String toBase64(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    private static String getSaltedHash(String s, String salt) throws NoSuchAlgorithmException {
        // hash password + salt using sha256
        MessageDigest hasher = MessageDigest.getInstance("SHA-256");
        String hash = toBase64(hasher.digest((s + salt).getBytes(StandardCharsets.UTF_8)));
        return String.format("%s.%s", hash, salt);
    }
}
