package com.assignment.inspien.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderGroupDto {

    private OrderHeaderDto header;
    private List<OrderItemDto> items;
}