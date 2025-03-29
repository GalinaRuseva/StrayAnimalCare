package app.user.repository;

import app.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByUsername(String username);

    @Query("SELECT u FROM User u" +
            " WHERE u.lastLogin < :lastLogin" +
            " AND  u.isActive = true" +
            " AND u.role != 'ADMIN'")
    List<User> findUsersByLastLoginBeforeAndActive(@Param(value = "lastLogin") LocalDateTime lastLogin);
}
