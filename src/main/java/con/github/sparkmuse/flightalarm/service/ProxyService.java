package con.github.sparkmuse.flightalarm.service;

import con.github.sparkmuse.flightalarm.ChromeDriverHelper;
import con.github.sparkmuse.flightalarm.config.ProxyConfig;
import con.github.sparkmuse.flightalarm.entity.Proxy;
import con.github.sparkmuse.flightalarm.entity.ProxyResult;
import con.github.sparkmuse.flightalarm.entity.ProxyResultType;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProxyService {

    private final ProxyConfig config;

    private ChromeDriver driver;

    public ProxyResult getProxy() {

        driver = ChromeDriverHelper.getDriver();

        driver.get(config.getProxyUrl());
        Document document = Jsoup.parse(driver.getPageSource());

        Elements rows = document.getElementsByAttribute(config.getRowCss());

        Optional<Proxy> proxy = rows
                .stream()
                .map(this::getProxy)
                .filter(this::isElite)
                .sorted(Comparator.comparingInt(Proxy::getSpeed))
                .filter(this::isValidServer)
                .findFirst();

        driver.close();
        driver.quit();

        ProxyResult result = new ProxyResult();

        if (proxy.isPresent()) {
            result.setProxyResultType(ProxyResultType.OK);
            result.setProxy(proxy.get());
        } else {
            result.setProxyResultType(ProxyResultType.NO_PROXY);
        }

        return result;
    }

    private boolean isElite(Proxy p) {
        return p.getAnonymity().equalsIgnoreCase(config.getProxyType());
    }

    private Proxy getProxy(Element element) {
        Elements cells = element.getElementsByTag("td");
        return new Proxy(
                cells.get(0).text(),
                cells.get(1).text(),
                cells.get(6).text(),
                new Integer(cells.get(3).text().replace(" ms", "")));
    }

    private Boolean isValidServer(Proxy proxy) {

        ChromeDriver driver = ChromeDriverHelper.getDriver(proxy);
        driver.get(config.getValidServerTestUrl());

        WebElement searchBox = null;
        try {
            searchBox = driver.findElement(By.cssSelector(config.getValidServerSearchCss()));
        } catch (NoSuchElementException ex) {
            //does not have search box
        }

        boolean isValid = (searchBox != null);

        driver.close();
        driver.quit();

        return isValid;
    }
}