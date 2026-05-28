package coding.interview.app.api.controllers;

import coding.interview.app.api.dto.flight.response.FlightResponseDTO;
import coding.interview.app.api.dto.flight.response.FlightWithPassengersResponseDto;
import coding.interview.app.api.dto.flight.requests.PassengerFlightRequest;
import coding.interview.app.api.dto.flight.requests.UpdateFlightRequest;
import coding.interview.app.api.entities.Flight;
import coding.interview.app.api.presenter.FlightPresenter;
import coding.interview.app.api.services.flight.AddPassengerService;
import coding.interview.app.api.services.flight.FlightService;
import coding.interview.app.api.services.flight.RemovePassengerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/flights")
@Tag(name = "Flights")
@RequiredArgsConstructor
public class FlightController {
    private final FlightService flightService;
    private final FlightPresenter flightPresenter;
    private final AddPassengerService addPassengerService;
    private final RemovePassengerService removePassengerService;

    @Operation(summary = "Get all flights without pagination")
    @PreAuthorize("hasAnyRole('ADMIN', 'AGENT', 'OPERATOR')")
    @GetMapping("/all")
    public ResponseEntity<List<FlightResponseDTO>> getAllFlights() {
        return ResponseEntity.ok(flightService.findAll().stream().map(flightPresenter::toDto).toList());
    }
    @Operation(summary = "Get all flights with pagination")
    @PreAuthorize("hasAnyRole('ADMIN', 'AGENT', 'OPERATOR')")
    @GetMapping
    public ResponseEntity<Page<FlightResponseDTO>> getAllAPagedFlights(@PageableDefault Pageable pageable) {
        return ResponseEntity.ok(flightService.findAllPaged(pageable).map(flightPresenter::toDto));
    }
    @Operation(summary = "Get a flight by its ID")
    @PreAuthorize("hasAnyRole('ADMIN', 'AGENT', 'OPERATOR')")
    @GetMapping("/{id}")
    public ResponseEntity<FlightResponseDTO> getFlightById(@PathVariable Long id) {
        final Flight flight = flightService.findById(id);
        return ResponseEntity.ok(flightPresenter.toDto(flight));
    }
    @Operation(summary = "Update a flight's status or other details")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    @PutMapping("/{id}")
    public ResponseEntity<FlightResponseDTO> updateFlight(@PathVariable Long id,
                                                          @RequestBody @Valid UpdateFlightRequest request) {
        var newFlight = flightPresenter.toUpdateRequestFlight(request);
        newFlight = flightService.updateFlight(id, newFlight);
        return ResponseEntity.ok(flightPresenter.toDto(newFlight));
    }
    @Operation(summary = "Add a passenger to a specific flight")
    @PreAuthorize("hasAnyRole('ADMIN', 'AGENT')")
    @PostMapping("/{flightId}/add/passengers")
    public ResponseEntity<FlightResponseDTO> addPassengerToFlight(@PathVariable("flightId") Long flightId,
                                                                  @Valid @RequestBody PassengerFlightRequest request) {
        addPassengerService.addPassenger(flightId, request.passengerId());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Remove a passenger from a specific flight")
    @PreAuthorize("hasAnyRole('ADMIN', 'AGENT')")
    @DeleteMapping("/{flightId}/remove/passengers")
    public ResponseEntity<FlightResponseDTO> removePassengerToFlight(@PathVariable("flightId") Long flightId,
                                                                     @Valid @RequestBody PassengerFlightRequest request) {
        removePassengerService.removePassenger(flightId, request.passengerId());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Get a flight with all its associated passengers")
    @PreAuthorize("hasAnyRole('ADMIN', 'AGENT', 'OPERATOR')")
    @GetMapping("/{flightId}/passengers")
    public ResponseEntity<FlightWithPassengersResponseDto> getFlightWithAllPassenger(@PathVariable Long flightId) {
        return ResponseEntity.ok(flightPresenter.toDetailDto(flightService.findAllAPassengerByFlight(flightId)));
    }
}
