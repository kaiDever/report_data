package xinmei.report.data.utils;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import xinmei.report.data.ex.ApiException;


public class HttpUtil {
    public static String processHttpRequest(OkHttpClient httpClient, Request request, Logger logger,
                                            String url, Boolean successFlag, Boolean errorFlag) {
        String result = null;
        try (Response response = httpClient.newCall(request).execute()) {
            if (response.code() != 200)
                throw new ApiException("请求数据失败");

            final ResponseBody responseBody = response.body();
            if (responseBody != null) {
                result = responseBody.string();
            }
            if (StringUtils.isBlank(result)) {
                logger.error("获取报表数据为空，url:{}", url);
            }
            assert result != null;
            if (errorFlag && result.contains("Error")) {
                throw new ApiException("请求数据失败" + result);
            }
            if (successFlag && !result.contains("success")) {
                throw new ApiException("请求数据失败" + result);
            }
            return result;
        } catch (Exception e) {
            logger.error("获取报表数据异常,url:{},{}", url, e);
        }
        return null;
    }
}
