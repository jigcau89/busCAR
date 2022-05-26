
package com.busCAR.busCAR.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailServicio {

    @Autowired
    private JavaMailSender sender;
    
    @Value("${spring.mail.username}")
    private String from;

    private static final String SUBJECT = "Correo de bienvenida";
    private static final String TEXT = "Bienvenido a nuestra página busCAR. Gracias por registrarte";

    @Async
    public void enviar(String to) {
        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setTo(to);
        mensaje.setFrom(from);
        mensaje.setSubject(SUBJECT);
        mensaje.setText(TEXT);
        sender.send(mensaje);
    }
    
    
    public void enviarThread(String to) {
        new Thread(() -> {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setFrom(from);
            message.setSubject(SUBJECT);
            message.setText(TEXT);
            sender.send(message);
        }).start();
    }
    
}
