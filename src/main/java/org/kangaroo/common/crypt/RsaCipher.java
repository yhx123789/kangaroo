//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.kangaroo.common.crypt;

public class RsaCipher implements Cipher {
    private String publicKey;
    private String privateKey;

    public RsaCipher(String publicKey, String privateKey) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    public byte[] encryptByPublicK(byte[] data) {
        return RSAUtils.encryptByPublicKey(data, this.publicKey);
    }

    public byte[] decryptByPublicK(byte[] data) {
        return RSAUtils.decryptByPublicKey(data, this.publicKey);
    }

    public byte[] encryptByPrivateK(byte[] data) {
        return RSAUtils.encryptByPrivateKey(data, this.privateKey);
    }

    public byte[] decryptByPrivateK(byte[] data) {
        return RSAUtils.decryptByPrivateKey(data, this.privateKey);
    }
}
