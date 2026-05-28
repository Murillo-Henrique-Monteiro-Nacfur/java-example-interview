package coding.interview.app.api.services.flight;

import coding.interview.app.api.entities.Flight;
import coding.interview.app.api.entities.FlightStatus;
import coding.interview.app.api.repositories.FlightRepository;
import coding.interview.app.env.EnvFlight;
import coding.interview.app.infrastructure.exceptions.SecureFlightNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FlightServiceTest {

    private static final String FLIGHT_NOT_FOUND = "Flight not found";
    private static final String ERROR_DATA_INTEGRITY_VIOLATION_EXCEPTION = "Error DataIntegrityViolationException";
    @Mock
    private FlightRepository flightRepository;

    @InjectMocks
    private FlightService flightService;

    @Test
    @DisplayName("Should return Flight when findById")
    void shouldReturnFlightWhenFindById() {
        Flight flight = EnvFlight.getConfirmedFlight();
        when(flightRepository.findById(any())).thenReturn(Optional.of(flight));

        var response = flightService.findById(1L);

        Assertions.assertThat(response).isNotNull().isEqualTo(flight);
    }

    @Test
    @DisplayName("Should throw NotFoundException when findById")
    void shouldThrowNotFoundExceptionWhenFindById() {
        when(flightRepository.findById(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> flightService.findById(1L))
                .isInstanceOf(SecureFlightNotFoundException.class)
                .hasMessage(FLIGHT_NOT_FOUND);
    }

    @Test
    @DisplayName("Should update flight with success")
    void shouldUpdateFlightWithSuccess() {
        Flight flight = EnvFlight.getConfirmedFlight();
        Flight newFlight = Flight.builder()
                .id(flight.getId())
                .code("code")
                .destination("destination")
                .origin("origin")
                .status(FlightStatus.finished).build();

        when(flightRepository.findById(any())).thenReturn(Optional.of(flight));
        when(flightRepository.save(any())).thenReturn(newFlight);

        var response = flightService.updateFlight(1L, newFlight);

        assertThat(response)
                .isNotNull()
                .isEqualTo(newFlight);
    }

    @Test
    @DisplayName("Should throw NotFoundException when update flight with invalid id")
    void shouldThrowNotFoundExceptionWhenUpdateFlightWithInvalidId() {
        when(flightRepository.findById(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> flightService.findById(1L))
                .isInstanceOf(SecureFlightNotFoundException.class)
                .hasMessage(FLIGHT_NOT_FOUND);
    }

    @Test
    @DisplayName("Should throw DataBaseException when update flight")
    void shouldThrowDataBaseExceptionWhenUpdateFlight() {
        when(flightRepository.findById(any())).thenReturn(Optional.of(EnvFlight.getConfirmedFlight()));
        when(flightRepository.save(any())).thenThrow(new DataIntegrityViolationException(ERROR_DATA_INTEGRITY_VIOLATION_EXCEPTION));

        assertThatThrownBy(() -> flightService.updateFlight(1L, EnvFlight.getConfirmedFlight()))
                .isInstanceOf(DataIntegrityViolationException.class)
                .hasMessage(ERROR_DATA_INTEGRITY_VIOLATION_EXCEPTION);
    }


    @Test
    @DisplayName("Should return all Flights when findAll")
    void shouldReturnListOfFlightsWhenFindAllIsCalledAndFlightsExist() {
        Flight flight = Flight.builder().id(1L).build();
        when(flightRepository.findAll()).thenReturn(List.of(flight));

        List<Flight> result = flightService.findAll();

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        verify(flightRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return empty list when findAll is called and no flights exist")
    void shouldReturnEmptyListWhenFindAllIsCalledAndNoFlightsExist() {
        when(flightRepository.findAll()).thenReturn(Collections.emptyList());

        List<Flight> result = flightService.findAll();

        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should return paged Flights when findAllPaged is called")
    void shouldReturnPagedFlightsWhenFindAllPagedIsCalled() {
        Pageable pageable = Pageable.ofSize(5);
        Page<Flight> flightPage = new PageImpl<>(List.of(Flight.builder().build()));
        when(flightRepository.findAll(pageable)).thenReturn(flightPage);

        Page<Flight> result = flightService.findAllPaged(pageable);

        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(1);
    }

    @Test
    @DisplayName("Should return saved Flight when save is called")
    void shouldReturnSavedFlightWhenSaveIsCalled() {
        Flight flightToSave = Flight.builder().code("F123").build();
        Flight savedFlight = Flight.builder().id(1L).code("F123").build();
        when(flightRepository.save(flightToSave)).thenReturn(savedFlight);

        Flight result = flightService.save(flightToSave);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isNotNull();
        assertThat(result.getCode()).isEqualTo("F123");
        verify(flightRepository, times(1)).save(flightToSave);
    }

    @Test
    @DisplayName("Should throw exception when save fails")
    void shouldThrowExceptionWhenSaveFails() {
        Flight flightToSave = Flight.builder().build();
        when(flightRepository.save(flightToSave)).thenThrow(new DataAccessException("DB error") {
        });

        assertThatThrownBy(() -> flightService.save(flightToSave))
                .isInstanceOf(DataAccessException.class);
    }


    @Test
    @DisplayName("Should return Flight with passengers when findAllAPassengerByFlightIsCalled")
    void shouldReturnFlightWithPassengersWhenFindAllAPassengerByFlightIsCalled() {
        Long flightId = 1L;
        Flight flight = Flight.builder().id(flightId).build();
        when(flightRepository.findByIdWithPassengers(flightId)).thenReturn(Optional.of(flight));

        Flight result = flightService.findAllAPassengerByFlight(flightId);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(flightId);
    }

    @Test
    @DisplayName("Should throw NotFoundException when findAllAPassengerByFlightIsCalled and flight does not exist")
    void shouldThrowNotFoundExceptionWhenFindAllAPassengerByFlightIsCalledAndFlightDoesNotExist() {
        Long flightId = 99L;
        when(flightRepository.findByIdWithPassengers(flightId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> flightService.findAllAPassengerByFlight(flightId))
                .isInstanceOf(SecureFlightNotFoundException.class)
                .hasMessage(FLIGHT_NOT_FOUND);
    }
}