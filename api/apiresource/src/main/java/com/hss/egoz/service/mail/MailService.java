package com.hss.egoz.service.mail;

import com.hss.egoz.constants.MailBox;

public interface MailService {

	void send(String eMail, String subject, String content, String fileLocation, MailBox mode);

}
