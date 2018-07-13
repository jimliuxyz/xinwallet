package com.xinwang.xinwallet.tools.crypto;

import android.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AESCipher {
    //    private static final String CipherMode = "AES/ECB/PKCS5Padding";使用ECB加密，不需要设置IV，但是不安全
    private static final String CipherMode = "AES/CFB/NoPadding";//使用CFB加密，需要设置IV

    public static String encrypt(String key, String data) throws Exception {
        try {
            Cipher cipher = Cipher.getInstance(CipherMode);
            SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
            cipher.init(Cipher.ENCRYPT_MODE, keyspec, new IvParameterSpec(
                    new byte[cipher.getBlockSize()]));
            byte[] encrypted = cipher.doFinal(data.getBytes());
            return Base64.encodeToString(encrypted, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String decrypt(String key, String data) throws Exception {
        try {
            byte[] encrypted1 = Base64.decode(data.getBytes(), Base64.DEFAULT);
            Cipher cipher = Cipher.getInstance(CipherMode);
            SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
            cipher.init(Cipher.DECRYPT_MODE, keyspec, new IvParameterSpec(
                    new byte[cipher.getBlockSize()]));
            byte[] original = cipher.doFinal(encrypted1);
            return new String(original, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

//    public static byte[] Encrypt(SecretKey secretKey, String msg) throws Exception
//    {
//        Cipher cipher = Cipher.getInstance("AES"); //: 等同 AES/ECB/PKCS5Padding
//        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
//        System.out.println("AES_DEFAULT IV:"+cipher.getIV());
//        System.out.println("AES_DEFAULT Algoritm:"+cipher.getAlgorithm());
//        byte[] byteCipherText = cipher.doFinal(msg.getBytes());
//        System.out.println("加密結果的Base64編碼：" + Base64.getEncoder().encodeToString(byteCipherText));
//        return byteCipherText;
//    }
//
//    public static byte[] Decrypt(SecretKey secretKey, byte[] cipherText) throws Exception
//    {
//        Cipher cipher = Cipher.getInstance("AES");
//        cipher.init(Cipher.DECRYPT_MODE, secretKey);
//        byte[] decryptedText = cipher.doFinal(cipherText);
//        String strDecryptedText = new String(decryptedText);
//        System.out.println("解密結果：" + strDecryptedText);
//        return decryptedText;
//    }
}
