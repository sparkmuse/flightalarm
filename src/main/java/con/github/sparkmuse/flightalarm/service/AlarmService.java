package con.github.sparkmuse.flightalarm.service;

import con.github.sparkmuse.flightalarm.entity.Price;
import con.github.sparkmuse.flightalarm.repository.PriceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlarmService {

    private final MailService mailService;
    private final PriceFetcherService fetcherService;
    private final PriceRepository repository;

    @Scheduled(cron = "0 * * ? * *")
    public void check() {

        Optional<Double> minPrice = fetcherService.getPrices();

        if(minPrice.isPresent()) {

            Optional<Price> foundPrice = repository.findById(Price.ID);

            if (foundPrice.isPresent()) {
                if (minPrice.get() < foundPrice.get().getNewPrice()) {
                    Price newPrice = repository.updatePrice(minPrice.get());
                    mailService.sendMessage("alfredo.lopez002@gmail.com", newPrice);
                } else {
                    log.info("oldPrice = {}  and newPrice = {}", foundPrice.get().getNewPrice(), minPrice.get());
                }
            }
        }
    }
}