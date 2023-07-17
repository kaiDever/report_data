package xinmei.report.data.provider.hasapi.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xinmei.report.data.dto.DataResponse;
import xinmei.report.data.provider.Provider;
import xinmei.report.data.utils.DateUtil;
import xinmei.report.data.utils.HttpUtil;

import java.util.*;

/**
 * algorix 日期效果 [startDate,endDate)
 */
@Service
public class AlgorixAgent extends Provider {
    private String userId = "60270";
    private String authorization = "3e79dd80d6741b4e92b23b366eae83d7fK";

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setAuthorization(String authorization) {
        this.authorization = authorization;
    }

    public AlgorixAgent() {
    }

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    OkHttpClient httpClient;
    List<Object> titles = Arrays.asList("Date", "request", "response", "impression", "fill_rate", "render_rate", "net_revenue", "ecpm");
    List<String> metrics = Arrays.asList("request", "response", "impression", "fill_rate", "render_rate", "net_revenue", "net_ecpm");
    List<String> dimensions = Collections.singletonList("date");

    @Override
    public String getDataByApi(String startDate, String endDate) {
        String api = "https://ssp.svr-algorix.com/api/report/v2";

        HttpUrl.Builder urlBuilder = Objects.requireNonNull(HttpUrl.parse(api)).newBuilder();
        String url = urlBuilder.build().toString();

        JSONObject requestBody = new JSONObject();
        requestBody.put("start", startDate);
        requestBody.put("end", endDate);
        requestBody.put("metrics", metrics);
        requestBody.put("dimensions", dimensions);
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(requestBody));

        Request request = new Request.Builder().url(url)
                .addHeader("x-userid", userId)
                .addHeader("x-authorization", authorization)
                .post(body)
                .build();

        return HttpUtil.processHttpRequest(httpClient, request, logger, url, true, false);
    }

    @Override
    public DataResponse covertDataResponse(String data) {
        if (data == null) {
            return null;
        }
        List<List<Object>> resultList = new ArrayList<>();
        DataResponse dataResponse = new DataResponse();

        JSONObject dataJson = JSONObject.parseObject(data);
        Object outerLayerData = dataJson.get("data");
        JSONObject outerLayerJSONData = (JSONObject) JSON.toJSON(outerLayerData);
        Object innerLayerJSONData = outerLayerJSONData.get("data");
        JSONArray jsonArray = (JSONArray) JSON.toJSON(innerLayerJSONData);

        for (Object individual : jsonArray) {
            JSONObject individualJSON = (JSONObject) JSON.toJSON(individual);
            List<Object> list = new ArrayList<>();
            list.add(individualJSON.get("date"));
            for (String metric : metrics) {
                list.add(individualJSON.get(metric));
            }
            resultList.add(list);
        }

        resultList.sort(DateUtil.comparator);
        dataResponse.setColumns(titles);
        dataResponse.setRows(resultList);

        return dataResponse;
    }
}
