package com.xencio.marquez.mapper;

import com.xencio.marquez.pojo.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author songyr
 * @date 2022/06/22
 */
@Mapper
public interface MarquezMapper {

    List<User> getUserList();
}
