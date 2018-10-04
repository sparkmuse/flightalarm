package con.github.sparkmuse.flightalarm.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "app.proxy")
@Data
public class ProxyConfig {
    private String proxyUrl;
    private String rowCss;
    private String exclusionProxyType;
    private String validServerTestUrl;
    private String validServerSearchCss;
}
