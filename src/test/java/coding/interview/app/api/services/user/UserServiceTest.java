package coding.interview.app.api.services.user;

import coding.interview.app.api.entities.Users;
import coding.interview.app.api.repositories.UserRepository;
import coding.interview.app.env.EnvUser;
import coding.interview.app.infrastructure.exceptions.SecureFlightNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("Should return user when user exists")
    void shouldReturnUserWhenUserExists() {
        String login = "user@example.com";
        Users user = EnvUser.getUsers();

        when(userRepository.findByLogin(login)).thenReturn(Optional.of(user));

        Users result = userService.findUserByLogin(login);

        assertThat(result).isNotNull();
        assertThat(result.getLogin()).isEqualTo(login);

        verify(userRepository, times(1)).findByLogin(login);
    }

    @Test
    @DisplayName("Should throw SecureFlightNotFoundException when user does not exist")
    void shouldThrowSecureFlightNotFoundExceptionWhenUserDoesNotExist() {
        String login = "nonexistent@example.com";

        when(userRepository.findByLogin(login)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.findUserByLogin(login))
                .isInstanceOf(SecureFlightNotFoundException.class)
                .hasMessage("User not found");

        verify(userRepository, times(1)).findByLogin(login);
    }
}