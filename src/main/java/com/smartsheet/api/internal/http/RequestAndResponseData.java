/*
 * Copyright (C) 2025 Smartsheet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.smartsheet.api.internal.http;

import com.smartsheet.api.Trace;
import org.apache.http.Header;
import org.apache.http.client.methods.HttpRequestBase;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * a POJO from which is generated JSON from HTTP request/response pairs
 */
public class RequestAndResponseData {
    public abstract static class HttpPayloadData {
        Map<String, String> headers;
        String body;

        public String getBody() {
            return body;
        }

        public boolean hasBody() {
            return body != null;
        }

        public boolean hasHeaders() {
            return headers != null;
        }

        public Map<String, String> getHeaders() {
            return headers;
        }

        abstract static class Builder<T extends HttpPayloadData> {
            public void withHeaders() {
                // this is seaprate from addHeader in case headers were requested but none found
                if (getDataObject().headers == null) {
                    getDataObject().headers = new TreeMap<>();
                }
            }

            public Builder addHeader(String key, String val) {
                withHeaders();
                getDataObject().headers.put(key, val);
                return this;
            }

            public Builder setBody(String body) {
                getDataObject().body = body;
                return this;
            }

            public abstract T build();

            public abstract void reset();

            protected abstract T getDataObject();
        }
    }

    public static class RequestData extends HttpPayloadData {
        private String command;

        public String getCommand() {
            return command;
        }

        public static class Builder extends HttpPayloadData.Builder<RequestData> {
            private RequestData dataObject;

            @Override
            public void reset() {
                dataObject = null;
            }

            @Override
            protected RequestData getDataObject() {
                if (dataObject == null) {
                    dataObject = new RequestData();
                }
                return dataObject;
            }

            /**
             * Add a command
             */
            public HttpPayloadData.Builder withCommand(String command) {
                getDataObject().command = command;
                return this;
            }

            /**
             * Build the RequestData
             */
            public RequestData build() {
                try {
                    // if nothing was added then nothing was built (i.e., this can be null)
                    return dataObject;
                } finally {
                    reset();
                }
            }
        }
    }

    public static class ResponseData extends HttpPayloadData {
        private String status;

        public String getStatus() {
            return status;
        }

        public static class Builder extends HttpPayloadData.Builder<ResponseData> {
            private ResponseData dataObject;

            @Override
            public void reset() {
                dataObject = null;
            }

            @Override
            protected ResponseData getDataObject() {
                if (dataObject == null) {
                    dataObject = new ResponseData();
                }
                return dataObject;
            }

            /**
             * Add a status
             */
            public HttpPayloadData.Builder withStatus(String status) {
                getDataObject().status = status;
                return this;
            }

            /**
             * Build the ResponseData
             */
            public ResponseData build() {
                try {
                    // if nothing was added then nothing was built (i.e., this can be null)
                    return dataObject;
                } finally {
                    reset();
                }
            }
        }
    }

    private static int TRUNCATE_LENGTH = Integer.getInteger("Smartsheet.trace.truncateLen", 1024);
    private static final String NULL_STRING = "null";

    public final RequestData request;
    public final ResponseData response;

    private RequestAndResponseData(RequestData requestData, ResponseData responseData) {
        request = requestData;
        response = responseData;
    }

    @Override
    public String toString() {
        return toString(false);
    }

    /**
     * Convert to a String
     */
    public String toString(boolean pretty) {
        final String eol = pretty ? "\n" : "";
        final String indent = pretty ? "  " : "";
        final String doubleIndent = indent + indent;
        final String tripleIndent = doubleIndent + indent;

        StringBuilder buf = new StringBuilder();
        buf.append("{").append(eol);
        buf.append(indent).append("request:");
        if (request == null) {
            buf.append("null,").append(eol);
        } else {
            buf.append("{").append(eol);
            buf
                    .append(doubleIndent)
                    .append("command:'")
                    .append(request.getCommand())
                    .append("'")
                    .append(",")
                    .append(eol);
            if (request.hasHeaders()) {
                buf.append(doubleIndent).append("headers:");
                if (request.getHeaders() == null) {
                    buf.append(NULL_STRING);
                } else {
                    buf.append("{").append(eol);
                    for (Map.Entry<String, String> header : request.headers.entrySet()) {
                        buf
                                .append(tripleIndent)
                                .append("'")
                                .append(header.getKey())
                                .append("':'")
                                .append(header.getValue())
                                .append("'")
                                .append(",")
                                .append(eol);
                    }
                    buf.append(doubleIndent).append("}").append(",").append(eol);
                }
            }
            if (request.hasBody()) {
                buf.append(doubleIndent).append("body:");
                if (request.body == null) {
                    buf.append(NULL_STRING);
                } else {
                    buf.append("'").append(request.body).append("'");
                }
                buf.append(eol);
            }
            buf.append(indent).append("},").append(eol);
        }
        buf.append(indent).append("response:");
        if (response == null) {
            buf.append(NULL_STRING).append(eol);
        } else {
            buf.append("{").append(eol);
            buf.append(doubleIndent).append("status:'").append(response.getStatus()).append("',").append(eol);
            if (response.hasHeaders()) {
                buf.append(doubleIndent).append("headers:");
                if (response.getHeaders() == null) {
                    buf.append(NULL_STRING);
                } else {
                    buf.append("{").append(eol);
                    for (Map.Entry<String, String> header : response.headers.entrySet()) {
                        buf
                                .append(tripleIndent)
                                .append("'")
                                .append(header.getKey())
                                .append("':'")
                                .append(header.getValue())
                                .append("',").append(eol);
                    }
                    buf.append(doubleIndent).append("},").append(eol);
                }
            }
            if (response.hasBody()) {
                buf.append(doubleIndent).append("body:");
                if (response.body == null) {
                    buf.append(NULL_STRING);
                } else {
                    buf.append("'").append(response.body).append("'");
                }
                buf.append(eol);
            }
            buf.append(indent).append("}").append(eol);
        }
        buf.append("}");
        return buf.toString();
    }

