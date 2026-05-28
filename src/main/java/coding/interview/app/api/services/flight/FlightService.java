package coding.interview.app.api.services.flight;

import coding.interview.app.api.entities.Flight;
import coding.interview.app.api.repositories.FlightRepository;
import coding.interview.app.infrastructure.exceptions.SecureFlightNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FlightService {

    private static final String FLIGHT_NOT_FOUND = "Flight not found";
    private final FlightRepository flightRepository;

    @Transactional(readOnly = true)
    public List<Flight> findAll() {
        return flightRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Page<Flight> findAllPaged(Pageable pageable) {
        return flightRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Flight findById(Long id) {
        return flightRepository.findById(id).orElseThrow(() -> new SecureFlightNotFoundException(FLIGHT_NOT_FOUND));
    }

    public Flight save(Flight flight) {
        return flightRepository.save(flight);
    }

    public Flight updateFlight(Long idFlight, Flight newFlight) {
        var oldFlight = findById(idFlight);
        newFlight = updateFlightData(oldFlight, newFlight);
        return save(newFlight);
    }

    private Flight updateFlightData(Flight oldFlight, Flight newFlight) {
        oldFlight.setCode(newFlight.getCode());
        oldFlight.setOrigin(newFlight.getOrigin());
        oldFlight.setDestination(newFlight.getDestination());
        oldFlight.setStatus(newFlight.getStatus());
        return oldFlight;
    }

    @Transactional(readOnly = true)
    public Flight findAllAPassengerByFlight(Long flightId) {
        return flightRepository.findByIdWithPassengers(flightId)
                .orElseThrow(() -> new SecureFlightNotFoundException(FLIGHT_NOT_FOUND));
    }
}
