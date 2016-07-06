package util;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.naming.spi.DirStateFactory.Result;

import net.sf.json.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import po.AccessToken;
import po.Button;
import po.ClickButton;
import po.Menu;

@SuppressWarnings("deprecation")
public class weixinUtil {
	//订阅号
	//private static final String APPID="wxad0e6a4ce7071d2a";
	//private static final String APPSECRET="2d10747dc1340e980a645b17e418fe42";
	//测试号
	private static final String APPID="wx53ae0ab51f76d777";
	private static final String APPSECRET="112979ca9f34a83a6761dc47aa8feea5";
	private static final String ACCESS_TOKEN_URL="https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
	private static final String UPLOAD_URL="https://api.weixin.qq.com/cgi-bin/media/upload?access_token=ACCESS_TOKEN&type=TYPE";
	private static final String CREATE_MENU_RUL="https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";
	private static final String DELETE_MENU_RUL="https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=ACCESS_TOKEN";
	
	/**GET请求
	 * 功能：用GET方式执行传入的url，获取执行url后接收到的信息，将信息转为String，再转为JSONObject格式返回。
	 * @param url
	 * @return
	 */
	//需要jar包：json-lib-2.4-jdk15.jar，这个jar包依赖于五个jar包commons-beanutils-1.8.3.jar，commons-collections-3.2.1.jar，commons-logging-1.1.1.jar，ezmorph-1.0.6.jar
	//共六个jar包
	public static JSONObject doGetStr(String url){
		
		//需要导入jar包：httpclient-4.5.jar，httpcore-4.4.1.jar
		DefaultHttpClient httpclient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);
		JSONObject jsonObject = null;
		
		try {
			HttpResponse response = httpclient.execute(httpGet);
			HttpEntity entity = response.getEntity();
			if(entity!=null){
				String resultString = EntityUtils.toString(entity,"utf-8");
				jsonObject = JSONObject.fromObject(resultString);
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonObject;
	}
	
	/**post请求
	 * 功能：用POST方式执行传入的url（之前将url信息outStr封装），获取执行url后接收到的信息，将信息转为String，再转为JSONObject格式返回。
	 * @param url outStr
	 * @return
	 */
	public static JSONObject doPostStr(String url,String outStr){
		DefaultHttpClient httpclient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url);
		JSONObject jsonObject = null;
		try {
			httpPost.setEntity(new StringEntity(outStr,"utf-8"));
			HttpResponse response = httpclient.execute(httpPost);
			String resultString = EntityUtils.toString(response.getEntity(),"utf-8");
			jsonObject = JSONObject.fromObject(resultString);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonObject;
	}
	
	/**
	 * 获取access_token
	 * 注意：微信公众账号access_token的获取次数不能超过2000次
	 * 如果每次都从微信平台获取的话可能会超过2000次，因此最好把access_token保存到本地
	 * 一个access_token有两个小时的有效期
	 * 一个access_token过期后才获取新的access_token
	 * 这样一天只需要获取12次，不会超过2000次的限制
	 * @return
	 */
	public static AccessToken getAccessToken(){
		AccessToken token = new AccessToken();
		String url = ACCESS_TOKEN_URL.replace("APPID", APPID).replace("APPSECRET", APPSECRET);
		JSONObject jsonObject = doGetStr(url);
		if(jsonObject!=null){
			//System.out.println("jsonObject:"+jsonObject.toString());
			token.setToken(jsonObject.getString("access_token"));
			token.setExpiresIn(jsonObject.getInt("expires_in"));
		}
		return token;
	}
	
	/**
	 * 处理文件上传,并获取medieID
	 * @param filePath
	 * @param accessToken
	 * @param type
	 * @return
	 * @throws IOException 
	 * 参考慕课网，java中的文件上传下载。
	 */
	public static String upload(String filePath,String accessToken,String type) throws IOException{
		File file = new File(filePath);
		if(!file.exists()||!file.isFile()){
			throw  new IOException("文件不存在！");
		}
		String url = UPLOAD_URL.replace("ACCESS_TOKEN", accessToken).replace("TYPE", type);
		URL urlObj = new URL(url);
		
		//连接
		HttpURLConnection connection = (HttpURLConnection) urlObj.openConnection();
		connection.setRequestMethod("POST");
		connection.setDoInput(true);
		connection.setDoOutput(true);
		connection.setUseCaches(false);
		
		//设置请求头信息
		String BOUNDRY = "----------"+System.currentTimeMillis();
		
		connection.setRequestProperty("Content-Type", "multipart/form-data;boundary="+BOUNDRY);
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append("--");
		sBuilder.append(BOUNDRY);
		sBuilder.append("\r\n");
		sBuilder.append("Content-Disposition:form-data;name=\"file\";filename=\""+file.getName()+"\"\r\n");
		sBuilder.append("Content-Type:application/octet-stream\r\n\r\n");
		
		byte[] head = sBuilder.toString().getBytes("utf-8");
		
		//获得输出流
		OutputStream out = new DataOutputStream(connection.getOutputStream());
		//输出表头
		out.write(head);
		
		//文件正文部分，把文件以流的方式推入到url中。
		DataInputStream in = new DataInputStream(new FileInputStream(file));
		int bytes = 0;
		byte[] bufferOut = new byte[1024];
		while((bytes=in.read(bufferOut))!=-1){
			out.write(bufferOut,0,bytes);
		}
		in.close();
		
		//结尾部分
		byte[] foot = ("\r\n--"+BOUNDRY+"\r\n").getBytes("utf-8");//定义最后的数据分割线
		
		out.write(foot);
		
		out.flush();
		out.close();
		
		
		StringBuffer buffer = new StringBuffer();
		BufferedReader reader = null;
		String result = null;
		
		//定义bufferedReader 输入流来读取URL的响应。
		reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String line = null;
		while((line = reader.readLine())!= null){
			buffer.append(line);
		}
		if(result==null){
			result=buffer.toString();
		}
		reader.close();
		
		JSONObject jsonObject = JSONObject.fromObject(result);
		System.out.println(jsonObject);
		
		return jsonObject.getString("media_id");
	}
	

	/**
	 * 创建自定义菜单。
	 * @param token
	 * @param menu
	 * @return
	 * 测试在test文件中
	 */
	public static int createMenu(String token,String menu){
		int result = 0;
		String url = CREATE_MENU_RUL.replace("ACCESS_TOKEN", token);
		JSONObject jsonObject = doPostStr(url, menu);
		if(jsonObject!=null){
			result = jsonObject.getInt("errcode");
		}
		return result;
	}
	
	/**
	 * 删除自定义菜单
	 * @param token
	 * @param menu
	 * @return
	 */
	public static int deleteMenu(String token){
		int result = 0;
		String url = DELETE_MENU_RUL.replace("ACCESS_TOKEN", token);
		JSONObject jsonObject = doGetStr(url);
		if(jsonObject!=null){
			result = jsonObject.getInt("errcode");
		}
		return result;
	}
}