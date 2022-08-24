package com.concurrency.domain.stock.facade;

import static org.junit.jupiter.api.Assertions.*;

import com.concurrency.domain.stock.Stock;
import com.concurrency.domain.stock.repository.StockRepository;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class LettuceLockStockFacadeTest {

    @Autowired private LettuceLockStockFacade lettuceLockStockFacade;
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

    @DisplayName("동시 요청 테스트")
    @Test
    public void concurrency() throws InterruptedException {
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    lettuceLockStockFacade.decrease(1L, 1L);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        Stock stock = stockRepository.findById(1L).orElseThrow();

        // 100 - (1 * 100) = 0
        assertEquals(0L, stock.getQuantity());
    }

}
