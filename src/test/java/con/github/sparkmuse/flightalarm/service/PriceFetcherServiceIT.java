package con.github.sparkmuse.flightalarm.service;

import con.github.sparkmuse.flightalarm.config.ChromeDriverConfig;
import con.github.sparkmuse.flightalarm.config.FetcherConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.RemoteWebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(
        classes = {PriceFetcherService.class, FetcherConfig.class, ChromeDriverConfig.class},
        properties = {
                "app.fetcher.url=file:///Users/alfredo/github/flightalarm/src/test/resources/finishedSearch.html"
        }
)
public class PriceFetcherServiceIT {

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