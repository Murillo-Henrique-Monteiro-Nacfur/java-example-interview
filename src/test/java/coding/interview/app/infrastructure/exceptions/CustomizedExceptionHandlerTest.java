package coding.interview.app.infrastructure.exceptions;

import coding.interview.app.infrastructure.exceptions.response.ExceptionResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.MethodParameter;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomizedExceptionHandlerTest {

    @Mock
    private WebRequest webRequest;

    @InjectMocks
    private CustomizedExceptionHandler exceptionHandler;

    @Test
    @DisplayName("Should return BadRequest when MethodArgumentNotValidException is thrown")
    void shouldReturnBadRequestWhenMethodArgumentNotValidExceptionIsThrown() throws NoSuchMethodException {
        BindingResult bindingResult = mock(BindingResult.class);
        MethodParameter parameter = new MethodParameter(this.getClass().getDeclaredMethod("shouldReturnBadRequestWhenMethodArgumentNotValidExceptionIsThrown"), -1);
        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(parameter, bindingResult);
        FieldError fieldError = new FieldError("objectName", "fieldName", "must not be null");

        when(bindingResult.getAllErrors()).thenReturn(List.of(fieldError));
        when(webRequest.getDescription(false)).thenReturn("uri=/test");

        ResponseEntity<Object> response = exceptionHandler.handleMethodArgumentNotValid(ex, new HttpHeaders(), HttpStatus.BAD_REQUEST, webRequest);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isInstanceOf(ExceptionResponse.class);
        ExceptionResponse<?> responseBody = (ExceptionResponse<?>) response.getBody();
        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getMessage()).isEqualTo("Validation failed for one or more argument");
        assertThat(responseBody.getFields()).hasSize(1);
    }

    @Test
    @DisplayName("Should return correct status when SecureFlightException is thrown")
    void shouldReturnCorrectStatusWhenSecureFlightExceptionIsThrown() {
        SecureFlightException ex = new SecureFlightException("Test error");

        ResponseEntity<ExceptionResponse<Object>> response = exceptionHandler.handleSecureFlightExceptions(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).isEqualTo("SecureFlight error");
        assertThat(response.getBody().getDetails()).isEqualTo("Test error");
    }

    @Test
    @DisplayName("Should return NotFound when SecureFlightNotFoundException is thrown")
    void shouldReturnNotFoundWhenSecureFlightNotFoundExceptionIsThrown() {
        SecureFlightNotFoundException ex = new SecureFlightNotFoundException("Resource not found");

        ResponseEntity<ExceptionResponse<Object>> response = exceptionHandler.handleSecureFlightNotFoundExceptions(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).isEqualTo("SecureFlight Not Found error");
        assertThat(response.getBody().getDetails()).isEqualTo("Resource not found");
    }

    @Test
    @DisplayName("Should return InternalServerError when DataIntegrityViolationException is thrown")
    void shouldReturnInternalServerErrorWhenDataIntegrityViolationExceptionIsThrown() {
        Throwable rootCause = new Throwable("Root cause message");
        Throwable cause = new Throwable("Cause message", rootCause);
        DataIntegrityViolationException ex = new DataIntegrityViolationException("Data integrity error", cause);

        ResponseEntity<ExceptionResponse<Object>> response = exceptionHandler.handleAllExceptions(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).isEqualTo("Internal error");
        assertThat(response.getBody().getDetails()).isEqualTo("DataBase error");
    }

    @Test
    @DisplayName("Should return Forbidden when AccessDeniedException is thrown")
    void shouldReturnForbiddenWhenAccessDeniedExceptionIsThrown() {
        AccessDeniedException ex = new AccessDeniedException("Access is denied");
        when(webRequest.getDescription(false)).thenReturn("uri=/protected-resource");

        ResponseEntity<ExceptionResponse<Object>> response = exceptionHandler.handleAccessDeniedException(ex, webRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).isEqualTo("Access Denied");
        assertThat(response.getBody().getDetails()).isEqualTo("You do not have the required permissions to access this resource.");
    }
}