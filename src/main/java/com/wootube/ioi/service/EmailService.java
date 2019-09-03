package com.wootube.ioi.service;

import com.wootube.ioi.service.exception.SendFailException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@Service
public class EmailService {
    private static final String EMAIL_CONTENTS = "현재 귀하의 계정은 비활성화 상태입니다. %s를 클릭할 시 활성화 상태로 변경됩니다. <br>";
    private static final String EMAIL_SUBJECT = "계정 비활성화 관련 안내 메일입니다.";

    private final Environment environment;
    private final VerifyKeyService verifyKeyService;
    private final JavaMailSender emailSender;

    @Value("${spring.mail.service.hostUrl}")
    private String hostUrl;

    @Autowired
    public EmailService(Environment environment, VerifyKeyService verifyKeyService, JavaMailSender emailSender) {
        this.environment = environment;
        this.verifyKeyService = verifyKeyService;
        this.emailSender = emailSender;
    }

    public void sendMessage(String inActiveUserEmail) {
        try {
            String verifyKey = verifyKeyService.createVerifyKey(inActiveUserEmail);
            String contents = String.format(EMAIL_CONTENTS, generateLink(hostUrl, inActiveUserEmail, verifyKey));
            MimeMessage message = generateMessage(inActiveUserEmail, contents);
            emailSender.send(message);
        } catch (MessagingException e) {
            throw new SendFailException();
        }
    }

    private String generateLink(String hostUrl, String inActiveUserEmail, String verifyKey) {
        return "<a href=\"http://" + hostUrl + ":" + environment.getProperty("local.server.port") + "/user/confirm?email=" + inActiveUserEmail + "&verifyKey=" + verifyKey + "\">여기</a>";
    }

    private MimeMessage generateMessage(String inActiveUserEmail, String contents) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        message.setSubject(EMAIL_SUBJECT);
        message.setText(contents, "UTF-8", "html");
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(inActiveUserEmail));
        return message;
    }
}
