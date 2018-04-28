package org.kangaroo.common.crypt;

import org.kangaroo.common.Base64;

public class CipherFactory {
    private static String PUBLIC_KEY;
    private static String PRIVATE_KEY;
    private static RsaCipher rsaCipher;

    public CipherFactory() {
    }

    public static void init() {
        rsaCipher = createRsaCipher();
    }

    private static RsaCipher createRsaCipher() {
        return new RsaCipher(PUBLIC_KEY, PRIVATE_KEY);
    }

    public static String encryptByPublicK(byte[] data) {
        return Base64.encodeToString(rsaCipher.encryptByPublicK(data), false);
    }

    public static String decryptByPublicK(String encrypted) {
        return new String(rsaCipher.decryptByPublicK(Base64.decode(encrypted)));
    }

    public static String encryptByPrivateK(byte[] data) {
        return Base64.encodeToString(rsaCipher.encryptByPrivateK(data), false);
    }

    public static String decryptByPrivateK(String encrypted) {
        return new String(rsaCipher.decryptByPrivateK(Base64.decode(encrypted)));
    }

    public static void setPUBLIC_KEY(String PUBLIC_KEY) {
        PUBLIC_KEY = PUBLIC_KEY;
    }

    public static void setPRIVATE_KEY(String PRIVATE_KEY) {
        PRIVATE_KEY = PRIVATE_KEY;
    }

    public static String getPublicKey() {
        return PUBLIC_KEY;
    }

    public static void setPublicKey(String publicKey) {
        PUBLIC_KEY = publicKey;
    }

    public static String getPrivateKey() {
        return PRIVATE_KEY;
    }

    public static void setPrivateKey(String privateKey) {
        PRIVATE_KEY = privateKey;
    }

    private static AesCipher createAesCipher() {
        return new AesCipher();
    }

    public static void main(String[] args) {
        String publicK = "";
        String privateK = "";
        setPUBLIC_KEY(publicK);
        setPRIVATE_KEY(privateK);
    }
}
