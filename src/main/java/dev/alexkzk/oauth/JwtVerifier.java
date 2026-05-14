package dev.alexkzk.oauth;

import java.nio.charset.StandardCharsets;
import java.security.PublicKey;
import java.security.Signature;
import java.util.Base64;

/**
 * Shared JWT verification logic — used by both Apigee and Spring Boot app.
 *
 * Verifies:
 * 1. Signature (proves Okta issued it, nobody tampered)
 * 2. Expiry (exp claim)
 *
 * Uses Okta's PUBLIC key only — private key never leaves Okta.
 */
public class JwtVerifier {

    private final PublicKey publicKey;

    public JwtVerifier(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public JwtClaims verify(String jwt) throws Exception {
        String[] parts = jwt.split("\\.");
        if (parts.length != 3) throw new SecurityException("Invalid JWT structure");

        // 1. Verify signature using Okta's public key (asymmetric RSA)
        String signingInput = parts[0] + "." + parts[1];
        byte[] signatureBytes = Base64.getUrlDecoder().decode(parts[2]);

        Signature sig = Signature.getInstance("SHA256withRSA");
        sig.initVerify(publicKey);
        sig.update(signingInput.getBytes(StandardCharsets.UTF_8));

        if (!sig.verify(signatureBytes)) {
            throw new SecurityException("JWT signature invalid — token was tampered with!");
        }

        // 2. Decode payload (NOT decryption — just base64 decode, payload is readable by anyone)
        String payloadJson = new String(Base64.getUrlDecoder().decode(parts[1]));
        System.out.println("  Decoded payload (visible to anyone): " + payloadJson);

        // 3. Check expiry
        long exp = extractLong(payloadJson, "exp");
        if (System.currentTimeMillis() / 1000 > exp) {
            throw new SecurityException("JWT expired!");
        }

        String subject = extractString(payloadJson, "sub");
        String roles = extractString(payloadJson, "roles");
        return new JwtClaims(subject, roles);
    }

    private String extractString(String json, String key) {
        int idx = json.indexOf("\"" + key + "\"");
        if (idx == -1) return "";
        int valueStart = json.indexOf(":", idx) + 1;
        String rest = json.substring(valueStart).trim();
        // Handle array values like ["USER","ADMIN"] — find closing bracket
        if (rest.startsWith("[")) {
            int end = rest.indexOf("]");
            return rest.substring(1, end).replaceAll("\"", "");
        }
        // Handle scalar string or number values
        int end = rest.indexOf(",");
        if (end == -1) end = rest.indexOf("}");
        return rest.substring(0, end).trim().replaceAll("\"", "");
    }

    private long extractLong(String json, String key) {
        String val = extractString(json, key);
        return Long.parseLong(val.trim());
    }

    public record JwtClaims(String subject, String roles) {}
}
