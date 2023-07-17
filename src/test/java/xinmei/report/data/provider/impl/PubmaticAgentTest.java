package xinmei.report.data.provider.impl;

import com.alibaba.fastjson.JSON;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import xinmei.report.data.TestBase;
import xinmei.report.data.dto.DataResponse;
import xinmei.report.data.provider.hasapi.impl.PubmaticAgent;

class PubmaticAgentTest extends TestBase {
    @Autowired
    private PubmaticAgent pubmaticAgent;

    @Test
    public void requestDataByApiTest() {

        String data = pubmaticAgent.getDataByApi("2023-06-01", "2023-06-10");
        DataResponse dataResponse = JSON.parseObject(data, DataResponse.class);
        System.out.println(JSON.toJSONString(dataResponse));
    }

    @Test
    public void refreshToken_andUpdateApiTokenToSql(){
        String response = pubmaticAgent.refreshToken();
        pubmaticAgent.updateApiTokenToSql(response);
    }

}