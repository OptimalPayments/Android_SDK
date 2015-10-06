package com.optimalpayments.common.impl;

/**
 * Created by Asawari.Vaidya on 26-06-2015.
 */

/*
 * This class will be extended by any builders nested within another builder.
 * done() will return the parent builder
 * */

public abstract class NestedBuilder<RTNT extends DomainObject,
        BLDRT extends GenericBuilder>
        implements BaseBuilder<RTNT>{

    private final BLDRT parent;

    public final BLDRT done() {
        return parent;
    } // end of done()

    public NestedBuilder(final BLDRT parent) {
        this.parent = parent;
    } // end of Constructor

} // end of class NestedBuilder
