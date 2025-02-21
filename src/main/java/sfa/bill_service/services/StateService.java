package sfa.bill_service.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import sfa.bill_service.constants.ApiErrorCodes;
import sfa.bill_service.dto.res.PaginatedResp;
import sfa.bill_service.entities.City;
import sfa.bill_service.entities.State;
import sfa.bill_service.exceptions.NoSuchElementFoundException;
import sfa.bill_service.repositories.StateRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StateService {
    private final StateRepository stateRepository;
    public State createState(String name) {
        if(stateRepository.findByStateName(name).isPresent()){
            throw new NoSuchElementFoundException(ApiErrorCodes.STATE_ALREADY_EXIT.getErrorCode(), ApiErrorCodes.STATE_ALREADY_EXIT.getErrorMessage());
        }
        return stateRepository.save(new State(name));
    }

    public State getStateByName(String name) {
        return stateRepository.findByStateName(name).orElseThrow(()-> new NoSuchElementFoundException(ApiErrorCodes.STATE_NOT_FOUND.getErrorCode(), ApiErrorCodes.STATE_NOT_FOUND.getErrorMessage()));
    }

    public List<State> createStateInBulk(List<String> nameList){
        List<State> states = new ArrayList<>();
        states = nameList.stream().map(State::new).toList();
        return stateRepository.saveAll(states);
    }
    public State findById(Long id) {
        return stateRepository.findById(id).orElseThrow(()-> new NoSuchElementFoundException(ApiErrorCodes.STATE_NOT_FOUND.getErrorCode(), ApiErrorCodes.STATE_NOT_FOUND.getErrorMessage()));
    }

    public PaginatedResp<State> getAllStates(int page, int pageSize, String sortBy, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, pageSize, sort);
        Page<State> all = stateRepository.findAll(pageable);
        List<State> states = all.stream().toList();
        return new PaginatedResp<>(all.getTotalElements(), all.getTotalPages(), page, states);
    }
}
