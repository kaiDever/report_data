package xinmei.report.data.requester.impl;

import com.alibaba.fastjson.JSON;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import xinmei.report.data.TestBase;
import xinmei.report.data.dto.DataResponse;

import java.io.IOException;
import java.io.InputStream;

class WorkSheetRequesterTest extends TestBase {
    @Autowired
    WorkSheetRequester workSheetRequester;

    @Before
    public DataResponse createDataResponse() throws IOException {
        final InputStream inputStream = GoogleSheetRequester.class.getResourceAsStream("/dataResponse.json");
        if (inputStream == null) {
            throw new NullPointerException("Resource is missing");
        }
        final String content = IOUtils.toString(inputStream);
        return JSON.parseObject(content, DataResponse.class);
    }

    @Test
    public void saveTest() throws IOException {
        DataResponse dataResponse = createDataResponse();
        workSheetRequester.save(dataResponse,"test");
    }

}