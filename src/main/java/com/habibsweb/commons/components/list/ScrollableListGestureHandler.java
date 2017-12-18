/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.habibsweb.commons.components.list;

import com.habibsweb.commons.components.ScrollableList;
import java.awt.Container;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;

/**
 * This class is to act a gesture for doing drang and drop with
 * <code>ScrollableList</code> components
 *
 * @author David Hamilton
 */
public class ScrollableListGestureHandler implements DragGestureListener, DragSourceListener {

    private Container parent;
    private ScrollableList child;

    /**
     * Constructs a new <code>ScrollableListGestureHandler</code>
     *
     * @param child the <code>ScrollableList</code> this is attached to
     */
    public ScrollableListGestureHandler(ScrollableList child) {
        this.child = child;
    }

    /**
     * Sets the <code>Container</code> for the <code>ScrollableList</code>
     *
     * @param parent the <code>Container</code> for the
     * <code>ScrollableList</code>
     */
    public void setParent(Container parent) {
        this.parent = parent;
    }

    /**
     * Gets the <code>Container</code> for the <code>ScrollableList</code>
     *
     * @return the <code>Container</code> for the <code>ScrollableList</code>
     */
    public Container getParent() {
        return parent;
    }
///
/// DragGestureListener
///

    @Override
    public void dragGestureRecognized(DragGestureEvent dge) {

    }

///
/// DragSourceListener
///
    @Override
    public void dragEnter(DragSourceDragEvent dsde) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void dragOver(DragSourceDragEvent dsde) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void dropActionChanged(DragSourceDragEvent dsde) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void dragExit(DragSourceEvent dse) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void dragDropEnd(DragSourceDropEvent dsde) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
