package con.github.sparkmuse.flightalarm.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "app.alarm")
@Data
public class AlarmConfig {
    private String email;
    private Double budget;
    private String cron;
}
