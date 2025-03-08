package app.picture.model;

import app.animal.model.Animal;
import jakarta.persistence.*;
import lombok.*;

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

    @Lob
    private byte[] imageData;

    private String title;

    @ManyToOne
    private Animal animal;
}
