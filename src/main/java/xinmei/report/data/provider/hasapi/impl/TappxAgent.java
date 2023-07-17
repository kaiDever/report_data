package xinmei.report.data.provider.hasapi.impl;

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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static xinmei.report.data.utils.NumUtil.coverNumeric;

@Service
public class TappxAgent extends Provider {
    @Autowired
    OkHttpClient httpClient;

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    List<Object> titles = Arrays.asList("Date", "request", "response", "impressions", "clicks", "revenues");


    @Override
    public String getDataByApi(String startDate, String endDate) {
        String api = "http://reporting.api.tappx.com/ssp/v3";
        String key = "aff4c79c9510dfa7ae3630cb18df2ba4";
        String userId = "27554";

        HttpUrl.Builder urlBuilder = Objects.requireNonNull(HttpUrl.parse(api)).newBuilder();

        urlBuilder.addQueryParameter("key", key)
                .addQueryParameter("id", userId)
                .addQueryParameter("date_start", startDate)
                .addQueryParameter("date_end", endDate)
                .addQueryParameter("gos", "0")
                .addQueryParameter("gapp", "0")
                .addQueryParameter("gadtype", "0");

        String url = urlBuilder.build().toString();
        Request request = new Request.Builder()
                .url(url)
                .get().build();
        return HttpUtil.processHttpRequest(httpClient, request, logger, url, false, false);
    }

    @Override
    public DataResponse covertDataResponse(String data) {
        if (data == null) {
            return null;
        }
        List<List<Object>> resultList = new ArrayList<>();
        DataResponse dataResponse = new DataResponse();
        String[] records = data.split("\\n");
        for (String record : records) {
            List<Object> list = Arrays.stream(record.split(";"))
                    .filter(attr -> attr.length() > 0)
                    .collect(Collectors.toList());
            resultList.add(list);
        }

        //修改数据为数值类型
        coverNumeric(resultList);

        resultList.sort(DateUtil.comparator);
        dataResponse.setRows(resultList);
        dataResponse.setColumns(titles);
        return dataResponse;
    }


}
