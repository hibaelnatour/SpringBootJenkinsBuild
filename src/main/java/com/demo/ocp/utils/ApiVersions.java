package com.demo.ocp.utils;

public enum ApiVersions {
    V1("v1/");

    private final String prefix;

    ApiVersions(final String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return this.prefix;
    }
}
