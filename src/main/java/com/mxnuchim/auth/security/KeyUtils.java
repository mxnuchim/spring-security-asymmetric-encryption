package com.mxnuchim.auth.security;

import java.io.InputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public final class KeyUtils {

    private KeyUtils() {}

    public static PrivateKey loadPrivateKey(final String pemPath) throws Exception {
        final String key = readKeyFromResource(pemPath)
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s+", "");

        final byte[] decoded = Base64.getDecoder().decode(key);
        final PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decoded);

        return KeyFactory.getInstance("RSA").generatePrivate(spec);
    }

    public static PublicKey loadPublicKey(final String pemPath) throws Exception {
        final String key = readKeyFromResource(pemPath)
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s+", "");

        final byte[] decoded = Base64.getDecoder().decode(key);
        final X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);

        return KeyFactory.getInstance("RSA").generatePublic(spec);
    }

    private static String readKeyFromResource(final String pemPath) throws Exception {
        try (InputStream is = KeyUtils.class
                .getClassLoader()
                .getResourceAsStream(pemPath)) {

            if (is == null) {
                throw new IllegalArgumentException(
                        "Could not find key file on classpath: " + pemPath
                );
            }

            return new String(is.readAllBytes());
        }
    }
}
