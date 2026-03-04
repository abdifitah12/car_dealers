package com.example.car.dealer.controller;

import com.example.car.dealer.entity.Contact;
import com.example.car.dealer.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ContactController {

    @Autowired
    private ContactService contactService;

    // ✅ 1️⃣ SHOW CONTACT INFO PAGE
    @GetMapping("/contact")
    public String showContactPage(Model model,
                                  @RequestParam(required = false) Long carId,
                                  @RequestParam(required = false) String brand,
                                  @RequestParam(required = false) String modelName,
                                  @RequestParam(required = false) Integer year,
                                  @RequestParam(required = false) Double price) {

        Contact contact = new Contact();

        // ✅ Prefill message with car info
        if (carId != null) {
            String msg = "Hello, I want more information about this car:\n"
                    + "Car ID: " + carId + "\n"
                    + "Brand: " + brand + "\n"
                    + "Model: " + modelName + "\n"
                    + "Year: " + year + "\n"
                    + "Price: $" + price + "\n";
            contact.setMessage(msg);
        }

        model.addAttribute("contact", contact);

        model.addAttribute("phone", "(206) 326-8924");
        model.addAttribute("email", "abdihaashin275@gmail.com");
        model.addAttribute("address", "SeaTac, WA, United States");
        model.addAttribute("mapQuery", "SeaTac, WA");

        return "contact";
    }


    // ✅ 2️⃣ SAVE CONTACT FORM
    @PostMapping("/contact")
    public String submitContact(@ModelAttribute("contact") Contact contact) {
        contactService.saveContact(contact);
        return "redirect:/contact?success";
    }

    // ✅ 3️⃣ ADMIN: LIST ALL CONTACTS
    @GetMapping("/list")
    public String showContactList(Model model) {
        model.addAttribute("contacts", contactService.getAllContacts());
        return "contact-list";
    }


    @PostMapping("/contact/delete/{id}")
    public String deleteContact(@PathVariable Long id) {
        contactService.deleteContact(id);
        return "redirect:/list?deleted";
    }
}
