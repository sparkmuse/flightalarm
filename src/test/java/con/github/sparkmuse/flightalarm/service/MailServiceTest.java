package con.github.sparkmuse.flightalarm.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.junit4.SpringRunner;

import javax.mail.internet.MimeMessage;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = "app.scheduling.enabled=false")
public class MailServiceTest {

    @Autowired
    private MailService mailService;

    @SpyBean
    private JavaMailSender mailSender;

    @Test
    public void send() {

        mailService.sendMessage("myemail@gmail.com",  200.0d, 300.0d);

        verify(mailSender).send(any(MimeMessage.class));
    }
}