package xinmei.report.data.provider.hasapi.impl;

import com.alibaba.fastjson.JSON;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import xinmei.report.data.TestBase;
import xinmei.report.data.dto.DataResponse;

class TappxAgentTest extends TestBase {
    @Autowired
    TappxAgent tappxAgent;

    @Test
    public void getDataByApi(){
        String startDate = "2023-06-19";
        String endDate = "2023-06-24";
        String data = tappxAgent.getDataByApi(startDate, endDate);
        System.out.println(data);
    }

    @Test
    public void requestDataTest() {
        String startDate = "2023-06-19";
        String endDate = "2023-06-24";
        DataResponse dataResponse = tappxAgent.requestData(startDate, endDate);
        System.out.println(JSON.toJSONString(dataResponse));
    }


    }