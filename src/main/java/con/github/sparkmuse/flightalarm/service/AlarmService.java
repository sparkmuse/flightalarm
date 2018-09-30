package con.github.sparkmuse.flightalarm.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class AlarmService {

//    @Scheduled(cron = "0 0 0/2 * * ?")
    @Scheduled(cron = "0 * * ? * *")
    public void check() {

        System.out.println("here I am triggered");
    }
}
