package com.autumn.sdk.data;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class DesUtil {
    public static String encrypt(String input, String key){
        byte[] crypted = null;
        try{
            SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, skey);
            crypted = cipher.doFinal(input.getBytes());
        }catch(Exception e){
			System.out.println(e.toString());
		}
		if (crypted == null)return null;
		return new String(Base64.encodeBase64(crypted));
	}

	public static String decrypt(String input, String key){
        byte[] output = null;
        try{
            SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skey);
            output = cipher.doFinal(Base64.decodeBase64(input.getBytes()));
		}catch(Exception e){
			//把这个数据写出来
        }
        if (output == null)return null;
        return new String(output);
    }

    public static void main(String[] args) {
        String key = "1234567891234567";
        String data = "example";

        System.out.println(DesUtil.encrypt(data, key));

        System.out.println(DesUtil.decrypt(DesUtil.encrypt(data, key), key));


    }

}  
