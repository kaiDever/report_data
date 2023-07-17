package xinmei.report.data.provider.impl;

import com.alibaba.fastjson.JSON;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import xinmei.report.data.TestBase;
import xinmei.report.data.dto.DataResponse;
import xinmei.report.data.provider.druid.DruidAgent;

import java.util.Map;

class DruidAgentTest extends TestBase {
    @Autowired
    DruidAgent innerAgent;

    @Test
    public void reportDataTest(){
        String startDate = "2023-06-01";
        String endDate = "2023-06-02";
        Map<String, DataResponse> dataByDruid = innerAgent.getDataByDruid(startDate, endDate);
        System.out.println(JSON.toJSONString(dataByDruid));
    }
}