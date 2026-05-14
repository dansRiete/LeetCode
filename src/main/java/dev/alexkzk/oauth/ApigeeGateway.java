package dev.alexkzk.oauth;

import java.security.PublicKey;

/**
 * Simulates Apigee (API Gateway).
 *
 * Sits in front of all Spring Boot services.
 * First line of defence — rejects invalid/expired tokens before they reach your app.
 *
 * Uses Okta's PUBLIC key (fetched from JWKS endpoint at startup).
 * Does NOT do fine-grained authorization — just validates the token is legit.
 */
public class ApigeeGateway {

    private final JwtVerifier verifier;

    public ApigeeGateway(PublicKey oktaPublicKey) {
        // Apigee fetches Okta's public key from JWKS endpoint once at startup
        this.verifier = new JwtVerifier(oktaPublicKey);
        System.out.println("[Apigee] Loaded Okta public key from JWKS endpoint.");
    }

    // Returns the verified claims to pass downstream, or throws if invalid
    public JwtVerifier.JwtClaims validateAndRoute(String bearerToken, String targetService) throws Exception {
        System.out.println("[Apigee] Incoming request to: " + targetService);
        System.out.println("[Apigee] Validating Bearer token...");

        JwtVerifier.JwtClaims claims = verifier.verify(bearerToken);
        System.out.println("[Apigee] Token valid. User: " + claims.subject() + " → routing to " + targetService);
        return claims;
    }
}
