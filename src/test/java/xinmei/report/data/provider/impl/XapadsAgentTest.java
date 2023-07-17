package xinmei.report.data.provider.impl;

import com.alibaba.fastjson.JSON;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import xinmei.report.data.TestBase;
import xinmei.report.data.dto.DataResponse;
import xinmei.report.data.provider.hasapi.impl.XapadsAgent;

class XapadsAgentTest extends TestBase {
    @Autowired
    XapadsAgent xapadsAgent;

    @Test
    public void getReportData() {
        String startDate = "2023-06-01";
        String endDate = "2023-06-01";
        DataResponse dataResponse = xapadsAgent.requestData(startDate, endDate);
        System.out.println(JSON.toJSONString(dataResponse));
    }

}