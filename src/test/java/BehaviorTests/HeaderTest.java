package BehaviorTests;

import io.github.openunirest.http.Headers;
import io.github.openunirest.http.HttpResponse;
import io.github.openunirest.http.JsonNode;
import io.github.openunirest.http.Unirest;
import io.github.openunirest.request.GetRequest;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static util.TestUtil.mapOf;

public class HeaderTest extends BddTest {
    @Test
    public void testCustomUserAgent() {
        Unirest.get(MockServer.GET)
                .header("user-agent", "hello-world")
                .asObject(RequestCapture.class)
                .getBody()
                .assertHeader("User-Agent", "hello-world");
    }

    @Test
    public void testBasicAuth() {
        Unirest.get(MockServer.GET)
                .basicAuth("user", "test")
                .asObject(RequestCapture.class)
                .getBody()
                .assertHeader("Authorization", "Basic dXNlcjp0ZXN0")
                .assertBasicAuth("user", "test");
    }

    @Test
    public void unicodeBasicAuth() {
        Unirest.get(MockServer.GET)
                .basicAuth("こんにちは", "こんにちは")
                .asObject(RequestCapture.class)
                .getBody()
                .assertHeader("Authorization", "Basic 44GT44KT44Gr44Gh44GvOuOBk+OCk+OBq+OBoeOBrw==")
                .assertBasicAuth("こんにちは", "こんにちは");
    }

    @Test
    public void testDefaultHeaders() {
        Unirest.setDefaultHeader("X-Custom-Header", "hello");
        Unirest.setDefaultHeader("user-agent", "foobar");

        HttpResponse<JsonNode> jsonResponse = Unirest.get(MockServer.GET).asJson();

        parse(jsonResponse)
                .assertHeader("X-Custom-Header", "hello")
                .assertHeader("User-Agent", "foobar");

        jsonResponse = Unirest.get(MockServer.GET).asJson();
        parse(jsonResponse)
                .assertHeader("X-Custom-Header", "hello")
                .assertHeader("User-Agent", "foobar");

        Unirest.clearDefaultHeaders();

        jsonResponse = Unirest.get(MockServer.GET).asJson();
        parse(jsonResponse)
                .assertNoHeader("X-Custom-Header");
    }

    @Test
    public void testCaseInsensitiveHeaders() {
        GetRequest request = Unirest.get(MockServer.GET)
                .header("Name", "Marco");

        assertEquals(1, request.getHeaders().size());
        assertEquals("Marco", request.getHeaders().get("name").get(0));
        assertEquals("Marco", request.getHeaders().get("NAme").get(0));
        assertEquals("Marco", request.getHeaders().get("Name").get(0));

        parse(request.asJson())
                .assertHeader("Name", "Marco");

        request = Unirest.get(MockServer.GET).header("Name", "Marco").header("Name", "John");
        assertEquals(1, request.getHeaders().size());
        assertEquals("Marco", request.getHeaders().get("name").get(0));
        assertEquals("John", request.getHeaders().get("name").get(1));
        assertEquals("Marco", request.getHeaders().get("NAme").get(0));
        assertEquals("John", request.getHeaders().get("NAme").get(1));
        assertEquals("Marco", request.getHeaders().get("Name").get(0));
        assertEquals("John", request.getHeaders().get("Name").get(1));
    }

    @Test
    public void testHeaderNamesCaseSensitive() {
        // Verify that header names are the same as server (case sensitive)
        final Headers headers = new Headers();
        headers.put("Content-Type", Arrays.asList("application/json"));

        assertEquals("Only header \"Content-Type\" should exist", null, headers.getFirst("cOnTeNt-TyPe"));
        assertEquals("Only header \"Content-Type\" should exist", null, headers.getFirst("content-type"));
        assertEquals("Only header \"Content-Type\" should exist", "application/json", headers.getFirst("Content-Type"));
    }

    @Test
    public void canPassHeadersAsMap() {
        Unirest.post(MockServer.POST)
                .headers(mapOf("one", "foo", "two", "bar", "three", null))
                .asObject(RequestCapture.class)
                .getBody()
                .assertHeader("one", "foo")
                .assertHeader("two", "bar")
                .assertHeader("three", "");
    }

    @Test
    public void basicAuthOnPosts() {
        Unirest.post(MockServer.POST)
                .basicAuth("user", "test")
                .asObject(RequestCapture.class)
                .getBody()
                .assertHeader("Authorization", "Basic dXNlcjp0ZXN0")
                .assertBasicAuth("user", "test");
    }

    @Test
    public void willNotCacheBasicAuth() {
        Unirest.get(MockServer.GET)
                .basicAuth("foo", "bar")
                .asObject(RequestCapture.class)
                .getBody()
                .assertBasicAuth("foo", "bar");

        Unirest.get(MockServer.GET)
                .basicAuth("baz", "qux")
                .asObject(RequestCapture.class)
                .getBody()
                .assertBasicAuth("baz", "qux");
    }
}
