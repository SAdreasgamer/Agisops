package com.aegisops.ingestion.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class WebhookSignatureVerifier {

    @Value("${aegisops.webhook.github.secret:mock-github-secret-key-12345}")
    private String githubSecret;

    public boolean isValidGitHubSignature(String payload, String signatureHeader) {
        if (signatureHeader == null || !signatureHeader.startsWith("sha256=")) {
            return false;
        }
        
        String expectedSignature = signatureHeader.substring("sha256=".length());
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(githubSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            mac.init(secretKeySpec);
            byte[] rawHmac = mac.doFinal(payload.getBytes(StandardCharsets.UTF_8));
            
            // Convert bytes to hex string
            StringBuilder hexString = new StringBuilder();
            for (byte b : rawHmac) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            String computedSignature = hexString.toString();
            
            // Constant-time secure comparison
            return MessageDigest.isEqual(
                    computedSignature.getBytes(StandardCharsets.UTF_8), 
                    expectedSignature.getBytes(StandardCharsets.UTF_8)
            );
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            return false;
        }
    }
}
