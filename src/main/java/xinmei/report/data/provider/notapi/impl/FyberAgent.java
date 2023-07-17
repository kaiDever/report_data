package xinmei.report.data.provider.notapi.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
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
public class FyberAgent extends Provider {
    @Autowired
    OkHttpClient httpClient;

    private static final String requestBodyTemplate = "{\"query\":{\"cube\":\"bids_demand\",\"filters\":[],\"dateRange\":{\"start\":\"%s\",\"end\":\"%s\"},\"splits\":[{\"dimension\":{\"id\":\"\",\"key\":\"time\",\"attrId\":\"\",\"formula\":\"$time.timeBucket(P1D)\",\"name\":\"time\",\"help\":\"\",\"consists\":[\"time\"]},\"limit\":50,\"orderBy\":{\"id\":\"\",\"formula\":\"\",\"key\":\"time\",\"consists\":[]},\"direction\":\"ascending\"}],\"measures\":[{\"id\":\"TotalSpend\",\"key\":\"TotalSpend\",\"attrId\":\"\",\"formula\":\"$main.sum($TotalSpend)\",\"pattern\":\"\",\"name\":\"TotalSpend\",\"help\":\"\",\"type\":\"num\",\"calc\":false,\"consists\":[]},{\"id\":\"bidRequests\",\"key\":\"bidRequests\",\"attrId\":\"\",\"formula\":\"$main.sum($BidsMetric)\",\"pattern\":\"\",\"name\":\"bidRequests\",\"help\":\"\",\"type\":\"num\",\"calc\":false,\"consists\":[]},{\"id\":\"validBids\",\"key\":\"validBids\",\"attrId\":\"\",\"formula\":\"$main.sum($ValidBidsMetric)\",\"pattern\":\"\",\"name\":\"validBids\",\"help\":\"\",\"type\":\"num\",\"calc\":false,\"consists\":[]},{\"id\":\"ClearsMetric\",\"key\":\"ClearsMetric\",\"attrId\":\"\",\"formula\":\"$main.sum($ClearsMetric)\",\"pattern\":\"\",\"name\":\"ClearsMetric\",\"help\":\"\",\"type\":\"num\",\"calc\":false,\"consists\":[]},{\"id\":\"ctr\",\"key\":\"ctr\",\"attrId\":\"\",\"formula\":\"$main.sum($ClicksMetric) / $main.sum($ImpressionsMetric)\",\"pattern\":\"\",\"name\":\"ctr\",\"help\":\"\",\"type\":\"num\",\"calc\":false,\"consists\":[]}]},\"queryId\":\"4e8fd46e-722d-4eb8-b48e-ed7f001bb3bc\",\"isDownload\":false}";
    private final List<String> metrics = Arrays.asList("TotalSpend", "bidRequests", "validBids", "ClearsMetric", "ctr");
    private final List<Object> titles = Arrays.asList("Date/Time", "Total Spend", "Bid Requests", "Bid Responses", "Bids Won", "CTR");

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    public String loginAndGetData(String startDate, String endDate) {
        // 获取登陆token
        String loginToken = getLoginToken();
        // 利用loginToken获取pecan-token
        String pecanToken = getPecanToken(loginToken);
        // 请求数据
        return getDataByToken(pecanToken, startDate, endDate);
    }


    @Override
    public String getDataByApi(String startDate, String endDate) {
        return loginAndGetData(startDate, endDate);
    }

    @Override
    public DataResponse covertDataResponse(String data) {
        if (data == null) {
            return null;
        }
        List<List<Object>> resultList = new ArrayList<>();
        DataResponse dataResponse = new DataResponse();

        JSONArray jsonArray = JSONArray.parseArray(data);
        for (Object mateData : jsonArray) {
            List<Object> list = new ArrayList<>();
            JSONObject mateDataJSON = (JSONObject) JSON.toJSON(mateData);
            JSONObject timeJSON = (JSONObject) JSON.toJSON(mateDataJSON.get("time"));
            list.add(timeJSON.get("start"));
            for (String metric : metrics) {
                list.add(mateDataJSON.get(metric));
            }
            resultList.add(list);
        }

        resultList.sort(DateUtil.comparator);
        dataResponse.setRows(resultList);
        dataResponse.setColumns(titles);
        return dataResponse;
    }

    private String getLoginToken() {
        String email = "tina.yang@kikatech.com";
        String passWord = "Xinmei@3651";
        String maze = "3bc2zt";
        String loginApi = "https://console.fyber.com/auth/local";

        // 构建登录请求的表单数据
        RequestBody formBody = new FormBody.Builder()
                .add("email", email)
                .add("maze", maze)
                .add("password", passWord)
                .build();

        // 构建登录请求
        Request request = new Request.Builder()
                .url(loginApi) // 替换为登录接口的URL
                .post(formBody)
                .build();

        try (Response loginResponse = httpClient.newCall(request).execute()) {
            // 检查响应是否成功
            if (loginResponse.code() != 200) {
                throw new Exception("登录失败：" + loginResponse);
            }

            assert loginResponse.body() != null;
            String responseBody = loginResponse.body().string();
            if (StringUtils.isNotBlank(responseBody)) {
                JSONObject jsonObject = JSON.parseObject(responseBody);
                return jsonObject.get("token").toString();
            }
        } catch (Exception e) {
            logger.error("登陆token获取失败 {}", e.getMessage());
        }
        return null;
    }

    private String getPecanToken(String loginToken) {
        if (loginToken == null) {
            return null;
        }
        String getPecanTokenApi = "https://console.fyber.com/api/pivot/mkurl";

        HttpUrl.Builder urlBuilder = Objects.requireNonNull(HttpUrl.parse(getPecanTokenApi)).newBuilder();
        String url = urlBuilder.build().toString();

        Request request = new Request.Builder().url(url)
                .header("Cookie", "collins_token=" + loginToken + ";collins_user_id=227068")
                .get().build();
        String response = HttpUtil.processHttpRequest(httpClient, request, logger, getPecanTokenApi, false, false);
        if (response == null) {
            return null;
        }
        JSONObject responseJSON = JSONObject.parseObject(response);
        return responseJSON.get("token").toString();

    }

    private String getDataByToken(String token, String startDate, String endDate) {
        if (token == null) {
            return null;
        }
        String[] tokens = token.split("\\.");
        // 使用Base64解码
        byte[] decodedBytes = Base64.getDecoder().decode(tokens[1]);
        String decodedToken = new String(decodedBytes);
        JSONObject pecanTokenJSON = JSONObject.parseObject(decodedToken);
        String pecanToken = pecanTokenJSON.get("sessionToken").toString();

        String dataApi = "https://pecan.fyber.com/api/v2/dynamic-reports/bids/raver/single-split";

        String body = String.format(requestBodyTemplate, startDate, endDate);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), body);

        // 使用cookie发送其他接口的请求
        Request dataRequest = new Request.Builder()
                .url(dataApi)
                .header("pecan-token", pecanToken)
                .post(requestBody)
                .build();

        return HttpUtil.processHttpRequest(httpClient, dataRequest, logger, dataApi, false, false);
    }

}
