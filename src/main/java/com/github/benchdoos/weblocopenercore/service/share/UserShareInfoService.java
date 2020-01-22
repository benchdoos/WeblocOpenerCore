package com.github.benchdoos.weblocopenercore.service.share;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benchdoos.weblocopenercore.core.constants.StringConstants;
import com.github.benchdoos.weblocopenercore.preferences.PreferencesManager;
import com.github.benchdoos.weblocopenercore.utils.CoreUtils;
import lombok.extern.log4j.Log4j2;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.UUID;

@Log4j2
public class UserShareInfoService {

    public void sendInfo(@NotNull UUID uuid) throws IOException {
        log.info("Sharing user info. UUID: {}", uuid);
        final HttpPost post = new HttpPost(String.format(StringConstants.SHARE_USER_INFO_URL, uuid.toString()));
        final HttpEntity entity = prepareUserInfo();

        post.setEntity(entity);

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(post)) {
            log.info("Http request was send successfully. (code: {}, response: {})",
                    response.getStatusLine().getStatusCode(),
                    response.getEntity());
        }

    }

    private HttpEntity prepareUserInfo() throws IOException {
        final BasicHttpEntity result = new BasicHttpEntity();

        final UserLoginDto userLoginDto = prepareUserLoginDto();

        log.info("Prepared user info: {}", userLoginDto);

        ObjectMapper mapper = new ObjectMapper();
        final byte[] bytes = mapper.writeValueAsBytes(userLoginDto);

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);

        result.setContent(byteArrayInputStream);
        return result;
    }

    private UserLoginDto prepareUserLoginDto() {
        final UserLoginDto result = new UserLoginDto();

        final Locale currentLocale = Locale.getDefault();

        result.setApplicationVersion(CoreUtils.getApplicationVersionString());
        result.setCountryName(currentLocale.getDisplayCountry());
        result.setSelectedLanguage(PreferencesManager.getLocale().getLanguage());

        return result;
    }
}
