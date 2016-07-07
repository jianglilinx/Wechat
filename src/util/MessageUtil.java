package util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

import po.*;

import com.thoughtworks.xstream.XStream;


public class MessageUtil {
	
	public static final String MESSAGE_TEXT = "text";//文本
	public static final String MESSAGE_NEWS = "news";//语音
	public static final String MESSAGE_IMAGE = "image";//图片
	public static final String MESSAGE_VOICE = "voice";//语音
	public static final String MESSAGE_VIDEO = "video";//视频
	public static final String MESSAGE_LINK = "link";//链接
	public static final String MESSAGE_LOCATION = "location";//地理位置消息
	public static final String MESSAGE_EVENT = "event";//事件消息
	public static final String MESSAGE_SUBSCRIBE = "subscribe";//加关注事件
	public static final String MESSAGE_UNSUBSCRIBE = "unsubscribe";//取消关注事件
	public static final String MESSAGE_CLICK = "CLICK";//click类型菜单
	public static final String MESSAGE_VIEW = "VIEW";//view类型菜单
	public static final String MESSAGE_SCANCODE = "scancode_push";//扫码类型菜单
	/**
	 * 将xml文件转为map结构。
	 * @param request
	 * @return map
	 * @throws IOException
	 * @throws DocumentException
	 */
	public static Map<String, String> XmlToMap(HttpServletRequest request) 
			throws IOException, DocumentException {
		Map<String, String> map = new HashMap<String, String>();
		
		SAXReader reader = new SAXReader();
		
		InputStream ins = request.getInputStream();
		Document doc = reader.read(ins);
		
		Element root = doc.getRootElement();
		List<Element> list =  root.elements();
		
		 for(Element e : list){
			 map.put(e.getName(), e.getText());
		 }
		 ins.close();
		 
		return map;
	}
	
	/**
	 * 将文本消息对象转化为xml类型。
	 * @param textMessage
	 * @return
	 */
	public static String TextToMessage(TextMessage textMessage) {
		XStream xstream = new XStream();
		xstream.alias("xml",textMessage.getClass());
		return xstream.toXML(textMessage);
	}
	/**
	 * 将图文消息对象转化为xml类型。
	 * @param newsMessage
	 * @return
	 */
	public static String newsMessageToXml(NewsMessage newsMessage) {
		XStream xstream = new XStream();
		xstream.alias("xml",newsMessage.getClass());
		xstream.alias("item",new News().getClass());
		return xstream.toXML(newsMessage);
	}
	/**
	 * 将图片消息对象转化为xml类型
	 * @param imageMessage
	 * @return String
	 */
	public static String imageMessageToXml(ImageMessage imageMessage) {
		XStream xstream = new XStream();
		xstream.alias("xml",imageMessage.getClass());
		return xstream.toXML(imageMessage);
	}
	
	
	/**
	 * 拼接文本 主菜单
	 * @return
	 */
	public static String menuText(){
		StringBuffer sBuffer  = new StringBuffer();
		sBuffer.append("欢迎您关注我的个人微信订阅号，请根据以下菜单选择您想要的服务：\n\n");
		sBuffer.append("1: 订阅号内容介绍。\n");
		sBuffer.append("2: 静夜思\n");
		sBuffer.append("3：获取一个精美的图文消息\n");
		sBuffer.append("?: 调出此菜单\n\n");
		return sBuffer.toString();
	}
	public static String firstText(){
		StringBuffer sBuffer  = new StringBuffer();
		sBuffer.append("这个订阅号主要用于微信公众订阅号的开发！");
		return sBuffer.toString();
	}
	public static String secondText(){
		StringBuffer sBuffer  = new StringBuffer();
		sBuffer.append("床前明月光，");
		 sBuffer.append("疑是地上霜。\n");
		sBuffer.append("举头望明月，");
		sBuffer.append("低头思故乡。\n");
		return sBuffer.toString();
	}

	/**
	 * 拼接文本消息
	 * 输入文本内容
	 * 返回xml类型对象
	 */
	public static String initTextMessage(String toUserName,String fromUserName,String text){
		String message=null;
		TextMessage text1 = new TextMessage();
		
		text1.setFromUserName(toUserName);
		text1.setToUserName(fromUserName);
		text1.setMsgType("text");
		text1.setCreateTime(new Date().getTime());
		text1.setContent(text);
		
		message = TextToMessage(text1);
		return message;
	}
	
