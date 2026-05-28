package coding.interview.app.api.dto.flight.requests;

import coding.interview.app.api.entities.FlightStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record UpdateFlightRequest(
        @NotEmpty
        @Schema(description = "Code of the flight", example = "AVRT")
        String code,
        @NotEmpty
        @Schema(description = "Origin of the flight", example = "origin")
        String origin,
        @NotEmpty
        @Schema(description = "Destination of the flight", example = "destination")
        String destination,
        @NotNull
        @Schema(description = "Status of the flight", example = "finished")
        FlightStatus status
) {
}
