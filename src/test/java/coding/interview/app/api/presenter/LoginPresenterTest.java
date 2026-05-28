package coding.interview.app.api.presenter;

import coding.interview.app.api.dto.authentication.LoginResponseDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class LoginPresenterTest {

    @InjectMocks
    private LoginPresenter loginPresenter;

    @Test
    @DisplayName("Should return login response dto when token is valid")
    void shouldReturnLoginResponseDtoWhenTokenIsValid() {
        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";

        LoginResponseDTO result = loginPresenter.presentLoginSuccess(token);

        assertThat(result).isNotNull();
        assertThat(result.token()).isEqualTo(token);
    }

    @Test
    @DisplayName("Should return login response dto with null token when token is null")
    void shouldReturnLoginResponseDtoWithNullTokenWhenTokenIsNull() {
        String token = null;

        LoginResponseDTO result = loginPresenter.presentLoginSuccess(token);

        assertThat(result).isNotNull();
        assertThat(result.token()).isNull();
    }
}