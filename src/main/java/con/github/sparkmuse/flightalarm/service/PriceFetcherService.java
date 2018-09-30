package con.github.sparkmuse.flightalarm.service;

import com.sun.org.apache.xpath.internal.operations.Bool;
import con.github.sparkmuse.flightalarm.config.FetcherConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
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
    private final ChromeDriver driver;

    @Autowired
    private final FetcherConfig fetcherConfig;

    public Optional<Double> getPrices() {

        driver.get(fetcherConfig.getUrl());

        if (ifFinished()) {
            List<WebElement> elements = driver.findElements(By.cssSelector(PRICE_CSS_SELECTOR));

            return elements
                    .stream()
                    .map(WebElement::getText)
                    .map(this::removeEuro)
                    .map(Double::new)
                    .min(Comparator.comparingDouble(s -> s));

        }

        return Optional.empty();
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
