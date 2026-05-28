package coding.interview.app.api.services.passenger;

import coding.interview.app.api.entities.Passenger;
import coding.interview.app.api.repositories.PassengerRepository;
import coding.interview.app.env.EnvPassenger;
import coding.interview.app.infrastructure.exceptions.SecureFlightNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PassengerServiceTest {

    @Mock
    private PassengerRepository passengerRepository;

    @InjectMocks
    private PassengerService passengerService;

    @Test
    @DisplayName("Should return Passenger when findById is called and Passenger exists")
    void shouldReturnPassengerWhenFindByIdIsCalledAndPassengerExists() {
        Passenger passenger = EnvPassenger.getPassenger();
        when(passengerRepository.findById(anyLong())).thenReturn(Optional.of(passenger));

        Passenger result = passengerService.findById(passenger.getId());

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(passenger.getId());
        verify(passengerRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("shouldThrowNotFoundExceptionWhenFindByIdIsCalledAndPassengerDoesNotExist")
    void shouldThrowNotFoundExceptionWhenFindByIdIsCalledAndPassengerDoesNotExist() {
        Long passengerId = 99L;
        when(passengerRepository.findById(passengerId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> passengerService.findById(passengerId))
                .isInstanceOf(SecureFlightNotFoundException.class)
                .hasMessage("Passenger not found");
    }

    @Test
    @DisplayName("shouldReturnPagedPassengersWhenFindAllPagedIsCalled")
    void shouldReturnPagedPassengersWhenFindAllPagedIsCalled() {
        Pageable pageable = Pageable.ofSize(5);
        Page<Passenger> passengerPage = new PageImpl<>(List.of(EnvPassenger.getPassenger()));
        when(passengerRepository.findAll(pageable)).thenReturn(passengerPage);

        Page<Passenger> result = passengerService.findAllPaged(pageable);

        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(1);
    }

    @Test
    @DisplayName("shouldThrowExceptionWhenFindAllPagedFails")
    void shouldThrowExceptionWhenFindAllPagedFails() {
        Pageable pageable = Pageable.ofSize(5);
        when(passengerRepository.findAll(pageable)).thenThrow(new DataAccessException("DB error") {
        });

        assertThatThrownBy(() -> passengerService.findAllPaged(pageable))
                .isInstanceOf(DataAccessException.class);
    }
}