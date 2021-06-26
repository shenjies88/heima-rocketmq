package com.shenjies88.practice.heimarocketmq;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author shenjies88
 * @since 2021/6/26-4:06 下午
 */
@AllArgsConstructor
@Data
public class OrderStep {

    private Long orderId;
    private String name;
}
