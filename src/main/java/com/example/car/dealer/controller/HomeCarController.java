package com.example.car.dealer.controller;

import com.example.car.dealer.carEnum.CarStatus;
import com.example.car.dealer.dto.CarResponse;
import com.example.car.dealer.entity.Car;
import com.example.car.dealer.entity.Contact;
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
    public String getAllCars(Model model) {

        List<Car> cars = carService.getAllCars();
        List<CarResponse> carDtos = carResponseService.toDtoList(cars);

        model.addAttribute("cars", carDtos);

        // dropdown makes
        model.addAttribute("makes", carService.getAllCarMakes());

        return "car-list";
    }


    // ================= CAR DETAILS PAGE =================
    @GetMapping("/cars/{id}")
    public String carDetails(@PathVariable Long id, Model model) {
        Car car = carRepository.findById(id).orElseThrow();
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
            carStatus = CarStatus.valueOf(status.trim().toUpperCase()); // NEW/USED
        }

        List<Car> cars = "desc".equalsIgnoreCase(order)
                ? carRepository.filterDescAvailable(carStatus, make, model, minPrice, maxPrice)
                : carRepository.filterAscAvailable(carStatus, make, model, minPrice, maxPrice);


        return ResponseEntity.ok(carResponseService.toDtoList(cars));
    }
}
