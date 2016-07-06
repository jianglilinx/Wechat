package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.dom4j.DocumentException;

import util.MessageUtil;
import util.checkUtil;

public class WeixinServlet extends HttpServlet {
	/**
	 * token验证
	 */
    protected void doGet(HttpServletRequest req,HttpServletResponse resp)
    		throws ServletException,IOException {
    	String signature = req.getParameter("signature");
    	String timestamp = req.getParameter("timestamp");
    	String nonce = req.getParameter("nonce");
    	String echostr = req.getParameter("echostr");
    	
    	PrintWriter outPrintWriter = resp.getWriter();
    	if(checkUtil.checkSignature(signature, timestamp, nonce)){
    		outPrintWriter.print(echostr);
    	}
	}
    
    /**
     * 处理客户端发来的文本消息。
     */
    protected void doPost(HttpServletRequest req,HttpServletResponse resp)
    		throws ServletException,IOException{
    	req.setCharacterEncoding("utf-8");
    	resp.setCharacterEncoding("utf-8");
    	
    	PrintWriter out= resp.getWriter();
		try {
			Map<String, String> map = MessageUtil.XmlToMap(req);
			String ToUserName = map.get("ToUserName");
			String FromUserName = map.get("FromUserName");
			String MsgType = map.get("MsgType");
			String Content = map.get("Content");
			String message = null;
			
			if(MsgType.equals(MessageUtil.MESSAGE_TEXT)){
				if("1".equals(Content)){
					message = MessageUtil.initTextMessage(ToUserName, FromUserName, MessageUtil.firstText());
				}else if("2".equals(Content)){
					message = MessageUtil.initTextMessage(ToUserName, FromUserName, MessageUtil.secondText());
				}else if("3".equals(Content)){
					message = MessageUtil.initNewsMessage(ToUserName, FromUserName);
				}else if("4".equals(Content)){
					message = MessageUtil.initImageMessage(ToUserName, FromUserName);
				}else if("?".equals(Content)||"？".equals(Content)){
					message = MessageUtil.initTextMessage(ToUserName, FromUserName, MessageUtil.menuText());
				}else{
					message = MessageUtil.initTextMessage(ToUserName, FromUserName, "我不明白你的意思，请输入“？”按菜单提示操作！");
				}
			}else if(MsgType.equals(MessageUtil.MESSAGE_EVENT)){
				String eventType = map.get("Event");
				if(MessageUtil.MESSAGE_SUBSCRIBE.equals(eventType)){
					message = MessageUtil.initTextMessage(ToUserName, FromUserName, "欢迎关注我的个人微信订阅号，请输入“？”按菜单提示操作！");
				}else if(MessageUtil.MESSAGE_CLICK.equals(eventType)){
					message = MessageUtil.initNewsMessage(ToUserName, FromUserName);
				}else if(MessageUtil.MESSAGE_VIEW.equals(eventType)){
					String url = map.get("EnentKey");
					message = MessageUtil.initTextMessage(ToUserName, FromUserName, url);
				}else if(MessageUtil.MESSAGE_SCANCODE.equals(eventType)){
					String urlString = map.get("EnentKey");
					message = MessageUtil.initTextMessage(ToUserName, FromUserName, urlString);
				}
			}else if(MessageUtil.MESSAGE_LOCATION.equals(MsgType)){
				String label = map.get("Label");
				message = MessageUtil.initTextMessage(ToUserName, FromUserName, label);
			}
			out.print(message);
		} catch (DocumentException e) {
			e.printStackTrace();
		}finally{
			out.close();
		}
	}
}
