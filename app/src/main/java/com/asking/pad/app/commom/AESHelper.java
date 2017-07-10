package com.asking.pad.app.commom;


import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import Decoder.BASE64Decoder;

/**
 * Created by jswang on 2017/6/9.
 */

public class AESHelper {
    //注意: 这里的password(秘钥必须是16位的)
    private static final String keyBytes = "www.91asking.com";

    static final String algorithmStr = "AES/ECB/PKCS5Padding";

    private static byte[] decrypt(byte[] content) throws Exception {
        byte[] keyStr = keyBytes.getBytes();
        SecretKeySpec key = new SecretKeySpec(keyStr, "AES");
        Cipher cipher = Cipher.getInstance(algorithmStr);
        cipher.init(Cipher.DECRYPT_MODE, key);//   ʼ
        byte[] result = cipher.doFinal(content);
        return result;
    }

    /**
     * 解密
     */
    public static String decode(String content) throws Exception {
        //解密之前,先将输入的字符串按照16进制转成二进制的字节数组,作为待解密的内容输入
        BASE64Decoder base64Decoder = new BASE64Decoder();
        byte[] b = decrypt(base64Decoder.decodeBuffer(content));
        return new String(b);
    }

    public static  byte[] decodeByte(String content) throws Exception {
        //解密之前,先将输入的字符串按照16进制转成二进制的字节数组,作为待解密的内容输入
        BASE64Decoder base64Decoder = new BASE64Decoder();
        byte[] b = decrypt(base64Decoder.decodeBuffer(content));
        return b;
    }

}
