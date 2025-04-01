package app.scheduler;

import app.user.model.User;
import app.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SchedulerUTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private InactiveUsersCheckScheduler inactiveUsersCheckScheduler;

    @Test
    void givenEmptyListOfInactiveUsers_whenDeactivateInactiveUsers_thenNoSwitchStatus() {
        // given
        when(userService.getUsersWithoutLoginMoreThanThreeMonths()).thenReturn(new ArrayList<>());

        // when
        inactiveUsersCheckScheduler.deactivateInactiveUsers();

        // Assert
        verify(userService, times(1)).getUsersWithoutLoginMoreThanThreeMonths();
        verify(userService, never()).switchStatus(any());
    }

    @Test
    void givenListOfInactiveUsers_whenDeactivateInactiveUsers_thenSwitchUsersStatus() {
        // given
        List<User> inactiveUsers = new ArrayList<>();
        inactiveUsers.add(User.builder().id(UUID.randomUUID()).build());
        inactiveUsers.add(User.builder().id(UUID.randomUUID()).build());
        inactiveUsers.add(User.builder().id(UUID.randomUUID()).build());

        when(userService.getUsersWithoutLoginMoreThanThreeMonths()).thenReturn(inactiveUsers);

        // when
        inactiveUsersCheckScheduler.deactivateInactiveUsers();

        // then
        verify(userService, times(1)).getUsersWithoutLoginMoreThanThreeMonths();
        for (User user : inactiveUsers) {
            verify(userService, times(1)).switchStatus(user.getId());
        }
        assertEquals(3, inactiveUsers.size());
    }

    @Test
    void givenInactiveUser_whenDeactivateInactiveUsers_thenOneInactiveUser() {
        // Arrange
        List<User> inactiveUsers = new ArrayList<>();
        inactiveUsers.add(User.builder().id(UUID.randomUUID()).build());

        when(userService.getUsersWithoutLoginMoreThanThreeMonths()).thenReturn(inactiveUsers);

        // Act
        inactiveUsersCheckScheduler.deactivateInactiveUsers();

        // Assert
        verify(userService, times(1)).getUsersWithoutLoginMoreThanThreeMonths();
        verify(userService, times(1)).switchStatus(inactiveUsers.get(0).getId());
    }


}
