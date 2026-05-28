package coding.interview.app.api.dto.flight.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record PassengerFlightRequest(
        @NotNull
        @Schema(description = "Id of the passenger", example = "1")
        Long passengerId
) {
}
