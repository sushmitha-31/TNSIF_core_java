package com.example.demo;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DeliveryController {

    @Autowired
    private DeliveryPersonService service;

    @GetMapping("/delivery-persons")
    public List<DeliveryPerson> list() {
        return service.listAll();
    }

    @GetMapping("/delivery-persons/{id}")
    public ResponseEntity<DeliveryPerson> get(@PathVariable("id") int id) {
        try {
            DeliveryPerson deliveryPerson = service.get(id);
            return new ResponseEntity<DeliveryPerson>(deliveryPerson, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<DeliveryPerson>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/delivery-persons")
    public void add(@RequestBody DeliveryPerson deliveryPerson) {
        service.save(deliveryPerson);
    }

    // RESTful API method for Update operation
    @PutMapping("/delivery-persons/{id}")
    public ResponseEntity<?> update(
            @RequestBody DeliveryPerson deliveryPerson,
            @PathVariable("id") int id) {

        try {
            DeliveryPerson existing = service.get(id);

            existing.setName(deliveryPerson.getName());
            existing.setContactNo(deliveryPerson.getContactNo());

            service.save(existing);

            return new ResponseEntity<>(HttpStatus.OK);

        } catch (NoSuchElementException e) {

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    

    // RESTful API method for Delete operation
    @DeleteMapping("/delivery-persons/{id}")
    public void delete(@PathVariable("id") int id) {
        service.delete(id);
    }

    // Module-specific: assign a delivery person to an order
    @PutMapping("/orders/{orderId}/assign/{deliveryPersonId}")
    public ResponseEntity<?> assign(
            @PathVariable("orderId") long orderId,
            @PathVariable("deliveryPersonId") int deliveryPersonId) {

        try {
            DeliveryPerson assigned = service.assign(orderId, deliveryPersonId);
            return new ResponseEntity<DeliveryPerson>(assigned, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    // Module-specific: display delivery details for an order
    @GetMapping("/orders/{orderId}/delivery")
    public ResponseEntity<DeliveryPerson> getDeliveryDetails(
            @PathVariable("orderId") long orderId) {

        try {
            DeliveryPerson deliveryPerson = service.getDeliveryDetails(orderId);
            return new ResponseEntity<DeliveryPerson>(deliveryPerson, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<DeliveryPerson>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/delivery-persons/available")
    public List<DeliveryPerson> available() {
        return service.getAvailable();
    }
}

