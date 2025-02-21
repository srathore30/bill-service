package sfa.bill_service.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sfa.bill_service.dto.req.CityReq;
import sfa.bill_service.dto.res.PaginatedResp;
import sfa.bill_service.entities.City;
import sfa.bill_service.services.CityService;

import java.util.List;

@RestController
@RequestMapping("/cities")
@RequiredArgsConstructor
public class CityController {

    private final CityService cityService;

    @PostMapping
    public ResponseEntity<City> createCity(@RequestParam String name, @RequestParam Long stateId) {
        City city = cityService.createCity(name, stateId);
        return ResponseEntity.ok(city);
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<City>> createCityInBulk(@RequestBody List<CityReq> cityReqList) {
        List<City> cities = cityService.createCityInBulk(cityReqList);
        return ResponseEntity.ok(cities);
    }

    @GetMapping("/getCityByName/{name}")
    public ResponseEntity<City> getCityByName(@PathVariable String name){
        City city = cityService.getCityByName(name);
        return ResponseEntity.ok(city);
    }
    @GetMapping("/{id}")
    public ResponseEntity<City> getCityById(@PathVariable Long id) {
        City city = cityService.findById(id);
        return ResponseEntity.ok(city);
    }

    @GetMapping("/byStateId/{stateId}")
    public ResponseEntity<PaginatedResp<City>> getCitiesByState(
            @PathVariable Long stateId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "createdTime") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {

        PaginatedResp<City> cities = cityService.getAllCityByState(stateId, page, pageSize, sortBy, sortDirection);
        return ResponseEntity.ok(cities);
    }
}
