package com.team3.coffee_order.service;

import com.team3.coffee_order.domain.entity.Customer;
import com.team3.coffee_order.domain.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    // TODO: create

    // TODO: read

    // TODO: update

    // TODO: delete
    public ResponseEntity<Void> deleteCustomer(Long id){
        Customer customer = customerRepository.findById(id)
                .orElseThrow(()->new Exception("해당하는 고객을 찾을 수 없습니다."));


        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(customerRepository.delete(customer));
    }
}