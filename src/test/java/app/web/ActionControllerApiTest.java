package app.web;

import app.action.service.ActionService;
import app.animal.service.AnimalService;
import app.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ActionController.class)
public class ActionControllerApiTest {

    @MockitoBean
    private UserService userService;
    @MockitoBean
    private AnimalService animalService;

    @MockitoBean
    private ActionService actionService;

    @Autowired
    private MockMvc mockMvc;

}
