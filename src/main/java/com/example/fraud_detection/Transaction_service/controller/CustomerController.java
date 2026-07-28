package com.example.fraud_detection.Transaction_service.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import com.example.fraud_detection.Transaction_service.entity.*;
import com.example.fraud_detection.Transaction_service.service.CustomerService;
import com.example.fraud_detection.Transaction_service.dtos.*;
@RestController
@RequestMapping("/customer")
public class CustomerController {

    protected CustomerService customerService;

    public CustomerController(CustomerService customerService){
        this.customerService = customerService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> CreateCustomer(@RequestBody CreatedCustomerRequestDto customRequestDto)throws Exception
    {

        customerService.CreateCustomer(customRequestDto);
        System.out.println(customRequestDto.getName() + " " + customRequestDto.getEmail());

        return ResponseEntity.status(HttpStatus.CREATED).body(customRequestDto);
        
    }

       @PostMapping("/bulk")
    public ResponseEntity<?> CreateCustomer(@RequestBody List<Customer> customers) throws Exception
    { 

        customerService.CreateCustomerBulk(customers);

        return ResponseEntity.status(HttpStatus.CREATED).body(customers);
        
    }


    @GetMapping("/{id}")
    public ResponseEntity<Customer> GetCustomerById(@PathVariable Long Id) throws Exception
    {

        return ResponseEntity.ok( customerService.getCustomerById(Id));
    }

    @GetMapping("/all")
    public  ResponseEntity<List<Customer>> getAllCustomers() throws Exception
    {

        return ResponseEntity.ok(customerService.getAllCustomers());
    }
    
}
