package pwr.edu.pl.travelly.api.email.service;

import pwr.edu.pl.travelly.api.email.context.AbstractEmailContext;

import javax.mail.MessagingException;

public interface EmailService {
    void sendEmail(AbstractEmailContext email) throws MessagingException;
}
