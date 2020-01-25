package com.github.benchdoos.weblocopenercore.service.feedback.images;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benchdoos.weblocopenercore.core.constants.StringConstants;
import lombok.extern.log4j.Log4j2;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Log4j2
public class ImgbbImageSender implements ImageSender {
    private final static String rootUrl = "https://api.imgbb.com/1/upload";

    @Override
    public List<ImageInfo> sendImages(List<String> base64ImagesList) {
        final List<ImageInfo> result = new ArrayList<>();
        if (base64ImagesList != null && base64ImagesList.size() > 0) {
            try {
                final String url = new URIBuilder(rootUrl).addParameter("key", StringConstants.IMGBB_API_KEY).toString();

                for (String base64Image : base64ImagesList) {
                    final HttpPost post = new HttpPost(url);
                    final List<NameValuePair> postParameters = new ArrayList<>();
                    postParameters.add(new BasicNameValuePair("image", base64Image));
                    post.setEntity(new UrlEncodedFormEntity(postParameters, StandardCharsets.UTF_8));

                    try {
                        final ImgbbResponseDto imgbbResponseDto = sendRequest(post);
                        log.info("Image was sent. Status: {}", imgbbResponseDto.getStatus());

                        if (imgbbResponseDto.getData() != null) {
                            final String imageUrl = imgbbResponseDto.getData().getUrlViewer();
                            final String imageDeleteUrl = imgbbResponseDto.getData().getDeleteUrl();
                            final ImageInfo imageInfo = new ImageInfo(imageUrl, imageDeleteUrl);

                            result.add(imageInfo);
                        }
                    } catch (IOException e) {
                        log.warn("Could not send image", e);
                    }
                }
            } catch (URISyntaxException e) {
                log.warn("Could not prepare url", e);
            }
        }

        return result;
    }

    /**
     * Sends request and return
     *
     * @param post method
     * @return response from ImgBB
     * @throws IOException if something is wrong
     */
    private ImgbbResponseDto sendRequest(HttpPost post) throws IOException {
        try (final CloseableHttpClient httpClient = HttpClients.createDefault();
             final CloseableHttpResponse response = httpClient.execute(post)) {

            final int statusCode = response.getStatusLine().getStatusCode();
            log.info("Http request sent. (code: {}, response: {})",
                    statusCode,
                    response.getEntity());
            final ObjectMapper mapper = new ObjectMapper();
            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            return mapper.readValue(response.getEntity().getContent(), ImgbbResponseDto.class);
        }
    }
}
