package com.optimalpayments.Utils;

/**
 * Created by Asawari.Vaidya on 14-09-2015.
 */

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

import com.optimalpayments.common.*;
import com.optimalpayments.customervault.BillingAddress;
import com.optimalpayments.customervault.Card;
import com.optimalpayments.customervault.SingleUseToken;

public class Deserialize {

    // Log tag
    private static final String LOG_DESERIALIZE = "DESERIALIZE";

    /*
     * Convert JSON Response to Java Object
     *
     * @param: Environment object, JSON Object
     * @return: SingleUseToken Object
     * */
    public SingleUseToken fromJSON(JSONObject jsonResponse) {

        SingleUseToken sObjResponse = new SingleUseToken();
        Card cardObjResp = new Card();
        CardExpiry cardExpResp = new CardExpiry();
        BillingAddress billingResp = new BillingAddress();
        com.optimalpayments.common.Error errorResp = new com.optimalpayments.common.Error();

        JSONObject jsonRespCard;
        JSONObject jsonRespCardExp;
        JSONObject jsonRespBilling;
        JSONObject jsonRespError;
        JSONArray jsonArrLinks;
        JSONArray jsonArrFieldErr;
        JSONArray jsonArrDetails;

        ArrayList<Link> arrLinks;
        ArrayList<FieldError> arrFieldErrors;
        ArrayList<String> arrDetails;

        try {

            // SingleUseToken Fields
            if(!jsonResponse.isNull(Constants.TAG_ID))
                sObjResponse.setId(jsonResponse.getString(Constants.TAG_ID));
            if(!jsonResponse.isNull(Constants.TAG_PROFILE_ID))
                sObjResponse.setProfileId(jsonResponse.getString(Constants.TAG_PROFILE_ID));
            if(!jsonResponse.isNull(Constants.TAG_PAYMENT_TOKEN))
                sObjResponse.setPaymentToken(jsonResponse.getString(Constants.TAG_PAYMENT_TOKEN));
            if(!jsonResponse.isNull(Constants.TAG_TIME_TO_LIVE_SECONDS))
                sObjResponse.setTimeToLiveSeconds(jsonResponse.getInt(Constants.TAG_TIME_TO_LIVE_SECONDS));

            // Card Fields
            if(!jsonResponse.isNull(Constants.TAG_CARD)) {
                jsonRespCard =  new JSONObject(jsonResponse.getString(Constants.TAG_CARD));

                if(!jsonRespCard.isNull(Constants.TAG_ID))
                    cardObjResp.setId(jsonRespCard.getString(Constants.TAG_ID));
                if(!jsonRespCard.isNull(Constants.TAG_NICK_NAME))
                    cardObjResp.setNickName(jsonRespCard.getString(Constants.TAG_NICK_NAME));
                if(!jsonRespCard.isNull(Constants.TAG_STATUS))
                    cardObjResp.setStatus(jsonRespCard.getString(Constants.TAG_STATUS));
                if(!jsonRespCard.isNull(Constants.TAG_MERCHANT_REF_NUM))
                    cardObjResp.setMerchantRefNum(jsonRespCard.getString(Constants.TAG_MERCHANT_REF_NUM));
                if(!jsonRespCard.isNull(Constants.TAG_HOLDER_NAME))
                    cardObjResp.setHolderName(jsonRespCard.getString(Constants.TAG_HOLDER_NAME));
                if(!jsonRespCard.isNull(Constants.TAG_CARD_NUM))
                    cardObjResp.setCardNum(jsonRespCard.getString(Constants.TAG_CARD_NUM));
                if(!jsonRespCard.isNull(Constants.TAG_CARD_BIN))
                    cardObjResp.setCardBin(jsonRespCard.getString(Constants.TAG_CARD_BIN));
                if(!jsonRespCard.isNull(Constants.TAG_CVV))
                    cardObjResp.setCvv(jsonRespCard.getString(Constants.TAG_CVV));
                if(!jsonRespCard.isNull(Constants.TAG_TRACK1))
                    cardObjResp.setTrack1(jsonRespCard.getString(Constants.TAG_TRACK1));
                if(!jsonRespCard.isNull(Constants.TAG_TRACK2))
                    cardObjResp.setTrack2(jsonRespCard.getString(Constants.TAG_TRACK2));
                if(!jsonRespCard.isNull(Constants.TAG_LAST_DIGITS))
                    cardObjResp.setLastDigits(jsonRespCard.getString(Constants.TAG_LAST_DIGITS));
                if(!jsonRespCard.isNull(Constants.TAG_CARD_TYPE))
                    cardObjResp.setCardType(jsonRespCard.getString(Constants.TAG_CARD_TYPE));
                if(!jsonRespCard.isNull(Constants.TAG_BILLING_ADDRESS_ID))
                    cardObjResp.setBillingAddressId(jsonRespCard.getString(Constants.TAG_BILLING_ADDRESS_ID));
                if(!jsonRespCard.isNull(Constants.TAG_DEFAULT_CARD_INDICATOR))
                    cardObjResp.setDefaultCardIndicator(Boolean.parseBoolean(jsonRespCard.getString(Constants.TAG_DEFAULT_CARD_INDICATOR)));
                if(!jsonRespCard.isNull(Constants.TAG_PAYMENT_TOKEN))
                    cardObjResp.setPaymentToken(jsonRespCard.getString(Constants.TAG_PAYMENT_TOKEN));
                if(!jsonRespCard.isNull(Constants.TAG_SINGLE_USE_TOKEN))
                    cardObjResp.setSingleUseToken(jsonRespCard.getString(Constants.TAG_SINGLE_USE_TOKEN));

                // Card Expiry Fields
                if(!jsonRespCard.isNull(Constants.TAG_CARD_EXPIRY)) {
                    jsonRespCardExp = new JSONObject(jsonRespCard.getString(Constants.TAG_CARD_EXPIRY));

                    if(!jsonRespCardExp.isNull(Constants.TAG_MONTH))
                        cardExpResp.setMonth(jsonRespCardExp.getInt(Constants.TAG_MONTH));
                    if(!jsonRespCardExp.isNull(Constants.TAG_YEAR))
                        cardExpResp.setYear(jsonRespCardExp.getInt(Constants.TAG_YEAR));

                }

                // Billing Details Fields
                if(!jsonRespCard.isNull(Constants.TAG_BILLING_ADDRESS)) {
                    jsonRespBilling = new JSONObject(jsonRespCard.getString(Constants.TAG_BILLING_ADDRESS));

                    if(!jsonRespBilling.isNull(Constants.TAG_NICK_NAME))
                        billingResp.setNickName(jsonRespBilling.getString(Constants.TAG_NICK_NAME));
                    if(!jsonRespBilling.isNull(Constants.TAG_STREET))
                        billingResp.setStreet(jsonRespBilling.getString(Constants.TAG_STREET));
                    if(!jsonRespBilling.isNull(Constants.TAG_STREET2))
                        billingResp.setStreet2(jsonRespBilling.getString(Constants.TAG_STREET2));
                    if(!jsonRespBilling.isNull(Constants.TAG_CITY))
                        billingResp.setCity(jsonRespBilling.getString(Constants.TAG_CITY));
                    if(!jsonRespBilling.isNull(Constants.TAG_STATE))
                        billingResp.setState(jsonRespBilling.getString(Constants.TAG_STATE));
                    if(!jsonRespBilling.isNull(Constants.TAG_COUNTRY))
                        billingResp.setCountry(jsonRespBilling.getString(Constants.TAG_COUNTRY));
                    if(!jsonRespBilling.isNull(Constants.TAG_ZIP))
                        billingResp.setZip(jsonRespBilling.getString(Constants.TAG_ZIP));
                    if(!jsonRespBilling.isNull(Constants.TAG_PHONE))
                        billingResp.setPhone(jsonRespBilling.getString(Constants.TAG_PHONE));
                }

                // Set Internal Objects
                cardObjResp.setCardExpiry(cardExpResp);
                cardObjResp.setBillingAddress(billingResp);
                sObjResponse.setCard(cardObjResp);
            }

            // Set Error Fields
            if(!jsonResponse.isNull(Constants.TAG_ERROR)) {
                jsonRespError = new JSONObject(jsonResponse.getString(Constants.TAG_ERROR));

                // Set Error Code and Error Message
                if(!jsonRespError.isNull(Constants.TAG_CODE))
                    errorResp.setCode(jsonRespError.getString(Constants.TAG_CODE));
                if(!jsonRespError.isNull(Constants.TAG_MESSAGE))
                    errorResp.setMessage(jsonRespError.getString(Constants.TAG_MESSAGE));

                // Set Error Links
                if(!jsonRespError.isNull(Constants.TAG_LINKS)) {
                    arrLinks = new ArrayList<Link>();
                    jsonArrLinks = jsonRespError.getJSONArray(Constants.TAG_LINKS);

                    for (int i = 0; i < jsonArrLinks.length(); i++) {
                        JSONObject jsonLinks = jsonArrLinks.getJSONObject(i);

                        Link linkObj = new Link();
                        linkObj.setHref(jsonLinks.getString(Constants.TAG_HREF));
                        linkObj.setRel(jsonLinks.getString(Constants.TAG_REL));

                        arrLinks.add(linkObj);
                    }
                    errorResp.setLinks(arrLinks);
                }

                // Set Field Errors
                if(!jsonRespError.isNull(Constants.TAG_FIELD_ERRORS)) {

                    arrFieldErrors = new ArrayList<FieldError>();
                    jsonArrFieldErr = jsonRespError.getJSONArray(Constants.TAG_FIELD_ERRORS);

                    for (int i = 0; i < jsonArrFieldErr.length(); i++) {
                        JSONObject jsonLinks = jsonArrFieldErr.getJSONObject(i);

                        FieldError linkObj = new FieldError();
                        linkObj.setField(jsonLinks.getString(Constants.TAG_FIELD));
                        linkObj.setError(jsonLinks.getString(Constants.TAG_ERROR));

                        arrFieldErrors.add(linkObj);
                    }
                    errorResp.setFieldErrors(arrFieldErrors);
                }

                // Set Error Details
                if(!jsonRespError.isNull(Constants.TAG_DETAILS)) {
                    arrDetails = new ArrayList<String>();
                    jsonArrDetails = jsonRespError.getJSONArray(Constants.TAG_DETAILS);

                    for (int i = 0; i < jsonArrDetails.length(); i++)
                        arrDetails.add(jsonArrDetails.getJSONObject(i).toString());

                    errorResp.setDetails(arrDetails);
                }
                sObjResponse.setError(errorResp);
            }
        }
        catch (JSONException jsonExp) {
            // LOG
            if(Constants.TAG_LOG)
                Log.e(LOG_DESERIALIZE, jsonExp.toString());
        }
        return sObjResponse;
    } // end of fromJSON()
} // end of class Deserialize
