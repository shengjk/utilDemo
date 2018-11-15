package util.email;

import org.apache.log4j.Logger;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

/**
 * 公司自己的邮件服务器
 */
public class SendMail {
	private static Logger logger = Logger.getLogger(SendMail.class);
	private static SendMail instance = null;

	private SendMail() {

	}

	public static SendMail getInstance() {
		if (instance == null) {
			instance = new SendMail();
		}
		return instance;
	}

	public void send(String to[], String cs[], String ms[], String subject,
			String content, String formEmail, String fileList[]) {
		try {
			Properties p = new Properties(); // Properties p =
			// System.getProperties();
			p.put("mail.smtp.auth", "true");
			p.put("mail.transport.protocol", "smtp");
			p.put("mail.smtp.host", "mail.xxx.com");
			p.put("mail.smtp.port", "25");
			// 建立会话
			Session session = Session.getInstance(p);
			Message msg = new MimeMessage(session); // 建立信息
			BodyPart messageBodyPart = new MimeBodyPart();
			Multipart multipart = new MimeMultipart();
			msg.setFrom(new InternetAddress(formEmail)); // 发件人

			String toList = null;
			String toListcs = null;
			String toListms = null;

			// 发送,
			if (to != null) {
				toList = getMailList(to);
				new InternetAddress();
				InternetAddress[] iaToList = InternetAddress
						.parse(toList);
				msg.setRecipients(Message.RecipientType.TO, iaToList); // 收件人
			}

			// 抄送
			if (cs != null) {
				toListcs = getMailList(cs);
				new InternetAddress();
				InternetAddress[] iaToListcs = InternetAddress
						.parse(toListcs);
				msg.setRecipients(Message.RecipientType.CC, iaToListcs); // 抄送人
			}

			// 密送
			if (ms != null) {
				toListms = getMailList(ms);
				new InternetAddress();
				InternetAddress[] iaToListms = InternetAddress
						.parse(toListms);
				msg.setRecipients(Message.RecipientType.BCC, iaToListms); // 密送人
			}
			msg.setSentDate(new Date()); // 发送日期
			msg.setSubject(subject); // 主题
			msg.setText(content); // 内容
			// 显示以html格式的文本内容
			messageBodyPart.setContent(content, "text/html;charset=utf-8");
			multipart.addBodyPart(messageBodyPart);

			// 2.保存多个附件
			if (fileList != null) {
				addTach(fileList, multipart);
			}

			msg.setContent(multipart);
			// 邮件服务器进行验证
			Transport tran = session.getTransport("smtp");
			tran.connect("xxx", "xxx",
					"xxx");
			tran.sendMessage(msg, msg.getAllRecipients()); // 发送
			System.out.println("邮件发送成功");

		} catch (Exception e) {
			logger.info("邮件发送时异常",e);
		}
	}

	// 添加多个附件
	public void addTach(String fileList[], Multipart multipart)
			throws MessagingException, UnsupportedEncodingException {
		for (int index = 0; index < fileList.length; index++) {
			MimeBodyPart mailArchieve = new MimeBodyPart();
			FileDataSource fds = new FileDataSource(fileList[index]);
			mailArchieve.setDataHandler(new DataHandler(fds));
			mailArchieve.setFileName(MimeUtility.encodeText(fds.getName(),
					"utf-8", "B"));
			multipart.addBodyPart(mailArchieve);
		}
	}

	private String getMailList(String[] mailArray) {

		StringBuffer toList = new StringBuffer();
		int length = mailArray.length;
		if (mailArray != null && length < 2) {
			toList.append(mailArray[0]);
		} else {
			for (int i = 0; i < length; i++) {
				toList.append(mailArray[i]);
				if (i != (length - 1)) {
					toList.append(",");
				}

			}
		}
		return toList.toString();

	}
	public static void forSend(String subject,String content){
		SendMail send = SendMail.getInstance();
		String to[] = { "xxxxx"};
		String cs[] = null;
		String ms[] = null;

		if(content==null||content.length()==0){
			content = "这是邮件内容，仅仅是测试，不需要回复";
		}
		String fromEmail = "xxx";
		String[] arrArchievList = null;

		// 2.保存多个附件
		send.send(to, cs, ms, subject, content, fromEmail, arrArchievList);
	}
	
//	public static void main(String args[]) {
//		forSend(null);
//	}

}