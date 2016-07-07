package test;

import java.io.IOException;

import com.thoughtworks.xstream.io.binary.Token;

import net.sf.json.JSONObject;
import po.AccessToken;
import util.MessageUtil;
import util.weixinUtil;

public class weixintest {
	public static void main(String[] args) throws IOException {
		AccessToken token = weixinUtil.getAccessToken();
			/*将图片上传到微信服务器并取得 MediaId*/
		String acctoken = token.getToken();
		System.out.println(acctoken);
//		String path2="E:/1.jpg";
//			String mediaID = weixinUtil.upload(path, token.getToken(), "image");
//			String mediaID2 = weixinUtil.upload2(path2,token.getToken(),"image");
//		String meadiaID3 = weixinUtil.upload_pictext(MessageUtil.initPictext(),token.getToken());
//
//
//
//		System.out.println(meadiaID3);

			/*创建自定义菜单*/
//		int result = weixinUtil.createMenu(token.getToken(), MessageUtil.initMenu());

			/*删除自定义菜单*/
		//int result = weixinUtil.deleteMenu(token.getToken());

//			System.out.println(mediaID);
//		System.out.println(result);
//			System.out.println("永久id为"+mediaID2);

	}
}
