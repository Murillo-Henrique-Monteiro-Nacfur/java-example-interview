package coding.interview.app.api.services.flight;

import coding.interview.app.api.entities.Flight;
import coding.interview.app.api.entities.Passenger;
import coding.interview.app.api.services.passenger.PassengerService;
import coding.interview.app.api.validations.AddPassengerAlreadyOnFlightValidator;
import coding.interview.app.api.validations.FlightAvailableValidator;
import coding.interview.app.api.validations.IAddPassengerValidation;
import coding.interview.app.infrastructure.exceptions.SecureFlightException;
import coding.interview.app.infrastructure.exceptions.SecureFlightNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddPassengerServiceTest {

    private static final String VALIDATION_FAILED = "Validation failed";
    private static final String FLIGHT_NOT_FOUND = "Flight not found";
    private static final String PASSENGER_NOT_FOUND = "Passenger not found";
    @Mock
    private FlightService flightService;
    @Mock
    private PassengerService passengerService;
    @Mock
    private FlightAvailableValidator flightAvailableValidator;
    @Mock
    private AddPassengerAlreadyOnFlightValidator addPassengerAlreadyOnFlightValidator;

    private AddPassengerService addPassengerService;

    @BeforeEach
    void setUp() {
        List<IAddPassengerValidation> validators = List.of(flightAvailableValidator, addPassengerAlreadyOnFlightValidator);
        addPassengerService = new AddPassengerService(flightService, passengerService, validators);
    }

    @Test
    @DisplayName("Should add passenger and save changes when all validations pass")
    void shouldAddPassengerAndSaveChangesWhenAllValidationsPass() {
        Long flightId = 1L;
        Long passengerId = 10L;
        Flight flight = Flight.builder().id(flightId).passengers(new HashSet<>()).build();
        Passenger passenger = Passenger.builder().id(passengerId).build();

        when(flightService.findById(flightId)).thenReturn(flight);
        when(passengerService.findById(passengerId)).thenReturn(passenger);
        doNothing().when(flightAvailableValidator).validate(flight, passenger);
        doNothing().when(addPassengerAlreadyOnFlightValidator).validate(flight, passenger);

        addPassengerService.addPassenger(flightId, passengerId);

        verify(flightService, times(1)).findById(flightId);
        verify(passengerService, times(1)).findById(passengerId);
        verify(flightAvailableValidator, times(1)).validate(flight, passenger);
        verify(addPassengerAlreadyOnFlightValidator, times(1)).validate(flight, passenger);
        verify(flightService, times(1)).save(flight);
    }

    @Test
    @DisplayName("Should throw NotFoundException when flight does not exist")
    void shouldThrowNotFoundExceptionWhenFlightDoesNotExist() {
        Long flightId = 99L;
        Long passengerId = 10L;

        when(flightService.findById(flightId)).thenThrow(new SecureFlightNotFoundException(FLIGHT_NOT_FOUND));

        assertThatThrownBy(() -> addPassengerService.addPassenger(flightId, passengerId))
                .isInstanceOf(SecureFlightNotFoundException.class)
                .hasMessage(FLIGHT_NOT_FOUND);

        verify(passengerService, never()).findById(anyLong());
        verify(flightService, never()).save(any());
    }

    @Test
    @DisplayName("Should throw NotFoundException when passenger does not exist")
    void shouldThrowNotFoundExceptionWhenPassengerDoesNotExist() {
        Long flightId = 1L;
        Long passengerId = 99L;
        Flight flight = Flight.builder().id(flightId).build();

        when(flightService.findById(flightId)).thenReturn(flight);
        when(passengerService.findById(passengerId)).thenThrow(new SecureFlightNotFoundException(PASSENGER_NOT_FOUND));

        assertThatThrownBy(() -> addPassengerService.addPassenger(flightId, passengerId))
                .isInstanceOf(SecureFlightNotFoundException.class)
                .hasMessage(PASSENGER_NOT_FOUND);

        verify(flightService, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception and not save when any validation fails")
    void shouldThrowExceptionAndNotSaveWhenAnyValidationFails() {
        Long flightId = 1L;
        Long passengerId = 10L;
        Flight flight = Flight.builder().id(flightId).build();
        Passenger passenger = Passenger.builder().id(passengerId).build();

        when(flightService.findById(flightId)).thenReturn(flight);
        when(passengerService.findById(passengerId)).thenReturn(passenger);
        doThrow(new SecureFlightException(VALIDATION_FAILED)).when(flightAvailableValidator).validate(flight, passenger);

        assertThatThrownBy(() -> addPassengerService.addPassenger(flightId, passengerId))
                .isInstanceOf(SecureFlightException.class)
                .hasMessage(VALIDATION_FAILED);

        verify(flightAvailableValidator, times(1)).validate(flight, passenger);
        verify(addPassengerAlreadyOnFlightValidator, never()).validate(any(), any());
        verify(flightService, never()).save(any());
    }
}