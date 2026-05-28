package coding.interview.app.api.services.authentication;

import coding.interview.app.infrastructure.security.service.JwtService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthenticationService authenticationService;

    @Test
    @DisplayName("Should return token when authentication is successful")
    void shouldReturnTokenWhenAuthenticationIsSuccessful() {
        String login = "user@test.com";
        String password = "password123";
        String expectedToken = "fake.jwt.token";
        Authentication authentication = mock(Authentication.class);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(jwtService.generateToken(authentication)).thenReturn(expectedToken);

        String resultToken = authenticationService.authenticate(login, password);

        assertThat(resultToken).isNotNull();
        assertThat(resultToken).isEqualTo(expectedToken);

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService, times(1)).generateToken(authentication);
    }

    @Test
    @DisplayName("Should throw BadCredentialsException when authentication fails")
    void shouldThrowBadCredentialsExceptionWhenAuthenticationFails() {
        String login = "user@test.com";
        String password = "wrong-password";

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        assertThatThrownBy(() -> authenticationService.authenticate(login, password))
                .isInstanceOf(BadCredentialsException.class)
                .hasMessage("Invalid credentials");

        verify(jwtService, never()).generateToken(any());
    }
}