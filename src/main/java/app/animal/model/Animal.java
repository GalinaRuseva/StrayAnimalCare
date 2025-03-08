package app.animal.model;

import app.action.model.Action;
import app.healthRecord.model.HealthRecord;
import app.location.model.Location;
import app.picture.model.Picture;
import app.user.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Animal {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Type type;

    @Column(nullable = false)
    private String name;

    private LocalDate estimateDateOfBirth;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(nullable = false)
    private boolean isNeutered;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "animal")
    private List<Picture> pictures = new ArrayList<>();

    private String information;

    @Column(nullable = false)
    private LocalDateTime createdOn;

    @Column(nullable = false)
    private LocalDateTime updatedOn;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "animal")
    @OrderBy("createdOn DESC")
    private List<Action> actions = new ArrayList<>();

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "animal")
    @OrderBy("createdOn DESC")
    private List<HealthRecord> healthRecords = new ArrayList<>();

    @Embedded
    private Location location;

    @ManyToOne
    private User addedBy;

    public String getAge() {
        if (estimateDateOfBirth == null) {
            return "No data available";
        } else {
            return String.valueOf(LocalDate.now().getYear() - estimateDateOfBirth.getYear());
        }
    }

}
