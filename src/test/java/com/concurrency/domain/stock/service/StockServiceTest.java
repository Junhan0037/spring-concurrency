package com.concurrency.domain.stock.service;

import com.concurrency.domain.stock.Stock;
import com.concurrency.domain.stock.repository.StockRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class StockServiceTest {

    @Autowired private StockService stockService;
    @Autowired private StockRepository stockRepository;

    @BeforeEach
    public void before() {
        Stock stock = Stock.builder().productId(1L).quantity(100L).build();
        stockRepository.saveAndFlush(stock);
    }

    @AfterEach
    public void after() {
        stockRepository.deleteAll();
    }

    @Test
    public void stock_decrease() {
        stockService.decrease(1L, 1L);
        Stock stock = stockRepository.findById(1L).orElseThrow();
        assertEquals(99, stock.getQuantity());
    }

}
