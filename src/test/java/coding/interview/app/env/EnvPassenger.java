package coding.interview.app.env;

import coding.interview.app.api.entities.Passenger;

import java.time.LocalDate;

public class EnvPassenger {
    public static Passenger getPassenger() {
        return Passenger
                .builder()
                .id(1L)
                .name("Passenger 1")
                .email("Email.passenger1@gmail.com")
                .phoneNumber("123456789")
                .birthDate(LocalDate.of(1990, 1, 1))
                .build();
    }
}
