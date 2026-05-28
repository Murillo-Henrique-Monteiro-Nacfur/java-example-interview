package coding.interview.app.api.controllers;

import coding.interview.app.api.dto.authentication.LoginRequestDTO;
import coding.interview.app.api.dto.authentication.LoginResponseDTO;
import coding.interview.app.api.presenter.LoginPresenter;
import coding.interview.app.api.services.authentication.AuthenticationService;
import coding.interview.app.env.EnvLoginRequestDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationControllerTest {

    private static final String INVALID_CREDENTIALS_PROVIDED = "Invalid credentials provided";
    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private LoginPresenter loginPresenter;

    @InjectMocks
    private AuthenticationController authenticationController;

    @Test
    @DisplayName("Should return OK and token when login is successful")
    void shouldReturnOkAndTokenWhenLoginIsSuccessful() {
        LoginRequestDTO loginRequest = EnvLoginRequestDTO.getLoginRequestDTO();
        String fakeToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";
        LoginResponseDTO expectedResponse = new LoginResponseDTO(fakeToken);

        when(authenticationService.authenticate(loginRequest.getLogin(), loginRequest.getPassword()))
                .thenReturn(fakeToken);
        when(loginPresenter.presentLoginSuccess(fakeToken))
                .thenReturn(expectedResponse);

        ResponseEntity<LoginResponseDTO> responseEntity = authenticationController.login(loginRequest);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().token()).isEqualTo(fakeToken);

        verify(authenticationService, times(1)).authenticate(loginRequest.getLogin(), loginRequest.getPassword());
        verify(loginPresenter, times(1)).presentLoginSuccess(fakeToken);
    }

    @Test
    @DisplayName("Should throw exception when authentication fails")
    void shouldThrowExceptionWhenAuthenticationFails() {
        LoginRequestDTO loginRequest = EnvLoginRequestDTO.getLoginRequestDTO();
        when(authenticationService.authenticate(loginRequest.getLogin(), loginRequest.getPassword()))
                .thenThrow(new BadCredentialsException(INVALID_CREDENTIALS_PROVIDED));

        assertThatThrownBy(() -> authenticationController.login(loginRequest))
                .isInstanceOf(BadCredentialsException.class)
                .hasMessage(INVALID_CREDENTIALS_PROVIDED);

        verify(loginPresenter, never()).presentLoginSuccess(any());
    }
}