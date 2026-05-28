package coding.interview.app.api.validations;

import coding.interview.app.api.entities.Flight;
import coding.interview.app.api.entities.FlightStatus;
import coding.interview.app.api.entities.Passenger;
import coding.interview.app.infrastructure.exceptions.SecureFlightException;
import org.springframework.stereotype.Component;

@Component
public class FlightAvailableValidator implements IAddPassengerValidation, IRemovePassengerValidation{
    @Override
    public void validate(Flight flight, Passenger passenger) {
        if (FlightStatus.finished.equals(flight.getStatus())) {
            throw new SecureFlightException("Flight already finished");
        }
    }

}
