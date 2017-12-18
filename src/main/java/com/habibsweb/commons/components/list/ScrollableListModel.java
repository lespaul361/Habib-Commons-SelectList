/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.habibsweb.commons.components.list;

import com.habibsweb.commons.components.event.ScrollableListModelListener;
import java.beans.PropertyChangeListener;
import java.util.Comparator;

/**
 * The interface for the model supported by <code>ScrollableList</code>. Custom
 * models should extend the <code>AbstractScrollableListModel</code> class.
 *
 * @author Charles Hamilton
 */
public interface ScrollableListModel {
   public static final String PROP_SELECTIONMODE = "PROP_SELECTIONMODE";
    /**
     * Allows 1 item selected at a time
     */
    public static int SELECTION_MODE_SINGLE = 0;
    /**
     * Allows multiple items to be selected when clicked
     */
    public static int SELECTION_MODE_MULTI_CLICK = 1;
    /**
     * Allows multiple items to be selected when control key or shift key is
     * held down
     */
    public static int SELECTION_MODE_MULTI_KEYDOWN = 2;

    /**
     * Returns the number of items in the model.
     *
     * @return the number of items in the model
     */
    public int getItemCount();

    /**
     * Gets if a item is editable.
     *
     * @param itemIndex the index of the item.
     * @return a <code>boolean</code> specifying if an item is editable
     */
    public boolean isItemEditable(int itemIndex);

    /**
     * Get the item located at the specified index.
     *
     * @param index the index of the item
     * @return the item located at the specified index.
     * @throws ArrayIndexOutOfBoundsException if the index is not in the array
     */
    public Object getValueAt(int index) throws ArrayIndexOutOfBoundsException;

    /**
     * Sets the value at the specified index.
     *
     * @param aValue the new value
     * @param index the index of the item to set
     * @throws ArrayIndexOutOfBoundsException if the index is not in the array
     */
    public void setValueAt(Object aValue, int index) throws ArrayIndexOutOfBoundsException;

    /**
     *
     * Adds a listener to the list that is notified each time a change to the
     * model occurs.
     *
     * @param l the ScrollableListModelListener
     */
    public void addScrollableListModelListener(ScrollableListModelListener l);

    /**
     *
     * Removes a listener to the list that is notified each time a change to the
     * model occurs.
     *
     * @param l the ScrollableListModelListener
     */
    public void removeScrollableListModelListener(ScrollableListModelListener l);

    /**
     * Adds an item to the <code>ScrollableList</code>
     *
     * @param item to be added to the <code>ScrollableList</code>
     */
    public void addItem(Object item);

    /**
     * Removes an item to the <code>ScrollableList</code>
     *
     * @param item to be removed from the <code>ScrollableList</code>
     */
    public void removeItem(Object item);

    /**
     * Removes an item to the <code>ScrollableList</code>
     *
     * @param index of the item to be removed from the
     * <code>ScrollableList</code>
     */
    public void removeItem(int index);

    /**
     * Sorts the items in the ScrollableList
     *
     * @param comparator the <code>Comparator</code> to use to sort the
     * <code>ScrollableList</code>
     */
    public void sort(Comparator comparator);

    /**
     * Gets the selection mode used
     *
     * @return the selection mode used
     * <p>
     * SELECTION_MODE_SINGLE = 0<br>
     * SELECTION_MODE_MULTI_CLICK = 1<br>
     * SELECTION_MODE_MULTI_KEYDOWN = 2<br>
     *
     */
    public int getSelectionMode();

    /**
     * Sets the selection mode used
     *
     * @param selectionMode the selection mode to be used
     * <p>
     * SELECTION_MODE_SINGLE = 0<br>
     * SELECTION_MODE_MULTI_CLICK = 1<br>
     * SELECTION_MODE_MULTI_KEYDOWN = 2<br>
     */
    public void setSelectionMode(int selectionMode);

    /**
     * Gets an array of the currently selected items
     *
     * @return an array of the currently selected items
     */
    public Object[] getSelectedItems();

    /**
     * Sets the selected status of an item
     *
     * @param item the item to change the selection status on
     * @param isSelected the selection status
     * @throws ArrayIndexOutOfBoundsException if the index is not in the array
     */
    public void setItemSelection(Object item, boolean isSelected)throws ArrayIndexOutOfBoundsException;
    
    /**
     * Sets the selected status of an item
     *
     * @param index the index of the item to change the selection status on
     * @param isSelected the selection status
     * @throws ArrayIndexOutOfBoundsException if the index is not in the array
     */
    public void setItemSelectionAt(int index, boolean isSelected)throws ArrayIndexOutOfBoundsException;

    /**
     * Gets the index of the item
     *
     * @param item the item to find
     * @return the index of the item
     */
    public int getItemIndex(Object item);

    /**
     * Gets an array of the data items
     *
     * @return an array of the data items
     */
    public Object[] getItems();
    
    /**
     * Adds a listener to listen for property changes
     * @param l a listener to listen for property changes
     */
    public void addPropertyChangeListener(PropertyChangeListener l);
    
    /**
     * Removes a listener to listen for property changes
     * @param l a listener to listen for property changes
     */
    public void removePropertyChangeListener(PropertyChangeListener l);
}
