//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.kangaroo.common.crypt;

public interface Cipher {
    byte[] encryptByPublicK(byte[] var1);

    byte[] decryptByPublicK(byte[] var1);

    byte[] encryptByPrivateK(byte[] var1);

    byte[] decryptByPrivateK(byte[] var1);
}
