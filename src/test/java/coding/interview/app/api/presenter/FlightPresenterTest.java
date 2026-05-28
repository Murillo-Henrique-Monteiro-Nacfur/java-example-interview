package coding.interview.app.api.presenter;

import coding.interview.app.api.dto.flight.requests.UpdateFlightRequest;
import coding.interview.app.api.dto.flight.response.FlightResponseDTO;
import coding.interview.app.api.dto.flight.response.FlightWithPassengersResponseDto;
import coding.interview.app.api.dto.passenger.PassengerResponseDTO;
import coding.interview.app.api.entities.Flight;
import coding.interview.app.api.entities.FlightStatus;
import coding.interview.app.api.entities.Passenger;
import coding.interview.app.env.EnvFlight;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FlightPresenterTest {

    @Mock
    private PassengerPresenter passengerPresenter;

    @InjectMocks
    private FlightPresenter flightPresenter;

    @Test
    @DisplayName("Should return flight response dto when flight is valid")
    void shouldReturnFlightResponseDtoWhenFlightIsValid() {
        Flight flight = EnvFlight.getConfirmedFlight();

        FlightResponseDTO result = flightPresenter.toDto(flight);

        assertThat(result).isNotNull()
                .hasFieldOrPropertyWithValue("id", flight.getId())
                .hasFieldOrPropertyWithValue("code", flight.getCode())
                .hasFieldOrPropertyWithValue("origin", flight.getOrigin())
                .hasFieldOrPropertyWithValue("destination", flight.getDestination())
                .hasFieldOrPropertyWithValue("status", flight.getStatus());
    }

    @Test
    @DisplayName("Should return null when toDto receives null")
    void shouldThrowNullPointerExceptionWhenToDtoReceivesNull() {
        var result = flightPresenter.toDto(null);

        assertThat(result).isNull();
    }

    @Test
    @DisplayName("Should return flight with passengers when flight has passengers")
    void shouldReturnFlightWithPassengersDtoWhenFlightIsValid() {
        Passenger passenger = Passenger.builder().id(10L).build();
        PassengerResponseDTO passengerDto = PassengerResponseDTO.builder().id(10L).build();
        Flight flight = EnvFlight.getConfirmedFlightWithPassengers(Set.of(passenger));

        when(passengerPresenter.toDto(any(Passenger.class))).thenReturn(passengerDto);

        FlightWithPassengersResponseDto result = flightPresenter.toDetailDto(flight);

        assertThat(result).isNotNull()
                .hasFieldOrPropertyWithValue("id", flight.getId())
                .hasFieldOrPropertyWithValue("code", flight.getCode())
                .hasFieldOrPropertyWithValue("origin", flight.getOrigin())
                .hasFieldOrPropertyWithValue("destination", flight.getDestination())
                .hasFieldOrPropertyWithValue("status", flight.getStatus());
        assertThat(result.getPassengers()).isNotNull()
                .hasSize(1)
                .contains(passengerDto);

        verify(passengerPresenter, times(1)).toDto(passenger);
    }

    @Test
    @DisplayName("Should return flight with empty passenger list when flight has no passengers")
    void shouldReturnFlightWithEmptyPassengerListWhenFlightHasNoPassengers() {
        Flight flight = EnvFlight.getConfirmedFlightWithPassengers(Collections.emptySet());

        FlightWithPassengersResponseDto result = flightPresenter.toDetailDto(flight);

        assertThat(result).isNotNull();
        assertThat(result.getPassengers()).isNotNull().isEmpty();

        verify(passengerPresenter, never()).toDto(any());
    }

    @Test
    @DisplayName("Should return null when toDetailDto receives null")
    void shouldReturnNullWhenToDetailDtoReceivesNull() {
        var result = flightPresenter.toDetailDto(null);

        assertThat(result).isNull();
    }

    @Test
    @DisplayName("Should return flight with null passengers when flight passenger set is null")
    void shouldReturnFlightWithNullPassengersWhenFlightPassengerSetIsNull() {
        Flight flightWithNullPassengers = EnvFlight.getConfirmedFlight();
        var result = flightPresenter.toDetailDto(flightWithNullPassengers);

        assertThat(result).isNotNull()
                .hasFieldOrPropertyWithValue("id", flightWithNullPassengers.getId())
                .hasFieldOrPropertyWithValue("code", flightWithNullPassengers.getCode())
                .hasFieldOrPropertyWithValue("origin", flightWithNullPassengers.getOrigin())
                .hasFieldOrPropertyWithValue("destination", flightWithNullPassengers.getDestination())
                .hasFieldOrPropertyWithValue("status", flightWithNullPassengers.getStatus())
                .hasFieldOrPropertyWithValue("passengers", null);

    }

    @Test
    @DisplayName("shouldReturnFlightEntityWhenRequestIsValid")
    void shouldReturnFlightEntityWhenRequestIsValid() {
        UpdateFlightRequest request = new UpdateFlightRequest("NYCUpdate", "LAXUpdate", "tesUpdate", FlightStatus.confirmed);

        Flight result = flightPresenter.toUpdateRequestFlight(request);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isNull();
        assertThat(result.getCode()).isEqualTo(request.code());
        assertThat(result.getOrigin()).isEqualTo(request.origin());
        assertThat(result.getDestination()).isEqualTo(request.destination());
        assertThat(result.getStatus()).isEqualTo(request.status());
    }

    @Test
    @DisplayName("Should return null whenToUpdateRequestFlightReceivesNull")
    void shouldReturnNullWhenToUpdateRequestFlightReceivesNull() {

        var result = flightPresenter.toUpdateRequestFlight(null);

        assertThat(result).isNull();
    }
}