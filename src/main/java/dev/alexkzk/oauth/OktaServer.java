package dev.alexkzk.oauth;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Simulates Okta (Authorization Server / Identity Provider).
 *
 * Responsibilities:
 * - Owns the private key (never shared)
 * - Issues signed JWT access tokens after user authenticates
 * - Issues refresh tokens
 * - Publishes public key (JWKS endpoint simulation)
 * - Revokes refresh tokens on logout
 */
public class OktaServer {

    private final KeyPair keyPair;
    private final Map<String, String> refreshTokenStore = new HashMap<>(); // refreshToken → userId

    public OktaServer() throws NoSuchAlgorithmException {
        KeyPairGenerator gen = KeyPairGenerator.getInstance("RSA");
        gen.initialize(2048);
        this.keyPair = gen.generateKeyPair();
        System.out.println("[Okta] RSA key pair generated. Public key published to JWKS endpoint.");
    }

    // Step 1: User authenticates (Authorization Code flow - after redirect & code exchange)
    public TokenResponse issueTokens(String userId, String[] roles) throws Exception {
        String accessToken = buildJwt(userId, roles, 60); // 60 seconds TTL
        String refreshToken = UUID.randomUUID().toString();
        refreshTokenStore.put(refreshToken, userId);

        System.out.println("[Okta] Issued access token for user: " + userId);
        System.out.println("[Okta] Issued refresh token (stored server-side)");
        return new TokenResponse(accessToken, refreshToken);
    }

    // Step 2: Client exchanges refresh token for new access token
    public TokenResponse refresh(String refreshToken, String[] roles) throws Exception {
        String userId = refreshTokenStore.get(refreshToken);
        if (userId == null) {
            throw new SecurityException("[Okta] Refresh token invalid or revoked!");
        }
        String newAccessToken = buildJwt(userId, roles, 60);
        System.out.println("[Okta] Issued new access token via refresh for user: " + userId);
        return new TokenResponse(newAccessToken, refreshToken);
    }

    // Step 3: Revoke on logout
    public void revokeRefreshToken(String refreshToken) {
        refreshTokenStore.remove(refreshToken);
        System.out.println("[Okta] Refresh token revoked. User must re-authenticate.");
    }

    // Public key shared with all verifiers (Apigee, Spring Boot apps)
    public PublicKey getPublicKey() {
        return keyPair.getPublic();
    }

    // Minimal JWT: base64(header).base64(payload).signature
    private String buildJwt(String userId, String[] roles, int ttlSeconds) throws Exception {
        String header = Base64.getUrlEncoder().withoutPadding()
                .encodeToString("{\"alg\":\"RS256\",\"typ\":\"JWT\"}".getBytes());

        long exp = System.currentTimeMillis() / 1000 + ttlSeconds;
        String payloadJson = String.format(
                "{\"sub\":\"%s\",\"roles\":[\"%s\"],\"exp\":%d,\"iat\":%d}",
                userId, String.join("\",\"", roles), exp, System.currentTimeMillis() / 1000
        );
        String payload = Base64.getUrlEncoder().withoutPadding()
                .encodeToString(payloadJson.getBytes());

        String signingInput = header + "." + payload;
        Signature sig = Signature.getInstance("SHA256withRSA");
        sig.initSign(keyPair.getPrivate());
        sig.update(signingInput.getBytes(StandardCharsets.UTF_8));
        String signature = Base64.getUrlEncoder().withoutPadding().encodeToString(sig.sign());

        return signingInput + "." + signature;
    }

    public record TokenResponse(String accessToken, String refreshToken) {}
}
