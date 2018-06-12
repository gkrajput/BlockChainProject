package com.gaurav.blockchain;

public class CommonUtil {

	public static String byteArrayToHexString(byte[] input) {
		if (null == input) {
			return "NULL";
		}
		StringBuffer hex = new StringBuffer();
    	for (int i=0;i<input.length;i++) {
    	  hex.append(Integer.toHexString(0xFF & input[i]));
    	}
    	return hex.toString();
	}
}
