package com.carbonaro.ReactiveSimplifiedPicPay.repositories;

import com.carbonaro.ReactiveSimplifiedPicPay.domain.entities.Transaction;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends ReactiveMongoRepository<Transaction, String> {
}
