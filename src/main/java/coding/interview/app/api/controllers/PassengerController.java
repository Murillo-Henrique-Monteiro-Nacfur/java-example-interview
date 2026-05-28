package coding.interview.app.api.controllers;

import coding.interview.app.api.dto.passenger.PassengerResponseDTO;
import coding.interview.app.api.presenter.PassengerPresenter;
import coding.interview.app.api.services.passenger.PassengerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/passengers")
@Tag(name = "Passengers")
@RequiredArgsConstructor
public class PassengerController {

    private final PassengerService passengerService;
    private final PassengerPresenter passengerPresenter;

    @Operation(summary = "Get all passengers with pagination")
    @PreAuthorize("hasAnyRole('ADMIN', 'AGENT')")
    @GetMapping
    public ResponseEntity<Page<PassengerResponseDTO>> getAllPagedPassenger(@PageableDefault Pageable pageable) {
        return ResponseEntity.ok(passengerService.findAllPaged(pageable).map(passengerPresenter::toDto));
    }

}
