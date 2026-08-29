package com.mall.common.transaction;

import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * ClassName:AfterCommitExecutor
 * Package:com.mall.common.transaction
 * Description:
 *
 * @Author:lyp
 * @Create:2026/8/29 - 16:51
 * @Version: v1.0
 *
 */
public final class AfterCommitExecutor {
    public AfterCommitExecutor() {
    }

    public static void execute(Runnable task){
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    task.run();
                }
            });
        }else {
            task.run();
        }
    }
}
