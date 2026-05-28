package coding.interview.app.api.dto.flight.response;

import coding.interview.app.api.entities.FlightStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class FlightResponseDTO {

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
}
