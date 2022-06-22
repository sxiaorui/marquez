package com.xencio.marquez.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author songyr
 * @date 2022/06/22
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Shareholder {
    private String name;
    private Double percent;
}
