package xinmei.report.data.provider.hasapi.impl;

import com.alibaba.fastjson.JSON;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import xinmei.report.data.TestBase;
import xinmei.report.data.dto.DataResponse;


class OpenxRTBAgentTest extends TestBase {
    @Autowired
    OpenxRTBAgent openxRTBAgent;

    @Test
    public void getDataByApiTest(){
        String startDate = "2023-06-01";
        String endDate = "2023-06-05";
        System.out.println(openxRTBAgent.getDataByApi(startDate,endDate));
    }

    @Test
    public void getReportDataTest(){
        String startDate = "2023-06-01";
        String endDate = "2023-06-05";
        DataResponse dataResponse = openxRTBAgent.requestData(startDate, endDate);
        System.out.println(JSON.toJSONString(dataResponse));
    }
}