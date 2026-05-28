package coding.interview.app.api.validations;

import coding.interview.app.api.entities.Flight;
import coding.interview.app.api.entities.Passenger;
import coding.interview.app.infrastructure.exceptions.SecureFlightException;
import org.springframework.stereotype.Component;

@Component
public class RemovePassengerAlreadyOutOfFlightValidator implements IRemovePassengerValidation{
    @Override
    public void validate(Flight flight, Passenger passenger) {
        if (flight.getPassengers().contains(passenger)) {
            return;
        }
        throw new SecureFlightException("This passenger is not on the flight.");
    }
}
