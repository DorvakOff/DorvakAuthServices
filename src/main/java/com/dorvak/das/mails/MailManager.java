package com.dorvak.das.mails;

import com.dorvak.das.DorvakAuthServicesApplication;
import com.dorvak.das.config.Configuration;
import com.dorvak.das.utils.MultiThreading;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class MailManager {

    private final Resource bannerFile;

    private final Session session;

    private final SpringTemplateEngine thymeleafTemplateEngine;

    public MailManager(SpringTemplateEngine thymeleafTemplateEngine) {
        this.thymeleafTemplateEngine = thymeleafTemplateEngine;
        this.bannerFile = new FileSystemResource("DAS/resources/mail-templates/banner.png");
        Configuration configuration = DorvakAuthServicesApplication.getInstance().getConfiguration();
        Properties properties = new Properties();
        properties.put("mail.smtp.host", configuration.getMailHost());
        properties.put("mail.smtp.port", configuration.getMailPort());
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.ssl.protocols", "TLSv1.2");
        properties.put("mail.smtp.ssl.trust", "*");

        session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(configuration.getMailAddress(), configuration.getMailPassword());
            }
        });
    }

    public void sendMail(String subject, String body, String... to) {
        this.sendMail(subject, body, Arrays.asList(to));
    }

    private void sendMail(String subject, String body, List<String> to) {
        MultiThreading.runAsync(() -> {
            try {
                MimeMessage message = new MimeMessage(session);
                Configuration configuration = DorvakAuthServicesApplication.getInstance().getConfiguration();
                message.setFrom(new InternetAddress(configuration.getMailAddress(), configuration.getMailUsername()));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(String.join(",", to)));
                message.setSubject(subject);


                MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");
                messageHelper.setText(body, true);
                messageHelper.addInline("banner.png", bannerFile);

                Transport.send(messageHelper.getMimeMessage());
            } catch (MessagingException | UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void sendMailWithTemplate(String subject, String template, Map<String, Object> variables, String... to) {
        Context context = new Context();
        context.setVariables(variables);

        String body = thymeleafTemplateEngine.process(template, context);

        this.sendMail(subject, body, to);
    }
}
