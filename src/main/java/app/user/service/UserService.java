package app.user.service;

import app.animal.model.Animal;
import app.exception.DomainException;
import app.exception.UsernameAlreadyExistException;
import app.location.model.Location;
import app.picture.client.PictureClient;
import app.picture.dto.PictureUploadResponse;
import app.picture.service.PictureService;
import app.security.AuthenticationMetadata;
import app.user.model.User;
import app.user.model.UserRole;
import app.user.repository.UserRepository;
import app.web.dto.RegisterRequest;
import app.web.dto.UserEditRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private  final PictureService pictureService;
    private final PictureClient pictureClient;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, PictureService pictureService, PictureClient pictureClient) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.pictureService = pictureService;
        this.pictureClient = pictureClient;
    }

    public User register(RegisterRequest registerRequest) {

        Optional<User> optionalUser = userRepository.findByUsername(registerRequest.getUsername());
        if (optionalUser.isPresent()) {
            throw new UsernameAlreadyExistException("Username [%s] already exist.".formatted(registerRequest.getUsername()));
        }

        Location location = Location.builder()
                .country(StringUtils.trim(registerRequest.getCountry()))
                .city(StringUtils.trim(registerRequest.getCity()))
                .neighborhood(StringUtils.trim(registerRequest.getNeighborhood()))
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

        user.setActive(!user.isActive());

        user.setUpdatedOn(LocalDateTime.now());

        userRepository.save(user);
    }

    public void switchRole(UUID userId) {
        User user = getById(userId);

        if (user.getRole() == UserRole.USER) {
            user.setRole(UserRole.ADMIN);
        } else {
            user.setRole(UserRole.USER);
        }

        user.setUpdatedOn(LocalDateTime.now());

        userRepository.save(user);
    }

    public void followAnimal(Animal animal, UUID userId) {

        User user = getById(userId);

        boolean isAlreadyFollowed = user.getFollowedAnimals().contains(animal);

        if (isAlreadyFollowed) {
            log.info("Animal with id [%s] is already followed.".formatted(animal.getId()));
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
//        MultipartFile profilePictureFile = userEditRequest.getProfilePictureFile();
//        if(!profilePictureFile.isEmpty()) {
//            ResponseEntity<PictureUpload> pictureUploadResponse = pictureClient.pictureUpload(profilePictureFile);
//            if(pictureUploadResponse.getStatusCode().is2xxSuccessful()){
//                user.setProfilePicture(pictureUploadResponse.getBody().getId());
//            }
//        }


        user.setFirstName(userEditRequest.getFirstName());
        user.setLastName(userEditRequest.getLastName());

//        user.setProfilePicture(userEditRequest.getProfilePicture());
        user.setEmail(userEditRequest.getEmail());
        user.setPhoneNumber(userEditRequest.getPhoneNumber());
        user.getLocation().setCountry(userEditRequest.getCountry());
        user.getLocation().setCity(userEditRequest.getCity());
        user.getLocation().setNeighborhood(userEditRequest.getNeighborhood());
        user.setUpdatedOn(LocalDateTime.now());

        userRepository.save(user);
    }

    public User resetProfilePicture(UUID id) {
        User user = getById(id);
        user.setProfilePicture(null);
        return this.userRepository.save(user);
    }

//    public void saveProfilePicture(UUID id, MultipartFile file) {
//        User user = getById(id);
//        String storedPictureId = pictureService.saveProfilePicture(file);
//        user.setProfilePicture(storedPictureId);
//        userRepository.save(user);
//    }

    public User saveProfilePicture(UUID id, MultipartFile file) {
        User user = getById(id);
        if (!file.isEmpty()) {
            ResponseEntity<PictureUploadResponse> pictureUpload = pictureClient.pictureUpload(file);
            if (pictureUpload.getStatusCode().is2xxSuccessful()) {
                user.setProfilePicture(pictureUpload.getBody().getId());
            }
        }
        //if the file is empty?? TO DO
        return this.userRepository.save(user);
    }
}
