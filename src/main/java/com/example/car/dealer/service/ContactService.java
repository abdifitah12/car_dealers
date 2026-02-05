package com.example.car.dealer.service;

import com.example.car.dealer.entity.Contact;
import com.example.car.dealer.repository.ContactRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContactService {

    private final ContactRepository contactRepository;

    public Contact saveContact(Contact contact) {
        return contactRepository.save(contact);
    }

    public List<Contact> getAllContacts() {
        return contactRepository.findAll();
    }

    public void deleteContact(Long id) {
        contactRepository.deleteById(id);
    }
}
