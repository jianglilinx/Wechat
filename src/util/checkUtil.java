package util;

import java.util.Arrays;


public class checkUtil {
	private static final String Token = "jiang";
	
	public static boolean checkSignature(String signature,String timestamp,String nonce){
		String[] arr = new String[]{Token,timestamp,nonce};
		//排序
		Arrays.sort(arr);
		//字符串拼接
		StringBuffer contentBuffer = new StringBuffer();
		for(int i = 0;i<arr.length;i++){
			contentBuffer.append(arr[i]);
		}
		//sha1加密
		String tempString = SHA1.hex_sha1(contentBuffer.toString());
		
		return tempString.equals(signature);
	}
	
}
