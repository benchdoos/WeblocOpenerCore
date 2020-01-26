package com.github.benchdoos.weblocopenercore.utils.http;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jetbrains.annotations.Nullable;
import org.jsoup.internal.StringUtil;

import java.io.IOException;

@Log4j2
public class HttpUtils<T> {
    public HttpUtils() {
    }

    public HttpResponse<T> sendHttpRequest(HttpRequestBase request) throws IOException {
        try (final CloseableHttpClient httpClient = HttpClients.createDefault();
             final CloseableHttpResponse response = httpClient.execute(request)) {

            final int statusCode = response.getStatusLine().getStatusCode();
            final String responseString = EntityUtils.toString(response.getEntity());
            if (statusCode == 200) {
                log.info("Http request was send successfully. (code: {}, response: {})",
                        statusCode,
                        responseString);
            } else {
                log.warn("Http request received wrong code. (code: {}, response: {})",
                        statusCode,
                        responseString);
            }

            final ObjectMapper mapper = new ObjectMapper();
            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

            @Nullable final T value;
            if (!StringUtil.isBlank(responseString)) {
                value = mapper.readValue(responseString, new TypeReference<T>() {
                });
            } else {
                value = null;
            }

            return new HttpResponse<T>(statusCode, value);
        }
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @Builder
    public static class HttpResponse<T> {
        private int code;
        private T response;
    }

}