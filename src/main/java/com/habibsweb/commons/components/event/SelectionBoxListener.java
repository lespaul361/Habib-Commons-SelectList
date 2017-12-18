/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.habibsweb.commons.components.event;

import java.util.EventListener;

/**
 * Interface to handle events from the <code>SelectionBox</code>
 *
 * @author David Hamilton
 */
public interface SelectionBoxListener extends EventListener {

    /**
     * Event fired when the <code>SelectionBox</code> is created, resized, or
     * completed
     *
     * @param e a <code>SelectionBoxEvent</code>
     */
    public void selectionBoxSelectionChange(SelectionBoxEvent e);
}
