package com.xencio.marquez.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author songyr
 * @date 2022/06/22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShareholderPojo {
    private Long id;
    private String shareholderName;
    private String shareholderEnterprise;
    private Double shareholderPercent;
}
