package con.github.sparkmuse.flightalarm.service;

import con.github.sparkmuse.flightalarm.ChromeDriverHelper;
import con.github.sparkmuse.flightalarm.config.FetcherConfig;
import con.github.sparkmuse.flightalarm.entity.*;
import con.github.sparkmuse.flightalarm.repository.PriceResultRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
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

    private final FetcherConfig fetcherConfig;
    private final ProxyService proxyService;
    private final PriceResultRepository repository;

    private ChromeDriver driver;

    public Optional<Double> getPrices() {

        PriceResult priceResult = new PriceResult();
        ProxyResult proxyServerResult = proxyService.getProxy();
        priceResult.setProxyResult(proxyServerResult);

        if (proxyServerResult.getProxyResultType() == ProxyResultType.OK) {
            driver = ChromeDriverHelper.getDriver(proxyServerResult.getProxy());
        } else {
            driver = ChromeDriverHelper.getDriver();
        }

        driver.get(fetcherConfig.getUrl());

        if (hasCaptcha()) {
            log.info("captcha is activated");
            closeDriver();

            priceResult.setResult(PriceResultType.CAPTCHA);
            repository.save(priceResult);

            return Optional.empty();
        }

        if (!isFinished()) {
            log.info("browser did not finish loading page.");
            closeDriver();

            priceResult.setResult(PriceResultType.OTHER);
            repository.save(priceResult);

            return Optional.empty();
        }

        List<WebElement> elements = driver.findElements(By.cssSelector(PRICE_CSS_SELECTOR));

        Optional<Double> minimum = elements
                .stream()
                .map(WebElement::getText)
                .map(this::removeEuro)
                .map(this::fixDecimals)
                .map(Double::new)
                .min(Comparator.comparingDouble(s -> s));

        closeDriver();

        priceResult.setResult(PriceResultType.OK);
        priceResult.setPrice(minimum.get());
        repository.save(priceResult);

        return minimum;
    }

    private void closeDriver() {
        driver.close();
        driver.quit();
    }

    private String fixDecimals(String original) {
        return original.replace(".", "").replace(",", "");
    }

    private String removeEuro(String original) {
        return original.replace(" EUR", "");
    }

    private Boolean isFinished() {

        long startTime = System.currentTimeMillis();
        WebElement element = null;
        while (element == null && (System.currentTimeMillis() - startTime) < fetcherConfig.getTimeOut()) {
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
