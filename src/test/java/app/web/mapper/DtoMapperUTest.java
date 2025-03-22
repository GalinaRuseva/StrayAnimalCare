package app.web.mapper;

import app.location.model.Location;
import app.user.model.User;
import app.user.repository.UserRepository;
import app.web.dto.UserEditRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class DtoMapperUTest {

    @Mock
    private Location location;

    @Test
    void givenHappyPath_whenMappingUserToUserEditRequest(){

        Location location = new Location("Bulgaria", "Sofia", "Izgrev");

        // Given
        User user = User.builder()
                .firstName("galina")
                .lastName("ruseva")
                .profilePicture("www.image.com")
                .email("galina@gmail.com")
                .phoneNumber("089999111222")
                .location(location)
                .build();

        // When
        UserEditRequest resultDto = DtoMapper.mapUserToUserEditRequest(user);

        // Then
        assertEquals(user.getFirstName(), resultDto.getFirstName());
        assertEquals(user.getLastName(), resultDto.getLastName());
        //assertEquals(user.getProfilePicture(), resultDto.getProfilePicture());
        assertEquals(user.getEmail(), resultDto.getEmail());
        assertEquals(user.getPhoneNumber(), resultDto.getPhoneNumber());
        assertEquals(user.getLocation().getCountry(), resultDto.getCountry());
        assertEquals(user.getLocation().getCity(), resultDto.getCity());
        assertEquals(user.getLocation().getNeighborhood(), resultDto.getNeighborhood());
    }

}
