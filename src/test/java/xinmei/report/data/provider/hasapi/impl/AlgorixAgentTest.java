package xinmei.report.data.provider.hasapi.impl;

import com.alibaba.fastjson.JSON;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import xinmei.report.data.TestBase;
import xinmei.report.data.dto.DataResponse;

class AlgorixAgentTest extends TestBase {

    @Autowired
    AlgorixAgent algorixAgent;

    @Test
    public void requestDataTest(){
        String start = "2023-06-23";
        String end = "2023-06-25";
        DataResponse dataResponse = algorixAgent.requestData(start, end);
        System.out.println(JSON.toJSONString(dataResponse));
    }

}