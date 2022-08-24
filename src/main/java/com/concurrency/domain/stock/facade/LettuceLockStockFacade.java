package com.concurrency.domain.stock.facade;

import com.concurrency.domain.stock.repository.RedisLockRepository;
import com.concurrency.domain.stock.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LettuceLockStockFacade {

    private final RedisLockRepository redisLockRepository;
    private final StockService stockService;

    public void decrease(Long key, Long quantuty) throws InterruptedException {
        while (redisLockRepository.lock(key)) {
            Thread.sleep(100);
        }

        try {
            stockService.decrease(key, quantuty);
        } finally {
            redisLockRepository.unlock(key);
        }
    }

}
