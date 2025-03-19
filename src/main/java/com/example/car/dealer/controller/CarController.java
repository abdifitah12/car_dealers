package com.example.car.dealer.controller;

import com.example.car.dealer.entity.Car;
import com.example.car.dealer.entity.Contact;
import com.example.car.dealer.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/cars")
public class CarController {

    @Autowired
    private CarService carService;

    @GetMapping("/add")
    public String showAddCarForm(Model model) {

        model.addAttribute("car", new Car());
        model.addAttribute("contact", new Contact());
        return "add-car";  // Form to add cars
    }

    @PostMapping("/add")
    public String addCar(@ModelAttribute("car") Car car, @RequestParam("imageFiles") MultipartFile[] imageFiles, RedirectAttributes redirectAttributes) {

        List<byte[]> images = new ArrayList<>();
        for (MultipartFile file : imageFiles) {
            if (!file.isEmpty()) {
                try {
                    images.add(file.getBytes());
                } catch (IOException e) {
                    redirectAttributes.addFlashAttribute("error", "Error uploading images.");
                    return "redirect:/cars/add";
                }
            }
        }

        car.setImages(images);
        carService.addCar(car);
        redirectAttributes.addFlashAttribute("success", "Car added successfully!");
        return "redirect:/cars";
    }


}



