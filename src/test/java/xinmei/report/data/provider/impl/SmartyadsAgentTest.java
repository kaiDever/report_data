package xinmei.report.data.provider.impl;

import com.alibaba.fastjson.JSON;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import xinmei.report.data.TestBase;
import xinmei.report.data.dto.DataResponse;
import xinmei.report.data.provider.hasapi.impl.SmartyadsAgent;

class SmartyadsAgentTest extends TestBase {

    @Autowired
    SmartyadsAgent smartyadsAgent;

    @Test
    public void requestDataTest() {
        String startDate = "2023-06-19";
        String endDate = "2023-06-25";
        DataResponse dataResponse = smartyadsAgent.requestData(startDate, endDate);
        System.out.println(JSON.toJSONString(dataResponse));
    }
}