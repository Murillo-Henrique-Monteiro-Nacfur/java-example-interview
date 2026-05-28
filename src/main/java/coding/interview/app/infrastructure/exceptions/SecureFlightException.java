package coding.interview.app.infrastructure.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class SecureFlightException extends RuntimeException {

    private final HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

    public SecureFlightException(String message) {
        super(message);
    }
}
