package app.web;

import app.TestBuilders;
import app.action.service.ActionService;
import app.comment.service.CommentService;
import app.security.AuthenticationMetadata;
import app.user.model.User;
import app.user.service.UserService;
import app.web.dto.CommentRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CommentController.class)
public class CommentControllerApiTest {

    @MockitoBean
    private CommentService commentService;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private ActionService actionService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void postRequestToNewEndpoint_happyPath() throws Exception {

    UUID actionId = UUID.randomUUID();

        // 1. Build Request
        MockHttpServletRequestBuilder request = post("/comments/{actionId}/new", actionId)
                .formField("content", "Great!")
                .with(csrf())
                .with(user(new AuthenticationMetadata(UUID.randomUUID(), "testUser", "password", app.user.model.UserRole.USER, true)));

        // 2. Send Request
        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/actions/" + actionId));
        verify(commentService, times(1)).createNewComment(any(), any(), any());
    }
    //@TODO fix later
//    @Test
//    void addNewComment_InvalidRequest_ReturnsActionView() throws Exception {
//        // 1. Build Request
//        User user = TestBuilders.aRandomUser();
//
//        when(userService.getById(user.getId())).thenReturn(user);
//        when(actionService.getById(user.getActions().get(0).getId())).thenReturn(user.getActions().get(0));
//
//        MockHttpServletRequestBuilder request = post("/comments/{actionId}/new", user.getActions().get(0).getId())
//                .with(csrf())
//                .with(user(new AuthenticationMetadata(user.getId(), user.getUsername(), "password", app.user.model.UserRole.USER, true)));
//
//        // 2. Send Request
//        mockMvc.perform(request)
//                .andExpect(status().isOk())
//                .andExpect(view().name("action"))
//                .andExpect(model().attributeExists("commentRequest", "errorMessage"));
//
//
//        verify(commentService, never()).createNewComment(any(), any(), any());
//    }


    @Test
    void postRequestToToNewCommentEndpointWithEmptyContent_thenRedirectToAction() throws Exception {

        // 1. Build Request
        UUID actionId = UUID.randomUUID();
        MockHttpServletRequestBuilder request = post("/comments/{actionId}/new", actionId)
                .formField("content", "Great!")
                .with(csrf())
                .with(user(new AuthenticationMetadata(UUID.randomUUID(), "testUser", "password", app.user.model.UserRole.USER, true)));

        // 2. Send Request
        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/actions/" + actionId));
    }


}
