package xinmei.report.data.provider.impl;

import com.alibaba.fastjson.JSON;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import xinmei.report.data.TestBase;
import xinmei.report.data.dto.DataResponse;
import xinmei.report.data.provider.hasapi.impl.CriteoAgent;

class CriteoAgentTest extends TestBase {

    @Autowired
    CriteoAgent criteoAgent;

    @Test
    public void requesterDataTest() {
        String startDate = "2023-07-07";
        String endDate = "2023-07-13";
        DataResponse dataResponse = criteoAgent.requestData(startDate, endDate);
        System.out.println(JSON.toJSONString(dataResponse));
    }

}