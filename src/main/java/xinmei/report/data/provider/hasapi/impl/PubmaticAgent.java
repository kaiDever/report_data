package xinmei.report.data.provider.hasapi.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xinmei.report.data.bean.ApiToken;
import xinmei.report.data.bean.ApiTokenExample;
import xinmei.report.data.dto.DataResponse;
import xinmei.report.data.dto.PubTokenResponse;
import xinmei.report.data.ex.ApiException;
import xinmei.report.data.mapper.ApiTokenMapper;
import xinmei.report.data.provider.Provider;
import xinmei.report.data.utils.HttpUtil;

import java.util.List;
import java.util.Objects;

@Service
public class PubmaticAgent extends Provider {
    @Autowired
    OkHttpClient httpClient;

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final String TOKEN_PREFIX = "Bearer ";
    private String reportApi = "http://api.pubmatic.com/v1/analytics/data/publisher/158271";

    public void setReportApi(String reportApi) {
        this.reportApi = reportApi;
    }

    @Autowired
    private ApiTokenMapper apiTokenMapper;

    public PubmaticAgent() {
    }


    public String getDataByApi(String startDate, String endDate) {
        ApiToken apiToken = getAuthorizationFromSql();

        HttpUrl.Builder urlBuilder = Objects.requireNonNull(HttpUrl.parse(reportApi)).newBuilder();
        urlBuilder.addQueryParameter("dateUnit", "date")
                .addQueryParameter("dimensions", "date")
                .addQueryParameter("metrics", "revenue,paidImpressions,ecpm,totalImpressions,gEcpm")
                .addQueryParameter("pageNumber", "1")
                .addQueryParameter("fromDate", startDate)
                .addQueryParameter("toDate", endDate)
                .addQueryParameter("pageSize", "31")
                .addQueryParameter("filters", "")
                .addQueryParameter("sort", "date")
                .addQueryParameter("isAdunitHierarchy", "false");

        String url = urlBuilder.build().toString();
        Request request = new Request.Builder().url(url)
                .header("Accept", "application/json")
                .header("Authorization", TOKEN_PREFIX + apiToken.getAccessToken())
                .get().build();
        return HttpUtil.processHttpRequest(httpClient, request, logger, url, false, false);
    }

    @Override
    public DataResponse covertDataResponse(String data) {
        return JSON.parseObject(data, DataResponse.class);
    }


    public ApiToken getAuthorizationFromSql() {
        ApiTokenExample apiTokenExample = new ApiTokenExample();
        ApiTokenExample.Criteria criteria = apiTokenExample.createCriteria();
        criteria.andNameEqualTo("pubmatic");

        List<ApiToken> apiTokens = apiTokenMapper.selectByExample(apiTokenExample);

        if (apiTokens != null && apiTokens.size() > 0) {
            return apiTokens.get(0);
        }
        return null;
    }

    public String refreshToken() {
        ApiToken apiToken = getAuthorizationFromSql();
        MediaType mediaType = MediaType.parse("application/json");

        JSONObject params = new JSONObject();
        params.put("email", "rtb@kikatech.com");
        params.put("apiProduct", "PUBLISHER");
        params.put("refreshToken", apiToken.getRefreshToken());
        RequestBody body = RequestBody.create(mediaType, params.toString());

        String refreshApi = "http://api.pubmatic.com/v1/developer-integrations/developer/refreshToken";
        Request request = new Request.Builder()
                .url(refreshApi)
                .put(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", TOKEN_PREFIX + apiToken.getAccessToken())
                .build();

        try (Response response = this.httpClient.newCall(request).execute()) {
            if (!response.isSuccessful())
                throw new ApiException("请求刷新token接口失败");

            final ResponseBody responseBody = response.body();
            String content = "";
            if (responseBody != null) {
                content = responseBody.string();
            }
            if (StringUtils.isBlank(content)) {
                throw new ApiException("请求刷新token接口失败");
            }
            return content;
        } catch (Exception e) {
            logger.error("刷新token失败,accessToken:[{}],refreshToken:[{}]",
                    apiToken.getAccessToken(), apiToken.getRefreshToken());
        }
        return null;
    }

    public void updateApiTokenToSql(String content) {

        if (content == null)
            return;

        PubTokenResponse pubTokenResponse = JSON.parseObject(content, PubTokenResponse.class);

        ApiToken apiToken = new ApiToken();
        apiToken.setAccessToken(pubTokenResponse.getAccessToken());
        apiToken.setRefreshToken(pubTokenResponse.getRefreshToken());

        ApiTokenExample apiTokenExample = new ApiTokenExample();
        ApiTokenExample.Criteria criteria = apiTokenExample.createCriteria();
        criteria.andNameEqualTo("pubmatic");

        apiTokenMapper.updateByExampleSelective(apiToken, apiTokenExample);
    }


}
