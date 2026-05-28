package coding.interview.app.api.services.passenger;

import coding.interview.app.api.entities.Passenger;
import coding.interview.app.api.repositories.PassengerRepository;
import coding.interview.app.infrastructure.exceptions.SecureFlightNotFoundException;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PassengerService {
    private final PassengerRepository passengerRepository;

    public Passenger findById(Long id) {
        return passengerRepository.findById(id).orElseThrow(() -> new SecureFlightNotFoundException("Passenger not found"));
    }
    @Transactional(readOnly = true)
    public Page<Passenger> findAllPaged(Pageable pageable) {
        return passengerRepository.findAll(pageable);
    }
}
