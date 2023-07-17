package xinmei.report.data.provider.hasapi.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xinmei.report.data.dto.DataResponse;
import xinmei.report.data.provider.Provider;
import xinmei.report.data.utils.DateUtil;
import xinmei.report.data.utils.HttpUtil;

import java.util.*;

@Service
public class XapadsAgent extends Provider {
    @Autowired
    OkHttpClient httpClient;

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final List<Object> title = Arrays.asList("Date", "Requests", "Responses", "Requested Bids", "Bids",
            "Average Bid eCPM", "Average Bid Floor", "Impression Wins",
            "Wins Price", "Estimated Revenue",
            "Gross Impressions", "Impression Win Rate", "Clicks", "CTR",
            "Verified Impressions", "Verified Clicks", "Verified Revenue");

    private final List<String> attributes = Arrays.asList("date", "rtb_pub_imp_requests", "rtb_pub_imp_coverage",
            "rtb_pub_imp_requests", "rtb_pub_imp_coverage", "rtb_pub_bids_price_ecpm", "rtb_pub_avg_bidfloor",
            "rtb_pub_gross_impressions", "rtb_pub_wins_price", "rtb_pub_revenue",
            "rtb_pub_gross_impressions", "rtb_pub_win_rate", "rtb_pub_clicks", "rtb_pub_ctr",
            "rtb_rem_impressions", "rtb_rem_clicks", "rtb_pub_revenue");

    @Override
    public String getDataByApi(String startDate, String endDate) {
        String api = "https://xml-console.xapads.com/publisher/api/ZoneReports/date";
        String token = "kZRTE2BosnTOFWf7";
        String dateCondition = formatDate(startDate, endDate);
        String range = "0-30";

        HttpUrl.Builder urlBuilder = Objects.requireNonNull(HttpUrl.parse(api)).newBuilder();

        urlBuilder.addQueryParameter("userToken", token)
                .addQueryParameter("filters", dateCondition)
                .addQueryParameter("range", range);

        String url = urlBuilder.build().toString();
        Request request = new Request.Builder().url(url)
                .header("Accept", "application/json")
                .get().build();

        return HttpUtil.processHttpRequest(httpClient, request, logger, url, false, true);
    }

    @Override
    public DataResponse covertDataResponse(String data) {
        if (null == data) {
            return null;
        }
        DataResponse dataResponse = new DataResponse();
        List<List<Object>> resultList = new ArrayList<>();
        JSONObject dateJson = JSON.parseObject(data);
        JSONObject response = dateJson.getJSONObject("response");
        JSONObject listJson = response.getJSONObject("list");
        // 将JsonObject转换为Map
        Map<String, JSONObject> map = new HashMap<>();
        for (Map.Entry<String, Object> entry : listJson.entrySet()) {
            map.put(entry.getKey(), (JSONObject) JSON.toJSON(entry.getValue()));
        }

        for (String key : map.keySet()) {
            JSONObject jsonObject = map.get(key);
            resultList.add(assignToList(attributes, jsonObject));
        }

        resultList.sort(DateUtil.comparator);
        dataResponse.setRows(resultList);
        dataResponse.setColumns(title);

        return dataResponse;
    }

    private String formatDate(String startDate, String endDate) {
        return String.format("date:%s_%s", startDate, endDate);
    }

    private List<Object> assignToList(List<String> attributes, JSONObject dataJson) {
        List<Object> resultList = new ArrayList<>();
        for (String attribute : attributes) {
            resultList.add(dataJson.get(attribute));
        }
        return resultList;
    }
}
