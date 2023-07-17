package xinmei.report.data.provider;

import org.springframework.scheduling.annotation.Async;
import xinmei.report.data.dto.DataResponse;

/**
 * report-date -> DateResponse
 */
public abstract class Provider {
    //获取数据之后转换为内部格式
    public DataResponse requestData(String startDate, String endDate) {
        String data = getDataByApi(startDate, endDate);
        return covertDataResponse(data);
    }

    public abstract String getDataByApi(String startDate, String endDate);

    public abstract DataResponse covertDataResponse(String data);
}
