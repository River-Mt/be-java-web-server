package http.request;

import http.HttpHeaders;
import http.Uri;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class HttpRequest {

    private static final Logger logger = LoggerFactory.getLogger(HttpRequest.class);

    private static final String ENTER = "\n";

    private final HttpStartLine startLine;
    private final HttpHeaders httpHeaders;
    private final HttpRequestBody requestBody;

    private HttpRequest(HttpStartLine startLine, HttpHeaders headers, HttpRequestBody requestBody) {
        this.startLine = startLine;
        this.httpHeaders = headers;
        this.requestBody = requestBody;
    }

    public static HttpRequest from(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
        String startLine = br.readLine();
        String extracted = extractHeaders(br);
        String[] headers = extracted.split(ENTER);

        return HttpRequest.from(HttpStartLine.from(startLine), HttpHeaders.from(headers), br);

    }

    public static HttpRequest from(HttpStartLine startLine, HttpHeaders headers, BufferedReader br) throws IOException {
        HttpMethod httpMethod = startLine.getMethod();

        if (httpMethod.equals(HttpMethod.GET)) {
            HttpRequestBody httpRequestBody = HttpRequestBody.createEmptyRequestBody();
            return new HttpRequest(startLine, headers, httpRequestBody);
        }

        String queryString = br.readLine();
        HttpRequestBody httpRequestBody = HttpRequestBody.createRequestBody(queryString);
        return new HttpRequest(startLine, headers, httpRequestBody);
    }


    private static String extractHeaders(BufferedReader br) throws IOException {
        StringBuilder sb = new StringBuilder();
        String line;

        while ((line = br.readLine()) != null && !line.isEmpty()) {
            sb.append(line).append(System.lineSeparator());
        }

        return sb.toString();
    }

    public HttpStartLine getStartLine() {
        return startLine;
    }

    public HttpMethod getMethod() {
        return startLine.getMethod();
    }

    public HttpRequestBody getRequestBody() {
        return requestBody;
    }

    public Uri getUri() {
        return startLine.getUri();
    }

    public Map<String, String> getHttpHeaders() {
        return httpHeaders.getHeaders();
    }

}
