package com.xencio.marquez.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xencio.marquez.common.Result;
import com.xencio.marquez.common.XencioHttpUtil;
import com.xencio.marquez.entity.Shareholder;
import com.xencio.marquez.mapper.MarquezMapper;
import com.xencio.marquez.pojo.EnterprisePojo;
import com.xencio.marquez.pojo.ShareholderPojo;
import com.xencio.marquez.pojo.XencioApiPojo;
import com.xencio.marquez.service.MarquezGraphService;
import com.xencio.marquez.service.MarquezService;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @author songyr
 * @date 2022/06/22
 */
@Service
public class MarquezServiceImpl implements MarquezService {

    @Autowired
    private MarquezMapper marquezMapper;

    @Autowired
    private MarquezGraphService marquezGraphService;

    @Override
    public Result createOneDataset() {
        Map<String, XencioApiPojo> xencioApiData = new HashMap<>();
        Map<String, String> xencioSqlData = new HashMap<>();
        Map<String, String> xencioBigSqlData = new HashMap<>();
        Map<String, List<Map<String, String>>> xencioTableData = new HashMap<>();
        try {
            // 爬取天眼查数据
            xencioApiData = getXencioApiData();

            // 存到数据库
            xencioSqlData = insertIntoDataBase(xencioApiData);

            // 插入到新表
            xencioBigSqlData = insertIntoBigDataBase();

            // 表字段
            xencioTableData = selectTableField();

            // 画图
            marquezGraphService.getGraph(xencioApiData,xencioSqlData,xencioBigSqlData,xencioTableData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.success(xencioApiData);
    }

    private void getGraph(Map<String, XencioApiPojo> xencioApiData, Map<String, String> xencioSqlData, Map<String, String> xencioBigSqlData, Map<String, List<Map<String, String>>> xencioTableData) {

    }

    private Map<String, List<Map<String, String>>> selectTableField() {
        List<Map<String, String>> tableEnterpriseField =  marquezMapper.selectEnterprise();
        List<Map<String, String>> tableShareholderField =  marquezMapper.selectShareholder();
        List<Map<String, String>> tableEnterpriseShareholderField =  marquezMapper.selectEnterpriseShareholder();

        Map<String, List<Map<String, String>>> result = new HashMap<>();
        result.put("enterprise",tableEnterpriseField);
        result.put("shareholder",tableShareholderField);
        result.put("enterpriseShareholder",tableEnterpriseShareholderField);
        return result;
    }

    private Map<String, String> insertIntoBigDataBase() {
        marquezMapper.insertEnterpriseShareholder();
        String sql = "INSERT INTO tb_enterprise_shareholder (`enterprise_name`,`shareholder_name`,`shareholder_percent`)\n"
            + " (\n" + "SELECT\n" + "\tt1.enterprise_name enterprise_name,\n"
            + "\tt2.shareholder_name shareholder_name,\n" + "\tt2.shareholder_percent shareholder_percent\n"
            + "FROM tb_enterprise t1\n"
            + "LEFT JOIN tb_shareholder t2 ON t1.enterprise_name = t2.shareholder_enterprise;\n" + ");";

        Map<String, String> result = new HashMap<>();
        result.put("enterpriseShareholderInfo",sql);
        return result;
    }

    /**
     * 将天眼查数据存到数据库
     * @param xencioApiData 天眼查数据
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Map<String, String> insertIntoDataBase(Map<String, XencioApiPojo> xencioApiData) {

        // 企业
        JSONObject enterpriseInfo = xencioApiData.get("enterpriseInfo").getRespData().getJSONObject("data");
        EnterprisePojo enterprise = new EnterprisePojo();
        enterprise.setEnterpriseName(enterpriseInfo.getString("name"));
        enterprise.setEnterpriseLocation(enterpriseInfo.getString("regLocation"));
        enterprise.setEmail(enterpriseInfo.getString("email"));
        marquezMapper.insertEnterpriseAll(Arrays.asList(enterprise));
        String sql1 = "insert into tb_enterprise (`enterprise_name`,`enterprise_location`,`email`) values ( ?,?,? )";
        sql1 = sql1.replaceFirst("[?]+","\"" + enterprise.getEnterpriseName() + "\"");
        sql1 = sql1.replaceFirst("[?]+","\"" + enterprise.getEnterpriseLocation() + "\"");
        sql1 = sql1.replaceFirst("[?]+","\"" + enterprise.getEmail() + "\"");

        // 股东
        List<ShareholderPojo> shareholderList = new ArrayList<>();
        JSONObject shareholdersInfo = xencioApiData.get("shareholdersInfo").getRespData().getJSONObject("data");
        Set<Map.Entry<String, Object>> entrySet = shareholdersInfo.entrySet();
        for (Map.Entry<String, Object> entry : entrySet) {
            String name = entry.getKey();

            List<Shareholder> shareholders = shareholdersInfo.getJSONArray(name).toJavaList(Shareholder.class);
            for (Shareholder shareholder1 : shareholders) {
                ShareholderPojo shareholder = new ShareholderPojo();
                shareholder.setShareholderName(name);
                shareholder.setShareholderEnterprise(shareholder1.getName());
                shareholder.setShareholderPercent(shareholder1.getPercent());

                shareholderList.add(shareholder);
            }
        }
        marquezMapper.insertShardholderAll(shareholderList);
        String sql2 = "insert into tb_shareholder (`shareholder_name`,`shareholder_enterprise`,`shareholder_percent`) values (\"何川\",\"上海京久实业有限公司\".\"0.3\")";

        Map<String, String> result = new HashMap<>();;
        result.put("enterpriseInfo",sql1);
        result.put("shareholdersInfo",sql2);
        return result;
    }

    /**
     * 请求天眼查接口
     * @return
     */
    private Map<String, XencioApiPojo> getXencioApiData() throws Exception{
        // 查询企业信息
        String uri1 = "https://api.xencio.com/rest/enterprise/?name=见知数据科技(上海)有限公司";
        JSONObject enterpriseInfo = getXencioEnterpriseInfo(uri1);

        // 查询股东占比
        String uri2 = "https://api.xencio.com/rest/enterprise/holder/relation";
        JSONObject shareholdersInfo = getXencioShareholdersInfo(uri2);

        Map<String, XencioApiPojo> result = new HashMap<>();
        result.put("enterpriseInfo",new XencioApiPojo(uri1,enterpriseInfo));
        result.put("shareholdersInfo",new XencioApiPojo(uri2,shareholdersInfo));

        return result;
    }

    private JSONObject getXencioShareholdersInfo(String uri) throws Exception {
        HttpClient httpClient = XencioHttpUtil.createClient();


        HttpPost httpPost = new HttpPost(uri);
        JSONObject reqJsonObject = new JSONObject();
        reqJsonObject.put("name", Arrays.asList("何川","王晓青").toArray());
        StringEntity requestEntity = new StringEntity(reqJsonObject.toString(), StandardCharsets.UTF_8);
        requestEntity.setContentType("application/json");
        httpPost.setEntity(requestEntity);
        HttpResponse httpResponse = httpClient.execute(httpPost);

        String shareholdersInfoStr = EntityUtils.toString(httpResponse.getEntity(),StandardCharsets.UTF_8);
        return JSON.parseObject(shareholdersInfoStr);
    }

    private JSONObject getXencioEnterpriseInfo(String uri) throws Exception{
        HttpClient httpClient = XencioHttpUtil.createClient();


        HttpGet httpGet = new HttpGet(uri);
        HttpResponse httpResponse = httpClient.execute(httpGet);

        String enterpriseInfoStr = EntityUtils.toString(httpResponse.getEntity());
        return JSON.parseObject(enterpriseInfoStr);
    }
}
