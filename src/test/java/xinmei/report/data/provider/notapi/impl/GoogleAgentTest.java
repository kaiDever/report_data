package xinmei.report.data.provider.notapi.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import xinmei.report.data.TestBase;

class GoogleAgentTest extends TestBase {

    @Autowired
    GoogleAgent googleAgent;

    @Test
    public void getDataByApiTest(){
        String startDate = "2023-07-01";
        String endDate = "2023-07-05";
        System.out.println(googleAgent.getDataByApi(startDate, endDate));
    }
}