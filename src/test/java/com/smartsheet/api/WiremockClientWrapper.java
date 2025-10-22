package com.smartsheet.api;

public class WiremockClientWrapper {
    private final Smartsheet smartsheet;
    private final WiremockClient wiremockClient;

    public WiremockClientWrapper(Smartsheet smartsheet, WiremockClient wiremockClient) {
        this.smartsheet = smartsheet;
        this.wiremockClient = wiremockClient;
    }

    public Smartsheet getSmartsheet() {
        return smartsheet;
    }

    public WiremockClient getWiremockClient() {
        return wiremockClient;
    }
}