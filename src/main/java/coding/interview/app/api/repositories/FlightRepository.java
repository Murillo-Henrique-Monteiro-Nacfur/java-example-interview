package coding.interview.app.api.repositories;

import coding.interview.app.api.entities.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {
    @Query(
            """
            SELECT f
            FROM Flight f
            LEFT JOIN FETCH f.passengers
            where f.id = :flightId
            """)
    Optional<Flight> findByIdWithPassengers(@Param("flightId") Long flightId);
}
