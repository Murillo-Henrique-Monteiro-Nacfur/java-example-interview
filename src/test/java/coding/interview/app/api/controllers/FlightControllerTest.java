package coding.interview.app.api.controllers;

import coding.interview.app.api.dto.flight.requests.PassengerFlightRequest;
import coding.interview.app.api.dto.flight.requests.UpdateFlightRequest;
import coding.interview.app.api.dto.flight.response.FlightResponseDTO;
import coding.interview.app.api.dto.flight.response.FlightWithPassengersResponseDto;
import coding.interview.app.api.entities.Flight;
import coding.interview.app.api.entities.FlightStatus;
import coding.interview.app.api.presenter.FlightPresenter;
import coding.interview.app.api.services.flight.AddPassengerService;
import coding.interview.app.api.services.flight.FlightService;
import coding.interview.app.api.services.flight.RemovePassengerService;
import coding.interview.app.env.EnvFlight;
import coding.interview.app.env.EnvFlightResponseDTO;
import coding.interview.app.infrastructure.exceptions.SecureFlightNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FlightControllerTest {

    private static final String FLIGHT_NOT_FOUND = "Flight not found";
    @InjectMocks
    private coding.interview.app.api.controllers.FlightController flightController;

    @Mock
    private FlightService flightService;
    @Mock
    private FlightPresenter flightPresenter;
    @Mock
    private AddPassengerService addPassengerService;
    @Mock
    private RemovePassengerService removePassengerService;

    @Test
    @DisplayName("Should Return Ok And A Flight Mapped To Dto When Get Flight By Id")
    void shouldReturnOkAndAFlightMappedToDtoWhenGetFlightById() {
        Flight flight = EnvFlight.getConfirmedFlight();
        FlightResponseDTO flightResponseDTO = EnvFlightResponseDTO.getConfirmedFlightResponseDto();
        when(flightService.findById(any())).thenReturn(flight);
        when(flightPresenter.toDto(flight)).thenReturn(flightResponseDTO);

        var response = flightController.getFlightById(1L);
        assertThat(response).isNotNull().hasFieldOrPropertyWithValue("status.value", HttpStatus.OK.value());
        assertThat(response.getBody())
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", flightResponseDTO.getId())
                .hasFieldOrPropertyWithValue("code", flightResponseDTO.getCode())
                .hasFieldOrPropertyWithValue("origin", flightResponseDTO.getOrigin())
                .hasFieldOrPropertyWithValue("destination", flightResponseDTO.getDestination())
                .hasFieldOrPropertyWithValue("status", flightResponseDTO.getStatus());

        verify(flightService, times(1)).findById(any());
        verify(flightPresenter, times(1)).toDto(any());
    }

    @Test
    @DisplayName("Should Throw Exception When Flight Not Found When Get Flight By Id")
    void shouldThrowExceptionWhenFlightNotFoundWhenGetFlightById() {
        when(flightService.findById(any())).thenThrow(new SecureFlightNotFoundException(FLIGHT_NOT_FOUND));

        assertThatThrownBy(() -> flightController.getFlightById(1L))
                .isInstanceOf(SecureFlightNotFoundException.class)
                .hasMessage(FLIGHT_NOT_FOUND);
    }

    @Test
    @DisplayName("Should Update Flight Without Error")
    void shouldUpdateFlightWithOutError() {
        Long flightId = 1L;
        UpdateFlightRequest request = new UpdateFlightRequest("NYCUpdate", "LAXUpdate", "tesUpdate", FlightStatus.confirmed);

        Flight updatedFlight = Flight
                .builder()
                .id(flightId)
                .code(request.code())
                .origin(request.origin())
                .destination(request.destination())
                .status(request.status())
                .build();

        FlightResponseDTO flightResponseDTO = FlightResponseDTO
                .builder()
                .id(flightId)
                .code(request.code())
                .origin(request.origin())
                .destination(request.destination())
                .status(request.status())
                .build();

        when(flightPresenter.toUpdateRequestFlight(any())).thenReturn(updatedFlight);
        when(flightService.updateFlight(anyLong(), any())).thenReturn(updatedFlight);
        when(flightPresenter.toDto(any())).thenReturn(flightResponseDTO);

        var response = flightController.updateFlight(flightId, request);

        assertThat(response).isNotNull().hasFieldOrPropertyWithValue("status.value", HttpStatus.OK.value());
        assertThat(response.getBody())
                .hasFieldOrPropertyWithValue("code", request.code())
                .hasFieldOrPropertyWithValue("origin", request.origin())
                .hasFieldOrPropertyWithValue("destination", request.destination())
                .hasFieldOrPropertyWithValue("status", request.status());

        verify(flightPresenter, times(1)).toUpdateRequestFlight(any());
        verify(flightService, times(1)).updateFlight(anyLong(), any());
        verify(flightPresenter, times(1)).toDto(any());
    }

    @Test
    @DisplayName("Should Throw Exception When Flight Not Found When Update Flight")
    void shouldThrowExceptionWhenFlightNotFoundWhenUpdateFlight() {
        Long flightId = 1L;
        UpdateFlightRequest request = new UpdateFlightRequest("NYCUpdate", "LAXUpdate", "tesUpdate", FlightStatus.confirmed);

        when(flightService.updateFlight(anyLong(), any())).thenThrow(new SecureFlightNotFoundException(FLIGHT_NOT_FOUND));

        assertThatThrownBy(() -> flightController.updateFlight(flightId, request))
                .isInstanceOf(SecureFlightNotFoundException.class)
                .hasMessage(FLIGHT_NOT_FOUND);

        verify(flightPresenter, times(1)).toUpdateRequestFlight(any());
        verify(flightService, times(1)).updateFlight(anyLong(), any());
        verify(flightPresenter, never()).toDto(any());
    }


    @Test
    @DisplayName("Should return ok and list of flights when flights exist")
    void shouldReturnOkAndListOfFlightsWhenFlightsExist() {
        Flight flight = Flight.builder().id(1L).build();
        FlightResponseDTO flightDto = FlightResponseDTO.builder().id(1L).build();

        when(flightService.findAll()).thenReturn(List.of(flight));
        when(flightPresenter.toDto(any(Flight.class))).thenReturn(flightDto);

        ResponseEntity<List<FlightResponseDTO>> response = flightController.getAllFlights();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).hasSize(1);

        verify(flightService, times(1)).findAll();
        verify(flightPresenter, times(1)).toDto(flight);
    }

    @Test
    @DisplayName("Should return ok and empty list when no flights exist")
    void shouldReturnOkAndEmptyListWhenNoFlightsExist() {
        when(flightService.findAll()).thenReturn(Collections.emptyList());

        ResponseEntity<List<FlightResponseDTO>> response = flightController.getAllFlights();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isEmpty();

        verify(flightService, times(1)).findAll();
        verify(flightPresenter, never()).toDto(any());
    }

    @Test
    @DisplayName("Should return ok and paged flights when flights exist")
    void shouldReturnOkAndPagedFlightsWhenFlightsExist() {
        Pageable pageable = Pageable.ofSize(10);
        Flight flight = EnvFlight.getConfirmedFlight();
        Page<Flight> flightPage = new PageImpl<>(List.of(flight), pageable, 1);
        FlightResponseDTO flightDto = FlightResponseDTO.builder().id(flight.getId()).build();

        when(flightService.findAllPaged(pageable)).thenReturn(flightPage);
        when(flightPresenter.toDto(any(Flight.class))).thenReturn(flightDto);

        ResponseEntity<Page<FlightResponseDTO>> response = flightController.getAllAPagedFlights(pageable);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTotalElements()).isEqualTo(1);

        verify(flightService, times(1)).findAllPaged(pageable);
    }

    @Test
    @DisplayName("Should return ok when passenger is added successfully")
    void shouldReturnOkWhenPassengerIsAddedSuccessfully() {
        Long flightId = 1L;
        PassengerFlightRequest request = new PassengerFlightRequest(10L);
        doNothing().when(addPassengerService).addPassenger(flightId, request.passengerId());

        ResponseEntity<FlightResponseDTO> response = flightController.addPassengerToFlight(flightId, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(addPassengerService, times(1)).addPassenger(flightId, 10L);
    }

    @Test
    @DisplayName("Should throw exception when service fails")
    void shouldThrowExceptionWhenAddPassengerServiceFails() {
        Long flightId = 99L;
        PassengerFlightRequest request = new PassengerFlightRequest(10L);
        doThrow(new SecureFlightNotFoundException(FLIGHT_NOT_FOUND)).when(addPassengerService).addPassenger(anyLong(), anyLong());

        assertThatThrownBy(() -> flightController.addPassengerToFlight(flightId, request))
                .isInstanceOf(SecureFlightNotFoundException.class);
    }

    @Test
    @DisplayName("Should return ok when passenger is removed successfully")
    void shouldReturnOkWhenPassengerIsRemovedSuccessfully() {
        Long flightId = 1L;
        PassengerFlightRequest request = new PassengerFlightRequest(10L);
        doNothing().when(removePassengerService).removePassenger(flightId, request.passengerId());

        ResponseEntity<FlightResponseDTO> response = flightController.removePassengerToFlight(flightId, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(removePassengerService, times(1)).removePassenger(flightId, 10L);
    }

    @Test
    @DisplayName("Should throw exception when service fails")
    void shouldThrowExceptionWhenRemovePassengerServiceFails() {
        Long flightId = 99L;
        PassengerFlightRequest request = new PassengerFlightRequest(10L);
        doThrow(new SecureFlightNotFoundException("Passenger not on flight"))
                .when(removePassengerService).removePassenger(anyLong(), anyLong());

        assertThatThrownBy(() -> flightController.removePassengerToFlight(flightId, request))
                .isInstanceOf(SecureFlightNotFoundException.class);
    }

    @Test
    @DisplayName("Should return ok and flight with passengers when flight exists")
    void shouldReturnOkAndFlightWithPassengersWhenFlightExists() {
        Long flightId = 1L;
        Flight flight = Flight.builder().id(flightId).build();
        FlightWithPassengersResponseDto responseDto = FlightWithPassengersResponseDto.builder().id(flightId).build();

        when(flightService.findAllAPassengerByFlight(flightId)).thenReturn(flight);
        when(flightPresenter.toDetailDto(flight)).thenReturn(responseDto);

        ResponseEntity<FlightWithPassengersResponseDto> response = flightController.getFlightWithAllPassenger(flightId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(flightId);
    }

    @Test
    @DisplayName("Should throw not found exception when flight does not exist")
    void shouldThrowNotFoundExceptionWhenGettingPassengersForNonExistentFlight() {
        Long flightId = 99L;
        when(flightService.findAllAPassengerByFlight(flightId))
                .thenThrow(new SecureFlightNotFoundException(FLIGHT_NOT_FOUND));

        assertThatThrownBy(() -> flightController.getFlightWithAllPassenger(flightId))
                .isInstanceOf(SecureFlightNotFoundException.class);
    }

}