package xinmei.report.data.provider.notapi.impl;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xinmei.report.data.dto.DataResponse;
import xinmei.report.data.provider.Provider;
import xinmei.report.data.utils.GoogleUtil;
import xinmei.report.data.utils.HttpUtil;

import java.util.Objects;

@Service
public class GoogleAgent extends Provider {

    @Autowired
    OkHttpClient httpClient;

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    final String req = "{\"1\":{\"2\":\"daily report\",\"3\":{\"1\":[{\"1\":[\"2\"],\"2\":[{\"1\":{\"1\":[{\"1\":\"2\"}]},\"2\":false}],\"3\":\"0\",\"4\":\"3000\"},{\"1\":[\"31\"],\"3\":\"0\",\"4\":\"3000\"}],\"4\":{\"1\":3},\"8\":{\"1\":0,\"2\":\"222409815\"}},\"4\":{\"3\":[{\"1\":2}]}},\"3\":false}";

    @Override
    public String getDataByApi(String startDate, String endDate) {
        try {
            String api = "https://realtimebidding.google.com/authorizedbuyers/222409815/querytool/ReportDefinitionService/Run";
            Credential credentials = GoogleUtil.getCredentials(GoogleNetHttpTransport.newTrustedTransport());
            String accessToken = credentials.getAccessToken();

            HttpUrl.Builder urlBuilder = Objects.requireNonNull(HttpUrl.parse(api)).newBuilder();
            urlBuilder.addQueryParameter("hl", "en-GB");
            String url = urlBuilder.build().toString();

            RequestBody body = new FormBody.Builder()
                    .add("f.req", req)
                    .build();

            Request request = new Request.Builder().url(url)
                    .addHeader("Authorization", "Bearer " + accessToken)
                    .post(body)
                    .build();

            return HttpUtil.processHttpRequest(httpClient, request, logger, url, false, false);
        } catch (Exception e) {
            logger.error("");
        }
        return null;
    }

    @Override
    public DataResponse covertDataResponse(String data) {
        return null;
    }
}
