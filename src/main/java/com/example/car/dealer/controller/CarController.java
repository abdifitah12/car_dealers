package com.example.car.dealer.controller;

import com.example.car.dealer.entity.Car;
import com.example.car.dealer.entity.CarImage;
import com.example.car.dealer.service.CarService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/cars")
public class CarController {

    private final CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping("/add")
    public String addCarPage(@ModelAttribute("car") Car car) {
        return "add-car";
    }

    @PostMapping("/add")
    public String addCar(@ModelAttribute("car") Car car,
                         @RequestParam("imageFiles") MultipartFile[] imageFiles,
                         RedirectAttributes redirectAttributes) {

        try {
            List<CarImage> images = new ArrayList<>();

            if (imageFiles != null) {
                for (MultipartFile file : imageFiles) {
                    if (file != null && !file.isEmpty()) {
                        CarImage img = new CarImage();
                        img.setImageData(file.getBytes());
                        img.setCar(car);
                        images.add(img);
                    }
                }
            }

            car.setImages(images);
            carService.addCar(car);

            redirectAttributes.addFlashAttribute("success", "Car added successfully!");
            return "redirect:/";

        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Error uploading images: " + e.getMessage());
            return "redirect:/cars/add";
        }
    }
}
