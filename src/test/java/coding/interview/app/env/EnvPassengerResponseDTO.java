package coding.interview.app.env;

import coding.interview.app.api.dto.passenger.PassengerResponseDTO;

public class EnvPassengerResponseDTO {

    public static PassengerResponseDTO getPassengerResponseDTO(){
        return PassengerResponseDTO.builder()
                .id(1L)
                .name("Passenger 1")
                .email("Email.passenger1@gmail.com")
                .phoneNumber("123456789")
                .birthDate(EnvPassenger.getPassenger().getBirthDate())
                .build();
    }
}
