package con.github.sparkmuse.flightalarm.service;

import con.github.sparkmuse.flightalarm.config.FetcherConfig;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PriceFetcherService {

    private static final String FINISHED_CSS_SELECTOR = ".Common-Results-SpinnerWithProgressBar.finished";
    private static final String PRICE_CSS_SELECTOR = "span[class='price option-text']";
    private static final String CAPTCHA_CSS_SELECTOR = "div[id='bd']";

    @Autowired
    private final FetcherConfig fetcherConfig;

    private ChromeDriver driver;

    public Optional<Double> getPrices() {

        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        driver = new ChromeDriver(options);

        driver.get(fetcherConfig.getUrl());

        if (ifFinished()) {
            List<WebElement> elements = driver.findElements(By.cssSelector(PRICE_CSS_SELECTOR));

            Optional<Double> minimum = elements
                    .stream()
                    .map(WebElement::getText)
                    .map(this::removeEuro)
                    .map(Double::new)
                    .min(Comparator.comparingDouble(s -> s));

            closeDriver();

            return minimum;
        }

        closeDriver();
        return Optional.empty();
    }

    private void closeDriver() {
        driver.close();
        driver.quit();
    }

    private String removeEuro(String original) {
        return original.replace(" EUR", "");
    }

    private Boolean ifFinished() {

        if(hasCaptcha()) {
            return false;
        }

        long startTime = System.currentTimeMillis();
        WebElement element = null;
        while (element == null && (System.currentTimeMillis()-startTime)< fetcherConfig.getTimeOut()) {
            try {
                element = driver.findElementByCssSelector(FINISHED_CSS_SELECTOR);
            } catch (NoSuchElementException ex) {
                // I am waiting here...
            }
        }
        return true;
    }

    private Boolean hasCaptcha() {
        WebElement captcha = null;
        try {
            captcha = driver.findElement(By.cssSelector(CAPTCHA_CSS_SELECTOR));
        } catch (NoSuchElementException ex) {
            //does not have captcha
        }
        return (captcha != null);
    }
}
