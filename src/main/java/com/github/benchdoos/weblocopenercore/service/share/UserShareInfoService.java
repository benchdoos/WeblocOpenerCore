package com.github.benchdoos.weblocopenercore.service.share;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benchdoos.weblocopenercore.core.constants.StringConstants;
import com.github.benchdoos.weblocopenercore.preferences.PreferencesManager;
import com.github.benchdoos.weblocopenercore.service.feedback.images.ImgbbResponseDto;
import com.github.benchdoos.weblocopenercore.utils.CoreUtils;
import com.github.benchdoos.weblocopenercore.utils.http.HttpUtils;
import lombok.extern.log4j.Log4j2;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BasicHttpEntity;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.UUID;

@Log4j2
public class UserShareInfoService {

    private final HttpUtils httpUtils = new HttpUtils<>(null);

    public void sendInfo(@NotNull UUID uuid) throws IOException {
        log.info("Sharing user info. UUID: {}", uuid);
        final String shareUserInfoUrl = getShareUserInfoUrl();
        final HttpPost post = new HttpPost(String.format(shareUserInfoUrl, uuid.toString()));
        final HttpEntity entity = prepareUserInfo();

        post.setHeader(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8");
        post.setEntity(entity);

        final int code = httpUtils.sendHttpRequest(post).getCode();
        log.info("Shared info about application. Status code: {}", code);
    }

    @NotNull
    private String getShareUserInfoUrl() {
        return PreferencesManager.isDevMode() ? StringConstants.SHARE_USER_INFO_DEV_MODE_URL : StringConstants.SHARE_USER_INFO_URL;
    }

    private HttpEntity prepareUserInfo() throws IOException {
        final BasicHttpEntity result = new BasicHttpEntity();

        final UserLoginDto userLoginDto = prepareUserLoginDto();

        log.info("Prepared user info: {}", userLoginDto);

        final ObjectMapper mapper = new ObjectMapper();
        final byte[] bytes = mapper.writeValueAsBytes(userLoginDto);

        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);

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
