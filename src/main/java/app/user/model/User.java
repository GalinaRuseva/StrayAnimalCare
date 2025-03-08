package app.user.model;

import app.action.model.Action;
import app.animal.model.Animal;
import app.comment.model.Comment;
import app.location.model.Location;
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
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String username;

    private String firstName;

    private String lastName;

    //private String profilePicture;

    //@Column(unique = false)
    private String email;

    //@Column(unique = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    private boolean isActive;

    @Column(nullable = false)
    private LocalDateTime createdOn;

    @Column(nullable = false)
    private LocalDateTime updatedOn;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
    @OrderBy("createdOn DESC")
    private List<Action> actions = new ArrayList<>();

    @Embedded
    private Location location;
    
    @ManyToMany(fetch = FetchType.EAGER)
    @OrderBy("createdOn DESC")
    //@JoinTable(name = "followedAnimal_follower", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "animal_id"))
    private List<Animal> followedAnimals = new ArrayList<>();
}
