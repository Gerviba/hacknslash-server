package hu.gerviba.hacknslash.backend.services;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import org.springframework.stereotype.Service;

/**
 * Common utility functions
 * @author Gergely Szabó
 */
@Service
public final class CommonsUtil {

    SecureRandom random = new SecureRandom();

    /**
     * Hash a String to SHA-256 String.
     * The input and the salt will be concatenated and than hashed.
     * @param input Input String
     * @param salt Salt of the password
     * @return SHA-256 if success or 'no-such-algorithm' if failed
     */
    public String hash(String input, String salt) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return bytesToHex(digest.digest((input + salt).getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "no-such-algorithm";
    }
    
    private static final char[] HEX = "0123456789ABCDEF".toCharArray();

    /**
     * Converts byte[] to String
     */
    private String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX[v >>> 4];
            hexChars[j * 2 + 1] = HEX[v & 0x0F];
        }
        return String.valueOf(hexChars);
    }

    /**
     * Generate random session id using the {@link SecureRandom}
     * @param seed A constant secret value
     * @return A random generated 64 long session ID
     */
    public String generateSessionId(String seed) {
        String salt = "";
        for (int i = 0; i < 64; ++i)
            salt += HEX[random.nextInt(16)];
        return hash(seed, salt);
    }

    /**
     * Generate salt using {@link SecureRandom}
     * @return Random generated 64 long salt.
     */
    public String generateSalt() {
        String salt = "";
        for (int i = 0; i < 64; ++i)
            salt += HEX[random.nextInt(16)];
        return salt;
    }
}
