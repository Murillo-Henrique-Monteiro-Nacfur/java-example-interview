package coding.interview.app.env;

import coding.interview.app.api.entities.Flight;
import coding.interview.app.api.entities.FlightStatus;
import coding.interview.app.api.entities.Passenger;

import java.util.Set;

public class EnvFlight {
    public static Flight getConfirmedFlight() {
        return getConfirmedFlightWithPassengers(null);
    }

    public static Flight getConfirmedFlightWithPassengers(Set<Passenger> passengers) {
        return Flight
                .builder()
                .id(1L)
                .code("NYC")
                .origin("LAX")
                .destination("tes")
                .status(FlightStatus.confirmed)
                .passengers(passengers)
                .build();
    }
}
