package sfa.bill_service.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import sfa.bill_service.constants.ApiErrorCodes;
import sfa.bill_service.dto.req.CityReq;
import sfa.bill_service.dto.res.PaginatedResp;
import sfa.bill_service.entities.City;
import sfa.bill_service.entities.State;
import sfa.bill_service.exceptions.NoSuchElementFoundException;
import sfa.bill_service.repositories.CityRepository;
import sfa.bill_service.repositories.StateRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CityService {
    private final StateRepository stateRepository;
    private final CityRepository cityRepository;
    public City createCity(String name, Long stateId) {
        if(cityRepository.findByCityName(name).isPresent()){
            throw new NoSuchElementFoundException(ApiErrorCodes.CITY_ALREADY_EXIT.getErrorCode(), ApiErrorCodes.CITY_ALREADY_EXIT.getErrorMessage());
        }
        return cityRepository.save(new City(name, stateRepository.findById(stateId).orElseThrow(()-> new NoSuchElementFoundException(ApiErrorCodes.CITY_NOT_FOUND.getErrorCode(), ApiErrorCodes.CITY_NOT_FOUND.getErrorMessage()))));
    }

    public List<City> createCityInBulk(List<CityReq> cityReqList){
        List<City> cityList = new ArrayList<>();
        for(CityReq cityReq : cityReqList){
            cityList.add(new City(cityReq.getName(), stateRepository.findById(cityReq.getStateId()).orElseThrow(()-> new NoSuchElementFoundException(ApiErrorCodes.CITY_NOT_FOUND.getErrorCode(), ApiErrorCodes.CITY_NOT_FOUND.getErrorMessage()))));
        }

        return cityRepository.saveAll(cityList);
    }
    public City findById(Long id) {
        return cityRepository.findById(id).orElseThrow(()-> new NoSuchElementFoundException(ApiErrorCodes.CITY_NOT_FOUND.getErrorCode(), ApiErrorCodes.CITY_NOT_FOUND.getErrorMessage()));
    }

   public City getCityByName(String name) {
        return cityRepository.findByCityName(name).orElseThrow(()-> new NoSuchElementFoundException(ApiErrorCodes.CITY_NOT_FOUND.getErrorCode(), ApiErrorCodes.CITY_NOT_FOUND.getErrorMessage()));
    }

    public PaginatedResp<City> getAllCityByState(Long stateId, int page, int pageSize, String sortBy, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, pageSize, sort);
        Page<City> all = cityRepository.findByStateId(stateId,pageable);
        List<City> cities = all.stream().toList();
        return new PaginatedResp<>(all.getTotalElements(), all.getTotalPages(), page, cities);
    }
}
