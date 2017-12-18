/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.habibsweb.commons.components.event;

import javax.swing.event.ChangeEvent;

/**
 *
 * @author Charles Hamilton
 */
public interface ItemEditorListener extends java.util.EventListener {

    /**
     * This tells the listeners the editor has ended editing
     *
     * @param e a <code>ChangeEvent</code>
     */
    public void editingStopped(ChangeEvent e);

    /**
     * This tells the listeners the editor has canceled editing
     *
     * @param e a <code>ChangeEvent</code>
     */
    public void editingCanceled(ChangeEvent e);
}
