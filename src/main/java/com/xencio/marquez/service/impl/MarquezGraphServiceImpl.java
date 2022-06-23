package com.xencio.marquez.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.xencio.marquez.common.XencioHttpUtil;
import com.xencio.marquez.dto.lineage.*;
import com.xencio.marquez.pojo.XencioApiPojo;
import com.xencio.marquez.service.MarquezGraphService;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author songyr
 * @date 2022/06/23
 */
@Service
public class MarquezGraphServiceImpl implements MarquezGraphService {

    /**
     *
     * @param xencioApiData 爬取天眼查数据
     * @param xencioSqlData 存到数据库
     * @param xencioBigSqlData 插入到新表
     * @param xencioTableData 表字段
     */
    @Override
    public void getGraph(Map<String, XencioApiPojo> xencioApiData, Map<String, String> xencioSqlData,
            Map<String, String> xencioBigSqlData, Map<String, List<Map<String, String>>> xencioTableData) throws Exception {
        stepFirst("COMPLETE",xencioApiData, xencioSqlData, xencioTableData);
        stepSecond("COMPLETE",xencioApiData, xencioSqlData, xencioTableData);
        stepThird("COMPLETE",xencioApiData, xencioSqlData, xencioBigSqlData, xencioTableData);
    }

    @Override
    public void stepThird(String eventType,Map<String, XencioApiPojo> xencioApiData, Map<String, String> xencioSqlData,
        Map<String, String> xencioBigSqlData, Map<String, List<Map<String, String>>> xencioTableData)
        throws IOException {
        // 封装json
        LineageDto lineageDto = getJson3(eventType,xencioApiData, xencioSqlData, xencioBigSqlData, xencioTableData);
        HttpClient httpClient = XencioHttpUtil.createClient();

        HttpPost httpPost = new HttpPost("http://39.99.225.122:3000/api/v1/lineage");
        StringEntity requestEntity = new StringEntity(JSONObject.toJSONString(lineageDto), StandardCharsets.UTF_8);
        requestEntity.setContentType("application/json");
        httpPost.setEntity(requestEntity);
        HttpResponse httpResponse = httpClient.execute(httpPost);
        String s = EntityUtils.toString(httpResponse.getEntity(), StandardCharsets.UTF_8);

        System.out.println(s);
    }

    private LineageDto getJson3(String eventType, Map<String, XencioApiPojo> xencioApiData, Map<String, String> xencioSqlData,
        Map<String, String> xencioBigSqlData, Map<String, List<Map<String, String>>> xencioTableData) {
        LineageDto lineageDto = new LineageDto();
        lineageDto.setEventType(eventType);
        lineageDto.setEventTime(new Date());

        // run
        lineageDto.setRun(new Run());

        // job
        Job job = new Job();
        job.setName("songyr_test43");
        job.setNamespace("songyr_test4");
        lineageDto.setJob(job);

        // input output
        setInputAndOutput3(lineageDto, xencioSqlData, xencioBigSqlData, xencioTableData);

        return lineageDto;
    }

    private void setInputAndOutput3(LineageDto lineageDto, Map<String, String> xencioBigSqlData,
        Map<String, String> xencioSqlData, Map<String, List<Map<String, String>>> xencioTableData) {
        setInput3(lineageDto, xencioTableData, xencioSqlData);
        setOutput3(lineageDto, xencioBigSqlData, xencioTableData);
    }

    private void setOutput3(LineageDto lineageDto, Map<String, String> xencioBigSqlData,
        Map<String, List<Map<String, String>>> xencioTableData) {
        // output
        List<Output> outputs = new ArrayList<>();

        // shareholdersInfo
        String sql = xencioBigSqlData.get("enterpriseShareholderInfo");
        List<Map<String, String>> tbEnterpriseShareholderFields = xencioTableData.get("enterpriseShareholder");
        Output output1 = new Output();
        output1.setNamespace("songyr_test4");
        output1.setName("tb_enterprise_shareholder");

        List<Field> fields1 = new ArrayList<>();
        for (Map<String, String> tbEnterpriseShareholderField : tbEnterpriseShareholderFields) {
            String name = tbEnterpriseShareholderField.get("Field");
            String type = tbEnterpriseShareholderField.get("Type");

            Field field = new Field();
            field.setName(name);
            field.setType(type);
            fields1.add(field);
        }
        Schema schema1 = new Schema();
        schema1.setFields(fields1);
        Facets facets1 = new Facets();
        facets1.setSchema(schema1);
        output1.setFacets(facets1);

        outputs.add(output1);

        lineageDto.setOutputs(outputs);
    }

