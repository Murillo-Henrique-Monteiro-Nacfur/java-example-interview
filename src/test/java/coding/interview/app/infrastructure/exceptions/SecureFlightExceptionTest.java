package coding.interview.app.infrastructure.exceptions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

class SecureFlightExceptionTest {

    @Test
    @DisplayName("Should correctly set message")
    void shouldCorrectlySetMessageAndDefaultHttpStatus() {
        String errorMessage = "This is a test error";

        SecureFlightException exception = new SecureFlightException(errorMessage);

        assertThat(exception.getMessage()).isEqualTo(errorMessage);
        assertThat(exception.getHttpStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("Should correctly set message")
    void shouldHandleNullMessageCorrectly() {
        SecureFlightException exception = new SecureFlightException(null);

        assertThat(exception.getMessage()).isNull();
        assertThat(exception.getHttpStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("Should always return BadRequest status")
    void shouldAlwaysReturnBadRequestStatusRegardlessOfConstructorInput() {
        SecureFlightException exception = new SecureFlightException("Another error");

        assertThat(exception.getHttpStatus()).isNotEqualTo(HttpStatus.NOT_FOUND);
        assertThat(exception.getHttpStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}