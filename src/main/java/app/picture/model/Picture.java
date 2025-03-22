package app.picture.model;

import app.animal.model.Animal;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Picture {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String storedPictureId;

    //private String pictureUniqueName;

    private LocalDateTime uploadDate;

    @ManyToOne
    private Animal animal;
}
