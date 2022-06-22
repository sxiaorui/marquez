package com.xencio.marquez.mapper;

import com.xencio.marquez.pojo.EnterprisePojo;
import com.xencio.marquez.pojo.ShareholderPojo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author songyr
 * @date 2022/06/22
 */
@Mapper
public interface MarquezMapper {
    void insertEnterpriseAll(@Param("enterpriseList") List<EnterprisePojo> enterpriseList);

    void insertShardholderAll(@Param("shareholderList") List<ShareholderPojo> shareholderList);

    void insertEnterpriseShareholder();

    List<Map<String, String>> selectEnterprise();

    List<Map<String, String>> selectShareholder();

    List<Map<String, String>> selectEnterpriseShareholder();
}
