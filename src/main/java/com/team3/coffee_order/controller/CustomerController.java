package com.team3.coffee_order.controller;

import com.team3.coffee_order.domain.entity.Customer;
import com.team3.coffee_order.domain.repository.CustomerRepository;
import com.team3.coffee_order.exception.NotFoundException;
import com.team3.coffee_order.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    // TODO: create

    // TODO: read

    // TODO: update

    // TODO: delete
    @Operation(summary = "회원 삭제" , description = "id로 회원의 정보를 삭제한다")
    @ApiResponses({
            @ApiResponse(responseCode =  "204", description = "회원 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "회원 삭제 실패 - 회원이 존재하지 않음")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id){
        return customerService.deleteCustomer(id);
    }
}