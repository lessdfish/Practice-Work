package com.mall.inventory;

import com.mall.common.exception.BusinessException;
import com.mall.inventory.service.InventoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * ClassName:InventoryConcurrencyTest
 * Package:com.mall.inventory
 * Description:
 *
 * @Author:lyp
 * @Create:2026/8/29 - 22:33
 * @Version: v1.0
 *
 */
@SpringBootTest
public class InventoryConcurrencyTest {
    @Autowired
    private InventoryService inventoryService;


    @Test
    void concurrentDeductStock() throws InterruptedException{
        int threadCount = 20;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);

        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch endLatch = new CountDownLatch(threadCount);

        AtomicInteger success = new AtomicInteger();
        AtomicInteger failed = new AtomicInteger();

        for (int i = 0; i < threadCount; i++) {
            executor.submit(()->{
                try {
                    startLatch.await();
                    inventoryService.deductStock(3L,1);
                    success.incrementAndGet();
            }catch (BusinessException e){
                    failed.incrementAndGet();
                }catch (InterruptedException e){
                    Thread.currentThread().interrupt();
                }finally {
                    endLatch.countDown();
                }
            });
        }

        startLatch.countDown();
        endLatch.await();
        executor.shutdown();

        System.out.println("Success =" +success.get());
        System.out.println("Failed = " + failed.get());
    }
}
