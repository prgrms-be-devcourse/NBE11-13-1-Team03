package com.team3.coffee_order.service;

import com.team3.coffee_order.domain.entity.Customer;
import com.team3.coffee_order.domain.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    // TODO: create
    @Transactional
    public Customer findOrCreateByEmail(String email) {
        return customerRepository.findByEmail(email)
                .orElseGet(() -> customerRepository.save(new Customer(email)));
    }

    // TODO: read

    // TODO: update

    // TODO: delete
}