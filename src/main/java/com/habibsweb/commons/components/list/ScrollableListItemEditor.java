/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.habibsweb.commons.components.list;

import com.habibsweb.commons.components.ItemEditor;
import com.habibsweb.commons.components.ScrollableList;
import java.awt.Component;

/**
 * This interface defines the method any object that would like to be an editor
 * of values for components such as <code>ScrollableList</code>needs to
 * implement.
 *
 * @author David Hamilton
 */
public interface ScrollableListItemEditor extends ItemEditor {

    /**
     * Gets a component for editing an item in a <code>ScrollableList</code>.
     * Component must implement <code>ScrollableListItemEditor</code> interface
     *
     * @param scrollableList the list to use this editor for
     * @param value the original value of the object
     * @param index the index of the item in the
     * <code>ScrollableListModel</code>
     * @param isSelected sets if the item is currently selected or not
     * @return a component for editing an item in a <code>ScrollableList</code>.
     * Component must implement <code>ScrollableListItemEditor</code> interface
     */
    Component getScrollableListItemEditorComponent(ScrollableList scrollableList,
            Object value, int index, boolean isSelected);

    /**
     * Gets the number of clicks to show the editor. A value of -1 prevents will
     * not show the editor on item clicks
     *
     * @return the number of clicks to show the editor. A value of -1 prevents
     * will not show the editor on item clicks
     */
    public int getNumberOfClicksToStart();

    /**
     * Sets the number of clicks to show the editor. A value of -1 prevents will
     * not show the editor on item clicks
     *
     * @param clickCount the number of clicks to show the editor. A value of -1
     * prevents will not show the editor on item clicks
     */
    public void getNumberOfClicksToStart(int clickCount);
}
