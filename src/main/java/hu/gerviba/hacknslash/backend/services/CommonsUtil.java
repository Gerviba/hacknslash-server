package hu.gerviba.hacknslash.backend.services;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import org.springframework.stereotype.Service;

@Service
public final class CommonsUtil {

    SecureRandom random = new SecureRandom();

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

    private String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX[v >>> 4];
            hexChars[j * 2 + 1] = HEX[v & 0x0F];
        }
        return String.valueOf(hexChars);
    }

    public String generateSessionId(String seed) {
        String salt = "";
        for (int i = 0; i < 64; ++i)
            salt += HEX[random.nextInt(16)];
        return hash(seed, salt);
    }

    public String generateSalt() {
        String salt = "";
        for (int i = 0; i < 64; ++i)
            salt += HEX[random.nextInt(16)];
        return salt;
    }
}
