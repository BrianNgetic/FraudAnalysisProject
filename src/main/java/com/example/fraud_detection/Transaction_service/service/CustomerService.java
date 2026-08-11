package com.example.fraud_detection.Transaction_service.service;

import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;



import com.example.fraud_detection.Transaction_service.entity.Customer;
import com.example.fraud_detection.Transaction_service.repository.CustomerRepository;
import com.example.fraud_detection.Transaction_service.dtos.*;
import com.example.fraud_detection.Transaction_service.mapper.*;
import com.example.fraud_detection.Transaction_service.exception.CustomerAlreadyExistsException;
import com.example.fraud_detection.Transaction_service.exception.CustomerNotFoundException;


import java.util.List;

@Service
public class CustomerService {

    protected CustomerRepository customerRepository;
    private TransactionMapper transactionMapper;

    public CustomerService(CustomerRepository customerRepository,
                            TransactionMapper transactionMapper){
        this.customerRepository = customerRepository;
        this.transactionMapper = transactionMapper;
    }


    @Transactional
    public void CreateCustomer(CreatedCustomerRequestDto requestDto) throws Exception{
        
    
        if(customerRepository.existsByemail(requestDto.getEmail())){
            throw new CustomerAlreadyExistsException();
        }
        customerRepository.save(transactionMapper.fromUserCreatedCustomerToCustomer(requestDto));
    }
    

    //not used for now
    public void CreateCustomerBulk(List<Customer> customers) throws Exception{
        
    
        for(Customer custo: customers){
        if(customerRepository.existsById(custo.getId())){
            throw new CustomerAlreadyExistsException("Could not save customer " + custo.getName() + "already exists");
        }
        customerRepository.save(custo);
    }
    }


    public Customer getCustomerById(Long id) throws Exception{
        if(!customerRepository.existsById(id)){
            throw new CustomerNotFoundException();
        }
        return customerRepository.getReferenceById(id);
    }

    @Transactional
    public List<Customer> getAllCustomers() throws Exception{

        List<Customer> allCustomers = customerRepository.findAll();

        if(allCustomers.isEmpty()) throw new CustomerNotFoundException("No Customers Found");
        return allCustomers;
       
    }

    
}
