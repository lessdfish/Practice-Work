package com.mall.seckill.service;

import com.mall.common.exception.BusinessException;
import com.mall.common.exception.ErrorCode;
import com.mall.seckill.domain.SeckillRequestRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * ClassName:ReconcileServiceImpl
 * Package:com.mall.seckill.service
 * Description:
 *
 * @Author:lyp
 * @Create:2026/9/1 - 23:48
 * @Version: v1.0
 *
 */
@Service
@RequiredArgsConstructor
public class ReconcileServiceImpl implements ReconcileService{
    private final SeckillResultService resultService;
    private final SeckillOrderService orderService;
    private final CompensationService compensationService;

    @Override
    public void reconcile(Long activityId,String requestId) {
        SeckillRequestRecord record = resultService.getRequestRecord(activityId,requestId);

        if (!"FAILED".equals(record.status())) {
            throw new BusinessException(ErrorCode.SECKILL_RECONCILE_NOT_ALLOWED);
        }

        Long orderId = orderService.findExistingOrderId(record.userId(), record.activityId());
        if (orderId!=null) {
            resultService.markSuccess(activityId,requestId,orderId);
            return;
        }

        compensationService.compensate(record.activityId(), record.userId());

        resultService.markFailedCompensated(activityId, requestId);
    }
}
