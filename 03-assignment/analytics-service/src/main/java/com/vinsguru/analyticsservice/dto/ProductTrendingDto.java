package com.vinsguru.analyticsservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductTrendingDto {

    private Integer productId;
    private Long viewCount;

}
