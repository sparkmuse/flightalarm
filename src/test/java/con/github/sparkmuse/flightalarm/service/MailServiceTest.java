package con.github.sparkmuse.flightalarm.service;

import con.github.sparkmuse.flightalarm.entity.Price;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.junit4.SpringRunner;

import javax.mail.internet.MimeMessage;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MailServiceTest {

    @Autowired
    private MailService mailService;

    @SpyBean
    private JavaMailSender mailSender;

    @Test
    public void send() {

        Price price = new Price(Price.ID, 300.0d, 200.0d);
        mailService.sendMessage("alfredo.lopez002@gmail.com", price);
        
        verify(mailSender).send(any(MimeMessage.class));
    }
}