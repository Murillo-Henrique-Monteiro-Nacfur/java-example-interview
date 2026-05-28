package coding.interview.app.api.dto.flight.response;

import coding.interview.app.api.dto.passenger.PassengerResponseDTO;
import coding.interview.app.api.entities.FlightStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class FlightWithPassengersResponseDto {

    @Schema(description = "Id of the flight", example = "1")
    private Long id;
    @Schema(description = "Code of the flight", example = "AVRT")
    private String code;
    @Schema(description = "Origin of the flight", example = "Origin")
    private String origin;
    @Schema(description = "Destination of the flight", example = "Destination")
    private String destination;
    @Schema(description = "Status of the flight", example = "finished")
    private FlightStatus status;
    @Schema(description = "Passengers of the flight", example = "[{ 'id': 1, 'name': 'John Doe' }...]")
    private List<PassengerResponseDTO> passengers;
}
