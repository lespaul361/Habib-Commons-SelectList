/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.habibsweb.commons.components.event;

import java.util.EventListener;

/**
 * Interface to listen for changed in a <code>ScrollableListModel</code>
 *
 * @author Charles Hamilton
 */
public interface ScrollableListModelListener extends EventListener {

    /**
     * Fired when there is a change with the <code>ScrollableListModel</code>
     *
     * @param evt a <code>ScrollableListModelEvent</code> class
     */
    public void scrollableListModelChanged(ScrollableListModelEvent evt);
}
