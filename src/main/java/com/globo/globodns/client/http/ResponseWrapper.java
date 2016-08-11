package com.globo.globodns.client.http;

import com.google.api.client.http.HttpResponse;

public class ResponseWrapper {
    int statusCode;
    String content;

    public ResponseWrapper(int statusCode, String content) {
        this.statusCode = statusCode;
        this.content = content;
    }
    public ResponseWrapper(HttpResponse response) {
        this.statusCode = response.getStatusCode();
        try {
            this.content = response.parseAsString();

        } catch (Exception e) {
            throw new RuntimeException("Error trying parse response to string: " + e.getMessage(), e);
        }
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
