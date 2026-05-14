package dev.alexkzk.oauth;

import java.security.PublicKey;

/**
 * Simulates your Spring Boot REST service.
 *
 * In real Spring Boot: configured via spring.security.oauth2.resourceserver.jwt.jwk-set-uri
 * Spring Security auto-validates the JWT and populates SecurityContext.
 *
 * Here: also validates the JWT (defence in depth after Apigee),
 * then applies fine-grained authorization based on roles.
 */
public class SpringBootService {

    private final JwtVerifier verifier;
    private final String serviceName;

    public SpringBootService(String serviceName, PublicKey oktaPublicKey) {
        this.serviceName = serviceName;
        // Fetches Okta public key from jwk-set-uri at startup (Spring Security does this automatically)
        this.verifier = new JwtVerifier(oktaPublicKey);
        System.out.println("[" + serviceName + "] Loaded Okta public key. Ready to validate JWTs.");
    }

    // Simulates a GET /users/{id} endpoint — any authenticated user
    public String getUser(String bearerToken, String userId) throws Exception {
        System.out.println("\n[" + serviceName + "] GET /users/" + userId);
        JwtVerifier.JwtClaims claims = verifier.verify(bearerToken);
        // Spring Security: @PreAuthorize("isAuthenticated()")
        System.out.println("[" + serviceName + "] Authorized. Returning user data for: " + userId);
        return "User{id=" + userId + ", requestedBy=" + claims.subject() + "}";
    }

    // Simulates a DELETE /users/{id} endpoint — ADMIN only
    public String deleteUser(String bearerToken, String userId) throws Exception {
        System.out.println("\n[" + serviceName + "] DELETE /users/" + userId);
        JwtVerifier.JwtClaims claims = verifier.verify(bearerToken);

        // Spring Security: @PreAuthorize("hasRole('ADMIN')")
        if (!claims.roles().contains("ADMIN")) {
            throw new SecurityException("[" + serviceName + "] 403 Forbidden — ADMIN role required. User has: " + claims.roles());
        }
        System.out.println("[" + serviceName + "] ADMIN authorized. Deleting user: " + userId);
        return "Deleted user: " + userId;
    }
}
