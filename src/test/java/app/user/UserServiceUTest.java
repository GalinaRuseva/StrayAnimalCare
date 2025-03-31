package app.user;

import app.animal.model.Animal;
import app.animal.service.AnimalService;
import app.exception.DomainException;
import app.exception.UsernameAlreadyExistException;
import app.location.model.Location;
import app.security.AuthenticationMetadata;
import app.user.model.User;
import app.user.model.UserRole;
import app.user.repository.UserRepository;
import app.user.service.UserService;
import app.web.dto.RegisterRequest;
import app.web.dto.UserEditRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceUTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AnimalService animalService;

    @InjectMocks
    private UserService userService;

    @Test
    void givenMissingUserFromDatabase_whenEditUserDetails_thenExceptionIsThrown() {

        UUID userId = UUID.randomUUID();
        UserEditRequest dto = UserEditRequest.builder().build();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(DomainException.class, () -> userService.editUserDetails(userId, dto));
    }

    @Test
    void givenExistingUser_whenEditTheirProfile_thenChangeTheirDetailsAndSaveToDatabase() {
        //given
        UUID userId = UUID.randomUUID();
        UserEditRequest dto = UserEditRequest.builder()
                .firstName("Galina")
                .lastName("Ruseva")
                .email("galina@gmail.com")
                .phoneNumber("0899991112222")
                .country("Bulgaria")
                .city("Sofia")
                .neighborhood("Izgrev")
                .build();

        Location location = Location.builder()
                .country("Bulgaria")
                .city("Sofia")
                .neighborhood("Mladost")
                .build();

        User user = User.builder()
                .username("galina")
                .location(location)
                .build();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // When
        userService.editUserDetails(userId, dto);

        // Then
        assertEquals("Galina", user.getFirstName());
        assertEquals("Ruseva", user.getLastName());
        assertEquals("galina@gmail.com", user.getEmail());
        assertEquals("0899991112222", user.getPhoneNumber());
        assertEquals("Bulgaria", user.getLocation().getCountry());
        assertEquals("Sofia", user.getLocation().getCity());
        assertEquals("Izgrev", user.getLocation().getNeighborhood());
        verify(userRepository, times(1)).save(user);
    }


    @Test
    void givenExistingUsername_whenRegister_thenExceptionIsThrown() {

        // Given
        RegisterRequest registerRequest = RegisterRequest.builder()
                .username("galina")
                .password("123123")
                .country("Bulgaria")
                .city("Sofia")
                .neighborhood("Izgrev")
                .build();

        when(userRepository.findByUsername(any())).thenReturn(Optional.of(new User()));

        // When & Then
        assertThrows(UsernameAlreadyExistException.class, () -> userService.register(registerRequest));
        verify(userRepository, never()).save(any());
    }

    // Test 2: Happy path Registration
    @Test
    void givenHappyPath_whenRegister() {

        // Given
        RegisterRequest registerRequest = RegisterRequest.builder()
                .username("galina")
                .password("123123")
                .country("Bulgaria")
                .city("Sofia")
                .neighborhood("Izgrev")
                .build();
        User user = User.builder()
                .build();
        when(userRepository.findByUsername(registerRequest.getUsername())).thenReturn(Optional.empty());
        when(userRepository.save(any())).thenReturn(user);

        // When
        User registeredUser = userService.register(registerRequest);

        //Then
        assertThat(registeredUser.getUsername()).isEqualTo(registerRequest.getUsername());
    }

    @Test
    void givenExistingUser_whenLoadUserByUsername_thenReturnCorrectAuthenticationMetadata() {

        // Given
        String username = "galina";
        User user = User.builder()
                .id(UUID.randomUUID())
                .isActive(true)
                .password("123123")
                .role(UserRole.ADMIN)
                .build();
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        // When
        UserDetails authenticationMetadata = userService.loadUserByUsername(username);

        // Then
        assertInstanceOf(AuthenticationMetadata.class, authenticationMetadata);
        AuthenticationMetadata result = (AuthenticationMetadata) authenticationMetadata;
        assertEquals(user.getId(), result.getUserId());
        assertEquals(username, result.getUsername());
        assertEquals(user.getPassword(), result.getPassword());
        assertEquals(user.isActive(), result.isActive());
        assertEquals(user.getRole(), result.getRole());
        assertThat(result.getAuthorities()).hasSize(1);
        assertEquals("ROLE_ADMIN", result.getAuthorities().iterator().next().getAuthority());
    }

    // Test 2: When User does not exist - then throws exception
    @Test
    void givenMissingUserFromDatabase_whenLoadUserByUsername_thenExceptionIsThrown() {

        // Given
        String username = "galina";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(DomainException.class, () -> userService.loadUserByUsername(username));
    }

    @Test
    void givenUserWithRoleUser_whenSwitchRole_thenUserGetsAdminRole() {

        // Given
        UUID userId = UUID.randomUUID();
        User user = User.builder()
                .id(userId)
                .role(UserRole.USER)
                .build();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // When
        userService.switchRole(userId);

        // Then
        assertThat(user.getRole()).isEqualTo(UserRole.ADMIN);
    }

    @Test
    void givenUserWithRoleAdmin_whenSwitchRole_thenUserGetsUserRole() {

        // Given
        UUID userId = UUID.randomUUID();
        User user = User.builder()
                .id(userId)
                .role(UserRole.ADMIN)
                .build();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // When
        userService.switchRole(userId);

        // Then
        assertThat(user.getRole()).isEqualTo(UserRole.USER);
    }

    @Test
    void givenUserWithStatusActive_whenSwitchStatus_thenUserStatusBecomeInactive() {

        // Given
        User user = User.builder()
                .id(UUID.randomUUID())
                .isActive(true)
                .build();
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        // When
        userService.switchStatus(user.getId());

        // Then
        assertFalse(user.isActive());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void givenUserWithStatusInactive_whenSwitchStatus_thenUserStatusBecomeActive() {

        // Given
        User user = User.builder()
                .id(UUID.randomUUID())
                .isActive(false)
                .build();
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        // When
        userService.switchStatus(user.getId());

        // Then
        assertTrue(user.isActive());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void givenExistingUsersInDatabase_whenGetAllUsers_thenReturnThemAll() {

        // Give
        List<User> userList = List.of(new User(), new User(), new User());
        when(userRepository.findAll()).thenReturn(userList);

        // When
        List<User> users = userService.getAllUsers();

        // Then
        assertThat(users).hasSize(3);
    }

    @Test
    void givenUnfollowedAnimal_whenFollow_thenAnimalIsAddToFollowedAnimalList() {

        //given
        Animal animal = Animal.builder()
                .id(UUID.randomUUID())
                .build();

        User user = User.builder()
                .id(UUID.randomUUID())
                .followedAnimals(new ArrayList<>())
                .build();

//        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        //when
        userService.followAnimal(animal, user);

        //then
        assertThat(animal).isEqualTo(user.getFollowedAnimals().get(0));
        assertThat(user.getFollowedAnimals()).hasSize(1);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void givenFollowedAnimal_whenUnfollow_thenAnimalIsNotInFollowedAnimalList() {

        //given
        Animal animal = Animal.builder()
                .id(UUID.randomUUID())
                .build();

        List<Animal> animalList = new ArrayList<>();
        animalList.add(animal);

        User user = User.builder()
                .id(UUID.randomUUID())
                .followedAnimals(animalList)
                .build();

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        //when
        userService.unfollowAnimalById(animal, user.getId());

        //then
        assertThat(user.getFollowedAnimals()).isEmpty();
        assertFalse(user.getFollowedAnimals().contains(animal));
        verify(userRepository, times(1)).save(user);
    }

//    @Test
//    void givenUsersWithStatusActiveAndLastLoginBeforeThreeMonths_whenDeactivateInactiveUsers_thenUsersStatusBecomeInactive() {
//
//        // Given
//        User user = User.builder()
//                .id(UUID.randomUUID())
//                .isActive(true)
//                .lastLogin(LocalDateTime.now().minusMonths(4))
//                .build();
//        when(userRepository.findUsersByLastLoginBeforeAndActive(LocalDateTime.now().minusMonths(3))).thenReturn(List.of(user));
//
//        // When
////        List<User> usersWithoutLoginMoreThanThreeMonths = userService.getUsersWithoutLoginMoreThanThreeMonths();
//        userService.switchStatus(user.getId());
//
//        // Then
////        assertEquals(usersWithoutLoginMoreThanThreeMonths.get(0).getId(), user.getId());
//        assertFalse(user.isActive());
//        verify(userRepository, times(1)).save(user);
//    }


}
