package com.github.utils;
import javax.crypto.Cipher;  
import javax.crypto.spec.IvParameterSpec;  
import javax.crypto.spec.SecretKeySpec;  


/**
* AES 是一种可逆加密算法，对用户的敏感信息加密处理 
* 对原始数据进行AES加密后，在进行Base64编码转化； 
* 流量网关平台采用 AES（Advanced Encryption Standard）加密算法，
* 加密模式为 AES-128-CBC，补码方式为 AES/CBC/PKCS5Padding，
* 密钥和向量均为 16  位，我们将为每位客户分配一套密钥和向量
* 
 * @author qinxuewu
 * 2019年1月17日上午10:47:08
*/  

public class AesUtil {

	    // 加密  
	    public static String encrypt(String sSrc, String encodingFormat, String sKey, String ivParameter) throws Exception {  
	        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");  
	        byte[] raw = sKey.getBytes();  
	        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");  
	        //使用CBC模式，需要一个向量iv，可增加加密算法的强度  
	        IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());
	        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);  
	        byte[] encrypted = cipher.doFinal(sSrc.getBytes(encodingFormat));  
	        return encodeBytes(encrypted);

	  }  	  
	    public static String encodeBytes(byte[] bytes) {
	    	StringBuffer buffer = new StringBuffer();
	    	for (int i = 0; i < bytes.length; i++) {
			    	buffer.append((char) (((bytes[i] >> 4) & 0xF) + ((int) 'a')));
			    	buffer.append((char) (((bytes[i]) & 0xF) + ((int) 'a')));
	    	}
	    		return buffer.toString();
	    }
	    

	    
	    private static String asHex(byte buf[]) {
	 		StringBuffer strbuf = new StringBuffer(buf.length);
	 		int i;
	 		for (i = 0; i < buf.length; i++) {
	 			if (((int) buf[i] & 0xff) < 0x10)
	 				strbuf.append("0");

	 			strbuf.append(Long.toString((int) buf[i] & 0xff, 16));
	 		}
	 		return strbuf.toString().toUpperCase();
	 	}

	 	private static byte[] asBin(String src) {
	 		if (src.length() < 1)
	 			return null;
	 		byte[] encrypted = new byte[src.length() / 2];
	 		for (int i = 0; i < src.length() / 2; i++) {
	 			int high = Integer.parseInt(src.substring(i * 2, i * 2 + 1), 16);
	 			int low = Integer.parseInt(src.substring(i * 2 + 1, i * 2 + 2), 16);
	 			encrypted[i] = (byte) (high * 16 + low);
	 		}
	 		return encrypted;
	 	}
	 	
	 	
	    /**
	     * 不带偏移量加密
	     * @param rawKey
	     * @param message
	     * @return
	     */
	 	public static String getEncrypt(String rawKey,String message) {
	 		byte[] key = asBin(rawKey);
	 		try {
	 			SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
	 			Cipher cipher = Cipher.getInstance("AES");
	 			cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
	 			byte[] encrypted = cipher.doFinal(message.getBytes());
	 			return asHex(encrypted);
	 		} catch (Exception e) {
	 			return null;
	 		}
	 	}
		
	    /**
	     * 不带偏移量解密
	     * @param rawKey
	     * @param message
	     * @return
	     */
	 	public static String getDecrypt(String rawKey,String encrypted) {
	 		if (encrypted != null && !encrypted.equals("")) {
	 			byte[] tmp = asBin(encrypted);
	 			byte[] key = asBin(rawKey);

	 			try {
	 				SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
	 				Cipher cipher = Cipher.getInstance("AES");
	 				cipher.init(Cipher.DECRYPT_MODE, skeySpec);
	 				byte[] decrypted = cipher.doFinal(tmp);
	 				return new String(decrypted);
	 			} catch (Exception e) {
	 				return "";
	 			}
	 		} else {
	 			return null;
	 		}
	 	}
	    public static void main(String[] args) throws Exception {  
			/* 
			* 加密用的Key 可以用26个字母和数字组成 
			* 此处使用AES-128-CBC加密模式，key需要为16位。 
			*/   
	        String cSrc = "我来自中国";   
	        String enString = AesUtil.encrypt(cSrc,"utf-8","gBNitlDim4G3tW54","9537783121404286");  
	        System.out.println("加密后的字串是："+ enString);  	  	      
  
	        
	        //不带偏移量
	        String rawKey = "b0b1f641313b2eafdd6522c4b638a5f7"; 
	        String message = "1234";//原数据
	 		String encrypted = getEncrypt(rawKey,message);
	 		System.out.println("加密后：" + encrypted);// 加密后

	    }  
	    
}
