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

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class RemovePassengerAlreadyOutOfFlightValidatorTest {

    @InjectMocks
    private RemovePassengerAlreadyOutOfFlightValidator validator;

    @Test
    @DisplayName("Should not throw exception when passenger is not on flight")
    void shouldNotThrowExceptionWhenPassengerIsOnFlight() {
        Passenger passenger = EnvPassenger.getPassenger();
        Flight flight = EnvFlight.getConfirmedFlightWithPassengers(Set.of(passenger));

        assertThatCode(() -> validator.validate(flight, passenger))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Should throw SecureFlightException when passenger is not on flight")
    void shouldThrowSecureFlightExceptionWhenPassengerIsNotOnFlight() {
        Passenger passenger = EnvPassenger.getPassenger();
        Flight flight = EnvFlight.getConfirmedFlightWithPassengers(new HashSet<>());

        assertThatThrownBy(() -> validator.validate(flight, passenger))
                .isInstanceOf(SecureFlightException.class)
                .hasMessage("This passenger is not on the flight.");
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