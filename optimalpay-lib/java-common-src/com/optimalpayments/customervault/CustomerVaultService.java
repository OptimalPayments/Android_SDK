package com.optimalpayments.customervault;

/**
 * Created by Asawari.Vaidya on 26-06-2015.
 */

import java.io.IOException;

import com.optimalpayments.OptimalApiClient;
import com.optimalpayments.common.OptimalException;
import com.optimalpayments.common.impl.Request;

public class CustomerVaultService {

    // End point values for API's URL
    private static final String URI = "customervault/v1";
    private static final String SINGLE_USE_TOKEN_PATH = "/singleusetokens";

    // Object of Class OptimalApiClient
    private final OptimalApiClient client;

    /**
    * Constructor
    *
    * @param client Optimal Api Client object.
    * */
    public CustomerVaultService(final OptimalApiClient client) {
        this.client = client;
    } // end of Constructor

    /**
     * Create SingleUseToken
     *
     * @param singleUseToken Single Use Token object.
     * @return SingleUseToken object
     * @throws IOException
     * @throws OptimalException
     * */
    public final SingleUseToken createSingleUseToken(
            final SingleUseToken singleUseToken)
            throws IOException, OptimalException {

        // Build request for API
        final Request request = Request.builder()
                .uri(prepareUri(URI + SINGLE_USE_TOKEN_PATH))
                .method(Request.RequestType.POST)
                .body(singleUseToken)
                .build();

        return (client.processRequest(request));
    } // end of createSingleUseToken()

    /**
     * Prepare Uri
     *
     * @param path Api Endpoint Path
     * @return Url
     * @throws OptimalException
     * */
    private String prepareUri(final String path) throws OptimalException {
        if(null == client.getAccount()) {
            throw new OptimalException("Missing or Invalid Account.");
        }
        return path;
    } // end of prepareUri()

} // end of class CustomerVaultService
