package com.xencio.marquez.controller;

import com.xencio.marquez.common.Result;

import com.xencio.marquez.service.MarquezService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author songyr
 * @date 2022/06/22
 */
@RestController
@RequestMapping("/xencio")
public class MarquezController {

    @Autowired
    private MarquezService marquezService;

    @GetMapping("/dataset/create")
    public <T> Result<T> test() {
        return marquezService.createOneDataset();
    }
}
