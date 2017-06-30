package com.asking.pad.app.commom;


import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;

import Decoder.BASE64Decoder;

/**
 * Created by jswang on 2017/6/9.
 */

public class AESHelper {
    final static String  decryptKey = "91asking";
//    /**
//     * AES解密
//     * @param encryptBytes 待解密的byte[]
//     * @return 解密后的String
//     * @throws Exception
//     */
//    public static String aesDecryptByBytes(byte[] encryptBytes) throws Exception {
//        KeyGenerator kgen = KeyGenerator.getInstance("AES");
//        kgen.init(128, new SecureRandom(decryptKey.getBytes("utf-8")));
//
//        Cipher cipher = Cipher.getInstance("AES");
//        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(kgen.generateKey().getEncoded(), "AES"));
//        byte[] decryptBytes = cipher.doFinal(encryptBytes);
//
//        return new String(decryptBytes);
//    }
//
//    /**
//     * 将base 64 code AES解密
//     * @param encryptStr 待解密的base 64 code
//     * @return 解密后的string
//     * @throws Exception
//     */
//    public static String aesDecrypt(String encryptStr) throws Exception {
//        byte[] bytes = base64Decode(encryptStr);
//        return TextUtils.isEmpty(encryptStr) ? null : aesDecryptByBytes(bytes);
//    }
//
//    /**
//     * base 64 decode
//     * @param base64Code 待解码的base 64 code
//     * @return 解码后的byte[]
//     * @throws Exception
//     */
//    public static byte[] base64Decode(String base64Code) throws Exception{
//        return TextUtils.isEmpty(base64Code) ? null : new BASE64Decoder().decodeBuffer(base64Code);
//    }

    public static String aesDecrypt(String args) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(128, new SecureRandom("91asking".getBytes()));
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(kgen.generateKey().getEncoded(), "AES"));
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] bytes = decoder.decodeBuffer(args);
        byte[] bytes1 = cipher.doFinal(bytes);
        return new String(bytes1);
    }
}
