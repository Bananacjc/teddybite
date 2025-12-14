package com.teddybite.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.teddybite.entity.Employee;

@Repository
public interface EmployeeRepository extends MongoRepository<Employee, String> {
    boolean existsByEmail(String email);

    boolean existsByContactNo(String contactNo);

    boolean existsByEmailAndEmployeeIDNot(String email, String employeeID);

    boolean existsByContactNoAndEmployeeIDNot(String contactNo, String employeeID);

    java.util.Optional<Employee> findByEmail(String email);
}
