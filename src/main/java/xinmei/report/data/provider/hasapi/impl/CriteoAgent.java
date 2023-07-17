package xinmei.report.data.provider.hasapi.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xinmei.report.data.dto.DataResponse;
import xinmei.report.data.provider.Provider;
import xinmei.report.data.utils.HttpUtil;

import java.util.*;

import static xinmei.report.data.utils.DateUtil.comparator;

/**
 * 日期要提前八天 只能使用UTC时区
 */
@Service
public class CriteoAgent extends Provider {
    @Autowired
    OkHttpClient httpClient;

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final List<Object> titles = Arrays.asList("Date", "Displays", "Views", "Cpm", "Revenue", "CTR", "Requests", "Participation", "win_rate");

    @Override
    public String getDataByApi(String startDate, String endDate) {
        String token = "9aa0a681-ff55-4782-b136-67a0e849494a";
        String metrics = "Requests,Revenue,CPM,Views,ParticipationRate,WinningRate,CTR,CriteoDisplays";
        String api = "https://pmc.criteo.com/api/stats";

        HttpUrl.Builder urlBuilder = Objects.requireNonNull(HttpUrl.parse(api)).newBuilder();
        urlBuilder.addQueryParameter("apitoken", token)
                .addQueryParameter("begindate", startDate)
                .addQueryParameter("enddate", endDate)
                .addQueryParameter("metrics", metrics)
                .addQueryParameter("currency", "USD")
                .addQueryParameter("timezone", "CET");

        String url = urlBuilder.build().toString();
        Request request = new Request.Builder().url(url)
                .header("Accept", "application/json")
                .get().build();
        return HttpUtil.processHttpRequest(httpClient, request, logger, url, false, false);
    }

    @Override
    public DataResponse covertDataResponse(String data) {
        if (data == null) {
            return null;
        }
        JSONArray records = JSONArray.parseArray(data);
        List<List<Object>> resultList = new ArrayList<>();
        DataResponse dataResponse = new DataResponse();
        for (Object record : records) {
            JSONObject jsonValue = JSONObject.parseObject(record.toString());
            List<Object> list = new ArrayList<>();
            list.add(jsonValue.get("TimeId"));
            list.add(jsonValue.get("CriteoDisplays"));
            list.add(jsonValue.get("Views"));
            list.add(jsonValue.get("CPM"));
            list.add(jsonValue.get("Revenue"));
            list.add(jsonValue.get("CTR"));
            list.add(jsonValue.get("Requests"));
            list.add(jsonValue.get("ParticipationRate"));
            list.add(jsonValue.get("WinningRate"));
            resultList.add(list);
        }

        resultList.sort(comparator);
        dataResponse.setRows(resultList);
        dataResponse.setColumns(titles);
        return dataResponse;
    }
}
