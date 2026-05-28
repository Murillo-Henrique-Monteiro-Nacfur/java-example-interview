package coding.interview.app.infrastructure.security.service;

import coding.interview.app.api.entities.Users;
import coding.interview.app.api.services.user.UserService;
import coding.interview.app.env.EnvUser;
import coding.interview.app.infrastructure.exceptions.SecureFlightNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserDetailServiceImplTest {

    private static final String USER_NOT_FOUND = "User not found";
    @Mock
    private UserService userService;

    @InjectMocks
    private UserDetailServiceImpl userDetailService;

    @Test
    @DisplayName("Should return UserDetails when user exists")
    void shouldReturnUserDetailsWhenUserExists() {
        String login = "admin@secureflight.com";
        Users user = EnvUser.getUsers();

        when(userService.findUserByLogin(login)).thenReturn(user);

        UserDetails result = userDetailService.loadUserByUsername(login);

        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo(user.getName());
        assertThat(result.getPassword()).isEqualTo(user.getPassword());
        assertThat(result.getAuthorities()).hasSize(1);
        assertThat(result.getAuthorities().iterator().next().getAuthority()).isEqualTo("ROLE_ADMIN");

        verify(userService, times(1)).findUserByLogin(login);
    }

    @Test
    @DisplayName("Should throw NotFoundException when user does not exist")
    void shouldThrowNotFoundExceptionWhenUserDoesNotExist() {
        String login = "nonexistent@secureflight.com";

        when(userService.findUserByLogin(login))
                .thenThrow(new SecureFlightNotFoundException(USER_NOT_FOUND));

        assertThatThrownBy(() -> userDetailService.loadUserByUsername(login))
                .isInstanceOf(SecureFlightNotFoundException.class)
                .hasMessage(USER_NOT_FOUND);

        verify(userService, times(1)
        ).findUserByLogin(login);
    }
}