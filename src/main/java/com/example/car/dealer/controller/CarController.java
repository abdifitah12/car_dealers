package com.example.car.dealer.controller;

import com.example.car.dealer.service.CarService;
import com.example.car.dealer.entity.Car;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/cars")
public class CarController {
    @Autowired
    private CarService carService;

    @GetMapping
    public String getAllCars(Model model) {
        List<Car> cars = carService.getAllCars();
        model.addAttribute("cars", cars);

        // Fetch unique car makes for the dropdown
        List<String> makes = carService.getAllCarMakes();
        model.addAttribute("makes", makes);

        return "car-list";
    }



    // GET request to fetch cars by status with sorting order
    @GetMapping("/filter")
    @ResponseBody
    public List<Car> getCarsByStatus(
            @RequestParam String status,
            @RequestParam(defaultValue = "asc") String order) {
        return carService.getCarsByStatus(status, order);
    }


    @PostMapping("/add")
    public ResponseEntity<Car> addCar(@RequestBody Car car) {
        return ResponseEntity.ok(carService.addCar(car));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCar(@PathVariable Long id) {
        carService.deleteCar(id);
        return ResponseEntity.noContent().build();
    }
}
