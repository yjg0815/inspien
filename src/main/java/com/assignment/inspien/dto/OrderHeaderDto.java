package com.assignment.inspien.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderHeaderDto {

    private String userId;
    private String name;
    private String address;
    private String status;
}