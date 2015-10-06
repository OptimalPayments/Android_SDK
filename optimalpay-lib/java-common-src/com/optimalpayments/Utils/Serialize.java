package com.optimalpayments.Utils;

/**
 * Created by Asawari.Vaidya on 14-09-2015.
 */

import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

import com.optimalpayments.customervault.SingleUseToken;

public class Serialize {

    // Log tag
    private static final String LOG_SERIALIZE = "SERIALIZE";

    /*
     * Convert a Java Object to JSON Object
     *
     * @param: Environment object, SingleUseToken Object
     * @return: JSON Object
     * */
    public JSONObject toJSON(SingleUseToken singleUseToken) {

        // Initialize JSON Objects
        JSONObject jsonResponse = new JSONObject();
        JSONObject jsonCard = new JSONObject();
        JSONObject jsonCardExp = new JSONObject();
        JSONObject jsonBilling = new JSONObject();
        JSONObject jsonErr = new JSONObject();
        try {
            //  Error Fields
            if (singleUseToken.getError() != null)
            {
                if(singleUseToken.getError().getCode() != null)
                    jsonErr.put(Constants.TAG_CODE, singleUseToken.getError().getCode());
                if(singleUseToken.getCard().getBillingAddress().getNickName() != null)
                    jsonErr.put(Constants.TAG_MESSAGE, singleUseToken.getError().getMessage());
            }
            // Billing Address Fields
            if (singleUseToken.getCard().getBillingAddress() != null)
            {
                if(singleUseToken.getCard().getBillingAddress().getNickName() != null)
                    jsonBilling.put(Constants.TAG_NICK_NAME, singleUseToken.getCard().getBillingAddress().getNickName());
                if(singleUseToken.getCard().getBillingAddress().getStreet() != null)
                    jsonBilling.put(Constants.TAG_STREET, singleUseToken.getCard().getBillingAddress().getStreet());
                if(singleUseToken.getCard().getBillingAddress().getStreet2() != null)
                    jsonBilling.put(Constants.TAG_STREET2, singleUseToken.getCard().getBillingAddress().getStreet2());
                if(singleUseToken.getCard().getBillingAddress().getCity() != null)
                    jsonBilling.put(Constants.TAG_CITY, singleUseToken.getCard().getBillingAddress().getCity());
                if(singleUseToken.getCard().getBillingAddress().getState() != null)
                    jsonBilling.put(Constants.TAG_STATE, singleUseToken.getCard().getBillingAddress().getState());
                if(singleUseToken.getCard().getBillingAddress().getCountry() != null)
                    jsonBilling.put(Constants.TAG_COUNTRY, singleUseToken.getCard().getBillingAddress().getCountry());
                if(singleUseToken.getCard().getBillingAddress().getZip() != null)
                    jsonBilling.put(Constants.TAG_ZIP, singleUseToken.getCard().getBillingAddress().getZip());
                if(singleUseToken.getCard().getBillingAddress().getPhone() != null)
                    jsonBilling.put(Constants.TAG_PHONE, singleUseToken.getCard().getBillingAddress().getPhone());
            }

            // Card Expiry Fields
            if (singleUseToken.getCard().getCardExpiry() != null)
            {
                if(singleUseToken.getCard().getCardExpiry().getMonth() != null)
                    jsonCardExp.put(Constants.TAG_MONTH, singleUseToken.getCard().getCardExpiry().getMonth());
                if(singleUseToken.getCard().getCardExpiry().getYear() != null)
                    jsonCardExp.put(Constants.TAG_YEAR, singleUseToken.getCard().getCardExpiry().getYear());
            }

            // Card Fields
            if (singleUseToken.getCard() != null)
            {
                if(singleUseToken.getCard().getId() != null)
                    jsonCard.put(Constants.TAG_ID, singleUseToken.getCard().getId());
                if(singleUseToken.getCard().getNickName() != null)
                    jsonCard.put(Constants.TAG_NICK_NAME, singleUseToken.getCard().getNickName());
                if(singleUseToken.getCard().getStatus() != null)
                    jsonCard.put(Constants.TAG_STATUS, singleUseToken.getCard().getStatus());
                if(singleUseToken.getCard().getMerchantRefNum() != null)
                    jsonCard.put(Constants.TAG_MERCHANT_REF_NUM, singleUseToken.getCard().getMerchantRefNum());
                if(singleUseToken.getCard().getHolderName() != null)
                    jsonCard.put(Constants.TAG_HOLDER_NAME, singleUseToken.getCard().getHolderName());
                if(singleUseToken.getCard().getCardNum() != null)
                    jsonCard.put(Constants.TAG_CARD_NUM, singleUseToken.getCard().getCardNum());
                if(singleUseToken.getCard().getCardBin() != null)
                    jsonCard.put(Constants.TAG_CARD_BIN, singleUseToken.getCard().getCardBin());
                if(singleUseToken.getCard().getCvv() != null)
                    jsonCard.put(Constants.TAG_CVV, singleUseToken.getCard().getCvv());
                if(singleUseToken.getCard().getTrack1() != null)
                    jsonCard.put(Constants.TAG_TRACK1, singleUseToken.getCard().getTrack1());
                if(singleUseToken.getCard().getTrack2() != null)
                    jsonCard.put(Constants.TAG_TRACK2, singleUseToken.getCard().getTrack2());
                if(singleUseToken.getCard().getLastDigits() != null)
                    jsonCard.put(Constants.TAG_LAST_DIGITS, singleUseToken.getCard().getLastDigits());
                if(singleUseToken.getCard().getCardExpiry() != null)
                    jsonCard.put(Constants.TAG_CARD_EXPIRY, jsonCardExp);
                if(singleUseToken.getCard().getCardType() != null)
                    jsonCard.put(Constants.TAG_CARD_TYPE, singleUseToken.getCard().getCardType());
                if(singleUseToken.getCard().getBillingAddress() != null)
                    jsonCard.put(Constants.TAG_BILLING_ADDRESS, jsonBilling);
                if(singleUseToken.getCard().getBillingAddressId() != null)
                    jsonCard.put(Constants.TAG_BILLING_ADDRESS_ID, singleUseToken.getCard().getBillingAddressId());
                if(singleUseToken.getCard().getDefaultCardIndicator() != null)
                    jsonCard.put(Constants.TAG_DEFAULT_CARD_INDICATOR, singleUseToken.getCard().getDefaultCardIndicator());
                if(singleUseToken.getCard().getPaymentToken() != null)
                    jsonCard.put(Constants.TAG_PAYMENT_TOKEN, singleUseToken.getCard().getPaymentToken());
                if(singleUseToken.getCard().getSingleUseToken() != null)
                    jsonCard.put(Constants.TAG_SINGLE_USE_TOKEN, singleUseToken.getCard().getSingleUseToken());
            }

            // Single Use Token Fields
            if (singleUseToken != null)
            {
                if(singleUseToken.getId() != null)
                    jsonResponse.put(Constants.TAG_ID, singleUseToken.getId());
                if(singleUseToken.getCard() != null)
                    jsonResponse.put(Constants.TAG_CARD, jsonCard);
                if(singleUseToken.getProfileId() != null)
                    jsonResponse.put(Constants.TAG_PROFILE_ID, singleUseToken.getProfileId());
                if(singleUseToken.getPaymentToken() != null)
                    jsonResponse.put(Constants.TAG_PAYMENT_TOKEN, singleUseToken.getPaymentToken());
                if(singleUseToken.getTimeToLiveSeconds() != null)
                    jsonResponse.put(Constants.TAG_TIME_TO_LIVE_SECONDS, singleUseToken.getTimeToLiveSeconds());
                if(singleUseToken.getError() != null)
                    jsonResponse.put(Constants.TAG_ERROR, jsonErr);
            }
        }
        catch (JSONException jsonExp) {
            // LOG
            if(Constants.TAG_LOG)
                Log.e(LOG_SERIALIZE, jsonExp.toString());
        }
        return jsonResponse;
    } // end of toJSON()
} // end of class Serialize
