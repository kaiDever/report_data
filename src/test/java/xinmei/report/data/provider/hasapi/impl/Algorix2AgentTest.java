package xinmei.report.data.provider.hasapi.impl;

import com.alibaba.fastjson.JSON;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import xinmei.report.data.TestBase;
import xinmei.report.data.dto.DataResponse;

class Algorix2AgentTest extends TestBase {
    @Autowired
    Algorix2Agent algorix2Agent;

    @Test
    public void requesterDataTest(){
        String start = "2023-06-19";
        String end = "2023-06-23";
        DataResponse dataResponse = algorix2Agent.requestData(start, end);
        System.out.println(JSON.toJSONString(dataResponse));
    }
}