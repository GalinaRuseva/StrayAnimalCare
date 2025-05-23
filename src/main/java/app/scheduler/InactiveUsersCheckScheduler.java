package app.scheduler;

import app.user.model.User;
import app.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class InactiveUsersCheckScheduler {

    private final UserService userService;

    @Autowired
    public InactiveUsersCheckScheduler(UserService userService) {
        this.userService = userService;
    }

    @Scheduled(cron = "0 0/3 * * * ?") // Runs every 3 minutes
    public void deactivateInactiveUsers() {

        List<User> inactiveUsers = userService.getUsersWithoutLoginMoreThanThreeMonths();

        if (inactiveUsers.isEmpty()) {
            log.info("No inactive users found.");
            return;
        }

        for (User user : inactiveUsers) {
            userService.switchStatus(user.getId());
            log.info("Deactivated user with id [%s]".formatted(user.getId()));
        }

        log.info("Deactivated [%d] inactive users.".formatted(inactiveUsers.size()));
    }

}
