package com.assignment.inspien.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDto {

    private String userId;
    private String itemId;
    private String itemName;
    private String price;
}