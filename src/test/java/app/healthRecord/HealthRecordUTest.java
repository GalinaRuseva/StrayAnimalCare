package app.healthRecord;

import app.action.model.Action;
import app.action.model.Type;
import app.animal.model.Animal;
import app.healthRecord.model.HealthRecord;
import app.healthRecord.repository.HealthRecordRepository;
import app.healthRecord.service.HealthRecordService;
import app.user.model.User;
import app.web.dto.ActionRequest;
import app.web.dto.HealthRecordRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HealthRecordUTest {

    @Mock
    private HealthRecordRepository healthRecordRepository;

    @InjectMocks
    private HealthRecordService healthRecordService;

    @Test
    void givenUserAnimalHealthRecordRequest_whenCreateNewHealthRecord_thenActionIsSaved() {

        //given
        User user = User.builder().build();
        Animal animal = Animal.builder().build();
        HealthRecordRequest healthRecordRequest = HealthRecordRequest.builder()
                .description("Mishi had her yearly vet check")
                .veterinaryInformation("Dr. Diylgerski, in Dr. Animals Vet Clinic, Bulgaria, Sofia, Mladost")
                .build();

        HealthRecord healthRecord = HealthRecord.builder()
                .description(healthRecordRequest.getDescription())
                .veterinaryInformation(healthRecordRequest.getVeterinaryInformation())
                .build();

        when(healthRecordRepository.save(any())).thenReturn(healthRecord);

        //when
        healthRecordService.createNewHealthRecord(user, animal, healthRecordRequest);

        //then
        ArgumentCaptor<HealthRecord> healthRecordCaptor = ArgumentCaptor.forClass(HealthRecord.class);
        verify(healthRecordRepository).save(healthRecordCaptor.capture());
        HealthRecord result = healthRecordCaptor.getValue();
        assertEquals(result.getId(), healthRecord.getId());
    }
}
