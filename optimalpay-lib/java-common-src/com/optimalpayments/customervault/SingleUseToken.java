package com.optimalpayments.customervault;

/**
 * Created by Asawari.Vaidya on 26-06-2015.
 */

import com.optimalpayments.annotations.Expose;
import com.optimalpayments.common.Error;
import com.optimalpayments.common.impl.BaseBuilder;
import com.optimalpayments.common.impl.BaseDomainObject;

public class SingleUseToken implements BaseDomainObject {

    @Expose
    private String id;
    private Card card;
    private String profileId;
    private String paymentToken;
    private Integer timeToLiveSeconds;
    private Error error;
	private String mConnectivityError;

    /**
    * Get ID
    *
    * @return Single Use Token Id.
    * */
    public final String getId() {
        return id;
    }

    /**
    * Set ID
    *
    * @param id Sets the Single Use Token id.
    * */
    public final void setId(final String id) {
        this.id = id;
    }

    /**
    * Get Card
    *
    * @return Object of Card.
    * */
    public final Card getCard() {
        return card;
    }

    /**
    * Set Card
    *
    * @param card Card Object
    * */
    public final void setCard(final Card card) {
        this.card = card;
    }

    /**
    * Get Profile Id
    *
    * @return Profile Id.
    * */
    public final String getProfileId() {
        return profileId;
    }

    /**
    * Set Profile Id
    *
    * @param profileId Profile Id
    * */
    public final void setProfileId(final String profileId) {
        this.profileId = profileId;
    }

    /**
    * Get Payment Token
    *
    * @return Payment Token
    * */
    public final String getPaymentToken() {
        return paymentToken;
    }

    /**
    * Set Payment Token
    *
    * @param paymentToken Payment Token
    * */
    public final void setPaymentToken(final String paymentToken) {
        this.paymentToken = paymentToken;
    }

    /**
    * Get Time to Live Seconds
    *
    * @return Time to live seconds.
    * */
    public final Integer getTimeToLiveSeconds() {
        return timeToLiveSeconds;
    }

    /**
    * Set Time to Live Seconds
    *
    * @param timeToLiveSeconds Time to live seconds
    * */
    public final void setTimeToLiveSeconds(final Integer timeToLiveSeconds) {
        this.timeToLiveSeconds = timeToLiveSeconds;
    }

    /**
    * Get Error
    *
    * @return Object of Error.
    * */
    public Error getError() {
        return error;
    }

    /**
    * Set Error
    *
    * @param error Error object
    * */
    public final void setError(final Error error) {
        this.error = error;
    }

	 public String getConnectivityError() {
		return mConnectivityError;
	}

	public void setConnectivityError(String mConnectivityError) {
		this.mConnectivityError = mConnectivityError;
	}

    /**
     * Get a SingleUseTokenBuilder
     *
     * @return SingleUseTokenBuilder object.
     * */
    public static SingleUseTokenBuilder builder() {
        return new SingleUseToken().new SingleUseTokenBuilder();
    } // end of builder()


    /**
     * The Builder class for SingleUseToken
     * */
    public class SingleUseTokenBuilder implements BaseBuilder<SingleUseToken> {

        private final SingleUseToken singleUseToken = new SingleUseToken();
        private Card.CardBuilder<SingleUseTokenBuilder> cardBuilder;


        @Override
        public final SingleUseToken build() {
            if(null != cardBuilder) {
                singleUseToken.setCard(cardBuilder.build());
            }
            return singleUseToken;
        } // end of build()

        /**
         * Set the Id property
         *
         * @param id Single Use Token Id
         * @return SingleUseTokenBuilder object.
         * */
        public final SingleUseTokenBuilder id(final String id) {
            singleUseToken.setId(id);
            return this;
        }

        /**
         * Set the ProfileId property
         *
         * @param profileId Profile Id
         * @return SingleUseTokenBuilder object.
         * */
        public final SingleUseTokenBuilder profileId(final String profileId) {
            singleUseToken.setProfileId(profileId);
            return this;
        }

        /**
         * Set the property Card
         *
         * @return Card.CardBuilder<SingleUseTokenBuilder> object.
         * */
        public final Card.CardBuilder<SingleUseTokenBuilder> card() {
            if(null == cardBuilder) {
                cardBuilder = new Card.CardBuilder<SingleUseTokenBuilder>(this);
            }
            return cardBuilder;
        }

        /**
         * Set the paymentToken property
         *
         * @param paymentToken Payment Token
         * @return SingleUseTokenBuilder object.
         * */
        public final SingleUseTokenBuilder payment(final String paymentToken) {
            singleUseToken.setPaymentToken(paymentToken);
            return this;
        }

        /**
         * Set the timeToLiveSeconds property
         *
         * @param timeToLiveSeconds Time to live seconds
         * @return SingleUseTokenBuilder object.
         * */
        public final SingleUseTokenBuilder timeToLiveSeconds(
                final Integer timeToLiveSeconds) {
            singleUseToken.setTimeToLiveSeconds(timeToLiveSeconds);
            return this;
        }

    } // end of class SingleUseTokenBuilder

} // end of class SingleUseToken
