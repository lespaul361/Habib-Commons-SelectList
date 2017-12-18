/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.habibsweb.commons.components.list;

import java.awt.datatransfer.DataFlavor;

/**
 *
 * @author David Hamilton
 */
public class DefaultScrollableListDataFlavor extends DataFlavor {

    private final Object val;

    /**
     * Constructs a new <code>DefaultScrollableListDataFlavor</code>
     *
     * @param value the value of the item
     */
    public DefaultScrollableListDataFlavor(Object value) {
        super(Object.class, null);
        this.val = value;
    }

    /**
     *Gets the value 
     * @return the value
     */
    public Object getValue() {
        return this.val;
    }
}
