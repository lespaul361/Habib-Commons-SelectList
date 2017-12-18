/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.habibsweb.commons.components.event;

import java.util.EventObject;

/**
 *
 * @author Charles Hamilton
 */
public class ScrollableListItemSelectionEvent extends EventObject {

    /**
     * Item has been selected
     */
    public static final int SELECTED = 1;
    /**
     * item has been unselected
     */
    public static final int UNSELECTED = 0;

    private final int index;
    private final int mode;
    /**
     * Constructs a new <code>ScrollableListItemSelectionEvent</code>
     *
     * @param source the <code>ScrollableList</code> that created the event
     * @param selection the mode of selected or unselected
     * @param index the index of the item
     */
    public ScrollableListItemSelectionEvent(Object source,int selection, int index) {
        super(source);
        this.index=index;
        this.mode=selection;
    }

    /**
     * Gets the index of the item
     * @return the index of the item
     */
    public int getIndex() {
        return index;
    }

    /**
     * Gets the selection mode
     * @return the selection mode
     */
    public int getMode() {
        return mode;
    }

}
