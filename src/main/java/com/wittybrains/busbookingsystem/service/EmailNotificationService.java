package com.wittybrains.busbookingsystem.service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.wittybrains.busbookingsystem.model.Booking;

@Service
public class EmailNotificationService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public EmailNotificationService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendBookingNotification(String message, Booking booking, String toEmail) throws MessagingException {
        if (toEmail == null || toEmail.isEmpty() || booking == null) {
            throw new IllegalArgumentException("Invalid email address or booking object.");
        }
        
        MimeMessage emailMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(emailMessage, true);
        helper.setTo(toEmail);
        helper.setSubject("Booking Confirmation");

        String text = String.format(
                "<html><body><p>Dear %s,</p>" +
                        "<p>%s</p>" +
                        "<p>Your booking with the following details has been confirmed:</p>" +
                        "<ul>" +
                        "<li>Schedule: %s - %s</li>" +
                        "<li>Date: %s</li>" +
                        "<li>Departure Time: %s</li>" +
                        "<li>Arrival Time: %s</li>" +
                        "<li>Bus Number: %s</li>" +
                        "<li>Total Amount: %s</li>"+
                        "</ul>" +
                        "<p>Thank you for choosing our service. Have a safe and pleasant journey!</p>" +
                        "<p>Best regards,</p>" +
                        "<p>The Bus Booking System Team</p>" +
                        "</body></html>",
            booking.getUser().getUsername(),
            message,
            booking.getSchedule().getSource(),
            booking.getSchedule().getDestination(),
            booking.getSchedule().getEstimatedDepartureTime(),
            booking.getSchedule().getEstimatedDepartureTime(),
            booking.getSchedule().getEstimatedArrivalTime(),
            booking.getSchedule().getBus().getNumber(),
            booking.getSchedule().getTotalAmount()
        );

        helper.setText(text, true);

        mailSender.send(emailMessage);
    }
}