    private void setInput3(LineageDto lineageDto, Map<String, List<Map<String, String>>> xencioTableData,
        Map<String, String> xencioSqlData) {
        // output
        List<Input> inputs = new ArrayList<>();

        // shareholdersInfo
        String sql2 = xencioSqlData.get("shareholdersInfo");
        List<Map<String, String>> tbShareholderFields = xencioTableData.get("shareholder");
        Input input1 = new Input();
        input1.setNamespace("songyr_test4");
        input1.setName("tb_shareholder");

        List<Field> fields1 = new ArrayList<>();
        for (Map<String, String> tbShareholderField : tbShareholderFields) {
            String name = tbShareholderField.get("Field");
            String type = tbShareholderField.get("Type");

            Field field = new Field();
            field.setName(name);
            field.setType(type);
            fields1.add(field);
        }
        Schema schema1 = new Schema();
        schema1.setFields(fields1);
        Facets facets1 = new Facets();
        facets1.setSchema(schema1);
        input1.setFacets(facets1);

        inputs.add(input1);

        // enterpriseInfo
        String sql1 = xencioSqlData.get("enterpriseInfo");
        List<Map<String, String>> tbEnterpriseFields = xencioTableData.get("enterprise");
        Input input2 = new Input();
        input2.setNamespace("songyr_test4");
        input2.setName("tb_enterprise");

        List<Field> fields = new ArrayList<>();
        for (Map<String, String> tbEnterpriseField : tbEnterpriseFields) {
            String name = tbEnterpriseField.get("Field");
            String type = tbEnterpriseField.get("Type");

            Field field = new Field();
            field.setName(name);
            field.setType(type);
            fields.add(field);
        }
        Schema schema = new Schema();
        schema.setFields(fields);
        Facets facets = new Facets();
        facets.setSchema(schema);
        input2.setFacets(facets);

        inputs.add(input2);

        lineageDto.setInputs(inputs);
    }

    @Override
    public void stepSecond(String eventType,Map<String, XencioApiPojo> xencioApiData, Map<String, String> xencioSqlData, Map<String, List<Map<String, String>>> xencioTableData)
        throws IOException {
        // 封装json
        LineageDto lineageDto = getJson2(eventType,xencioApiData, xencioSqlData, xencioTableData);

        HttpClient httpClient = XencioHttpUtil.createClient();

        HttpPost httpPost = new HttpPost("http://39.99.225.122:3000/api/v1/lineage");
        StringEntity requestEntity = new StringEntity(JSONObject.toJSONString(lineageDto), StandardCharsets.UTF_8);
        requestEntity.setContentType("application/json");
        httpPost.setEntity(requestEntity);
        HttpResponse httpResponse = httpClient.execute(httpPost);
        String s = EntityUtils.toString(httpResponse.getEntity(), StandardCharsets.UTF_8);

        System.out.println(s);
    }

    private LineageDto getJson2(String eventType, Map<String, XencioApiPojo> xencioApiData, Map<String, String> xencioSqlData, Map<String, List<Map<String, String>>> xencioTableData) {
        LineageDto lineageDto = new LineageDto();
        lineageDto.setEventType(eventType);
        lineageDto.setEventTime(new Date());

        // run
        lineageDto.setRun(new Run());

        // job
        Job job = new Job();
        job.setName("songyr_test42");
        job.setNamespace("songyr_test4");
        lineageDto.setJob(job);

        // input output
        setInputAndOutput2(lineageDto, xencioApiData, xencioSqlData, xencioTableData);

        return lineageDto;
    }

    private void setInputAndOutput2(LineageDto lineageDto, Map<String, XencioApiPojo> xencioApiData,
        Map<String, String> xencioSqlData, Map<String, List<Map<String, String>>> xencioTableData) {
        setInput2(lineageDto, xencioApiData);
        setOutput2(lineageDto, xencioSqlData, xencioTableData);
    }

    private void setOutput2(LineageDto lineageDto, Map<String, String> xencioSqlData,
        Map<String, List<Map<String, String>>> xencioTableData) {
        // output
        List<Output> outputs = new ArrayList<>();

        // shareholdersInfo
        String sql2 = xencioSqlData.get("shareholdersInfo");
        List<Map<String, String>> tbShareholderFields = xencioTableData.get("shareholder");
        Output output1 = new Output();
        output1.setNamespace("songyr_test4");
        output1.setName("tb_shareholder");

        List<Field> fields1 = new ArrayList<>();
        for (Map<String, String> tbShareholderField : tbShareholderFields) {
            String name = tbShareholderField.get("Field");
            String type = tbShareholderField.get("Type");

            Field field = new Field();
            field.setName(name);
            field.setType(type);
            fields1.add(field);
        }
        Schema schema1 = new Schema();
        schema1.setFields(fields1);
        Facets facets1 = new Facets();
        facets1.setSchema(schema1);
        output1.setFacets(facets1);

        outputs.add(output1);

        lineageDto.setOutputs(outputs);
    }

