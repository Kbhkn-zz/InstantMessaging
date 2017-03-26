package com.instantmessaging;

import org.apache.commons.io.IOUtils;
import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class InstantMessagingApplicationTests {
    private final Logger log = LoggerFactory.getLogger(InstantMessagingApplicationTests.class);
    private final String SENDER_PHONE_NO = "05068842316";

    @Test
    public void testRegisterService() throws IOException {
        StringEntity entity = new StringEntity(SENDER_PHONE_NO, ContentType.create("plain/text", Consts.UTF_8));
        entity.setChunked(true);

        HttpPost request = new HttpPost("http://localhost:9999/api/service/register");
        request.setEntity(entity);

        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);

        log.info("Status --> {}", httpResponse.getStatusLine().getStatusCode());
        log.info("Response --> {}", IOUtils.toString(httpResponse.getEntity().getContent()));

        org.junit.Assert.assertEquals(httpResponse.getStatusLine().getStatusCode(), 200);
    }

    @Test
    public void testGetPKeyService() throws IOException {
        HttpUriRequest request = new HttpGet("http://localhost:9999/api/service/getPkey/" + SENDER_PHONE_NO);

        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);

        log.info("Status --> {}", httpResponse.getStatusLine().getStatusCode());
        log.info("Response --> {}", IOUtils.toString(httpResponse.getEntity().getContent()));

        org.junit.Assert.assertEquals(httpResponse.getStatusLine().getStatusCode(), 200);
    }
}
