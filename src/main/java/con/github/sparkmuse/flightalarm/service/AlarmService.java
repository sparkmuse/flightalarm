package con.github.sparkmuse.flightalarm.service;

import con.github.sparkmuse.flightalarm.config.AlarmConfig;
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
    private final AlarmConfig alarmConfig;

    @Scheduled(cron = "0 */30 * ? * *")
    public void check() {

        Optional<Double> minPrice = fetcherService.getPrices();

        if (minPrice.isPresent()) {

            if (minPrice.get() < alarmConfig.getBudget()) {
                Price newPrice = repository.updatePrice(minPrice.get());
                mailService.sendMessage(alarmConfig.getEmail(), newPrice);
            } else {
                log.info("budget = {} and newPrice = {}", alarmConfig.getBudget(), minPrice.get());
            }

        }
    }
}