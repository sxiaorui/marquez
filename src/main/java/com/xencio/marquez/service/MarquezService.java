package com.xencio.marquez.service;

import com.xencio.marquez.common.Result;

/**
 * @author songyr
 * @date 2022/06/22
 */
public interface MarquezService {

    /**
     * 创建dataset
     * @param <T>
     * @return
     */
    <T> Result<T> createOneDataset();

}
