package com.xencio.marquez.service.impl;

import com.xencio.marquez.mapper.MarquezMapper;
import com.xencio.marquez.pojo.User;
import com.xencio.marquez.service.MarquezService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author songyr
 * @date 2022/06/22
 */
@Service
public class MarquezServiceImpl implements MarquezService {

    @Autowired
    private MarquezMapper marquezMapper;

    @Override
    public List<User> getUserList() {
        return marquezMapper.getUserList();
    }
}
