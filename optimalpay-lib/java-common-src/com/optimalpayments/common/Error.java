package com.optimalpayments.common;

/**
 * Created by Asawari.Vaidya on 26-06-2015.
 */

import java.util.ArrayList;

import com.optimalpayments.common.impl.DomainObject;

public class Error implements DomainObject {

    private String code;
    private String  message;
    private ArrayList<String> details;
    private ArrayList<FieldError> fieldErrors;
    private ArrayList<Link> links;

    /**
    * Get Error Code.
    *
    * @return Error Code.
    * */
    public final String getCode() {
        return code;
    }

    /**
    * Set Error Code.
    *
    * @param code Error Code.
    * */
    public final void setCode(final String code) {
        this.code = code;
    }

    /**
    * Get Error Message.
    *
    * @return Error Message.
    * */
    public final String getMessage() {
        return message;
    }

    /**
    * Set Error Message.
    *
    * @param message Error Message
    * */
    public final void setMessage(final String message) {
        this.message = message;
    }

    /**
    * Get Error Details.
    *
    * @return Array List of String Objects.
    * */
    public final ArrayList<String> getDetails() {
        return details;
    }

    /**
    * Set Error Details.
    *
    * @param details Error Details.
    * */
    public final void setDetails(final ArrayList<String> details) {
        this.details = details;
    }

    /**
    * Get Field Errors.
    *
    * @return Array List of FieldError Objects.
    * */
    public final ArrayList<FieldError> getFieldErrors() {
        return fieldErrors;
    }

    /**
    * Set Field Errors.
    *
    * @param fieldErrors Field Errors.
    * */
    public final void setFieldErrors(final ArrayList<FieldError> fieldErrors) {
        this.fieldErrors = fieldErrors;
    }

    /**
    * Get Error Links.
    *
    * @return Array List of Link Objects.
    * */
    public final ArrayList<Link> getLinks() {
        return links;
    }

    /**
    * Set Error Links.
    *
    * @param links Error Links.
    * */
    public final void setLinks(final ArrayList<Link> links) {
        this.links = links;
    }
} // end of class Error
