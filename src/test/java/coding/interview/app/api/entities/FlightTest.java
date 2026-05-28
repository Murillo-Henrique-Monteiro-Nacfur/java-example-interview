package coding.interview.app.api.entities;

import coding.interview.app.env.EnvPassenger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FlightTest {

    @Test
    @DisplayName("Should Add Passenger When Passenger Set Is Initialized")
    void shouldAddPassengerWhenPassengerSetIsInitialized() {
        Passenger passenger = EnvPassenger.getPassenger();
        Flight flight = Flight.builder()
                .passengers(new HashSet<>())
                .build();

        flight.addPassenger(passenger);

        assertThat(flight.getPassengers()).isNotNull();
        assertThat(flight.getPassengers()).hasSize(1);
        assertThat(flight.getPassengers()).contains(passenger);
    }

    @Test
    @DisplayName("Should Throw NullPointerException When Adding Passenger To Uninitialized Set")
    void shouldThrowNullPointerExceptionWhenAddingPassengerToUninitializedSet() {
        Passenger passenger = EnvPassenger.getPassenger();
        Flight flight = Flight.builder()
                .passengers(null)
                .build();

        assertThatThrownBy(() -> flight.addPassenger(passenger))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("Should Remove Passenger When Passenger Exists In Set")
    void shouldRemovePassengerWhenPassengerExistsInSet() {
        Passenger passengerToRemove = EnvPassenger.getPassenger();
        Set<Passenger> initialPassengers = new HashSet<>();
        initialPassengers.add(passengerToRemove);

        Flight flight = Flight.builder()
                .passengers(initialPassengers)
                .build();

        flight.removePassenger(passengerToRemove);

        assertThat(flight.getPassengers()).isNotNull();
        assertThat(flight.getPassengers()).isEmpty();
    }

    @Test
    @DisplayName("Should Do Nothing When Removing Non-Existent Passenger")
    void shouldDoNothingWhenRemovingNonExistentPassenger() {
        Passenger existingPassenger = EnvPassenger.getPassenger();
        Passenger nonExistentPassenger = Passenger.builder().id(2L).name("Jane Doe").build();
        Set<Passenger> initialPassengers = new HashSet<>();
        initialPassengers.add(existingPassenger);

        Flight flight = Flight.builder()
                .passengers(initialPassengers)
                .build();

        flight.removePassenger(nonExistentPassenger);

        assertThat(flight.getPassengers()).isNotNull();
        assertThat(flight.getPassengers()).hasSize(1);
        assertThat(flight.getPassengers()).contains(existingPassenger);
    }

    @Test
    @DisplayName("Should Throw NullPointerException When Removing Passenger From Uninitialized Set")
    void shouldThrowNullPointerExceptionWhenRemovingPassengerFromUninitializedSet() {
        Passenger passenger = EnvPassenger.getPassenger();
        Flight flight = Flight.builder()
                .passengers(null)
                .build();

        assertThatThrownBy(() -> flight.removePassenger(passenger))
                .isInstanceOf(NullPointerException.class);
    }
}