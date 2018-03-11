package io.github.openunirest.http;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.Sets;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.ContentType;
import spark.Request;

import javax.servlet.MultipartConfigElement;
import javax.servlet.http.Part;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;

import static java.lang.System.getProperty;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class RequestCapture {
    public Map<String, String> headers = new LinkedHashMap<>();
    public Map<String, File> files = new LinkedHashMap<>();
    public Multimap<String, String> params = HashMultimap.create();
    public String body;

    public RequestCapture() {
    }

    public RequestCapture(Request req) {
        writeHeaders(req);
        writeQuery(req);
    }

    public void writeBody(Request req) {
        this.body = req.body();
        parseBodyToFormParams(req);
    }

    private void parseBodyToFormParams(Request req) {
        URLEncodedUtils.parse(req.body() , Charset.forName("UTF-8"))
                .forEach(p -> {
                    params.put(p.getName(), p.getValue());
                });
    }

    public void writeMultipart(Request req) {
        req.raw().setAttribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement(getProperty("java.io.tmpdir")));

        try {
            for (Part p : req.raw().getParts()) {
                if (p.getContentType().equals(ContentType.APPLICATION_OCTET_STREAM.getMimeType())) {
                    buildFilePart(p);
                } else {
                    buildFormPart(p);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void buildFormPart(Part p) throws IOException {
        java.util.Scanner s = new Scanner(p.getInputStream()).useDelimiter("\\A");
        String value = s.hasNext() ? s.next() : "";
        params.put(p.getName(), value);
    }

    public void buildFilePart(Part part) throws IOException {
        File file = new File();
        file.fileName = part.getSubmittedFileName();
        file.type = part.getContentType();
        file.inputName = part.getName();
        file.body = TestUtil.toString(part.getInputStream());

        files.put(file.fileName, file);
    }

    public void asserBody(String s) {
        assertEquals(s, body);
    }


    public static class File {
        public String fileName;
        public String type;
        public String inputName;
        public String body;

        public void assertBody(String content){
            assertEquals(content, body);
        }
    }

    private void writeQuery(Request req) {
        req.queryParams().forEach(q -> params.putAll(q, Sets.newHashSet(req.queryMap(q).values())));
    }

    private void writeHeaders(Request req) {
        req.headers().forEach(h -> headers.put(h, req.headers(h)));
    }

    public void assertHeader(String key, String value) {
        assertEquals("Expected Header Failed", value, headers.get(key));
    }

    public void assertParam(String key, String value) {
        assertThat("Expected Query or Form value", params.get(key), hasItem(value));
    }

    public File getFile(String fileName) {
        return files.get(fileName);
    }
}
