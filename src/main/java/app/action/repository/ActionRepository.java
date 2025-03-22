package app.action.repository;

import app.action.model.Action;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ActionRepository extends JpaRepository<Action, UUID> {

//    @Query("SELECT a FROM Action AS a" +
//            " WHERE a.animal IN (SELECT fa FROM User u JOIN u.followedAnimals fa WHERE u.id = :userId)" +
//            " ORDER BY a.createdOn DESC")

    @Query("SELECT a FROM Action a" +
            " JOIN a.animal AS an" +
            " JOIN User u ON an MEMBER OF u.followedAnimals" +
            " WHERE u.id = :userId" +
            " ORDER BY a.createdOn DESC")
    List<Action> findActionsForFollowedAnimalByUserId(@Param("userId") UUID userId);
}
