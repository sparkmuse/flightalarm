package con.github.sparkmuse.flightalarm.service;

import con.github.sparkmuse.flightalarm.config.AlarmConfig;
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
    private final AlarmConfig alarmConfig;

    @Scheduled(cron = "${app.alarm.cron}")
    public void check() {

        Optional<Double> minPrice = fetcherService.getPrices();

        if (minPrice.isPresent()) {

            if (minPrice.get() < alarmConfig.getBudget()) {

                mailService.sendMessage(alarmConfig.getEmail(), minPrice.get(), alarmConfig.getBudget());
            } else {
                log.info("budget = {} and newPrice = {}", alarmConfig.getBudget(), minPrice.get());
            }

        }
    }
}