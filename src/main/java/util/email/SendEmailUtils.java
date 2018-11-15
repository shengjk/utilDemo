package util.email;

import com.sun.mail.util.MailSSLSocketFactory;
import org.apache.log4j.Logger;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.Properties;

/**
* Created by shengjk1 on 2016/11/4.
* Blog Address:http://blog.csdn.net/jsjsjs1789
*/
public class SendEmailUtils {
   private static Logger logger = Logger.getLogger("SendEmailUtils.class");
   
   //用户名密码验证，需要实现抽象类Authenticator的抽象方法PasswordAuthentication
   static class MyAuthenricator extends Authenticator{
	   String u = null;
	   String p = null;
	   public MyAuthenricator(String u,String p){
		   this.u=u;
		   this.p=p;
	   }
	   @Override
	   protected PasswordAuthentication getPasswordAuthentication() {
		   return new PasswordAuthentication(u,p);
	   }
   }
   
   public static  void send(String title,String context){
	   Properties prop = new Properties();
	   //协议
	   prop.setProperty("mail.transport.protocol", "smtps");
	   //服务器
	   prop.setProperty("mail.smtp.host", "smtp.exmail.qq.com");
	   //端口
	   prop.setProperty("mail.smtp.port", "25");
	   //使用smtp身份验证
	   prop.setProperty("mail.smtp.auth", "true");
	   //使用SSL，企业邮箱必需！
	   //开启安全协议
	   MailSSLSocketFactory sf = null;
	   try {
		   sf = new MailSSLSocketFactory();
		   sf.setTrustAllHosts(true);
	   } catch (GeneralSecurityException e1) {
		   e1.printStackTrace();
	   }
//        prop.put("mail.smtp.ssl.enable", "true");
	   prop.put("mail.smtp.starttls.enable", "true");
	   prop.put("mail.smtp.ssl.socketFactory", sf);
	   //
	   Session session = Session.getDefaultInstance(prop, new MyAuthenricator("发件人", "密码"));
//        session.setDebug(true);
	   MimeMessage mimeMessage = new MimeMessage(session);
	   try {
		   mimeMessage.setFrom(new InternetAddress("发件人",""));
		   Address[] addresses={new InternetAddress("xxx2收件人@xxxxxx.com"),new InternetAddress("xxx1@xxxxxx.com")};
		   
		   mimeMessage.addRecipients(Message.RecipientType.TO, addresses);
		   mimeMessage.setSubject("[重要] spark程序异常-"+title);
		   mimeMessage.setSentDate(new Date());
		   mimeMessage.setText("系统时间戳"+System.currentTimeMillis()+ "\n"+context);
		   mimeMessage.saveChanges();
		   Transport.send(mimeMessage);
	   } catch (Exception e) {
		   logger.error("邮件发送异常 " +e);
		   
	   }
   }
   
   public static void main(String[] args) {
	   SendEmailUtils.send("scanError","hahahahhaha");
	   System.out.println("{\"area_id\":220100,\"city\":\"长春市\",\"country\":\"中国\",\"district\":\"\",\"ip\":\"175.31.246.0\",\"province\":\"吉林省\"}");
   }

}