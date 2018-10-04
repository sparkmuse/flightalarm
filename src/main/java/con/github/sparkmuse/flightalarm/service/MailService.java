package con.github.sparkmuse.flightalarm.service;

import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MailService {

    private static final String ALARM_IMG = "alarm.jpg";
    private static final String MESSAGE_TEMPLATE = "message.ftl";

    private final JavaMailSender mailSender;
    private final Configuration freeMarkerConfig;

    public void sendMessage(String to, Double price, Double budget) {

        try {
            String id = UUID.randomUUID().toString();

            Map<String, Object> model = new HashMap<>();
            model.put("price", price);
            model.put("budget", budget);
            model.put("id", id);

            MimeMessage mimeMessage = mailSender.createMimeMessage();

            MimeMessageHelper helper =
                    new MimeMessageHelper(mimeMessage,
                            MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                            StandardCharsets.UTF_8.name());

            MimeBodyPart image = new MimeBodyPart();
            image.attachFile(new ClassPathResource(ALARM_IMG).getFile());
            image.setContentID("<" + id + ">");
            image.setDisposition(MimeBodyPart.INLINE);


            MimeBodyPart htmlPart = new MimeBodyPart();
            Template template = freeMarkerConfig.getTemplate(MESSAGE_TEMPLATE);
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
            htmlPart.setText(html, "UTF-8", "html");


            MimeMultipart content = new MimeMultipart();
            content.addBodyPart(htmlPart);
            content.addBodyPart(image);

            helper.setTo(to);
            helper.setSubject("New price alert");
            helper.setPriority(1);
            mimeMessage.setContent(content);

            mailSender.send(mimeMessage);

        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
}
