package com.launch.email;
import java.util.Properties;  
import javax.mail.Address;  
import javax.mail.BodyPart;  
import javax.mail.Message;  
import javax.mail.Multipart;  
import javax.mail.Session;  
import javax.mail.Transport;  
import javax.mail.internet.InternetAddress;  
import javax.mail.internet.MimeBodyPart;  
import javax.mail.internet.MimeMessage;  
import javax.mail.internet.MimeMultipart; 
public class MailInfo {
	public MimeMessage mimeMsg;
	private Session session;
	private Properties props;
	private String username = "";
	private String password = "";
	private Multipart mp;

	public MailInfo(String stmp) {
		setSmtpHost(stmp);
		createMimeMessage();
	}

	/**
	 * 
	 * @param hostName
	 */
	public void setSmtpHost(String hostName) {
		// System.out.println("------mail.stmp.host= " + hostName);
		if (props == null) {
			props = System.getProperties();
		}
		props.put("mail.smtp.host", hostName);
		props.put("mail.smtp.starttls.enable", "true");
	}

	public boolean createMimeMessage() {
		try {
			// System.out.println("------Session begin");
			session = Session.getInstance(props, null);
		} catch (Exception e) {
			System.out.println("Session.getInstance failed!" + e);
			return false;
		}
		// System.out.println("------MimeMessage begin");
		try {
			mimeMsg = new MimeMessage(session);
			mp = new MimeMultipart();
			return true;
		} catch (Exception e) {
			System.out.println("MimeMessage fiald! " + e.toString());
			return false;
		}
	}

	/**
	 * 
	 * @param need
	 */
	public void setNeedAuth(boolean need) {
		// System.out.println("------mail.smtp.auth=" + need);
		if (props == null) {
			props = System.getProperties();
		}
		if (need) {
			props.put("mail.smtp.auth", "true");
		} else {
			props.put("mail.smtp.auth", "false");
		}
	}

	/**
	 * 
	 * @param name
	 * @param pass
	 */
	public void setNamePass(String name, String pass) {
		username = name;
		password = pass;
	}

	/**
	 * 
	 * @param mailSubject
	 * @return boolean
	 */
	public boolean setSubject(String mailSubject) {
		// System.out.println("------Set title");
		try {
			if (!mailSubject.equals("") && mailSubject != null) {
				mimeMsg.setSubject(mailSubject);
			}
			return true;
		} catch (Exception e) {
			System.out.println("======Set title failed!");
			return false;
		}
	}

	/**
	 * 
	 * @param from
	 * @return
	 */
	public boolean setFrom(String from) {
		// System.out.println("------Set from");
		try {
			mimeMsg.setFrom(new InternetAddress(from));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 
	 * @param to
	 * @return
	 */
	public boolean setTo(String to) {
		// System.out.println("------Set to");
		if (to == null || to.equals("")) {
			return false;
		}
		try {
			mimeMsg.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(to));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean setCopyTo(String copyto) {
		if (copyto.equals("") || copyto == null) {
			return false;
		}
		try {
			String copy[];
			copy = copyto.split(";");
			for (int i = 0; i < copy.length; i++) {
				mimeMsg.setRecipients(Message.RecipientType.TO,
						(Address[]) InternetAddress.parse(copy[i]));
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean setOut() {
		try {
			mimeMsg.setContent(mp);
			mimeMsg.saveChanges();
			// System.out.println("------Sending mail");
			Session mailSession = Session.getInstance(props, null);
			Transport tp = mailSession.getTransport("smtp");
			tp.connect((String) props.getProperty("mail.stmp.host"), username,
					password);
			tp.sendMessage(mimeMsg,
					mimeMsg.getRecipients(Message.RecipientType.TO));
			// System.out.println("======Success");
			tp.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Set content
	 */
	public boolean setBody(String mailBody) {
		try {
			BodyPart bp = new MimeBodyPart();
			bp.setContent(
					"<meta http-equiv=Context-Type context=text/html;charset=gb2312>"
							+ mailBody, "text/html;charset=GB2312");
			mp.addBodyPart(bp);
			return true;
		} catch (Exception e) {
			System.out.println("======Set context Failed! " + e);
			return false;
		}
	}
}
