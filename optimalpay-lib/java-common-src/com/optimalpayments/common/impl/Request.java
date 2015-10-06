package com.optimalpayments.common.impl;

/**
 * Created by Asawari.Vaidya on 26-06-2015.
 */

import java.net.URLEncoder;
import java.util.HashMap;

import java.io.UnsupportedEncodingException;
import com.optimalpayments.common.OptimalException;

public final class Request {

    private BaseDomainObject body;
    private RequestType method;
    private HashMap<String, String> queryStr;
    private String uri;

    //private JsonSerializer serializer;

    private Request() {

    } // end of Constructor

    /*
    * Get Body
    *
    * @return: BaseDomainObject Object
    * */
    public final BaseDomainObject getBody() {
        return body;
    } // end of getBody()

    /*
    * Get Method
    *
    * @return: RequestType Object
    * */
    public final RequestType getMethod() {
        return method;
    } // end of getMethod()

    /*
    * Build Uri
    *
    * @param: String
    * @return: String
    * */
    public final String buildUri(final String apiEndPoint) throws OptimalException {
        if(null == uri) {
            throw new OptimalException("You must specify the uri when building this object");
        }
        // If this is a url, lets make sure that it contains our endpoint
        if(uri.contains("://")) {
            if(uri.indexOf(apiEndPoint) != 0) {
                throw new OptimalException("Unexpected endpoint in url: "
                        + uri
                        + "expected: "
                        + apiEndPoint);
            }
            return uri;
        }
        return apiEndPoint + "/" + uri +
                (null == queryStr || queryStr.isEmpty() ? "" : "?" + buildQueryString());

    } // end of buildUri()

    /*
     * Builds the Query String
     *
     * @return: String
     * @throws: RuntimeException
     * */
    private String buildQueryString() throws RuntimeException {
        final StringBuilder response = new StringBuilder();
        if(null != queryStr) {
            for(java.util.Map.Entry<String, String> entry : queryStr.entrySet()) {
                if(response.length() > 0) {
                    response.append("&");
                }

                try {
                    final String value = entry.getValue();
                    response.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                    response.append("=");
                    response.append(null != value? URLEncoder.encode(value, "UTF-8") : "");
                } // end of try block
                catch(UnsupportedEncodingException ex) {
                    throw new RuntimeException("This method requires UTF-8 encoding support", ex);
                } // end of catch block
            }
        }
        return response.toString();
    } // end of buildQueryString()

    /*
     * Allow easy building of request objects.
     * */
    public static RequestBuilder builder() {
        return new RequestBuilder();
    } // end of builder()

	/*
	public JsonSerializer getSerializer() {
	    return serializer;
	  }
	*/

    /*
     * RequestBuilder Class
     * */
    public static class RequestBuilder {

        private final Request request = new Request();

        /*
         * Build this request
         * */
        public final Request build() {
            return request;
        } // end of build()

        /*
         * Set the uri
         * */
        public final RequestBuilder uri(final String uri) {
            request.uri = uri;
            return this;
        } // end of uri()

        /*
         * Set the body
         * */
        public final RequestBuilder body(final BaseDomainObject body) {
            request.body = body;
            return this;
        } // end of body()

        /*
         * Set the method
         * */
        public final RequestBuilder method(final RequestType method) {
            request.method = method;
            return this;
        } // end of method()

        /*
         * Set the Query String
         * */
        public final RequestBuilder queryStr(final HashMap<String, String> queryStr) {
            request.queryStr = queryStr;
            return this;
        } // end of queryStr()

    } // end of class RequestBuilder

    /*
     * The Enum RequestType
     * */
    public enum RequestType{
        DELETE,
        GET,
        POST,
        PUT
    }

} // end of class Request

