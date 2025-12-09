package com.teddybite.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.teddybite.entity.Employee;

@Repository
public interface EmployeeRepository extends MongoRepository<Employee, String>{
    
}
