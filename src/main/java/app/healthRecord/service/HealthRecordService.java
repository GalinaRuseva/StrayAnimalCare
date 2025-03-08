package app.healthRecord.service;

import app.animal.model.Animal;
import app.healthRecord.model.HealthRecord;
import app.healthRecord.repository.HealthRecordRepository;
import app.user.model.User;
import app.web.dto.HealthRecordRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
public class HealthRecordService {

    private final HealthRecordRepository healthRecordRepository;

    @Autowired
    public HealthRecordService(HealthRecordRepository healthRecordRepository) {
        this.healthRecordRepository = healthRecordRepository;
    }

    public void createNewHealthRecord(User user, Animal animal, HealthRecordRequest healthRecordRequest) {

        HealthRecord healthRecord = HealthRecord.builder()
                .animal(animal)
                .createdOn(LocalDateTime.now())
                .description(healthRecordRequest.getDescription())
                .veterinaryInformation(healthRecordRequest.getVeterinaryInformation())
                .build();

        healthRecordRepository.save(healthRecord);
    }
}
