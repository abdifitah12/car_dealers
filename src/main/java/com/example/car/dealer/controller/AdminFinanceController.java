package com.example.car.dealer.controller;

import com.example.car.dealer.entity.Car;
import com.example.car.dealer.entity.CarFinance;
import com.example.car.dealer.service.AdminCarService;
import com.example.car.dealer.service.CarFinanceService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/cars")
public class AdminFinanceController {

    private final AdminCarService adminCarService;
    private final CarFinanceService carFinanceService;

    public AdminFinanceController(AdminCarService adminCarService, CarFinanceService carFinanceService) {
        this.adminCarService = adminCarService;
        this.carFinanceService = carFinanceService;
    }

    // Show finance form
    @GetMapping("/{id}/finance")
    public String showFinance(@PathVariable Long id, Model model) {
        Car car = adminCarService.getCarById(id);

        model.addAttribute("car", car);

        CarFinance finance = (car.getFinance() != null) ? car.getFinance() : new CarFinance();
        model.addAttribute("finance", finance);

        return "car-finance";
    }



    @PostMapping("/{id}/finance")
    public String saveFinance(@PathVariable Long id,
                              @RequestParam Integer termMonths,
                              @RequestParam Double apr,
                              @RequestParam Double downPayment,
                              @RequestParam(defaultValue = "0") Integer missedMonths,
                              @RequestParam String fullName,
                              @RequestParam String phone,
                              @RequestParam String email,
                              @RequestParam(required = false) String address) {

        Car car = adminCarService.getCarById(id);

        carFinanceService.upsertFinanceWithPerson(car, termMonths, apr, downPayment,
                missedMonths, fullName, phone, email, address);

        return "redirect:/admin/cars";
    }


    @PostMapping("/finance/{financeId}/pay")
    public String payMonthly(@PathVariable Long financeId,
                             @RequestParam(required = false) Double paidAmount) {

        carFinanceService.payMonthly(financeId, paidAmount); // service will decide

        return "redirect:/admin/cars/finance-list";
    }


}
