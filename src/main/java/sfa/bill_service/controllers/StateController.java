package sfa.bill_service.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sfa.bill_service.dto.res.PaginatedResp;
import sfa.bill_service.entities.City;
import sfa.bill_service.entities.State;
import sfa.bill_service.services.StateService;

import java.util.List;

@RestController
@RequestMapping("/states")
@RequiredArgsConstructor
public class StateController {

    private final StateService stateService;

    @PostMapping
    public ResponseEntity<State> createState(@RequestParam String name) {
        State state = stateService.createState(name);
        return ResponseEntity.ok(state);
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<State>> createStatesInBulk(@RequestBody List<String> nameList) {
        List<State> states = stateService.createStateInBulk(nameList);
        return ResponseEntity.ok(states);
    }

    @GetMapping("/getStateByName/{name}")
    public ResponseEntity<State> getStateByName(@PathVariable String name){
        State state = stateService.getStateByName(name);
        return ResponseEntity.ok(state);
    }
    @GetMapping("/{id}")
    public ResponseEntity<State> getStateById(@PathVariable Long id) {
        State state = stateService.findById(id);
        return ResponseEntity.ok(state);
    }

    @GetMapping
    public ResponseEntity<PaginatedResp<State>> getAllStates(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "createdTime") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {

        PaginatedResp<State> states = stateService.getAllStates(page, pageSize, sortBy, sortDirection);
        return ResponseEntity.ok(states);
    }
}
