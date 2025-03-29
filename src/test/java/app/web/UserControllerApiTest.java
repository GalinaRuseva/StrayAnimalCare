package app.web;

import app.TestBuilders;
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

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(UserController.class)
public class UserControllerApiTest {

    @MockitoBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void putUnauthorizedRequestToSwitchRole_shouldReturn404AndNotFoundView() throws Exception {

        // 1. Build Request
        AuthenticationMetadata principal = new AuthenticationMetadata(UUID.randomUUID(), "galina", "123123", UserRole.USER, true);
        MockHttpServletRequestBuilder request = put("/users/{id}/role", UUID.randomUUID())
                .with(user(principal))
                .with(csrf());

        // 2. Send Request
        mockMvc.perform(request)
                .andExpect(status().isNotFound())
                .andExpect(view().name("not-found"));
    }

    @Test
    void putAuthorizedRequestToSwitchRole_shouldRedirectToUsers() throws Exception {

        // 1. Build Request
        AuthenticationMetadata principal = new AuthenticationMetadata(UUID.randomUUID(), "galina", "123123", UserRole.ADMIN, true);
        MockHttpServletRequestBuilder request = put("/users/{id}/role", UUID.randomUUID())
                .with(user(principal))
                .with(csrf());

        // 2. Send Request
        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users"));
        verify(userService, times(1)).switchRole(any());
    }

    @Test
    void putAuthorizedRequestToGetAllUsers_shouldRedirectToUsers() throws Exception {

        // 1. Build Request
        AuthenticationMetadata principal = new AuthenticationMetadata(UUID.randomUUID(), "galina", "123123", UserRole.ADMIN, true);
        MockHttpServletRequestBuilder request = get("/users")
                .with(user(principal))
                .with(csrf());

        // 2. Send Request
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("users"));
        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void putUnauthorizedRequestToGetAllUsers_shouldReturn404AndNotFoundView() throws Exception {

        // 1. Build Request
        AuthenticationMetadata principal = new AuthenticationMetadata(UUID.randomUUID(), "galina", "123123", UserRole.USER, true);
        MockHttpServletRequestBuilder request = get("/users")
                .with(user(principal))
                .with(csrf());

        // 2. Send Request
        mockMvc.perform(request)
                .andExpect(status().isNotFound())
                .andExpect(view().name("not-found"));
    }

    @Test
    void putUnauthorizedRequestToSwitchUserStatus_shouldReturn404AndNotFoundView() throws Exception {

        // 1. Build Request
        AuthenticationMetadata principal = new AuthenticationMetadata(UUID.randomUUID(), "galina", "123123", UserRole.USER, true);
        MockHttpServletRequestBuilder request = put("/users/{id}/status", UUID.randomUUID())
                .with(user(principal))
                .with(csrf());

        // 2. Send Request
        mockMvc.perform(request)
                .andExpect(status().isNotFound())
                .andExpect(view().name("not-found"));
    }

    @Test
    void putAuthorizedRequestToSwitchUserStatus_shouldRedirectToUsers() throws Exception {

        // 1. Build Request
        AuthenticationMetadata principal = new AuthenticationMetadata(UUID.randomUUID(), "galina", "123123", UserRole.ADMIN, true);
        MockHttpServletRequestBuilder request = put("/users/{id}/status", UUID.randomUUID())
                .with(user(principal))
                .with(csrf());

        // 2. Send Request
        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users"));
        verify(userService, times(1)).switchStatus(any());
    }

    @Test
    void getProfileMenu_shouldReturnOtherUserProfileView() throws Exception {

        User user = TestBuilders.aRandomUser();
        User otherUser = TestBuilders.aRandomUser();
        AuthenticationMetadata principal = new AuthenticationMetadata(user.getId(), "galina", "123123", UserRole.ADMIN, true);

        when(userService.getById(user.getId())).thenReturn(user);
        when(userService.getById(otherUser.getId())).thenReturn(otherUser);

        // 1. Build Request
        MockHttpServletRequestBuilder request = get("/users/{id}/profile", otherUser.getId())
                .with(user(principal))
                .with(csrf());

        // 2. Send Request
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("user-profile-other-view"));

        verify(userService, times(2)).getById(any(UUID.class));
    }

    @Test
    void getProfileMenu_shouldReturnUserProfileView() throws Exception {

        User user = TestBuilders.aRandomUser();
        User otherUser = user;
        AuthenticationMetadata principal = new AuthenticationMetadata(user.getId(), "galina", "123123", UserRole.ADMIN, true);

        when(userService.getById(user.getId())).thenReturn(user);
        when(userService.getById(otherUser.getId())).thenReturn(otherUser);

        // 1. Build Request
        MockHttpServletRequestBuilder request = get("/users/{id}/profile", otherUser.getId())
                .with(user(principal))
                .with(csrf());

        // 2. Send Request
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("user-profile"));

        verify(userService, times(2)).getById(any(UUID.class));
    }

    @Test
    void getEditProfileMenu_shouldReturnUpdatedUserProfileView() throws Exception {

        User user = TestBuilders.aRandomUser();
        AuthenticationMetadata principal = new AuthenticationMetadata(user.getId(), "galina", "123123", UserRole.ADMIN, true);

        when(userService.getById(user.getId())).thenReturn(user);

        // 1. Build Request
        MockHttpServletRequestBuilder request = get("/users/{id}/edit-profile", user.getId())
                .with(user(principal))
                .with(csrf());

        // 2. Send Request
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("edit-user-profile"));

        verify(userService, times(1)).getById(any(UUID.class));
    }





}
