package coding.interview.app.env;

import coding.interview.app.api.dto.flight.response.FlightResponseDTO;
import coding.interview.app.api.entities.FlightStatus;

public class EnvFlightResponseDTO {
    public static FlightResponseDTO getConfirmedFlightResponseDto() {
        return FlightResponseDTO
                .builder()
                .id(1L)
                .code("NYC")
                .origin("LAX")
                .destination("tes")
                .status(FlightStatus.confirmed)
                .build();
    }

}
