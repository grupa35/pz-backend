package pl.shopgen.services;

public interface EmailService {
    void sendSimpleMessage(String to, String subject, String text);
}
