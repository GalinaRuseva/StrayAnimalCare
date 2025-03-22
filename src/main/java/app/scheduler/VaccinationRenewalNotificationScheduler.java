package app.scheduler;

import app.action.service.ActionService;
import app.animal.model.Animal;
import app.animal.service.AnimalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
public class VaccinationRenewalNotificationScheduler {

    private final AnimalService animalService;

    @Autowired
    public VaccinationRenewalNotificationScheduler(AnimalService animalService) {
        this.animalService = animalService;
    }


    public void checkUpcomingVaccinations() {

        LocalDateTime today = LocalDateTime.now();


    }
}
