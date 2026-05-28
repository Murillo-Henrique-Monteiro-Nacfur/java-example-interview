package coding.interview.app.api.validations;
import coding.interview.app.api.entities.Flight;
import coding.interview.app.api.entities.Passenger;
import coding.interview.app.env.EnvFlight;
import coding.interview.app.env.EnvPassenger;
import coding.interview.app.infrastructure.exceptions.SecureFlightException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class AddPassengerAlreadyOnFlightValidatorTest {

    @InjectMocks
    private AddPassengerAlreadyOnFlightValidator validator;

    @Test
    @DisplayName("Should not throw exception when passenger is not on flight")
    void shouldNotThrowExceptionWhenPassengerIsNotOnFlight() {
        Passenger passenger = EnvPassenger.getPassenger();
        Flight flight = EnvFlight.getConfirmedFlightWithPassengers(Collections.emptySet());

        assertThatCode(() -> validator.validate(flight, passenger))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Should throw SecureFlightException when passenger is already on flight")
    void shouldThrowSecureFlightExceptionWhenPassengerIsAlreadyOnFlight() {
        Passenger passenger = EnvPassenger.getPassenger();

        Flight flight = EnvFlight.getConfirmedFlightWithPassengers(Set.of(passenger));

        assertThatThrownBy(() -> validator.validate(flight, passenger))
                .isInstanceOf(SecureFlightException.class)
                .hasMessage("Flight already has this passenger");
    }

    @Test
    @DisplayName("Should throw NullPointerException when flight passenger set is null")
    void shouldThrowNullPointerExceptionWhenFlightPassengerSetIsNull() {
        Passenger passenger = EnvPassenger.getPassenger();
        Flight flight = EnvFlight.getConfirmedFlightWithPassengers(null);

        assertThatThrownBy(() -> validator.validate(flight, passenger))
                .isInstanceOf(NullPointerException.class);
    }
}