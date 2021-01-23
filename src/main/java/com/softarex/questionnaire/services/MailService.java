package com.softarex.questionnaire.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

@Service
public class MailService {
    private final JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String senderMail;
    @Value("${domain.name}")
    private String domain;
    @Value("${domain.https}")
    private boolean domainHttps;
    @Value("${mail.subjects.signup}")
    private String confirmSignUpSubject;
    @Value("${mail.subjects.passwordreset}")
    private String passwordResetSubject;
    @Value("${mail.subjects.passwordchange}")
    private String passwordChangeSubject;
    private String confirmSignUpMessageTemplate = "";
    private String passwordResetMessageTemplate = "";
    private String passwordChangeMessageTemplate = "";

    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
        InputStream confSignUpInputStream = ClassLoader.getSystemResourceAsStream("mail_templates/confirmSignUp.html");
        Scanner scn = new Scanner(confSignUpInputStream);
        while (scn.hasNextLine()) confirmSignUpMessageTemplate += scn.nextLine();
        InputStream passResetInputStream = ClassLoader.getSystemResourceAsStream("mail_templates/passwordReset.html");
        scn = new Scanner(passResetInputStream);
        while (scn.hasNextLine()) passwordResetMessageTemplate += scn.nextLine();
        InputStream passChangeInputStream = ClassLoader.getSystemResourceAsStream("mail_templates/passwordChange.html");
        scn = new Scanner(passChangeInputStream);
        while (scn.hasNextLine()) passwordChangeMessageTemplate += scn.nextLine();
    }

    private void sendMessage(String email, String subject, String template, Map<String, String> params) {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");
        try {
            helper.setFrom(senderMail);
            helper.setSubject(subject);
            helper.setTo(email);
            String messageText = template;
            for (Map.Entry<String, String> paramPair : params.entrySet()) { messageText = messageText.replace(paramPair.getKey(), paramPair.getValue()); }
            helper.setText(messageText, true);
        } catch (MessagingException e) { e.printStackTrace(); }
        mailSender.send(message);
    }

    public void sendSignUpConfirmation(String email, String code) {
        sendMessage(
                email,
                confirmSignUpSubject,
                confirmSignUpMessageTemplate,
                new HashMap<>() {{
                    put("{{domain}}", domain);
                    put("{{domain_with_protocol}}", domain.startsWith("http://") || domain.startsWith("https://") ? domain : (domainHttps ? "https://" : "http://") + domain);
                    put("{{code}}", code);
                }});
    }

    public void sendPasswordResetLink(String email, String code) {
        sendMessage(
                email,
                passwordResetSubject,
                passwordResetMessageTemplate,
                new HashMap<>() {{
                    put("{{domain}}", domain);
                    put("{{domain_with_protocol}}", domain.startsWith("http://") || domain.startsWith("https://") ? domain : (domainHttps ? "https://" : "http://") + domain);
                    put("{{code}}", code);
                }});
    }

    public void sendPasswordChangeLink(String email, String code) {
        sendMessage(
                email,
                passwordChangeSubject,
                passwordChangeMessageTemplate,
                new HashMap<>() {{
                    put("{{domain}}", domain);
                    put("{{domain_with_protocol}}", domain.startsWith("http://") || domain.startsWith("https://") ? domain : (domainHttps ? "https://" : "http://") + domain);
                    put("{{code}}", code);
                }});
    }
}