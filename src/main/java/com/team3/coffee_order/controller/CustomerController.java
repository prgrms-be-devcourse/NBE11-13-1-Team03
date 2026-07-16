package com.team3.coffee_order.controller;

import com.team3.coffee_order.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    // TODO: create

    // TODO: read

    // TODO: update

    // TODO: delete
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id){
        return customerService.deleteCustomer(id);
    }
}