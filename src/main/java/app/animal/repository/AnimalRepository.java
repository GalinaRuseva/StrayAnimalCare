package app.animal.repository;

import app.animal.model.Animal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AnimalRepository extends JpaRepository<Animal, UUID> {

    @Query("SELECT a FROM Animal a" +
            " WHERE LOWER(a.location.country) = LOWER(:country)" +
            " AND LOWER(a.location.city) = LOWER(:city)" +
            " AND LOWER(a.location.neighborhood) LIKE LOWER(CONCAT('%', :neighborhood, '%'))" +
            " ORDER BY a.name ASC, a.createdOn DESC")
    List<Animal> findByAnimalLocation(@Param(value = "country") String country, @Param(value = "city") String city, @Param(value = "neighborhood") String neighborhood);

}

