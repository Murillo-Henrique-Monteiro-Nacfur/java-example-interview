package coding.interview.app.api.controllers;

import coding.interview.app.api.dto.passenger.PassengerResponseDTO;
import coding.interview.app.api.entities.Passenger;
import coding.interview.app.api.presenter.PassengerPresenter;
import coding.interview.app.api.services.passenger.PassengerService;
import coding.interview.app.env.EnvPassenger;
import coding.interview.app.env.EnvPassengerResponseDTO;
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
class PassengerControllerTest {

    @Mock
    private PassengerService passengerService;

    @Mock
    private PassengerPresenter passengerPresenter;

    @InjectMocks
    private PassengerController passengerController;

    @Test
    @DisplayName("Should return ok and paged passengers when passengers exist")
    void shouldReturnOkAndPagedPassengersWhenPassengersExist() {
        Pageable pageable = Pageable.ofSize(5);
        Passenger passenger = EnvPassenger.getPassenger();
        Page<Passenger> passengerPage = new PageImpl<>(List.of(passenger), pageable, 1);
        PassengerResponseDTO passengerDto = EnvPassengerResponseDTO.getPassengerResponseDTO();

        when(passengerService.findAllPaged(pageable)).thenReturn(passengerPage);
        when(passengerPresenter.toDto(any(Passenger.class))).thenReturn(passengerDto);

        ResponseEntity<Page<PassengerResponseDTO>> response = passengerController.getAllPagedPassenger(pageable);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTotalElements()).isEqualTo(1);
        assertThat(response.getBody().getContent().get(0).getName()).isEqualTo(passengerDto.getName());

        verify(passengerService, times(1)).findAllPaged(pageable);
        verify(passengerPresenter, times(1)).toDto(passenger);
    }

    @Test
    @DisplayName("Should return ok and empty page when no passengers exist")
    void shouldReturnOkAndEmptyPageWhenNoPassengersExist() {
        Pageable pageable = Pageable.ofSize(5);
        Page<Passenger> emptyPage = new PageImpl<>(Collections.emptyList(), pageable, 0);

        when(passengerService.findAllPaged(pageable)).thenReturn(emptyPage);

        ResponseEntity<Page<PassengerResponseDTO>> response = passengerController.getAllPagedPassenger(pageable);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTotalElements()).isZero();
        assertThat(response.getBody().getContent()).isEmpty();

        verify(passengerService, times(1)).findAllPaged(pageable);
        verify(passengerPresenter, never()).toDto(any());
    }

    @Test
    @DisplayName("Should propagate exception when service fails")
    void shouldPropagateExceptionWhenServiceFails() {
        Pageable pageable = Pageable.ofSize(5);
        when(passengerService.findAllPaged(pageable)).thenThrow(new RuntimeException("Database connection failed"));

        assertThatThrownBy(() -> passengerController.getAllPagedPassenger(pageable))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Database connection failed");

        verify(passengerPresenter, never()).toDto(any());
    }
}