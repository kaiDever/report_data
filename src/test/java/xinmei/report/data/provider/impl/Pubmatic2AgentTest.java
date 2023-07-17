package xinmei.report.data.provider.impl;

import com.alibaba.fastjson.JSON;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import xinmei.report.data.TestBase;
import xinmei.report.data.dto.DataResponse;
import xinmei.report.data.provider.hasapi.impl.Pubmatic2Agent;

class Pubmatic2AgentTest extends TestBase {
    @Autowired
    Pubmatic2Agent pubmatic2Agent;

    @Test
    public void testSave() {
        DataResponse dataResponse = pubmatic2Agent.requestData("2023-06-01", "2023-06-10");
        System.out.println(JSON.toJSONString(dataResponse));
    }
}