package coding.interview.app.api.validations;

import coding.interview.app.api.entities.Flight;
import coding.interview.app.api.entities.Passenger;

public interface IAddPassengerValidation {

    void validate(Flight flight, Passenger passenger);

}
