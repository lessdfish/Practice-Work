package com.mall.inventory.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mall.common.exception.BusinessException;
import com.mall.common.exception.ErrorCode;
import com.mall.inventory.domain.InventoryReservation;
import com.mall.inventory.domain.InventoryReservationStatus;
import com.mall.inventory.domain.ProductInventory;
import com.mall.inventory.mapper.InventoryReservationMapper;
import com.mall.inventory.mapper.ProductInventoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * ClassName:InventoryServiceImpl
 * Package:com.mall.inventory.service
 * Description:
 *
 * @Author:lyp
 * @Create:2026/8/29 - 22:10
 * @Version: v1.0
 *
 */
@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService{
    private final ProductInventoryMapper inventoryMapper;
    private final InventoryReservationMapper reservationMapper;

    @Override
    public ProductInventory getByProductId(Long productId) {
        ProductInventory inventory = inventoryMapper.selectOne(
                new LambdaQueryWrapper<ProductInventory>().eq(ProductInventory::getProductId,productId)
        );
        if (inventory == null) {
            throw new BusinessException(ErrorCode.INVENTORY_NOT_FOUND);
        }
        return inventory;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void reserveStock(Long orderId, Long productId, Integer quantity) {
        int updated = inventoryMapper.reserve(productId,quantity);
        if (updated!=1) {
            throw new BusinessException(ErrorCode.STOCK_NOT_ENOUGH);
        }
        InventoryReservation reservation = new InventoryReservation();

        reservation.setOrderId(orderId);
        reservation.setProductId(productId);
        reservation.setQuantity(quantity);
        reservation.setStatus(InventoryReservationStatus.LOCKED.getCode());

        reservationMapper.insert(reservation);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void confirmStock(Long orderId) {
        InventoryReservation reservation = getReservation(orderId);

        int updated = reservationMapper.transitionStatus(
                orderId,
                InventoryReservationStatus.LOCKED.getCode(),
                InventoryReservationStatus.CONFIRMED.getCode()
        );

        if (updated == 0) {
            reservation = getReservation(orderId);

            if (reservation.getStatus() == InventoryReservationStatus.CONFIRMED.getCode()) {
                return;
            }
            throw new BusinessException(ErrorCode.INVENTORY_RESERVATION_CONFLICT);
        }

        int inventoryUpdated = inventoryMapper.confirm(
                reservation.getProductId(),
                reservation.getQuantity()
        );

        if (inventoryUpdated!=1) {
            throw new BusinessException(ErrorCode.INVENTORY_RESERVATION_CONFLICT);
        }
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void releaseStock(Long orderId) {
        InventoryReservation reservation = getReservation(orderId);

        int updated = reservationMapper.transitionStatus(orderId,InventoryReservationStatus.LOCKED.getCode(),
                InventoryReservationStatus.RELEASED.getCode());
        if (updated == 0) {
            reservation = getReservation(orderId);

            if (reservation.getStatus() == InventoryReservationStatus.RELEASED.getCode()) {
                return;
            }
            throw new BusinessException(ErrorCode.INVENTORY_RESERVATION_CONFLICT);
        }
        int inventoryUpdated = inventoryMapper.release(
                reservation.getProductId(),
                reservation.getQuantity()
        );

        if (inventoryUpdated != 1) {
            throw new BusinessException(ErrorCode.INVENTORY_RESERVATION_CONFLICT);
        }
    }

    private InventoryReservation getReservation(Long orderId){
        InventoryReservation reservation = reservationMapper.selectOne(
                new LambdaQueryWrapper<InventoryReservation>()
                        .eq(InventoryReservation::getOrderId,
                                orderId)
        );

        if (reservation == null) {
            throw new BusinessException(ErrorCode.INVENTORY_RESERVATION_NOT_FOUND);
        }
        return reservation;
    }
}
