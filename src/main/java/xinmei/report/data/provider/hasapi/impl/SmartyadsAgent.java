package xinmei.report.data.provider.hasapi.impl;

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
import xinmei.report.data.utils.HttpUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static xinmei.report.data.utils.DateUtil.comparator;

@Service
public class SmartyadsAgent extends Provider {

    @Autowired
    OkHttpClient httpClient;

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final List<Object> titles = Arrays.asList("Date", "求和项:Impressions", "求和项:Earn");

    public String getDataByApi(String startDate, String endDate) {
        String api = "https://exchange.smartyads.com/api/v1/ssp-report";
        String endPoint = "kika_US_EAST_kika_east";
        String token = "Hl7GTrtyZGTPzjVN1Kmw";
        HttpUrl.Builder urlBuilder = Objects.requireNonNull(HttpUrl.parse(api)).newBuilder();

        urlBuilder.addQueryParameter("endpoint", endPoint)
                .addQueryParameter("token", token)
                .addQueryParameter("from", startDate)
                .addQueryParameter("to", endDate);

        String url = urlBuilder.build().toString();
        Request request = new Request.Builder().url(url)
                .header("Accept", "application/json")
                .get().build();
        return HttpUtil.processHttpRequest(httpClient, request, logger, url, false, true);
    }

    public DataResponse covertDataResponse(String data) {
        if (data == null) {
            return null;
        }
        List<List<Object>> resultList = new ArrayList<>();
        DataResponse dataResponse = new DataResponse();

        JSONObject dataJson = JSONObject.parseObject(data);

        for (String key : dataJson.keySet()) {
            Object value = dataJson.get(key);
            JSONObject jsonValue = JSONObject.parseObject(value.toString());
            ArrayList<Object> list = new ArrayList<>();
            list.add(key);
            list.add(jsonValue.get("impressions"));
            list.add(jsonValue.get("revenue"));
            resultList.add(list);
        }

        resultList.sort(comparator);
        dataResponse.setRows(resultList);
        dataResponse.setColumns(titles);
        return dataResponse;
    }
}
