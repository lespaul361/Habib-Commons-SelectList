/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.habibsweb.commons.components.list;

import com.habibsweb.commons.components.event.ScrollableListModelEvent;
import com.habibsweb.commons.components.event.ScrollableListModelListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author Charles Hamilton
 */
public abstract class AbstractScrollableListModel implements ScrollableListModel, Serializable {

    /**
     * List of values
     */
    protected final List<Object> items = new ArrayList<>();

    /**
     * List of <code>ScrollableListModelListeners</code>
     */
    protected final List<ScrollableListModelListener> modelListeners = new ArrayList<>();

    /**
     * The selection mode used
     */
    private int selectionMode = ScrollableListModel.SELECTION_MODE_SINGLE;

    /**
     * List of selected values
     */
    protected final List<Object> selectedItems = new ArrayList<>();
    private final transient PropertyChangeSupport propertyChangeSupport = new java.beans.PropertyChangeSupport(this);

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public abstract boolean isItemEditable(int itemIndex);

    @Override
    public Object getValueAt(int index) throws ArrayIndexOutOfBoundsException {
        return items.get(index);
    }

    @Override
    public Object[] getItems() {
        return items.toArray();
    }

    @Override
    public void setValueAt(Object aValue, int index) throws ArrayIndexOutOfBoundsException {
        if ((items.size() < index) || (index < 0)) {
            throw new ArrayIndexOutOfBoundsException(index);
        }

        items.set(index, aValue);
        fireChangeEvent(index, index, ScrollableListModelEvent.UPDATE);
    }

    @Override
    public void addScrollableListModelListener(ScrollableListModelListener l) {
        this.modelListeners.add(l);
    }

    @Override
    public void removeScrollableListModelListener(ScrollableListModelListener l) {
        this.modelListeners.remove(l);
    }

    @Override
    public void addItem(Object item) {
        this.items.add(item);
        fireChangeEvent(this.items.size() - 1, this.items.size() - 1, ScrollableListModelEvent.INSERT);
    }

    @Override
    public void removeItem(Object item) {
        int index = this.items.indexOf(item);
        if (index > -1) {
            removeItem(index);
        }
    }

    @Override
    public void removeItem(int index) {
        Object value = this.items.get(index);
        //fire change before value is removed so a reference is still there
        fireChangeEvent(index, index, ScrollableListModelEvent.DELETE);
        if (selectedItems.contains(value)) {
            selectedItems.remove(value);
        }
        this.items.remove(index);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void sort(Comparator comparator) {
        this.items.sort(comparator);
        fireChangeEvent(0, this.items.size() - 1, ScrollableListModelEvent.SORTED);
    }

    @Override
    public Object[] getSelectedItems() {
        return selectedItems.toArray(new Object[selectedItems.size()]);
    }

    @Override
    public void setItemSelection(Object item, boolean isSelected) throws ArrayIndexOutOfBoundsException {
        int index = getItemIndex(item);
        if (index < 0) {
            return;
        }
        if (isSelected) {
            if (!selectedItems.contains(item)) {
                switch (getSelectionMode()) {
                    case SELECTION_MODE_SINGLE:
                        selectedItems.clear();
                        selectedItems.add(item);
                        break;
                    case SELECTION_MODE_MULTI_CLICK:
                        selectedItems.add(item);
                        break;
                    case SELECTION_MODE_MULTI_KEYDOWN:
                        selectedItems.add(item);
                        break;
                }
            }
        } else {
            selectedItems.remove(item);
        }
        fireChangeEvent(index, index, ScrollableListModelEvent.SELECTION_CHANGED);
    }

    @Override
    public void setItemSelectionAt(int index, boolean isSelected) throws ArrayIndexOutOfBoundsException {
        setItemSelection(items.get(index), isSelected);
    }

    @Override
    public int getItemIndex(Object item) {
        return this.items.indexOf(item);
    }

    /**
     * Launches the listeners for events fired
     *
     * @param firstItem the index of the first item changed
     * @param lastItem the index of the last item changed
     * @param type the type of change made
     *
     * <ul>
     * <li>
     * INSERT = 1
     * </li>
     * <li>
     * UPDATE = 0
     * </li>
     * <li>
     * DELETE = -1
     * </li>
     * <li>
     * SORTED = 3;
     * </li>
     * <li>
     * SELECTION_CHANGED = 4
     * </li>
     * </ul>
     */
    protected void fireChangeEvent(int firstItem, int lastItem, int type) {
        ScrollableListModelEvent evt = new ScrollableListModelEvent(this, firstItem, lastItem, type);
        for (ScrollableListModelListener l : this.modelListeners) {
            l.scrollableListModelChanged(evt);
        }
    }

    @Override
    public int getSelectionMode() {
        return selectionMode;
    }

    @Override
    public void setSelectionMode(int selectionMode) {
        int oldSelectionMode = this.selectionMode;
        this.selectionMode = selectionMode;
        propertyChangeSupport.firePropertyChange(ScrollableListModel.PROP_SELECTIONMODE, oldSelectionMode, selectionMode);
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener l) {
        propertyChangeSupport.addPropertyChangeListener(l);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener l) {
        propertyChangeSupport.removePropertyChangeListener(l);
    }

}
