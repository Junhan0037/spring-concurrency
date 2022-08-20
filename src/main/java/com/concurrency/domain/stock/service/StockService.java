package com.concurrency.domain.stock.service;

import com.concurrency.domain.stock.Stock;
import com.concurrency.domain.stock.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StockService {

    private final StockRepository stockRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW) // 부모의 트랜젝션과 별도로 새로운 트랜젝션 생성
    public synchronized void decrease(Long id, Long quantity) {
        // get stock
        Stock stock = stockRepository.findById(id).orElseThrow();

        // 재고 감소
        stock.decrease(quantity);

        // 저장
        stockRepository.saveAndFlush(stock);
    }

}
