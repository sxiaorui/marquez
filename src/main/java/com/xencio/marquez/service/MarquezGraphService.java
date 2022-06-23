package com.xencio.marquez.service;

import com.xencio.marquez.pojo.XencioApiPojo;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author songyr
 * @date 2022/06/23
 */
public interface MarquezGraphService {

    void getGraph(Map<String, XencioApiPojo> xencioApiData, Map<String, String> xencioSqlData, Map<String, String> xencioBigSqlData, Map<String, List<Map<String, String>>> xencioTableData) throws Exception;

    void stepThird(String eventType, Map<String, XencioApiPojo> xencioApiData, Map<String, String> xencioSqlData,
        Map<String, String> xencioBigSqlData, Map<String, List<Map<String, String>>> xencioTableData)
        throws IOException;

    void stepSecond(String eventType,Map<String, XencioApiPojo> xencioApiData, Map<String, String> xencioSqlData, Map<String, List<Map<String, String>>> xencioTableData)
        throws IOException;

    void stepFirst(String eventType, Map<String, XencioApiPojo> xencioApiData, Map<String, String> xencioSqlData, Map<String, List<Map<String, String>>> xencioTableData)
        throws IOException;
}
