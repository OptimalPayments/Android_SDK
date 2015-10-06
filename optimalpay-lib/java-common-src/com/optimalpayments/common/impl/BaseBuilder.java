package com.optimalpayments.common.impl;

/**
 * Created by Asawari.Vaidya on 26-06-2015.
 */

public interface BaseBuilder<RTNT extends DomainObject> extends GenericBuilder {
    public abstract RTNT build();
} // end of class BaseBuilder
