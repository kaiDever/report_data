package xinmei.report.data.config;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class HttpConfig {

    @Bean(name = "httpclient")
    public OkHttpClient createHttpClient(){
        return createOkHttpClientBuilder().build();
    }

    public OkHttpClient.Builder createOkHttpClientBuilder() {
        return new OkHttpClient.Builder()
                .readTimeout(30000, TimeUnit.MILLISECONDS)
                .writeTimeout(10000, TimeUnit.MILLISECONDS)
                .connectTimeout(6000, TimeUnit.MILLISECONDS)
                .retryOnConnectionFailure(true)
                .connectionPool(new ConnectionPool(1000, 5, TimeUnit.MINUTES));
    }

}
