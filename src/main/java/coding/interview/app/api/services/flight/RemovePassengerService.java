package coding.interview.app.api.services.flight;

import coding.interview.app.api.entities.Flight;
import coding.interview.app.api.entities.Passenger;
import coding.interview.app.api.services.passenger.PassengerService;
import coding.interview.app.api.validations.IRemovePassengerValidation;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RemovePassengerService {

    private final FlightService flightService;
    private final PassengerService passengerService;
    private final List<IRemovePassengerValidation> validationList;

    @Transactional
    public void removePassenger(Long flightId, Long passengerId) {
        Flight flight = flightService.findById(flightId);
        Passenger passenger = passengerService.findById(passengerId);
        doValidations(flight, passenger);
        flight.removePassenger(passenger);
        flightService.save(flight);
    }

    private void doValidations(Flight flight, Passenger passenger) {
        validationList.forEach(validation -> validation.validate(flight, passenger));
    }

}
