package xinmei.report.data.provider.hasapi.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.annotations.VisibleForTesting;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xinmei.report.data.dto.DataResponse;
import xinmei.report.data.ex.ApiException;
import xinmei.report.data.provider.Provider;
import xinmei.report.data.utils.DateUtil;
import xinmei.report.data.utils.HttpUtil;
import xinmei.report.data.utils.NumUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class RhythmoneAgent extends Provider {
    @Autowired
    OkHttpClient httpClient;

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final String apiKey = "4Z7MGfxSi3BzxUM6HsngTIlLNHFeRLMJ";

    @Override
    public String getDataByApi(String startDate, String endDate) {
        // 获取token
        String token = getToken();
        // 获取文件名称
        String fileName = getFileName(token, startDate, endDate);
        // 获取文件路径
        String filePath = getFilePath(fileName, token);
        // 读取sheet
        return covertSheetToString(filePath);
    }

    @Override
    public DataResponse covertDataResponse(String data) {
        if (data == null) {
            return null;
        }

        List<List<Object>> list = JSONObject.parseObject(data, List.class);
        DataResponse dataResponse = new DataResponse();
        List<Object> titles = list.remove(0);
        NumUtil.coverNumeric(list);

        list.sort(DateUtil.comparator);
        dataResponse.setRows(list);
        dataResponse.setColumns(titles);
        return dataResponse;
    }

    private String getToken() {
        String apiUserName = "100129@UnrulyAPI";
        String apiPassWord = "pR4)fheu";
        String tokenUrl = "https://api.unruly.co/ctrl/auth";

        String requestBody = String.format("username=%s&password=%s", apiUserName, apiPassWord);
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");

        RequestBody body = RequestBody.create(mediaType, requestBody);
        Request request = new Request.Builder()
                .url(tokenUrl)
                .post(body)
                .build();

        try (Response response = this.httpClient.newCall(request).execute()) {
            if (!response.isSuccessful())
                throw new ApiException("请求数据失败");
            final ResponseBody responseBody = response.body();
            if (responseBody != null) {
                String responseString = responseBody.string();
                JSONObject jsonObject = JSON.parseObject(responseString);
                Object access_token = jsonObject.get("access_token");
                if (access_token == null) {
                    throw new ApiException("请求数据失败");
                } else {
                    return access_token.toString();
                }
            }
        } catch (Exception e) {
            logger.error("获取报表数据异常,url:{}", tokenUrl, e);
        }
        return null;
    }


    private String getFileName(String token, String startDate, String endDate) {
        if (token == null) {
            return null;
        }
        String dateInterval = "DAILY";
        String fileNameApi = "https://api.unruly.co/ctrl/api/insights/supplySummary";

        HttpUrl.Builder urlBuilder = Objects.requireNonNull(HttpUrl.parse(fileNameApi)).newBuilder();
        startDate = startDate.replace("-", "");
        endDate = endDate.replace("-", "");
        urlBuilder.addQueryParameter("startDate", startDate)
                .addQueryParameter("endDate", endDate)
                .addQueryParameter("dateInterval", dateInterval)
                .addQueryParameter("apiKey", apiKey);
        String url = urlBuilder.build().toString();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{}");

        Request request = new Request.Builder().url(url)
                .header("Accept", "application/json")
                .header("Authorization", "Bearer " + token)
                .post(body)
                .build();

        return HttpUtil.processHttpRequest(httpClient, request, logger, url, false, false);
    }

    private String getFilePath(String fileName, String token) {
        if (fileName == null) {
            return null;
        }
        String downLoadFileApi = "https://api.unruly.co/ctrl/api/download/fileURL";

        HttpUrl.Builder urlBuilder = Objects.requireNonNull(HttpUrl.parse(downLoadFileApi)).newBuilder();
        urlBuilder.addQueryParameter("fileName", fileName)
                .addQueryParameter("apiKey", apiKey);
        String url = urlBuilder.build().toString();

        Request request = new Request.Builder().url(url)
                .header("Accept", "application/json")
                .header("Authorization", "Bearer " + token)
                .get()
                .build();

        try (Response response = this.httpClient.newCall(request).execute()) {
            if (response.code() != 200)
                throw new ApiException("请求数据失败");

            final ResponseBody responseBody = response.body();
            if (responseBody != null) {
                String responseString = responseBody.string();
                JSONObject jsonObject = JSON.parseObject(responseString);
                Object s3_file_url = jsonObject.get("S3_FILE_URL");
                if (s3_file_url == null) {
                    throw new ApiException("请求数据失败");
                } else {
                    return s3_file_url.toString();
                }
            }
        } catch (Exception e) {
            logger.error("获取报表数据异常,url:{}", url, e);
        }
        return null;
    }

    @VisibleForTesting
    public String covertSheetToString(String filePath) {
        try {
            List<List<Object>> resultList = new ArrayList<>();
            URL url = new URL(filePath); // 替换为实际的CSV文件URL

            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String line;

            while ((line = reader.readLine()) != null) {
                String[] data = line.replace("\"", "").split(",");
                List<Object> list = new ArrayList<>(Arrays.asList(data));
                resultList.add(list);
            }
            reader.close();
            return JSON.toJSONString(resultList);
        } catch (IOException e) {
            logger.error("远端文件读取失败，file url{}", filePath);
        }
        return null;
    }
}

