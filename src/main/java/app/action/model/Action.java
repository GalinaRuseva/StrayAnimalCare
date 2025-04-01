package app.action.model;

import app.animal.model.Animal;
import app.comment.model.Comment;
import app.user.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EqualsAndHashCode
public class Action {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Animal animal;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Type type;

    private String description;

    @Column(nullable = false)
    private LocalDateTime createdOn;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "action")
    @OrderBy("createdOn DESC")
    private List<Comment> comments= new ArrayList<>();

}
