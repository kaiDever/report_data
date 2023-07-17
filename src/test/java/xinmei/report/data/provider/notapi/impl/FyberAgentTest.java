package xinmei.report.data.provider.notapi.impl;

import com.alibaba.fastjson.JSON;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import xinmei.report.data.TestBase;
import xinmei.report.data.dto.DataResponse;

class FyberAgentTest extends TestBase {
    @Autowired
    FyberAgent fyberAgent;

    @Test
    public void requestDataTest(){
        String startDate = "2023-07-01";
        String endDate = "2023-07-05";
        DataResponse dataResponse = fyberAgent.requestData(startDate, endDate);
        System.out.println(JSON.toJSONString(dataResponse));
    }

}