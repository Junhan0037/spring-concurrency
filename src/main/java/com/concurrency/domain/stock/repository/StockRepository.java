package com.concurrency.domain.stock.repository;

import com.concurrency.domain.stock.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<Stock, Long> {
}
