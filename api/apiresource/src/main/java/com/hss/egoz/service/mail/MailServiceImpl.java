package com.hss.egoz.service.mail;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hss.egoz.constants.MailBox;
import com.hss.egoz.constants.Secrets;
import com.hss.egoz.service.util.DataService;

/*
 * @author Satyam Pandey
 * Service to Send Mail...
 * */
@Service
public class MailServiceImpl implements MailService {

	@Autowired
	private DataService dataService;

	private final String userId;
	private final String password;
	private final String host;
	private final String port;

	public MailServiceImpl() {
		userId = Secrets.E_MAIL;
		password = Secrets.PASSWORD;
		host = "smtp.gmail.com";
		port = "587";
	}

	@Override
	public void send(String eMail, String subject, String content, String fileLocation, MailBox mode) {

		// Get Properties Object...
		java.util.Properties props = this.getProperties();

		// Get Session Object...
		javax.mail.Session session = this.getSession(props);
		try {

			// Generate from, to and create MimeMessage Object...
			javax.mail.internet.InternetAddress from = new javax.mail.internet.InternetAddress(userId);
			javax.mail.internet.InternetAddress to = new javax.mail.internet.InternetAddress(eMail);
			javax.mail.internet.MimeMessage message = new javax.mail.internet.MimeMessage(session);
			message.setSentDate(new java.util.Date());
			message.setFrom(from);
			message.addRecipient(javax.mail.Message.RecipientType.TO, to);
			message.setSubject(subject);

			// Set Content according to mode of Mail Content...
			if (mode == MailBox.ATTACHMENT && fileLocation!=null)
				message.setContent(this.getMultipart(content, fileLocation));
			else if (mode == MailBox.HTML)
				message.setContent(content, "text/html");
			else if (mode == MailBox.PLAIN)
				message.setText(content);

			javax.mail.Transport.send(message);

		} catch (Exception exc) {
			exc.printStackTrace();
		}

	}

	// Create Properties object
	private java.util.Properties getProperties() {
		java.util.Properties properties = new java.util.Properties();
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.port", port);
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.ssl.trust", host);
		properties.put("mail.smtp.auth", "true");
		return properties;
	}

	// Get Session Object
	private javax.mail.Session getSession(java.util.Properties props) {
		return javax.mail.Session.getDefaultInstance(props, new javax.mail.Authenticator() {
			protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
				return new javax.mail.PasswordAuthentication(userId, password);
			}
		});
	}

	// Get Multipart Data
	private Multipart getMultipart(String content, String fileLocation) throws javax.mail.MessagingException {

		Multipart multipart = new MimeMultipart();
		BodyPart body = new MimeBodyPart();

		// Add content to Multipart...
		body.setContent(content, "text/html");
		multipart.addBodyPart(body);

		// Add File to Attachment...
		body = new MimeBodyPart();
		DataSource source = new FileDataSource(fileLocation);
		body.setDataHandler(new DataHandler(source));
		String fileName = dataService.breakString(fileLocation, "/")
				.get(dataService.breakString(fileLocation, "/").size() - 1);
		fileName = dataService.breakString(fileName, "\\").get(dataService.breakString(fileName, "\\").size() - 1);
		body.setFileName(fileName);
		multipart.addBodyPart(body);

		// Return Multipart...
		return multipart;
	}
}