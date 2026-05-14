package dev.alexkzk.oauth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Demonstrates the full OAuth2/OIDC flow:
 *
 * 1. Okta issues tokens after user authenticates
 * 2. Apigee validates token at the gateway (first checkpoint)
 * 3. Spring Boot validates token + applies fine-grained authz (second checkpoint)
 * 4. Access token expiry + refresh flow
 * 5. Tampered token rejected
 * 6. Revoked refresh token rejected
 */
class OAuthFlowTest {

    private OktaServer okta;
    private ApigeeGateway apigee;
    private SpringBootService userService;

    @BeforeEach
    void setUp() throws Exception {
        System.out.println("\n========== SETUP ==========");
        okta = new OktaServer();
        apigee = new ApigeeGateway(okta.getPublicKey());
        userService = new SpringBootService("UserService", okta.getPublicKey());
    }

    @Test
    @DisplayName("Happy path: USER authenticates, reads data via Apigee → Spring Boot")
    void happyPath_userCanReadData() throws Exception {
        System.out.println("\n========== TEST: Happy Path ==========");

        // Step 1: User logs in via Okta (Authorization Code flow)
        OktaServer.TokenResponse tokens = okta.issueTokens("alice", new String[]{"USER"});

        // Step 2: Apigee validates at the gateway
        apigee.validateAndRoute(tokens.accessToken(), "UserService");

        // Step 3: Spring Boot handles the request
        String result = userService.getUser(tokens.accessToken(), "42");
        System.out.println("[Test] Response: " + result);

        assertTrue(result.contains("alice"));
    }

    @Test
    @DisplayName("Authorization: USER cannot delete — ADMIN role required")
    void userCannotDelete_adminRequired() throws Exception {
        System.out.println("\n========== TEST: Role-based Authorization ==========");

        OktaServer.TokenResponse tokens = okta.issueTokens("alice", new String[]{"USER"});

        SecurityException ex = assertThrows(SecurityException.class,
                () -> userService.deleteUser(tokens.accessToken(), "42"));

        System.out.println("[Test] Correctly rejected: " + ex.getMessage());
        assertTrue(ex.getMessage().contains("403"));
    }

    @Test
    @DisplayName("Authorization: ADMIN can delete")
    void adminCanDelete() throws Exception {
        System.out.println("\n========== TEST: ADMIN Delete ==========");

        OktaServer.TokenResponse tokens = okta.issueTokens("bob-admin", new String[]{"USER", "ADMIN"});
        String result = userService.deleteUser(tokens.accessToken(), "42");

        System.out.println("[Test] Response: " + result);
        assertTrue(result.contains("Deleted"));
    }

    @Test
    @DisplayName("Security: tampered token is rejected by signature check")
    void tamperedToken_rejected() throws Exception {
        System.out.println("\n========== TEST: Tampered Token ==========");

        OktaServer.TokenResponse tokens = okta.issueTokens("alice", new String[]{"USER"});

        // Attacker tries to modify payload to add ADMIN role
        String[] parts = tokens.accessToken().split("\\.");
        String fakePayload = java.util.Base64.getUrlEncoder().withoutPadding()
                .encodeToString("{\"sub\":\"alice\",\"roles\":[\"ADMIN\"],\"exp\":9999999999,\"iat\":0}".getBytes());
        String tamperedToken = parts[0] + "." + fakePayload + "." + parts[2];

        System.out.println("[Attacker] Tampered token with ADMIN role...");
        SecurityException ex = assertThrows(SecurityException.class,
                () -> userService.deleteUser(tamperedToken, "42"));

        System.out.println("[Test] Correctly rejected: " + ex.getMessage());
        assertTrue(ex.getMessage().contains("tampered") || ex.getMessage().contains("invalid"));
    }

    @Test
    @DisplayName("Refresh flow: get new access token using refresh token")
    void refreshToken_issuesNewAccessToken() throws Exception {
        System.out.println("\n========== TEST: Refresh Flow ==========");

        OktaServer.TokenResponse original = okta.issueTokens("alice", new String[]{"USER"});
        System.out.println("[Client] Original access token obtained.");

        // Simulate access token expiry → client uses refresh token
        OktaServer.TokenResponse refreshed = okta.refresh(original.refreshToken(), new String[]{"USER"});
        System.out.println("[Client] New access token obtained via refresh.");

        // New token should work
        String result = userService.getUser(refreshed.accessToken(), "42");
        System.out.println("[Test] Response with refreshed token: " + result);
        assertTrue(result.contains("alice"));
    }

    @Test
    @DisplayName("Logout: revoked refresh token cannot be used")
    void revokedRefreshToken_rejected() throws Exception {
        System.out.println("\n========== TEST: Revocation ==========");

        OktaServer.TokenResponse tokens = okta.issueTokens("alice", new String[]{"USER"});

        // User logs out — Okta revokes refresh token
        okta.revokeRefreshToken(tokens.refreshToken());

        // Client tries to refresh → should fail
        SecurityException ex = assertThrows(SecurityException.class,
                () -> okta.refresh(tokens.refreshToken(), new String[]{"USER"}));

        System.out.println("[Test] Correctly rejected: " + ex.getMessage());
        assertTrue(ex.getMessage().contains("revoked") || ex.getMessage().contains("invalid"));
    }
}
