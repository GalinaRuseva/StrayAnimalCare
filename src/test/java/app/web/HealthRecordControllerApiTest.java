package app.web;

import app.action.service.ActionService;
import app.animal.service.AnimalService;
import app.healthRecord.service.HealthRecordService;
import app.security.AuthenticationMetadata;
import app.user.service.UserService;
import app.web.dto.HealthRecordRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.UUID;

import static app.TestBuilders.aRandomAnimal;
import static app.TestBuilders.aRandomUser;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HealthRecordController.class)
public class HealthRecordControllerApiTest {

    @MockitoBean
    private HealthRecordService healthRecordService;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private AnimalService animalService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void postRequestToNewEndpoint_happyPath() throws Exception {

    UUID animalId = UUID.randomUUID();

        // 1. Build Request
        MockHttpServletRequestBuilder request = post("/healthRecords/{animalId}/new", animalId)
                .formField("description", "Urgent operation!")
                .formField("veterinaryInformation", "Dr. Dyulgerski, Dr. Animals vet clinic.")
                .with(csrf())
                .with(user(new AuthenticationMetadata(UUID.randomUUID(), "testUser", "password", app.user.model.UserRole.USER, true)));

        // 2. Send Request
        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/animals/" + animalId + "/profile"));
        verify(healthRecordService, times(1)).createNewHealthRecord(any(), any(), any());
    }

    @Test
    void postRequestToNewHealthRecordEndpointWithEmptyContent_thenReturnsAnimalProfileViewWithErrors() throws Exception {

        // 1. Build Request
        UUID animalId = UUID.randomUUID();

        when(userService.getById(any())).thenReturn(aRandomUser());
        when(animalService.getById(any())).thenReturn(aRandomAnimal());

        MockHttpServletRequestBuilder request = post("/healthRecords/{animalId}/new", animalId)
                .formField("description", "")
                .formField("veterinaryInformation", "")
                .with(csrf())
                .with(user(new AuthenticationMetadata(UUID.randomUUID(), "testUser", "password", app.user.model.UserRole.USER, true)));

        // 2. Send Request
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("animal-profile"))
                .andExpect(model().attributeExists("healthRecordRequest", "user", "animal"));
    }


}