    /**
     * factory method for creating a RequestAndResponseData object from request and response data with the specifid trace fields
     */
    public static RequestAndResponseData of(HttpRequestBase request, HttpEntitySnapshot requestEntity,
                                            HttpResponse response, HttpEntitySnapshot responseEntity,
                                            Set<Trace> traces) {
        RequestData.Builder requestBuilder = new RequestData.Builder();
        ResponseData.Builder responseBuilder = new ResponseData.Builder();

        if (request != null) {
            requestBuilder.withCommand(request.getMethod() + " " + request.getURI());
            boolean binaryBody = false;
            if (traces.contains(Trace.RequestHeaders) && request.getAllHeaders() != null) {
                requestBuilder.withHeaders();
                for (Header header : request.getAllHeaders()) {
                    String headerName = header.getName();
                    String headerValue = header.getValue();
                    if ("Authorization".equals(headerName) && headerValue.length() > 0) {
                        headerValue = "Bearer ****" + headerValue.substring(Math.max(0, headerValue.length() - 4));
                    } else if ("Content-Disposition".equals(headerName)) {
                        binaryBody = true;
                    }
                    requestBuilder.addHeader(headerName, headerValue);
                }
            }
            if (requestEntity != null) {
                if (traces.contains(Trace.RequestBody)) {
                    requestBuilder.setBody(binaryBody ? binaryBody(requestEntity) : getContentAsText(requestEntity));
                } else if (traces.contains(Trace.RequestBodySummary)) {
                    requestBuilder.setBody(binaryBody
                            ? binaryBody(requestEntity)
                            : truncateAsNeeded(getContentAsText(requestEntity), TRUNCATE_LENGTH));
                }
            }
        }
        if (response != null) {
            boolean binaryBody = false;
            responseBuilder.withStatus(response.getStatusText());
            if (traces.contains(Trace.ResponseHeaders) && response.getHeaders() != null) {
                responseBuilder.withHeaders();
                for (Map.Entry<String, String> header : response.getHeaders().entrySet()) {
                    String headerName = header.getKey();
                    String headerValue = header.getValue();
                    if ("Content-Disposition".equals(headerName)) {
                        binaryBody = true;
                    }
                    responseBuilder.addHeader(headerName, headerValue);
                }
            }
            if (responseEntity != null) {
                if (traces.contains(Trace.ResponseBody)) {
                    responseBuilder.setBody(binaryBody ? binaryBody(responseEntity) : getContentAsText(responseEntity));
                } else if (traces.contains(Trace.ResponseBodySummary)) {
                    responseBuilder.setBody(binaryBody
                            ? binaryBody(responseEntity)
                            : truncateAsNeeded(getContentAsText(responseEntity), TRUNCATE_LENGTH));
                }
            }
        }
        return new RequestAndResponseData(requestBuilder.build(), responseBuilder.build());
    }

    static String binaryBody(HttpEntitySnapshot entity) {
        return "**possibly-binary(type:" + entity.getContentType() + ", len:" + entity.getContentLength() + ")**";
    }

    /**
     * Get the Content as a String
     */
    public static String getContentAsText(HttpEntitySnapshot entity) {
        if (entity == null) {
            return "";
        }
        byte[] contentBytes = entity.getContentArray();
        String contentAsText;
        contentAsText = new String(contentBytes, StandardCharsets.UTF_8);
        return contentAsText;
    }

    /**
     * Truncate the string to the desired length if needed
     */
    public static String truncateAsNeeded(String string, int truncateLen) {
        if (truncateLen == -1) {
            return string;
        }
        truncateLen = Math.min(string.length(), truncateLen);
        String suffix = truncateLen < string.length() ? "..." : "";
        return string.substring(0, truncateLen) + suffix;
    }
}
