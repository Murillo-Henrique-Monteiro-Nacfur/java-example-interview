package coding.interview.app.api.validations;

import coding.interview.app.api.entities.Flight;
import coding.interview.app.api.entities.Passenger;

public interface IRemovePassengerValidation {

    void validate(Flight flight, Passenger passenger);

}
