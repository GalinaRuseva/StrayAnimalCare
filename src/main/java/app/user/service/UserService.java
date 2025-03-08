package app.user.service;

import app.action.service.ActionService;
import app.animal.model.Animal;
import app.animal.repository.AnimalRepository;
import app.animal.service.AnimalService;
import app.exception.DomainException;
import app.location.model.Location;
import app.security.AuthenticationMetadata;
import app.user.model.User;
import app.user.model.UserRole;
import app.user.repository.UserRepository;
import app.web.dto.LoginRequest;
import app.web.dto.RegisterRequest;
import app.web.dto.UserEditRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AnimalService animalService;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, AnimalService animalService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.animalService = animalService;
    }

    public User register(RegisterRequest registerRequest) {

        Optional<User> optionalUser = userRepository.findByUsername(registerRequest.getUsername());
        if (optionalUser.isPresent()) {
            throw new DomainException("Username [%s] already exist.".formatted(registerRequest.getUsername()));
        }

        Location location = Location.builder()
                .country(registerRequest.getCountry())
                .city(registerRequest.getCity())
                .neighborhood(registerRequest.getNeighborhood())
                .build();

        User user = User.builder()
                .username(registerRequest.getUsername())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(UserRole.USER)
                .isActive(true)
                .location(location)
                .createdOn(LocalDateTime.now())
                .updatedOn(LocalDateTime.now())
                .build();

        userRepository.save(user);

        log.info("Successfully create new user account for username [%s] and id [%s]".formatted(user.getUsername(), user.getId()));

        return user;
    }

    public User getById(UUID id) {

        return userRepository.findById(id).orElseThrow(() -> new DomainException("User with id [%s] does not exist.".formatted(id)));
    }

    public void switchStatus(UUID userId) {

        User user = getById(userId);

        if (user.isActive()) {
            user.setActive(false);
        } else {
            user.setActive(true);
        }

        userRepository.save(user);
    }

    public void switchRole(UUID userId) {
        User user = getById(userId);

        user.setActive(!user.isActive());
        userRepository.save(user);
    }

    public void followAnimal(Animal animal, UUID userId) {

        User user = getById(userId);

        boolean isAlreadyFollowed = user.getFollowedAnimals().contains(animal);

        if (isAlreadyFollowed) {
            return;
        }

        user.getFollowedAnimals().add(animal);
        userRepository.save(user);
    }

    public void unfollowAnimalById(Animal animal, UUID userId) {

        User user = getById(userId);
        user.getFollowedAnimals().remove(animal);

        userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username).orElseThrow(() -> new DomainException("User with this username does not exist."));

        return new AuthenticationMetadata(user.getId(), username, user.getPassword(), user.getRole(), user.isActive());
    }

    public List<User> getAllUsers() {

        return userRepository.findAll();
    }

    public void editUserDetails(UUID userId, UserEditRequest userEditRequest) {

        User user = getById(userId);

        user.setFirstName(userEditRequest.getFirstName());
        user.setLastName(userEditRequest.getLastName());
        user.setEmail(userEditRequest.getEmail());
        user.setPhoneNumber(userEditRequest.getPhoneNumber());
        user.getLocation().setCountry(userEditRequest.getCountry());
        user.getLocation().setCity(userEditRequest.getCity());
        user.getLocation().setNeighborhood(userEditRequest.getNeighborhood());
        user.setUpdatedOn(LocalDateTime.now());

        userRepository.save(user);
    }
}
