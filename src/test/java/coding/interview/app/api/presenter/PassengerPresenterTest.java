package coding.interview.app.api.presenter;

import coding.interview.app.api.dto.passenger.PassengerResponseDTO;
import coding.interview.app.api.entities.Passenger;
import coding.interview.app.env.EnvPassenger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class PassengerPresenterTest {

    @InjectMocks
    private PassengerPresenter passengerPresenter;

    @Test
    @DisplayName("Should return passenger response dto when passenger is valid")
    void shouldReturnPassengerResponseDtoWhenPassengerIsValid() {
        Passenger passenger = EnvPassenger.getPassenger();

        PassengerResponseDTO result = passengerPresenter.toDto(passenger);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(passenger.getId());
        assertThat(result.getName()).isEqualTo(passenger.getName());
        assertThat(result.getEmail()).isEqualTo(passenger.getEmail());
        assertThat(result.getPhoneNumber()).isEqualTo(passenger.getPhoneNumber());
        assertThat(result.getBirthDate()).isEqualTo(passenger.getBirthDate());
    }

    @Test
    @DisplayName("Should return null when toDto receives null")
    void shouldReturnNullWhenPassengerIsNull() {
        PassengerResponseDTO result = passengerPresenter.toDto(null);

        assertThat(result).isNull();
    }
}