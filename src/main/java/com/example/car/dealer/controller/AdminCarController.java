package com.example.car.dealer.controller;

import com.example.car.dealer.entity.Car;
import com.example.car.dealer.service.AdminCarService;
import com.example.car.dealer.service.CarResponseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/admin/cars")
public class AdminCarController {

    private final AdminCarService adminCarService;
    private final CarResponseService carResponseService;

    public AdminCarController(AdminCarService adminCarService,
                              CarResponseService carResponseService) {
        this.adminCarService = adminCarService;
        this.carResponseService = carResponseService;
    }

    @GetMapping
    public String adminList(Model model) {
        model.addAttribute("cars", carResponseService.toDtoList(adminCarService.getAllCars()));
        return "admin-cars";
    }

    @GetMapping("/{id}/edit")
    public String showEditPage(@PathVariable Long id, Model model) {

        Car car = adminCarService.getCarById(id); // load car
        model.addAttribute("car", car);

        return "car-edit"; // car-edit.html
    }


    @PostMapping("/{id}/edit")
    public String updateCar(@PathVariable Long id,
                            @ModelAttribute("car") Car car,
                            @RequestParam(value = "imageFiles", required = false) List<MultipartFile> imageFiles) {

        adminCarService.updateCar(id, car, imageFiles);

        // go where you want after save:
        return "redirect:/";  // home page
        // or: return "redirect:/admin/cars?updated";
    }



    @PostMapping("/{id}/delete")
    public String deleteCar(@PathVariable Long id) {
        adminCarService.deleteCar(id);
        return "redirect:/admin/cars";
    }

}
