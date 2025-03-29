package app.web;

import app.TestBuilders;
import app.animal.model.Animal;
import app.animal.service.AnimalService;
import app.security.AuthenticationMetadata;
import app.user.model.User;
import app.user.model.UserRole;
import app.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AnimalController.class)
public class AnimalControllerApiTest {

    @MockitoBean
    private AnimalService animalService;
    @MockitoBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getNewAnimalPage_shouldReturnNewAnimalView() throws Exception {

        User user = TestBuilders.aRandomUser();
        AuthenticationMetadata principal = new AuthenticationMetadata(user.getId(), "", "", UserRole.ADMIN, true);

        when(userService.getById(user.getId())).thenReturn(user);

        // 1. Build Request
        MockHttpServletRequestBuilder request = get("/animals/new")
                .with(user(principal))
                .with(csrf());

        // 2. Send Request
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("new-animal"))
                .andExpect(model().attributeExists("animalRequest"));;

        verify(userService, times(1)).getById(any(UUID.class));
    }

    @Test
    void getAnimalProfileMenu_shouldReturnAnimalProfileView() throws Exception {

        User user = TestBuilders.aRandomUser();
        Animal animal = TestBuilders.aRandomAnimal();
        AuthenticationMetadata principal = new AuthenticationMetadata(user.getId(), "", "", UserRole.ADMIN, true);

        when(userService.getById(user.getId())).thenReturn(user);
        when(animalService.getById(animal.getId())).thenReturn(animal);

        // 1. Build Request
        MockHttpServletRequestBuilder request = get("/animals/{id}/profile", animal.getId())
                .with(user(principal))
                .with(csrf());

        // 2. Send Request
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("animal-profile"));

        verify(userService, times(1)).getById(any(UUID.class));
        verify(animalService, times(1)).getById(any(UUID.class));
    }

    @Test
    void getAllAnimalPage_shouldReturnAllAnimalView() throws Exception {

        User user = TestBuilders.aRandomUser();
        Animal animal = TestBuilders.aRandomAnimal();
        AuthenticationMetadata principal = new AuthenticationMetadata(user.getId(), "", "", UserRole.ADMIN, true);

        when(userService.getById(user.getId())).thenReturn(user);
        when(animalService.getAllSystemAnimals()).thenReturn(List.of(animal));

        // 1. Build Request
        MockHttpServletRequestBuilder request = get("/animals")
                .with(user(principal))
                .with(csrf());

        // 2. Send Request
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("animals"));

        verify(userService, times(1)).getById(any(UUID.class));
    }

    @Test
    void getEditAnimalProfileMenu_shouldReturnEditAnimalProfileView() throws Exception {

        User user = TestBuilders.aRandomUser();
        Animal animal = TestBuilders.aRandomAnimal();
        AuthenticationMetadata principal = new AuthenticationMetadata(user.getId(), "", "", UserRole.ADMIN, true);

        when(userService.getById(user.getId())).thenReturn(user);
        when(animalService.getById(animal.getId())).thenReturn(animal);

        // 1. Build Request
        MockHttpServletRequestBuilder request = get("/animals/{id}/edit-profile", animal.getId())
                .with(user(principal))
                .with(csrf());

        // 2. Send Request
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("edit-animal-profile"));

        verify(userService, times(1)).getById(any(UUID.class));
        verify(animalService, times(1)).getById(any(UUID.class));
    }

}
