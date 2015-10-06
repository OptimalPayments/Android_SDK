package com.optimalpayments;

/**
 * Created by Asawari.Vaidya on 26-06-2015.
 */

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.io.IOException;
import java.io.OutputStream;

import javax.net.ssl.HttpsURLConnection;

import android.util.Base64;
import java.net.URL;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.optimalpayments.Utils.Constants;
import com.optimalpayments.Utils.Deserialize;
import com.optimalpayments.Utils.Serialize;
import com.optimalpayments.common.OptimalException;
import com.optimalpayments.common.impl.BaseDomainObject;
import com.optimalpayments.common.impl.Request;
import com.optimalpayments.customervault.CustomerVaultService;
import com.optimalpayments.customervault.SingleUseToken;

public class OptimalApiClient {

    // Credentials
    private final String apiEndPoint;
    private final String keyId;
    private final String keyPassword;
    private String accountNumber;

    // Customer Vault Service object
    private CustomerVaultService customerVaultService;

    // Set the timeout in milliseconds until a connection is established.
    // The default value is zero, that means the timeout is not used.
    private static final int TIMEOUT_CONNECTION = 10*1000;

    // Set the default read timeout
    // in milliseconds which is the timeout for waiting for data.
    private static final int READ_TIMEOUT = 10*1000;

    // Log tag
    private static final String LOG_OPTIMAL_API_CLIENT = "OPTIMAL API CLIENT";

    /**
     * Constructor 1
     *
     * @param keyId Api Key
     * @param keyPassword Api Password
     * @param environment Environment(TEST/LIVE)
     * */
    public OptimalApiClient(
            final String keyId,
            final String keyPassword,
            final Environment environment) {

        this.keyId = keyId;
        this.keyPassword = keyPassword;

        apiEndPoint = environment.getUrl();
    } // end of Constructor

    /**
     * Constructor 2
     *
     * @param keyId Api Key
     * @param keyPassword Api Password
     * @param environment Environment(TEST/LIVE)
     * @param accountNumber Merchant Account Number
     * */
    public OptimalApiClient(
            final String keyId,
            final String keyPassword,
            final Environment environment,
            final String accountNumber) {

        this(keyId, keyPassword, environment);
        this.setAccount(accountNumber);

    } // end of Constructor

    /**
     * Set Account Number
     *
     * @param accountNumber Merchant Account Number
     * */
    private void setAccount(String accountNumber) {
        this.accountNumber = accountNumber;
    } // end of setAccount()

    /**
     * Get Account Number
     *
     * @return Merchant Account Number
     * */
    public final String getAccount() {
        return accountNumber;
    } // end of getAccount()

    /**
     * Customer Vault Service
     *
     * @return Customer Vault Service object
     * */
    public final CustomerVaultService customerVaultService() {
        if (null == customerVaultService) {
            customerVaultService = new CustomerVaultService(this);
        }
        return customerVaultService;
    } // end of customerVaultService()

    /**
     * Create a connection from a Request and return a specified type.
     *
     * @param request The request object to be processed.
     * @return SingleUseToken Object
     * @throws IOException
     * @throws OptimalException
     * */
    public final <T extends BaseDomainObject> SingleUseToken processRequest(
            final Request request)
            throws IOException, OptimalException {

        SingleUseToken sObj;
        final URL url = new URL(request.buildUri(apiEndPoint));

        // LOG
        if(Constants.TAG_LOG)
            Log.i(LOG_OPTIMAL_API_CLIENT, "Beta URL.");

        final HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

        // LOG
        if(Constants.TAG_LOG)
            Log.i(LOG_OPTIMAL_API_CLIENT, "HTTPS Connection established successfully.");

        try {
            connection.setConnectTimeout(TIMEOUT_CONNECTION);
            connection.setReadTimeout(READ_TIMEOUT);

            connection.setDoOutput(true);
            connection.setDoInput(true);

            connection.addRequestProperty("Authorization", "Basic " + getAuthenticatedString());
            connection.setRequestMethod(request.getMethod().toString());
            connection.addRequestProperty("Content-Type", "application/json");

            final OutputStream os = connection.getOutputStream();
            final DataOutputStream dos = new DataOutputStream(os);
            try {
                dos.write(serializeObject(request).getBytes("UTF-8"));
                dos.flush();
            } finally {
                dos.close();
                os.close();
            }
            sObj = getReturnObject(connection);
            return sObj;
        }
        finally {
            connection.disconnect();

            // LOG
            if(Constants.TAG_LOG)
                Log.i(LOG_OPTIMAL_API_CLIENT, "HTTPS Connection Disconnected.");
        }
    } // end of processRequest()

