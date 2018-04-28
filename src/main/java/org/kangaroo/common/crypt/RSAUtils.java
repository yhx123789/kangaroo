package org.kangaroo.common.crypt;

import java.io.ByteArrayOutputStream;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.Cipher;
import org.kangaroo.common.Base64;

public class RSAUtils {
    private static final int KEY_SIZE = 1024;

    public RSAUtils() {
    }

    public static Map<String, Object> getKeyPair() throws NoSuchAlgorithmException {
        Map<String, Object> map = new HashMap();
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(1024);
        KeyPair keyPair = generator.generateKeyPair();
        RSAPrivateKey privateKey = (RSAPrivateKey)keyPair.getPrivate();
        RSAPublicKey publicKey = (RSAPublicKey)keyPair.getPublic();
        map.put("private", privateKey);
        map.put("public", publicKey);
        return map;
    }

    public static byte[] encryptByPublicKey(byte[] data, String publicKey) {
        byte[] key = Base64.decodeFast(publicKey);
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(key);

        try {
            KeyFactory rsaFactory = KeyFactory.getInstance("RSA");
            PublicKey publicK = rsaFactory.generatePublic(x509EncodedKeySpec);
            Cipher cipher = Cipher.getInstance(rsaFactory.getAlgorithm());
            cipher.init(1, publicK);
            int len = data.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            for(int offset = 0; offset < len; offset += 117) {
                byte[] buf;
                if (len - offset > 117) {
                    buf = cipher.doFinal(data, offset, 117);
                } else {
                    buf = cipher.doFinal(data, offset, len - offset);
                }

                out.write(buf);
            }

            byte[] result = out.toByteArray();
            out.close();
            return result;
        } catch (Exception var12) {
            throw new IllegalStateException(var12.getMessage(), var12);
        }
    }

    public static byte[] encryptByPrivateKey(byte[] data, String privateKey) {
        byte[] key = Base64.decodeFast(privateKey);
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(key);

        try {
            KeyFactory rsaFactory = KeyFactory.getInstance("RSA");
            PrivateKey privateK = rsaFactory.generatePrivate(pkcs8EncodedKeySpec);
            Cipher cipher = Cipher.getInstance(rsaFactory.getAlgorithm());
            cipher.init(1, privateK);
            int len = data.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            for(int offset = 0; offset < len; offset += 117) {
                byte[] buf;
                if (len - offset > 117) {
                    buf = cipher.doFinal(data, offset, 117);
                } else {
                    buf = cipher.doFinal(data, offset, len - offset);
                }

                out.write(buf);
            }

            byte[] result = out.toByteArray();
            out.close();
            return result;
        } catch (Exception var12) {
            throw new IllegalStateException(var12.getMessage(), var12);
        }
    }

    public static byte[] decryptByPublicKey(byte[] data, String publicKey) {
        byte[] key = Base64.decodeFast(publicKey);
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(key);

        try {
            KeyFactory rsaFactory = KeyFactory.getInstance("RSA");
            PublicKey publicK = rsaFactory.generatePublic(x509EncodedKeySpec);
            Cipher cipher = Cipher.getInstance(rsaFactory.getAlgorithm());
            cipher.init(2, publicK);
            int len = data.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            for(int offset = 0; offset < len; offset += 128) {
                byte[] buf;
                if (len - offset > 128) {
                    buf = cipher.doFinal(data, offset, 128);
                } else {
                    buf = cipher.doFinal(data, offset, len - offset);
                }

                out.write(buf);
            }

            byte[] result = out.toByteArray();
            out.close();
            return result;
        } catch (Exception var12) {
            throw new IllegalStateException(var12.getMessage(), var12);
        }
    }

    public static byte[] decryptByPrivateKey(byte[] data, String privateKey) {
        byte[] key = Base64.decodeFast(privateKey);
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(key);

        try {
            KeyFactory rsaFactory = KeyFactory.getInstance("RSA");
            PrivateKey privateK = rsaFactory.generatePrivate(pkcs8EncodedKeySpec);
            Cipher cipher = Cipher.getInstance(rsaFactory.getAlgorithm());
            cipher.init(2, privateK);
            int len = data.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            for(int offset = 0; offset < len; offset += 128) {
                byte[] buf;
                if (len - offset > 128) {
                    buf = cipher.doFinal(data, offset, 128);
                } else {
                    buf = cipher.doFinal(data, offset, len - offset);
                }

                out.write(buf);
            }

            byte[] result = out.toByteArray();
            out.close();
            return result;
        } catch (Exception var12) {
            throw new IllegalStateException(var12.getMessage(), var12);
        }
    }

    public static String sign(byte[] data, String privateKey) {
        byte[] key = Base64.decodeFast(privateKey);
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(key);

        try {
            KeyFactory rsaFactory = KeyFactory.getInstance("RSA");
            PrivateKey privateK = rsaFactory.generatePrivate(pkcs8EncodedKeySpec);
            Signature signature = Signature.getInstance("MD5withRSA");
            signature.initSign(privateK);
            signature.update(data);
            return Base64.encodeToString(signature.sign(), false);
        } catch (Exception var7) {
            throw new IllegalStateException(var7.getMessage(), var7);
        }
    }

    public static boolean verify(byte[] data, String publicKey, String sign) {
        byte[] key = Base64.decodeFast(publicKey);
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(key);

        try {
            KeyFactory rsaFactory = KeyFactory.getInstance("RSA");
            PublicKey publicK = rsaFactory.generatePublic(x509EncodedKeySpec);
            Signature signature = Signature.getInstance("MD5withRSA");
            signature.initVerify(publicK);
            signature.update(data);
            return signature.verify(Base64.decodeFast(sign));
        } catch (Exception var8) {
            throw new IllegalStateException(var8.getMessage(), var8);
        }
    }
}
