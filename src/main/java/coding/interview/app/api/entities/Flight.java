package coding.interview.app.api.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    private String origin;
    private String destination;
    @Enumerated(EnumType.STRING)
    private FlightStatus status;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "flight_passenger",
            joinColumns = @JoinColumn(name = "flight_id"),
            inverseJoinColumns = @JoinColumn(name = "passenger_id"))
    private Set<Passenger> passengers;

    public void addPassenger(Passenger passenger) {
        passengers.add(passenger);
    }

    public void removePassenger(Passenger passenger) {
        passengers.remove(passenger);
    }
}