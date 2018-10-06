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
import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@SpringBootTest(
        classes = {PriceFetcherService.class, FetcherConfig.class},
        properties = {
                "app.fetcher.url=file:///Users/alfredo/github/flightalarm/src/test/resources/finishedSearch.html",
                "app.scheduling.enabled=false"
        }
)
public class PriceFetcherServiceIT {

    @Autowired
    private PriceFetcherService service;

    @Test
    public void canGetValue() {

        Optional<Double> price = service.getPrices();

        assertThat(price, is(Optional.of(100.0d)));
    }
}