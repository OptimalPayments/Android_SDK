package com.optimalpayments.common;

/**
 * Created by Asawari.Vaidya on 26-06-2015.
 */

import com.optimalpayments.annotations.Expose;
import com.optimalpayments.common.impl.DomainObject;
import com.optimalpayments.common.impl.GenericBuilder;
import com.optimalpayments.common.impl.NestedBuilder;

public class CardExpiry implements DomainObject {

    @Expose
    private Integer month;
    @Expose
    private Integer year;

    /**
    * Get Card Expiry Month.
    *
    * @return Card Expiry Month.
    * */
    public final Integer getMonth() {
        return month;
    }

    /**
    * Set Card Expiry Month.
    *
    * @param month Card Expiry Month.
    * */
    public final void setMonth(final Integer month) {
        this.month = month;
    }

    /**
    * Get Card Expiry Year.
    *
    * @return Card Expiry Year.
    * */
    public final Integer getYear() {
        return year;
    }

    /**
    * Set Card Expiry Year.
    *
    * @param year Card Expiry Year.
    * */
    public final void setYear(final Integer year) {
        this.year = year;
    }

    /**
     * The sub-builder class for CardExpiry.
     *
     * @param <BLDRT> the parent builder
     */
    public static class CardExpiryBuilder<BLDRT extends GenericBuilder> extends
            NestedBuilder<CardExpiry, BLDRT> {

        private final CardExpiry cardExpiry = new CardExpiry();

        /**
         * Constructor.
         *
         * @param parent Parent object.
         */
        public CardExpiryBuilder(final BLDRT parent) {
            super(parent);
        }

        /**
         * Build this CardExpiry object.
         *
         * @return CardExpiry object.
         */
        @Override
        public final CardExpiry build() {
            return cardExpiry;
        }

        /**
         * Set the month property for Card Expiry.
         *
         * @param month Card Expiry Month.
         * @return CardExpiryBuilder object.
         */
        public final CardExpiryBuilder<BLDRT> month(final Integer month) {
            cardExpiry.setMonth(month);
            return this;
        }

        /**
         * Set the year property for Card Expiry.
         *
         * @param year Card Expiry Year.
         * @return CardExpiryBuilder object.
         */
        public final CardExpiryBuilder<BLDRT> year(final Integer year) {
            cardExpiry.setYear(year);
            return this;
        }
    }
} // end of class CardExpiry
