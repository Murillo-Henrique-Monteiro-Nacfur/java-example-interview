package coding.interview.app.api.presenter;

import coding.interview.app.api.dto.flight.requests.UpdateFlightRequest;
import coding.interview.app.api.dto.flight.response.FlightResponseDTO;
import coding.interview.app.api.dto.flight.response.FlightWithPassengersResponseDto;
import coding.interview.app.api.entities.Flight;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static java.util.Objects.isNull;

@Component
@RequiredArgsConstructor
public class FlightPresenter {

    private final PassengerPresenter passenderPresenter;

    public FlightResponseDTO toDto(Flight flight) {
        return isNull(flight) ? null :
                FlightResponseDTO.builder()
                        .id(flight.getId())
                        .code(flight.getCode())
                        .origin(flight.getOrigin())
                        .destination(flight.getDestination())
                        .status(flight.getStatus())
                        .build();
    }

    public FlightWithPassengersResponseDto toDetailDto(Flight flight) {
        return isNull(flight) ? null :
                FlightWithPassengersResponseDto.builder()
                        .id(flight.getId())
                        .code(flight.getCode())
                        .origin(flight.getOrigin())
                        .destination(flight.getDestination())
                        .status(flight.getStatus())
                        .passengers(isNull(flight.getPassengers()) ? null : flight.getPassengers().stream().map(passenderPresenter::toDto).toList())
                        .build();
    }

    public Flight toUpdateRequestFlight(UpdateFlightRequest request) {
        return isNull(request) ? null : Flight
                .builder()
                .code(request.code())
                .origin(request.origin())
                .destination(request.destination())
                .status(request.status())
                .build();
    }

}
