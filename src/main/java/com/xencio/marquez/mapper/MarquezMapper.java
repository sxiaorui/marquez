package com.xencio.marquez.mapper;

import com.xencio.marquez.pojo.EnterprisePojo;
import com.xencio.marquez.pojo.ShareholderPojo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author songyr
 * @date 2022/06/22
 */
@Mapper
public interface MarquezMapper {
    void insertEnterpriseAll(@Param("enterpriseList") List<EnterprisePojo> enterpriseList);

    void insertShardholderAll(@Param("shareholderList") List<ShareholderPojo> shareholderList);
}
