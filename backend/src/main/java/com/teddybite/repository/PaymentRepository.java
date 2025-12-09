package com.teddybite.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.teddybite.entity.Payment;

@Repository
public interface PaymentRepository extends MongoRepository<Payment, String> {
}
