/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.habibsweb.commons.components.list;

import com.habibsweb.commons.components.ScrollableList;
import java.awt.Component;
import java.beans.PropertyChangeListener;

/**
 * This interface defines the method required by any object that would like to
 * be a renderer for items in a <code>ScrollableList</code>.
 *
 * @author Charles Hamilton
 */
public interface ScrollableListRenderer {

    /**
     * Returns the component used for drawing the list item. This method is used
     * to configure the renderer appropriately before drawing.
     *
     * @param scrollableList the <code>ScrollableList</code> this is rendering
     * for. Returned <code>Components</code> must implements
     * <code>ScrollableListRenderer</code>
     * @param value the value of the item to be rendered
     * @param isSelected sets if the item is selected or not
     * @param index the index of the item to be rendered
     * @return the <code>Component</code> that is rendered. Returned
     * <code>Components</code> must implements
     * <code>ScrollableListRenderer</code>
     */
    Component getScrollableListItemRendererComponent(ScrollableList scrollableList, Object value,
            boolean isSelected, int index);

    /**
     * Used to update the value of the renderer
     *
     * @param value the new value
     */
    public void upDateValue(Object value);

    /**
     * Updates the rendered object to be select or not
     *
     * @param b selected or not
     */
    public void setSelected(boolean b);

    /**
     * Gets the margin to the right
     *
     * @return the margin to the right
     */
    public int getRightMargin();

    /**
     * Gets the margin to the left
     *
     * @return the margin to the left
     */
    public int getLeftMargin();

    /**
     * Gets the margin to the bottom
     *
     * @return the margin to the bottom
     */
    public int getBottomMargin();

    /**
     * t
     * Gets the margin to the top
     *
     * @return the margin to the top
     */
    public int getTopMargin();

    /**
     * Sets the margin to the right
     *
     *
     * @param value the margin to the right
     */
    public void setRightMargin(int value);

    /**
     * Sets the margin to the left
     *
     * @param value the margin to the left
     */
    public void setLeftMargin(int value);

    /**
     * sets the margin to the bottom
     *
     * @param value the margin to the bottom
     */
    public void setBottomMargin(int value);

    /**
     *
     * Sets the margin to the top
     *
     * @param value the margin to the top
     */
    public void getTopMargin(int value);

    /**
     * Adds a listener to listen for property changes
     *
     * @param l <code>PropertyChangeListener</code>
     */
    public void addPropertyChangeListener(PropertyChangeListener l);

    /**
     * Removes a listener to listen for property changes
     *
     * @param l <code>PropertyChangeListener</code>
     */
    public void removePropertyChangeListener(PropertyChangeListener l);

    /**
     * Sets if this <code>ScrollableListRenderer</code> supports drag and drop
     *
     * @param b enables or disables drag and drop
     */
    public void setDragEnabled(boolean b);

    /**
     * Gets if this <code>ScrollableListRenderer</code> supports drag and drop
     *
     * @return if this <code>ScrollableListRenderer</code> supports drag and
     * drop
     */
    public boolean getDragEnabled();
}
