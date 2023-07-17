package xinmei.report.data.provider.notapi.impl;

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

@Service
public class AdmixerAgent extends Provider {
    @Autowired
    OkHttpClient httpClient;

    private final List<Object> titles = Arrays.asList("Date", "Requests", "Filtered Requests", "Average Bid Floor. $", "Impressions", "Viewable Impressions", "Fill Rate", "Publisher's Revenue. ", "Publisher's Ecpm");
    private static final String bodyTemplate = "{\"Variables\":[\"Date\"],\"Dimensions\":[],\"DateFrom\":\"%s\",\"DateTo\":\"%s\",\"Order\":\"V\",\"Descending\":true,\"MetricsIdName\":{\"RealRequests\":\"RealRequests\",\"FraudedRequests\":\"FraudedRequests\",\"SspBFInNetCurrency\":\"SspBFInNetCurrency\",\"V\":\"V\",\"CV\":\"CV\",\"Expenses\":\"Expenses\"},\"ReportConnectionSettingsId\":14,\"Zone\":[],\"DataCenter\":[],\"ExternalPublisherId\":[],\"Country\":[],\"OSFamily\":[],\"Region\":[],\"Domain\":[],\"Size\":[],\"Environment\":[]}";

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public String getDataByApi(String startDate, String endDate) {
        String rndts = "1689237031882";

        String authorization = "Bearer eyJhbGciOiJodHRwOi8vd3d3LnczLm9yZy8yMDAxLzA0L3htbGRzaWctbW9yZSNyc2Etc2hhMjU2Iiwia2lkIjoiMXEydzNlNHI1dDZ5N3U4aTlvMHAiLCJ0eXAiOiJKV1QifQ.eyJuYmYiOjE2ODg0NjM3MTAsImV4cCI6MTY5MTA1NTcxMCwiaXNzIjoiaHR0cHM6Ly9pZGVudGl0eS5hZG1peGVyLm5ldCIsImF1ZCI6WyJodHRwczovL2lkZW50aXR5LmFkbWl4ZXIubmV0L3Jlc291cmNlcyIsImFwaSIsImRtcGFwaSIsImh0dHA6Ly9pZGVudGl0eS5hZG1peGVyLm5ldCIsImh0dHA6Ly9zYW5kYm94LmFkbWl4ZXIubmV0IiwiaHR0cHM6Ly9wb3J0YWwuYWRtaXhlci5uZXQiLCJodHRwczovL3RyYWRlZGVzay5hZG1peGVyLm5ldCIsImxlZ2FjeWFwaSIsIm9wZW5pZCIsInByb2ZpbGUiXSwiY2xpZW50X2lkIjoiVUkiLCJzdWIiOiJ0aW5hLnlhbmdAa2lrYXRlY2guY29tIiwiYXV0aF90aW1lIjoxNjg4NDYzNzEwLCJpZHAiOiJsb2NhbCIsInVzZXJfbG9naW4iOiJ0aW5hLnlhbmdAa2lrYXRlY2guY29tIiwiSWQiOiJkYzJiZmRmNC05NWRiLTQwMWYtYjkyNC0wMjlkMWJmNjlmOTIiLCJTdWJzY3JpcHRpb25JZCI6IjUxODFiNTVlLTQwNTMtNGMyZS04YjE4LTU5ZWJhNDBiYmUwZCIsInJvbGVUeXBlIjoiMCIsIlN1YnNjcmlwdGlvblR5cGUiOiIxNCIsImFjY291bnRTdGF0ZSI6IjEiLCJkZWZhdWx0QXBwVXJsIjoiYWR4cG9ydGFsLmFkbWl4ZXIubmV0IiwiQWNjb3VudElkIjoiNTY4MzQiLCJ1c2VyX2lkIjoiMzg1NzkiLCJ1c2VyX25hbWUiOiJUaW5hIiwiVGltZVpvbmVTdHJpbmciOiJVVEMiLCJodHRwOi8vc2NoZW1hcy5taWNyb3NvZnQuY29tL3dzLzIwMDgvMDYvaWRlbnRpdHkvY2xhaW1zL3JvbGUiOiJBZG1pbiIsIkxDSUQiOiIwIiwidGVzdEFwcFVybCI6ImFkeHBvcnRhbC1zMi5hZG1peGVyLm5ldCIsInNjb3BlIjpbImFwaSIsInRrbmluZm8iLCJkbXBhcGkiLCJodHRwOi8vaWRlbnRpdHkuYWRtaXhlci5uZXQiLCJodHRwOi8vc2FuZGJveC5hZG1peGVyLm5ldCIsImh0dHBzOi8vcG9ydGFsLmFkbWl4ZXIubmV0IiwiaHR0cHM6Ly90cmFkZWRlc2suYWRtaXhlci5uZXQiLCJsZWdhY3lhcGkiLCJvcGVuaWQiLCJwcm9maWxlIiwib2ZmbGluZV9hY2Nlc3MiXSwiYW1yIjpbInB3ZCJdfQ.gzcOhqoJAtJMjHiHTFGFCuwpFwb1vw4SuZUx_tYkoKwt7b-MsI0WVL0VOfnNYveH9FCrtZ1aNb0jPWMmAuFaG5nFNOMn0dQeivC8Q1la5R3Nh2dV9O9xM1rlhdTxjK17tWmZphddphgOObRrqnTatXfRjD9n7DKeTEx17HkYq576n_XubmojLvGKux4OiYxyzGBCtrlAwE0EmbQRCJs4QCrQDQ9tE99QPnNPnF4pgsxjhEMCQf85ys8lWtFIscaIkkR9L2s_gq26QwRXKCnhw9CSf8ABFi6EtUbnYW6zf-sNsFFVq5FhxqaUDiYU9kCAoVvatw-sKioYpvP1W8XWjA";

        String dataApi = "https://adxportal.admixer.net/GrossReport/GetGrossInvReportStr";

        HttpUrl.Builder urlBuilder = Objects.requireNonNull(HttpUrl.parse(dataApi)).newBuilder();
        urlBuilder.addQueryParameter("rndts", rndts);
        String url = urlBuilder.build().toString();

        String body = String.format(bodyTemplate, startDate, endDate);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), body);

        // 使用cookie发送其他接口的请求
        Request dataRequest = new Request.Builder()
                .url(url)
                .header("Authorization", authorization)
                .post(requestBody)
                .build();

        return HttpUtil.processHttpRequest(httpClient, dataRequest, logger, url, false, false);


    }

    @Override
    public DataResponse covertDataResponse(String data) {
        if (data == null) {
            return null;
        }
        List<List<Object>> resultList = new ArrayList<>();
        DataResponse dataResponse = new DataResponse();

        JSONObject jsonObject = JSONObject.parseObject(data);
        Object datas = jsonObject.get("Template");
        JSONArray dataArray = (JSONArray) JSON.toJSON(datas);

        for (Object metaData : dataArray) {
            List<Object> list = new ArrayList<>();
            JSONObject metaDataJSON = (JSONObject) JSON.toJSON(metaData);
            String time = metaDataJSON.getString("Key");

            if (time.equals("-1")) {
                continue;
            }

            list.add(time);
            Object dataObject = metaDataJSON.get("Data");
            JSONObject dataJSON = (JSONObject) JSON.toJSON(dataObject);
            list.add(dataJSON.get("RealRequests"));
            list.add(dataJSON.get("FraudedRequests"));
            list.add(dataJSON.get("SspAvgBF"));
            list.add(dataJSON.get("V"));
            list.add(dataJSON.get("CV"));
            list.add(dataJSON.get("FillRealRate"));
            list.add(dataJSON.get("Expenses"));
            list.add(dataJSON.get("ExpensesECPM"));
            resultList.add(list);
        }

        resultList.sort(DateUtil.comparator);
        dataResponse.setRows(resultList);
        dataResponse.setColumns(titles);
        return dataResponse;
    }

}
