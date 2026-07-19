package com.team3.coffee_order.service;

import com.team3.coffee_order.domain.entity.Customer;
import com.team3.coffee_order.domain.repository.CustomerRepository;
import com.team3.coffee_order.exception.NotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
                .orElseThrow(()->new NotFoundException("해당하는 회원을 찾을 수 없습니다."));

        customerRepository.delete(customer);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}