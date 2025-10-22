package com.smartsheet.api;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.matching.RequestPatternBuilder;
import com.github.tomakehurst.wiremock.verification.LoggedRequest;
import com.smartsheet.api.internal.http.DefaultHttpClient;
import com.smartsheet.api.internal.json.JacksonJsonSerializer;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class WiremockClient {
    private final DefaultHttpClient client;
    private static final String BASE_URI = "http://localhost:8080/2.0/";

    public WiremockClient(Map<String, String> customClientHeaders) {
        List<BasicHeader> headers = customClientHeaders.entrySet().stream()
                .map(entry -> new BasicHeader(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
        RequestConfig config = RequestConfig.custom().build();
        client = new DefaultHttpClient(
                HttpClientBuilder.create()
                        .setDefaultRequestConfig(config)
                        .setDefaultHeaders(headers)
                        .build(),
                new JacksonJsonSerializer()
        );
    }

    public Smartsheet getSmartsheetClient(String accessToken) {
        return new SmartsheetBuilder()
                .setAccessToken(accessToken)
                .setBaseURI(BASE_URI)
                .setHttpClient(client)
                .build();
    }

    /**
     * Find a request with a given x-request-id header.
     *
     * @param requestId id of the request
     * @throws IllegalArgumentException if more than one request is found
     * @return the matched wiremock request or null if no request is found
     */
    public LoggedRequest findWiremockRequest(String requestId) {
        List<LoggedRequest> requests = WireMock.findAll(
                RequestPatternBuilder.newRequestPattern()
                        .withHeader("x-request-id", WireMock.equalTo(requestId))
        );

        if (requests.isEmpty()) {
            return null;
        }

        if (requests.size() > 1) {
            throw new IllegalArgumentException("More than one request found.");
        }

        return requests.get(0);
    }
}