package com.ala.ala.services;


import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


@Service
public class NotificationService {

    private final JavaMailSender javaMailSender;
    public static final String ACCOUNT_SID = "AC793dde566f68b2320f85780e2bf8dc10";
    public static final String AUTH_TOKEN = "cc5dba8f8dafb806ab5694b52f38829f";

    @Autowired
    public NotificationService(JavaMailSender javaMailSender){
        this.javaMailSender = javaMailSender;
    }

    @Async
    public void sendMailNotification(String email, String subject, String text)
            throws MailException, InterruptedException {

        System.out.println("Sending email...");

        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(email);
        mail.setFrom("alaaeddinn.zid@esprit.tn");
        mail.setSubject(subject);
        mail.setText(text);
        javaMailSender.send(mail);

        System.out.println("Email Sent!");
    }

    @Async
    public void sendMessageNotification(String to, String from, String text) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Message message = Message.creator(
                        new PhoneNumber(to),
                        new PhoneNumber(from),
                        text)
                .create();
    }
}
