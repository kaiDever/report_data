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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * algorix客户端查询日期最后一天的数据不准确
 */
@Service
public class AlgorixSSPAgent extends Provider {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    OkHttpClient httpClient;
    List<Object> titles = Arrays.asList("Date", "request", "response", "impression", "gross_revenue", "fill_rate", "render_rate", "ecpm");
    List<String> metrics = Arrays.asList("request", "response", "impression", "revenue", "fill_rate", "render_rate", "ecpm");

    @Override
    public String getDataByApi(String startDate, String endDate) {
        String api = "http://buyer.svr-algorix.com/api/report/demand_report_v2";
        String userId = "181";
        String authorization = "55c5337c44e4df3f5de64a3e7539a9b3";

        HttpUrl.Builder urlBuilder = Objects.requireNonNull(HttpUrl.parse(api)).newBuilder();

        urlBuilder.addQueryParameter("x-userid", userId)
                .addQueryParameter("x-authorization", authorization)
                .addQueryParameter("limit", "40")
                .addQueryParameter("start", startDate)
                .addQueryParameter("end", endDate)
                .addQueryParameter("dimensions[]", "date");
        for (String metric : metrics) {
            urlBuilder.addQueryParameter("metrics[]", metric);
        }

        String url = urlBuilder.build().toString();
        Request request = new Request.Builder().url(url)
                .header("Accept", "application/json")
                .get().build();
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