    /**
     * Get the return object back from the connection.
     *
     * @param <T> an extension of BaseDomainObject
     * @param connection HttpsUrlConnection
     * @return Single Use Token object
     * @throws IOException
     */
    private <T extends BaseDomainObject> SingleUseToken getReturnObject(
            HttpsURLConnection connection)
            throws IOException {

        SingleUseToken sObj = null;
        try {
            InputStream is = connection.getInputStream();
            try {
                sObj = deserializeStream(is);
            } finally {
                is.close();
            }
        } catch (IOException ioExp) {
            InputStream is = connection.getErrorStream();
            try {
                sObj = deserializeStream(is);
                return sObj;
            } finally {
                is.close();
            }
        }
        return sObj;
    } // end of getReturnObject()

    /**
     *  Take an input stream and return the JSON de-serialized version.
     *
     *  @param is InputStream Object
     *  @return Single Use Token object
     * */
    private <T extends BaseDomainObject> SingleUseToken deserializeStream(
            InputStream is) {

        SingleUseToken sObjResp = null;
        BufferedReader br = null;
        StringBuilder sbResponse = new StringBuilder();

        String line;
        try {
            br = new BufferedReader(new InputStreamReader(is));
            line = br.readLine();
            while (line != null) {
                sbResponse.append(line);
                line = br.readLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ioExp) {
                    // LOG
                    if(Constants.TAG_LOG)
                        Log.e(LOG_OPTIMAL_API_CLIENT, ioExp.getMessage());
                }
            }
        }

        // LOG
        if(Constants.TAG_LOG)
            Log.i(LOG_OPTIMAL_API_CLIENT, "Deserialization done successfully.");

        try {
            JSONObject jsonResponse = new JSONObject(sbResponse.toString());
            sObjResp = (new Deserialize()).fromJSON(jsonResponse);
        }
        catch (JSONException jsonExp) {
            // LOG
            if(Constants.TAG_LOG)
                Log.e(LOG_OPTIMAL_API_CLIENT, jsonExp.getMessage());
        }
        return sObjResp;
    }

    /**
     * Take a Domain Object, and JSON Serialize it.
     *
     * @param request The request object to be processed.
     * @return JSON Encoding of the Request Object
     * */
    private String serializeObject(Request request) {

        /**
         * Lint warning here:
         * method invocation serializeObj.toString() may produce Null Pointer Exception.
         */
        JSONObject serializeObj = null;
        try {
            serializeObj = new JSONObject();
            serializeObj = (new Serialize()).toJSON((SingleUseToken) request.getBody());

            // LOG
            if(Constants.TAG_LOG)
                Log.i(LOG_OPTIMAL_API_CLIENT, "Serialization done successfully.");
        }
        catch(NullPointerException npExp) {
            // LOG
            if(Constants.TAG_LOG)
                Log.e(LOG_OPTIMAL_API_CLIENT, npExp.getMessage());
        }
        return (serializeObj != null ? serializeObj.toString() : null);
    } // end of serializeObject()

    /**
     * Gets the Base64 Encoded Authenticated string.
     *
     * @return Base64 Encoded Authentication String
     * @throws IOException
     * */
    private String getAuthenticatedString() throws IOException {

        return Base64.encodeToString((keyId + ':' + keyPassword).getBytes("UTF-8"),
                Base64.NO_WRAP);
    } // end of getAuthenticatedString()

} // end of class OptimalApiClient
