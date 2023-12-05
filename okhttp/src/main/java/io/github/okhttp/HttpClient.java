package io.github.okhttp;

import java.util.List;
import io.github.okhttp.chain.Interceptor;

/**
 * OkhttpClient创建类
 */
public class HttpClient {
    private Dispather dispather;

    private List<Interceptor> interceptors;

    private int retryTimes;

    private ConnectionPool connectionPool;

    public static final class Builder {

        HttpClient httpClient;

        public HttpClient build() {
            return new HttpClient();
        }
    }
}
