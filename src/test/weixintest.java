package test;

import java.io.IOException;

import com.thoughtworks.xstream.io.binary.Token;

import net.sf.json.JSONObject;
import po.AccessToken;
import util.MessageUtil;
import util.weixinUtil;

public class weixintest {
	public static void main(String[] args){
		try {
			AccessToken token = weixinUtil.getAccessToken();
			/*将图片上传到微信服务器并取得 MediaId*/
			String path="F:/123.jpg";
			String mediaID = weixinUtil.upload(path, token.getToken(), "image");
			
			
			/*创建自定义菜单*/
			int result = weixinUtil.createMenu(token.getToken(), MessageUtil.initMenu());
			
			/*删除自定义菜单*/
			//int result = weixinUtil.deleteMenu(token.getToken());
			
			System.out.println(mediaID);
			System.out.println(result);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
