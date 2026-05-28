package coding.interview.app.api.dto.passenger;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
public class PassengerResponseDTO {
    @Schema(description = "Id of the Passenger", example = "1")
    private Long id;
    @Schema(description = "Name of the Passenger", example = "John Doe")
    private String name;
    @Schema(description = "E-mail of the Passenger", example = "john@gmail.com")
    private String email;
    @Schema(description = "Phone number of the Passenger", example = "555-5645")
    private String phoneNumber;
    @Schema(description = "BirthDate of the Passenger", example = "2000-01-01")
    private LocalDate birthDate;
}
