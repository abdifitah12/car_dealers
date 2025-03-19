package com.example.car.dealer.controller;

import com.example.car.dealer.entity.Contact;
import com.example.car.dealer.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class ContactController {

    @Autowired
    private ContactService contactService;

    @GetMapping("/list")
    public String showContactList(Model model) {
        model.addAttribute("contacts", contactService.getAllContacts());
        return "contact-list";
    }


    @PostMapping("/contact")
    public String submitContact(@ModelAttribute("contact") Contact contact) {
        contactService.saveContact(contact);
        return "redirect:/";  // Redirect to avoid form resubmission
    }



}