	/**
	 * 拼接图文消息
	 * 返回xml类型对象
	 */
	public static String initNewsMessage(String toUserName,String fromUserName){
		String message = null;
		List<News> newsList = new ArrayList<News>();
		NewsMessage newsMessage = new NewsMessage();

		News news = new News();
		news.setTitle("订阅号功能介绍");
		news.setDescription("这个订阅号主要用于微信公众订阅号的开发，学习！这个订阅号主要用于微信公众订阅号的开发，学习！这个订阅号主要用于微信公众订阅号的开发，学习！");
		news.setPicUrl("http://lichangqing.duapp.com/weisxin/image/fire.jpg");
		news.setUrl("www.baidu.com");
		
		News news1 = new News();
		news1.setTitle("订阅号功能介绍");
		news1.setDescription("这个订阅号主要用于微信公众订阅号的开发，学习！这个订阅号主要用于微信公众订阅号的开发，学习！这个订阅号主要用于微信公众订阅号的开发，学习！");
		news1.setPicUrl("http://lichangqing.duapp.com/weisxin/image/fire.jpg");
		news1.setUrl("http://www.huajiufu.com/");
		
		News news2 = new News();
		news2.setTitle("订阅号功能介绍");
		news2.setDescription("这个订阅号主要用于微信公众订阅号的开发，学习！这个订阅号主要用于微信公众订阅号的开发，学习！这个订阅号主要用于微信公众订阅号的开发，学习！");
		news2.setPicUrl("http://lichangqing.duapp.com/weisxin/image/fire.jpg");
		news2.setUrl("http://www.huajiufu.com/");
		
		News news3 = new News();
		news3.setTitle("订阅号功能介绍");
		news3.setDescription("这个订阅号主要用于微信公众订阅号的开发，学习！这个订阅号主要用于微信公众订阅号的开发，学习！这个订阅号主要用于微信公众订阅号的开发，学习！");
		news3.setPicUrl("http://lichangqing.duapp.com/weisxin/image/fire.jpg");
		news3.setUrl("http://www.huajiufu.com/");
		
		newsList.add(news);
		newsList.add(news1);
		newsList.add(news2);
		newsList.add(news3);

		newsMessage.setToUserName(fromUserName);
		newsMessage.setFromUserName(toUserName);
		newsMessage.setCreateTime(new Date().getTime());
		newsMessage.setMsgType(MESSAGE_NEWS);
		newsMessage.setArticles(newsList);
		newsMessage.setArticleCount(newsList.size());
		
		message = newsMessageToXml(newsMessage);
		return message;
	}
	
	/**
	 * 拼接图片消息
	 * 
	 */
	public static String initImageMessage(String toUserName,String fromUserName){
		ImageMessage imageMessage = new ImageMessage();
		Image image = new Image();
		image.setMediaId("hxAmTfwVuL2GXt8fP-GEkM2b2G2KpgpffjOAmgv7qRr0MxkPzgE8Z5PTaSImJjI1");
		
		imageMessage.setFromUserName(toUserName);
		imageMessage.setToUserName(fromUserName);
		imageMessage.setCreateTime(new Date().getTime());
		imageMessage.setMsgType(MESSAGE_IMAGE);
		imageMessage.setImage(image);
		
		return imageMessageToXml(imageMessage);
	}
	
	/**
	 * 组装菜单
	 * @return String类型的menu描述。
	 */
	public static String initMenu(){
		Menu menu = new Menu();
		
		ClickButton button1 = new ClickButton();
		button1.setName("click1");
		button1.setType("click");
		button1.setKey("1");
		
		ClickButton button2 = new ClickButton();
		button2.setName("view1");
		button2.setType("view");
		button2.setUrl("http://www.imooc.com/");
		button2.setKey("2");
		
		ClickButton button3 = new ClickButton();
		button3.setName("扫码事件");
		button3.setType("scancode_push");
		button3.setKey("3");
		
		ClickButton button4 = new ClickButton();
		button4.setName("地理位置");
		button4.setType("location_select");
		button4.setKey("11");
		
		Button button = new Button();
		button.setName("菜单");
		button.setSub_button(new Button[]{button3,button4});
		
		menu.setButton(new Button[]{button1,button2,button});
		
		return JSONObject.fromObject(menu).toString();
	}

	public static String initPictext(){
		PicText picText = new PicText();
		picText.setAuthor("jiang");
		picText.setContent("heiheixixi");
		picText.setContent_source_url("www.baidu.com");
		picText.setDigest("heihei");
		picText.setShow_cover_pic(0);
		picText.setTitle("Success!");
		picText.setThumb_media_id("");
		return JSONObject.fromObject(picText).toString();
	}

}
