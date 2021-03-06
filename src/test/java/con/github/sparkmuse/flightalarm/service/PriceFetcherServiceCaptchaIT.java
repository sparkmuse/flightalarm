package con.github.sparkmuse.flightalarm.service;

import con.github.sparkmuse.flightalarm.config.FetcherConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(
        classes = {PriceFetcherService.class, FetcherConfig.class},
        properties = {
                "app.fetcher.url=file:///Users/alfredo/github/flightalarm/src/test/resources/captcha.html",
                "app.scheduling.enabled=false"
        }
)
public class PriceFetcherServiceCaptchaIT {

    @Autowired
    private PriceFetcherService service;

    @Test
    public void getsEmptyValueWithCaptcha() {

        Optional<Double> price = service.getPrices();

        assertThat(price, is(Optional.empty()));
    }
}