# 재고시스템으로 알아보는 동시성이슈 해결방법

- 동시성 이슈가 무엇인지
- 동시성 이슈를 처리하는 방법들

간단한 재고 시스템을 직접 만들어보면서 동시성 이슈에 대해 알아보고, 문제를 해결하는 방법에 대해 알아봅니다.

## 목차

1. Race Condition  
   둘 이상의 스레드가 공유 데이터에 엑세스할 수 있고 동시에 변경하려고 할 때 발생하는 문제  
   하나의 스레드만 데이터에 엑세스 할 수 있도록 한다. -> `synchronized` 사용


2. Database
   - Pessimistic Lock (exclusive Lock)  
      실제로 데이터에 Lock을 걸어서 정합성을 맞추는 방법입니다.  
      Lock을 걸게되면 다른 트랜젝션에서는 lock이 해제되기전에는 데이터를 가져갈 수 없게됩니다.  
      데드락이 걸릴 수 있기 때문에 주의하여 사용해야 합니다.
   - Optimistic Lock  
      실제로 Lock을 이용하지 않고 version을 이용함으로써 정합성을 맞추는 방법입니다.  
      먼저 데이터를 읽은 후에 update를 수행할 때 현재 내가 읽는 버전이 맞는지 확인하여 업데이트 합니다.  
      내가 읽는 버전에서 수정사항이 생겼을 경우에는 application에서 다시 읽은 후에 작업을 수행해야 합니다.
   - Named Lock  
      이름을 가진 metadata locking 입니다.  
      이름을 가진 lock을 획득한 후 해제될때까지 다른 세션이 해당 lock을 획득할 수 없도록 합니다.  
      주의할점으로는 트랜젝션이 종료될 때 lock이 자동으로 해제되지 않습니다.  
      별도의 명령어로 해제를 수행하거나 선점시간이 끝나야 해제됩니다.


3. Redis Distributed Lock  
   Redis 의 기능을 활용해 동시성을 제어해 봅니다.
   - Lettuce  
      `setnx` 명령어를 활용하여 분산락 구현  
      `spin lock` 방식 (스레드가 lock 획득을 반복해서 시도)  
      구현이 간단하다.  
      spring data redis 를 사용하면 lettuce 가 기본이기 때문에 라이브러리를 사용하지 않아도 된다.  
      spin lock 방식이기 때문에 동시에 많은 스레드가 lock 획득 대기 상태라면 redis 에 부하가 갈 수 있다.
   - Redisson  
      `pub-sub` 기반으로 Lock 구현 제공 (채널에 lock 해제를 알리고 대기 스레드에게 시도 요청)  
      lock 획득 재시도를 기본으로 제공한다.  
      pub-sub 방식이기 때문에 lettuce 와 비교했을 때 redis 부하가 덜 일어난다.  
      별도의 라이브러리를 사용해야한다.


## 실무에서는?

- 재시도가 필요하지 않은 lock => lettuce
- 재시도가 필요한 경우 => redisson


## 참고

- https://dev.mysql.com/doc/refman/8.0/en/glossary.html#glos_exclusive_lock
- https://dev.mysql.com/doc/refman/8.0/en/innodb-locking.html
- https://dev.mysql.com/doc/refman/8.0/en/locking-functions.html
