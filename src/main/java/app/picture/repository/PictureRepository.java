package app.picture.repository;

import app.animal.model.Animal;
import app.picture.model.Picture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PictureRepository extends JpaRepository<Picture, UUID> {


    List<Picture> findByAnimal(Animal animal);
}
