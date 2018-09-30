package con.github.sparkmuse.flightalarm.service;

import con.github.sparkmuse.flightalarm.entity.Price;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;
    private final Configuration freeMarkerConfig;

    public void sendMessage(String to, String subject, String text) {

        try {
            String id = UUID.randomUUID().toString();

            Map<String, Object> model = new HashMap<>();
            model.put("price", new Price(Price.ID, 300.0d, 200.0d));
            model.put("id", id);

            MimeMessage mimeMessage = mailSender.createMimeMessage();

            MimeMessageHelper helper =
                    new MimeMessageHelper(mimeMessage,
                            MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                            StandardCharsets.UTF_8.name());

            MimeBodyPart image = new MimeBodyPart();
            image.attachFile(new ClassPathResource("alarm.jpg").getFile());
            image.setContentID("<" + id + ">");
            image.setDisposition(MimeBodyPart.INLINE);


            MimeBodyPart htmlPart = new MimeBodyPart();
            Template template = freeMarkerConfig.getTemplate("message.ftl");
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
            htmlPart.setText(html, "UTF-8", "html");


            MimeMultipart content = new MimeMultipart();
            content.addBodyPart(htmlPart);
            content.addBodyPart(image);


            helper.setTo(to);
            helper.setSubject(subject);
            mimeMessage.setContent(content);

            mailSender.send(mimeMessage);

        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
}
