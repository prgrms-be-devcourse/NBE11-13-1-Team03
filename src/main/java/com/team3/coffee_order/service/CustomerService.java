package com.team3.coffee_order.service;

import com.team3.coffee_order.domain.entity.Customer;
import com.team3.coffee_order.domain.repository.CustomerRepository;
import com.team3.coffee_order.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
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
    @Transactional
    public void deleteCustomer(Long id){

        Customer customer = customerRepository.findById(id)
                .orElseThrow(()->new NotFoundException("해당하는 회원을 찾을 수 없습니다."));

        customerRepository.delete(customer);
    }
}
