package com.xencio.marquez.pojo;

import com.alibaba.fastjson.JSONObject;
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
public class XencioApiPojo {
    private String uri;
    private JSONObject respData;
}
