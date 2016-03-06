package cn.service;

/**
 * MD5 data generate tool class
 *
 */

import java.security.*;
import java.io.*;

public class MD5Generator {
	
	 private final static String[] strDigits = { "0", "1", "2", "3", "4", "5",
         "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };
	
	private static InputStream createInputStream(File file) {
		InputStream is = null;
		try {
			is = new BufferedInputStream(new FileInputStream(file));
		} catch(Exception e) {
			e.printStackTrace();
		}
		return is;
	}
	
	private static String generateMD5ForFile(File file) {
		InputStream is = createInputStream(file);
		byte[] buf = new byte[4096];
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			int count = 0;
			while((count = is.read(buf)) > 0) {
				md.update(buf, 0, count);
			}
			byte[] md5 = md.digest();
			is.close();
			buf = null;
			return md5HashToString(md5);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	private static String generateMD5(byte[] data, int offset, int len) {
		if(data.length == 0) {
			return "";
		}
		
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(data, offset, len);
			byte[] md5 = md.digest();
			return md5HashToString(md5);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	private static String generateMD5bak(byte[] data, int offset, int len) {
		if(data.length == 0) {
			return "";
		}
		
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(data, offset, len);
			byte[] md5 = md.digest();
			return md5HashToStringbak(md5);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	private static String md5HashToStringbak(byte[] input) {
		StringBuffer strBuf = new StringBuffer();
		int value;
		for(int i = 0; i < input.length; i++) {
			value = input[i];
			value = value > 0 ? value : value + 256;
			String str = Integer.toHexString(value);
			if(str.length() < 2)
				str = "0" + str;
			strBuf.append(str);
		}
		return strBuf.toString();
		
	}
	
	
	private static String md5HashToString(byte[] input) {
		/*StringBuffer strBuf = new StringBuffer();
		int value;
		for(int i = 0; i < input.length; i++) {
			value = input[i];
			value = value > 0 ? value : value + 256;
			String str = Integer.toHexString(value);
			if(str.length() < 2)
				str = "0" + str;
			strBuf.append(str);
		}
		return strBuf.toString();*/
		
		  StringBuffer sBuffer = new StringBuffer();
	        for (int i = 0; i < input.length; i++) {
	            sBuffer.append(byteToArrayString(input[i]));
	        }
	        return sBuffer.toString();
	}
	
	 
	// 返回形式为数字跟字符串
    private static String byteToArrayString(byte bByte) {
        int iRet = bByte;
        // System.out.println("iRet="+iRet);
        if (iRet < 0) {
            iRet += 256;
        }
        int iD1 = iRet / 16;
        int iD2 = iRet % 16;
        return strDigits[iD1] + strDigits[iD2];
    }
	
	public static String getMD5Value(File file) {
		return generateMD5ForFile(file);
	}
	
	public static String getMD5Value(String src) {
		if(src == null) {
			return null;
		}
		if(src == "") {
			return "";
		}
			
		byte[] data = src.getBytes();
		return generateMD5(data, 0, data.length);
	}
	
	public static String getMD5Valuebak(String src) {
		if(src == null) {
			return null;
		}
		if(src == "") {
			return "";
		}
			
		byte[] data = src.getBytes();
		return generateMD5bak(data, 0, data.length);
	}
	
	public static String reverseMD5Value(String value){
		String md5_val = MD5Generator.getMD5Value(value).substring(8,24);
		StringBuilder sb = new StringBuilder(md5_val);
		String reverse_MD5_val = sb.reverse().toString();
		System.out.println("[reverseMD5Value] reverseMD5Value: " + reverse_MD5_val);
		return reverse_MD5_val;
	}
	
	public static String reverseMD5Valuebak(String value){
		String md5_val = MD5Generator.getMD5Value(value).substring(8,24);
		StringBuilder sb = new StringBuilder(md5_val);
		String reverse_MD5_val = sb.reverse().toString();
		System.out.println("[reverseMD5Value] reverseMD5Value: " + reverse_MD5_val);
		return reverse_MD5_val;
	}
	
	public static void main(String[] args) {
//		String testFileMD5 = "1c7f31883ec4bb5a460f5bd751a04558";
//		String file = "d:\\emf-sdo-xsd-Examples-2.3.0M5.zip";
//		long tmOrg = System.currentTimeMillis();
//		String md5  = getMD5Value(new File(file));
//		System.out.println("MD5: " + md5);
//		System.out.println("the result of MD5 verify: " + md5.equals(testFileMD5));
//		long tmLast = System.currentTimeMillis() - tmOrg;
//		System.out.println("Use time :" + tmLast + " ms");
		
		String str = "haiyilian";
		System.out.println(getMD5Value(str));
		System.out.println(getMD5Valuebak(str));
		/*str = "password";
		System.out.println(getMD5Value(str));
		str = getMD5Value(str);
		System.out.println(getMD5Value(str));*/
	}
}
