package con.github.sparkmuse.flightalarm.service;

import con.github.sparkmuse.flightalarm.config.ChromeDriverConfig;
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
        classes = {PriceFetcherService.class, FetcherConfig.class, ChromeDriverConfig.class},
        properties = {
                "app.fetcher.url=file:///Users/alfredo/github/flightalarm/src/test/resources/unfinishedSearch.html"
        }
)
public class PriceFetcherServiceTimeoutIT {

    @Autowired
    private PriceFetcherService service;

    @Autowired
    private ChromeDriver driver;

    @Test
    public void canGetValue() {

        Optional<Double> price = service.getPrices();

        assertThat(price, is(Optional.of(100.0d)));
    }
}