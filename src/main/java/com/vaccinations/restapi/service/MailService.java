package com.vaccinations.restapi.service;

import com.google.api.client.util.Base64;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
import com.vaccinations.restapi.dto.ReservationDTO;
import com.vaccinations.restapi.model.Mail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Slf4j
@Service
public class MailService {
    private final String SUCCESS_EMAIL_TEMPLATE = "success-email-template";
    private final String FAILURE_EMAIL_TEMPLATE = "failure-email-template";
    private final Gmail gmail;
    private final SpringTemplateEngine templateEngine;

    public MailService(Gmail gmail, SpringTemplateEngine templateEngine) {
        this.gmail = gmail;
        this.templateEngine = templateEngine;
    }

    public Mail createRegistrationConfirmMail(ReservationDTO reservationDTO) {
        Mail mail = createRegistrationMailTemplate(reservationDTO.getEmail());

        Map<String, Object> model = new HashMap<>();
        model.put("sex", (reservationDTO.getPesel().charAt(9) - '0') % 2 == 0 ? "FEMALE" : "MALE");
        model.put("date", reservationDTO.getVaccinationDTO().getDate());
        model.put("time", reservationDTO.getVaccinationDTO().getTime());
        model.put("address", reservationDTO.getVaccinationDTO().getAddress());
        mail.setModel(model);
        mail.setTemplate(SUCCESS_EMAIL_TEMPLATE);

        return mail;
    }

    public Mail createRegistrationRefusalMail(String recipient) {
        Mail mail = createRegistrationMailTemplate(recipient);
        mail.setTemplate(FAILURE_EMAIL_TEMPLATE);

        return mail;
    }

    public void sendMail(Mail mail) throws MessagingException, IOException {
        Message message = createMessageWithEmail(createMimeMessage(mail));
        message = gmail.users().messages().send("me", message).execute();
        log.info("Message id: " + message.getId() + " " + message.toPrettyString());
    }

    private Message createMessageWithEmail(MimeMessage email) throws IOException, MessagingException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        email.writeTo(buffer);
        byte[] bytes = buffer.toByteArray();
        String encodedEmail = Base64.encodeBase64URLSafeString(bytes);
        Message message = new Message();
        message.setRaw(encodedEmail);

        return message;
    }

    private MimeMessage createMimeMessage(Mail mail) throws MessagingException {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        MimeMessage email = new MimeMessage(session);
        email.setFrom(new InternetAddress(mail.getFrom()));
        email.setRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(mail.getTo()));
        email.setSubject(mail.getSubject());
        email.setContent(getHtmlContent(mail), "text/html; charset=UTF-8");

        return email;
    }

    private String getHtmlContent(Mail mail) {
        Context context = new Context();
        context.setVariables(mail.getModel());

        return templateEngine.process(mail.getTemplate(), context);
    }

    private Mail createRegistrationMailTemplate(String recipient) {
        Mail mail = new Mail();
        mail.setFrom("e-rejestracja covid-19 <covid19.vaccinations.2021@gmail.com>");
        mail.setTo(recipient);
        mail.setSubject("Rejestracja na szczepienie przeciw COVID-19");

        return mail;
    }
}