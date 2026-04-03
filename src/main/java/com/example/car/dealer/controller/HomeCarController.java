package com.example.car.dealer.controller;

import com.example.car.dealer.carEnum.CarAvailability;
import com.example.car.dealer.carEnum.CarStatus;
import com.example.car.dealer.dto.CarResponse;
import com.example.car.dealer.entity.Car;
import com.example.car.dealer.repository.CarRepository;
import com.example.car.dealer.service.CarResponseService;
import com.example.car.dealer.service.CarService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/")
public class HomeCarController {

    private final CarService carService;
    private final CarRepository carRepository;
    private final CarResponseService carResponseService;

    public HomeCarController(CarService carService,
                             CarRepository carRepository,
                             CarResponseService carResponseService) {
        this.carService = carService;
        this.carRepository = carRepository;
        this.carResponseService = carResponseService;
    }

    // ================= HOME PAGE =================
    @GetMapping
    public String getAllCars(@RequestParam(required = false) String status,
                             @RequestParam(required = false) String make,
                             @RequestParam(required = false) String model,
                             @RequestParam(required = false) Double minPrice,
                             @RequestParam(required = false) Double maxPrice,
                             @RequestParam(defaultValue = "asc") String order,
                             Model modelMap) {

        List<Car> cars = carService.filterCars(status, make, model, minPrice, maxPrice, order);

        // show only AVAILABLE cars
        List<Car> availableCars = cars.stream()
                .filter(car -> car.getAvailability() == CarAvailability.AVAILABLE)
                .toList();

        List<CarResponse> carDtos = carResponseService.toDtoList(availableCars);

        modelMap.addAttribute("cars", carDtos);
        modelMap.addAttribute("makes", carService.getAllCarMakes());
        modelMap.addAttribute("selectedStatus", status);
        modelMap.addAttribute("selectedMake", make);
        modelMap.addAttribute("selectedModel", model);
        modelMap.addAttribute("selectedOrder", order);

        return "car-list";
    }

    // ================= CAR DETAILS PAGE =================
    @GetMapping("/cars/{id}")
    public String carDetails(@PathVariable Long id, Model model) {
        Car car = carRepository.findById(id).orElseThrow();

        // block details page if not available
        if (car.getAvailability() != CarAvailability.AVAILABLE) {
            return "redirect:/";
        }

        CarResponse carDto = carResponseService.toDto(car);
        model.addAttribute("car", carDto);
        return "car-details";
    }

    // ================= MODELS BY MAKE =================
    @GetMapping("/models")
    @ResponseBody
    public List<String> getModels(@RequestParam String make) {
        return carService.getModelsByMake(make);
    }

    // ================= FILTER ENDPOINT =================
    @GetMapping("/filter")
    @ResponseBody
    public ResponseEntity<List<CarResponse>> filterCars(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String make,
            @RequestParam(required = false) String model,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(defaultValue = "asc") String order) {

        CarStatus carStatus = null;
        if (status != null && !status.isBlank()) {
            carStatus = CarStatus.valueOf(status.trim().toUpperCase());
        }

        List<Car> cars = "desc".equalsIgnoreCase(order)
                ? carRepository.filterDescAvailable(carStatus, make, model, minPrice, maxPrice)
                : carRepository.filterAscAvailable(carStatus, make, model, minPrice, maxPrice);

        // extra safety: only AVAILABLE
        List<Car> availableCars = cars.stream()
                .filter(car -> car.getAvailability() == CarAvailability.AVAILABLE)
                .toList();

        return ResponseEntity.ok(carResponseService.toDtoList(availableCars));
    }
}