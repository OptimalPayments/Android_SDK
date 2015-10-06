package com.optimalpayments.common;

/**
 * Created by Asawari.Vaidya on 26-06-2015.
 */

import com.optimalpayments.common.impl.DomainObject;

public class Link implements DomainObject {

    private String rel;
    private String href;

    /**
    * Get Rel.
    *
    * @return Rel.
    * */
    public final String getRel() {
        return rel;
    }

    /**
    * Set Rel.
    *
    * @param rel Rel.
    * */
    public final void setRel(final String rel) {
        this.rel = rel;
    }

    /**
    * Get Href.
    *
    * @return Href.
    * */
    public final String getHref() {
        return href;
    }

    /**
    * Set Href.
    *
    * @param href Href.
    * */
    public final void setHref(final String href) {
        this.href = href;
    }

} // end of class Link
