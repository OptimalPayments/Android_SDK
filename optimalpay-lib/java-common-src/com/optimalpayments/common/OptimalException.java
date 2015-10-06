package com.optimalpayments.common;

/**
 * Created by Asawari.Vaidya on 26-06-2015.
 */

import com.optimalpayments.common.impl.BaseDomainObject;

public class OptimalException extends Exception {

	private static final long serialVersionUID = 1L;
	private final BaseDomainObject rawResponse;
    private final String code;

    /**
    * Constructor 1
    *
    * @param message Exception Message.
    * */
    public OptimalException(final String message) {
        this(message, null);
    } // end of Constructor

    /**
    * Constructor 2
    *
    * @param message Exception Message.
    * @param cause Exception Cause.
    * */
    public OptimalException(final String message, final Throwable cause) {
        super(message, cause);
        rawResponse = null;
        code = null;
    } // end of Constructor

    /**
     * Constructor 3
     *
     * @param obj Base Domain Object.
     * @param cause Exception cause.
     * */
    public OptimalException(final BaseDomainObject obj, final Throwable cause) {
        super(null == obj || null == obj.getError()
                        ? "An unknown error occurred"
                        : obj.getError().getMessage(),
                cause);
        rawResponse = obj;
        code = null == obj || null == obj.getError()
                ? null
                : obj.getError().getCode();
    } // end of Constructor

    /**
     * Get Raw Response.
     * @return Raw Response.
     */
    public final BaseDomainObject getRawResponse() {
        return rawResponse;
    } // end of getRawResponse()

    /**
     * Get Code.
     * @return Code.
     */
    public final String getCode() {
        return code;
    } // end of getCode()

} // end of class OptimalException
