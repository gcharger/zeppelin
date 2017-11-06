package org.apache.zeppelin.configuration;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.zeppelin.conf.ZeppelinConfiguration;
import org.apache.zeppelin.rest.AbstractTestRestApi;
import org.apache.zeppelin.security.DirAccessTest;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class RequestHeaderSizeTest extends AbstractTestRestApi {

        @Test
        public void testHeaderTooLarge_cause_413() throws Exception {
        synchronized (this) {
            AbstractTestRestApi.startUp(DirAccessTest.class.getSimpleName());
            HttpClient httpClient = new HttpClient();
            GetMethod getMethod = new GetMethod(getUrlToTest() + "/app/");
            String headerValue = RandomStringUtils.randomAlphanumeric(15000);
            getMethod.setRequestHeader("too_large_header",headerValue);
            httpClient.executeMethod(getMethod);
            AbstractTestRestApi.shutDown();
            assertThat(getMethod.getStatusCode(), is(HttpStatus.SC_REQUEST_TOO_LONG));
        }
    }

    @Test
    public void testRequestHeaderSizeLimit() throws Exception {
        synchronized (this) {
            System.setProperty(ZeppelinConfiguration.ConfVars.ZEPPELIN_SERVER_DEFAULT_DIR_ALLOWED.getVarName(), "20000");
            AbstractTestRestApi.startUp(DirAccessTest.class.getSimpleName());
            HttpClient httpClient = new HttpClient();
            GetMethod getMethod = new GetMethod(getUrlToTest() + "/app/");
            String headerValue = RandomStringUtils.randomAlphanumeric(15000);
            getMethod.setRequestHeader("too_large_header",headerValue);
            httpClient.executeMethod(getMethod);
            AbstractTestRestApi.shutDown();
            assertThat(getMethod.getStatusCode(), is(HttpStatus.SC_OK));
        }
    }
}
