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
    public String showContactPage(Model model) {

        // For contact form
        model.addAttribute("contact", new Contact());

        // ✅ Your contact information
        model.addAttribute("phone", "(206) 326-8924");
        model.addAttribute("email", "abdihaashin275@gmail.com");
        model.addAttribute("address", "SeaTac, WA, United States");

        // ✅ Map location query (Google Maps embed)
        model.addAttribute("mapQuery", "SeaTac, WA");

        return "contact"; // contact.html
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
}
