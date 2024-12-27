package com.carbonaro.ReactiveSimplifiedPicPay.repositories;

import com.carbonaro.ReactiveSimplifiedPicPay.domain.entities.Transaction;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
interface ITransactionRepository extends ReactiveMongoRepository<Transaction, String> {
}
