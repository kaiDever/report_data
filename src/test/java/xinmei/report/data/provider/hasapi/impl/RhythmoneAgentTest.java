package xinmei.report.data.provider.hasapi.impl;

import com.alibaba.fastjson.JSON;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import xinmei.report.data.TestBase;
import xinmei.report.data.dto.DataResponse;

class RhythmoneAgentTest extends TestBase {
    @Autowired
    RhythmoneAgent rhythmoneAgent;

    @Test
    public void covertSheetToStringTest() {
        String filePath = "https://fusionreports.s3.amazonaws.com/supply_summary-20230624-20230626-EST-USD-1690080415669.csv?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20230630T100322Z&X-Amz-SignedHeaders=host&X-Amz-Expires=300&X-Amz-Credential=AKIAVABAWEVUP2KC6Z5B%2F20230630%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Signature=cc7ea71f5cbd8a0aebaa4bc089cda856a6cba5372bdb6eab558ff0bbc8ac94ba";
        rhythmoneAgent.covertSheetToString(filePath);
    }

    @Test
    public void requestDataTest() {
        String startDate = "2023-06-19";
        String endDate = "2023-06-24";
        DataResponse dataResponse = rhythmoneAgent.requestData(startDate, endDate);
        System.out.println(JSON.toJSONString(dataResponse));
    }

}