package coding.interview.app.api.validations;

import coding.interview.app.api.entities.Flight;
import coding.interview.app.api.entities.FlightStatus;
import coding.interview.app.api.entities.Passenger;
import coding.interview.app.infrastructure.exceptions.SecureFlightException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class FlightAvailableValidatorTest {

    @InjectMocks
    private FlightAvailableValidator validator;

    @ParameterizedTest
    @EnumSource(value = FlightStatus.class, names = {"confirmed", "cancelled"})
    @DisplayName("Should not throw exception when flight status is not finished")
    void shouldNotThrowExceptionWhenFlightStatusIsNotFinished(FlightStatus status) {
        Passenger passenger = Passenger.builder().build();
        Flight flight = Flight.builder()
                .status(status)
                .build();

        assertThatCode(() -> validator.validate(flight, passenger))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Should throw SecureFlightException when flight status is finished")
    void shouldThrowSecureFlightExceptionWhenFlightStatusIsFinished() {
        Passenger passenger = Passenger.builder().build();
        Flight flight = Flight.builder()
                .status(FlightStatus.finished)
                .build();

        assertThatThrownBy(() -> validator.validate(flight, passenger))
                .isInstanceOf(SecureFlightException.class)
                .hasMessage("Flight already finished");
    }

    @Test
    @DisplayName("Should throw NullPointerException when passenger is null")
    void shouldThrowNullPointerExceptionWhenFlightIsNull() {
        Passenger passenger = Passenger.builder().build();

        assertThatThrownBy(() -> validator.validate(null, passenger))
                .isInstanceOf(NullPointerException.class);
    }
}