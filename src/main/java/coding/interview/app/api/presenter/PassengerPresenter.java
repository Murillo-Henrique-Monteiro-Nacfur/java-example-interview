package coding.interview.app.api.presenter;

import coding.interview.app.api.dto.passenger.PassengerResponseDTO;
import coding.interview.app.api.entities.Passenger;
import org.springframework.stereotype.Component;

import static java.util.Objects.isNull;

@Component
public class PassengerPresenter {

    public PassengerResponseDTO toDto(Passenger passenger){
        return isNull(passenger) ? null : PassengerResponseDTO
                .builder()
                .id(passenger.getId())
                .email(passenger.getEmail())
                .name(passenger.getName())
                .phoneNumber(passenger.getPhoneNumber())
                .birthDate(passenger.getBirthDate())
                .build();
    }

}
