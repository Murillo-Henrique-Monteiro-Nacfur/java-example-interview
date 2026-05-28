package coding.interview.app.infrastructure.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class SecureFlightNotFoundException extends RuntimeException {

    private final HttpStatus httpStatus = HttpStatus.NOT_FOUND;

    public SecureFlightNotFoundException(String message) {
        super(message);
    }

}
