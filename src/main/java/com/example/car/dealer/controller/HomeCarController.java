package com.example.car.dealer.controller;

import com.example.car.dealer.carEnum.CarStatus;
import com.example.car.dealer.entity.Car;
import com.example.car.dealer.entity.Contact;
import com.example.car.dealer.repository.CarRepository;
import com.example.car.dealer.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping
public class HomeCarController {

    @Autowired
    private CarService carService;

    @Autowired
    private CarRepository carRepository;

    @GetMapping
    public String getAllCars(Model model) {
        List<Car> cars = carService.getAllCars();
        model.addAttribute("cars", cars);
        model.addAttribute("contact", new Contact());

        // Fetch unique car makes for the dropdown
        List<String> makes = carService.getAllCarMakes();
        model.addAttribute("makes", makes);

        return "car-list";
    }


    // GET request to fetch cars by status with sorting order


    @GetMapping("/contact")
    public String showContactForm(Model model) {
        model.addAttribute("contact", new Contact());
        return "contact-form"; // Ensure this matches your Thymeleaf template name
    }

    @GetMapping("/filter")
    public ResponseEntity<List<Car>> filterCars(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String make,
            @RequestParam(required = false) String model,
            @RequestParam(defaultValue = "asc") String order) {

        // Convert status to Enum
        CarStatus carStatus = null;
        if (status != null && !status.isEmpty()) {
            try {
                carStatus = CarStatus.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body(Collections.emptyList());
            }
        }

        List<Car> filteredCars = carRepository.findByFilters(carStatus, make, model, order);
        return ResponseEntity.ok(filteredCars);
    }



}