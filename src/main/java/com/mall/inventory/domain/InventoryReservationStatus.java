package com.mall.inventory.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * ClassName:InventoryReservationStatus
 * Package:com.mall.inventory.domain
 * Description:
 *
 * @Author:lyp
 * @Create:2026/9/4 - 16:09
 * @Version: v1.0
 *
 */
@Getter
@RequiredArgsConstructor
public enum InventoryReservationStatus {
    LOCKED(0,"已预占"),
    CONFIRMED(1,"已确认"),
    RELEASED(2,"已释放");

    private final int code;
    private final String description;
}
