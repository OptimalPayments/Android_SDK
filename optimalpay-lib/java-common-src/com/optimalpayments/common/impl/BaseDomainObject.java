package com.optimalpayments.common.impl;

/**
 * Created by Asawari.Vaidya on 26-06-2015.
 */

import com.optimalpayments.common.Error;

public interface BaseDomainObject extends DomainObject {
    Error getError();

} // end of interface BaseDOmainObject
