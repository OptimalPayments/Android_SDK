package com.optimalpayments;

/**
 * Created by Asawari.Vaidya on 26-06-2015.
 */

public enum Environment {

    LIVE("https://api.netbanx.com"),
    TEST("https://api.test.netbanx.com");

    private final String url;

    /**
     * Constructor
     *
     * @param url Api URL.
     */
    Environment(String url) {
        this.url = url;
    } // end of Constructor

    /**
     * Return Url
     * @return Url for specified environment.
     */
    public String getUrl() {
        return url;
    }
} // end of enum Environment