    private void setInput2(LineageDto lineageDto, Map<String, XencioApiPojo> xencioApiData) {
        // input
        List<Input> inputs = new ArrayList<>();
        // shareholdersInfo
        XencioApiPojo shareholdersInfo = xencioApiData.get("shareholdersInfo");
        String uri2 = shareholdersInfo.getUri();
        Input input2 = new Input();
        input2.setName(uri2);
        input2.setNamespace("songyr_test4");
        inputs.add(input2);
        lineageDto.setInputs(inputs);
    }

    @Override
    public void stepFirst(String eventType, Map<String, XencioApiPojo> xencioApiData, Map<String, String> xencioSqlData, Map<String, List<Map<String, String>>> xencioTableData)
        throws IOException {
        // 封装json
        LineageDto lineageDto = getJson1(eventType,xencioApiData, xencioSqlData, xencioTableData);

        HttpClient httpClient = XencioHttpUtil.createClient();

        HttpPost httpPost = new HttpPost("http://39.99.225.122:3000/api/v1/lineage");
        StringEntity requestEntity = new StringEntity(JSONObject.toJSONString(lineageDto), StandardCharsets.UTF_8);
        requestEntity.setContentType("application/json");
        httpPost.setEntity(requestEntity);
        HttpResponse httpResponse = httpClient.execute(httpPost);
        String s = EntityUtils.toString(httpResponse.getEntity(), StandardCharsets.UTF_8);

        System.out.println(s);
    }

    private LineageDto getJson1(String eventType, Map<String, XencioApiPojo> xencioApiData, Map<String, String> xencioSqlData, Map<String, List<Map<String, String>>> xencioTableData) {
        LineageDto lineageDto = new LineageDto();
        lineageDto.setEventType(eventType);
        lineageDto.setEventTime(new Date());

        // run
        lineageDto.setRun(new Run());

        // job
        Job job = new Job();
        job.setName("songyr_test41");
        job.setNamespace("songyr_test4");
        lineageDto.setJob(job);

        // input output
        setInputAndOutput1(lineageDto,xencioApiData,xencioSqlData,xencioTableData);

        return lineageDto;
    }

    private void setInputAndOutput1(LineageDto lineageDto, Map<String, XencioApiPojo> xencioApiData,
        Map<String, String> xencioSqlData, Map<String, List<Map<String, String>>> xencioTableData) {
        setInput1(lineageDto, xencioApiData);
        setOutput1(lineageDto, xencioSqlData,xencioTableData);
    }

    private void setOutput1(LineageDto lineageDto, Map<String, String> xencioSqlData,
        Map<String, List<Map<String, String>>> xencioTableData) {
        // output
        List<Output> outputs = new ArrayList<>();

        // enterpriseInfo
        String sql1 = xencioSqlData.get("enterpriseInfo");
        List<Map<String, String>> tbEnterpriseFields = xencioTableData.get("enterprise");
        Output output = new Output();
        output.setNamespace("songyr_test4");
        output.setName("tb_enterprise");

        List<Field> fields = new ArrayList<>();
        for (Map<String, String> tbEnterpriseField : tbEnterpriseFields) {
            String name = tbEnterpriseField.get("Field");
            String type = tbEnterpriseField.get("Type");

            Field field = new Field();
            field.setName(name);
            field.setType(type);
            fields.add(field);
        }
        Schema schema = new Schema();
        schema.setFields(fields);
        Facets facets = new Facets();
        facets.setSchema(schema);
        output.setFacets(facets);

        outputs.add(output);

        lineageDto.setOutputs(outputs);
    }

    private void setInput1(LineageDto lineageDto, Map<String, XencioApiPojo> xencioApiData) {
        // input
        List<Input> inputs = new ArrayList<>();

        // enterpriseInfo
        XencioApiPojo enterpriseInfo = xencioApiData.get("enterpriseInfo");
        String uri1 = enterpriseInfo.getUri();
        Input input1 = new Input();
        input1.setName(uri1);
        input1.setNamespace("songyr_test4");
        inputs.add(input1);
        
        lineageDto.setInputs(inputs);
    }

}
