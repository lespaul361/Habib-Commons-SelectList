/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.habibsweb.commons.components.event;

/**
 * Interface for a listener that listens for selection changes
 *
 * @author Charles Hamilton
 */
public interface ScrollableListItemSelectionListener extends java.util.EventListener {

    /**
     * A method for items that have their selection status changed
     *
     * @param evt event for selection change
     */
    public void scrollableListItemSelectionChanged(ScrollableListItemSelectionEvent evt);
}
