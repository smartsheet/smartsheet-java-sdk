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

package com.smartsheet.api;

import org.apache.commons.io.IOUtils;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * A server for answering HTTP requests with test response data.
 */
public class HttpTestServer {
    private Server server;
    private String requestBody;
    private int port;
    private String contentType;
    private byte[] responseBody;
    private int status;
    private String lastRequestUrl;

    public HttpTestServer() {
        this.port = 9090;
        this.contentType = "application/json";
    }

    public HttpTestServer(String mockData) {
        this();
        setResponseBody(mockData);
    }

    public void start() throws Exception {
        server = new Server(port);
        server.setHandler(getMockHandler());
        server.start();
        status = HttpServletResponse.SC_OK;
    }

    /**
     * Creates an {@link AbstractHandler handler} returning an arbitrary String as a response.
     *
     * @return never <code>null</code>.
     */
    public Handler getMockHandler() {
        Handler handler = new AbstractHandler() {

            //@Override
            public void handle(String target, Request baseRequest, HttpServletRequest request,
                               HttpServletResponse response) throws IOException, ServletException {

                setRequestBody(IOUtils.toString(baseRequest.getInputStream()));
                lastRequestUrl = baseRequest.getOriginalURI();
                response.setStatus(getStatus());
                response.setContentType(getContentType());

                byte[] body = getResponseBody();

                response.setContentLength(body.length);
                IOUtils.write(body, response.getOutputStream());

                baseRequest.setHandled(true);
            }
        };
        return handler;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return this.status;
    }

    public String getLastRequestUrl() {
        return this.lastRequestUrl;
    }

    public void stop() throws Exception {
        server.stop();
    }

    public void setResponseBody(byte[] responseBody) {
        this.responseBody = responseBody;
    }

    public void setResponseBody(String responseBody) {
        setResponseBody(responseBody.getBytes());
    }

    public void setResponseBody(File file) throws IOException {
        InputStream is = new FileInputStream(file);
        setResponseBody(IOUtils.toByteArray(is));
        is.close();
    }

    public byte[] getResponseBody() {
        return responseBody;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public String getRequestBody() {
        return requestBody;
    }

    protected Server getServer() {
        return server;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
