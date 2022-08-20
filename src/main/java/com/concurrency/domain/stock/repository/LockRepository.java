package com.concurrency.domain.stock.repository;

import com.concurrency.domain.stock.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * 커넥션풀 부족을 일으킬 수 있으므로 실무에서는 데이터소스를 분리해야한다.
 * 분산락을 구현할 때 주로 사용
 * 타임아웃을 손쉽게 구현할 수 있다.
 * 세션관리 주의 필요
 */
public interface LockRepository extends JpaRepository<Stock, Long> {

    @Query(value = "select get_lock(:key, 1000)", nativeQuery = true)
    void getLock(String key);

    @Query(value = "select release_lock(:key)", nativeQuery = true)
    void releaseLock(String key);

}
