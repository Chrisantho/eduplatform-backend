package com.eduplatform.service;

import com.sendgrid.*;
import com.sendgrid.helpers.mail.objects.Personalization;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.*;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final String sendGridApiKey;
    private final String fromEmail;

    public EmailService() {
        this.sendGridApiKey = System.getenv("SENDGRID_API_KEY");
        this.fromEmail = System.getenv("SENDGRID_FROM_EMAIL");
    }

    public boolean sendPasswordResetCode(String toEmail, String code, String userName) {
        if (sendGridApiKey == null || sendGridApiKey.isEmpty() || fromEmail == null || fromEmail.isEmpty()) {
            System.out.println("SendGrid not configured. Password reset code for " + toEmail + ": " + code);
            return false;
        }

        String subject = "Your EduPlatform Password Reset Code";

        String html = "<div style=\"font-family: Arial, sans-serif; max-width: 500px; margin: 0 auto; padding: 20px;\">"
                + "<div style=\"text-align: center; padding: 20px 0;\">"
                + "<h2 style=\"color: #3b82f6; margin: 0;\">EduPlatform</h2>"
                + "</div>"
                + "<div style=\"background: #f8fafc; border-radius: 8px; padding: 30px; text-align: center;\">"
                + "<h3 style=\"margin-top: 0;\">Password Reset Request</h3>"
                + "<p>Hi " + escapeHtml(userName) + ",</p>"
                + "<p>You requested a password reset. Use the code below:</p>"
                + "<div style=\"background: #ffffff; border: 2px dashed #3b82f6; border-radius: 8px; padding: 20px; margin: 20px 0;\">"
                + "<span style=\"font-size: 32px; font-weight: bold; letter-spacing: 8px; color: #1e293b;\">" + code + "</span>"
                + "</div>"
                + "<p style=\"color: #64748b; font-size: 14px;\">This code expires in 15 minutes.</p>"
                + "</div></div>";

        String text = "Hi " + userName + ",\n\nYour password reset code is: " + code
                + "\n\nThis code expires in 15 minutes.\n\n- EduPlatform Team";

        try {
            Email from = new Email(fromEmail, "EduPlatform");
            Email to = new Email(toEmail);
            Content htmlContent = new Content("text/html", html);
            Mail mail = new Mail();
            mail.setFrom(from);
            mail.setSubject(subject);

            Personalization personalization = new Personalization();
            personalization.addTo(to);
            mail.addPersonalization(personalization);

            mail.addContent(new Content("text/plain", text));
            mail.addContent(new Content("text/html", html));

            SendGrid sg = new SendGrid(sendGridApiKey);
            Request request = new Request();
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            Response response = sg.api(request);

            if (response.getStatusCode() >= 200 && response.getStatusCode() < 300) {
                System.out.println("Password reset email sent to " + toEmail);
                return true;
            } else {
                System.out.println("SendGrid error " + response.getStatusCode() + ": " + response.getBody());                System.out.println("FALLBACK: Password reset code for " + toEmail + ": " + code);
                return false;
            }
        } catch (Exception e) {
            System.out.println("Failed to send email: " + e.getMessage());
            System.out.println("FALLBACK: Password reset code for " + toEmail + ": " + code);
            return false;
        }
    }

    private String escapeHtml(String input) {
        if (input == null) return "";
        return input.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
    }
}