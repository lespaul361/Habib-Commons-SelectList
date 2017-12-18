/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.habibsweb.commons.components;

import com.habibsweb.commons.components.event.ItemEditorListener;
import com.habibsweb.commons.components.list.ScrollableListItemEditor;
import java.io.Serializable;
import java.util.EventListener;
import java.util.EventObject;
import javax.swing.event.ChangeEvent;
import javax.swing.event.EventListenerList;

/**
 *
 * @author David Hamilton
 */
public abstract class AbstractScrollableListItemEditor implements ScrollableListItemEditor, Serializable, EventListener {

    /**
     * holds the events
     */
    protected EventListenerList listeners = new EventListenerList();

    /**
     * variable for <code>ChangeEvent</code>
     */
    transient protected ChangeEvent changeEvent = null;

    @Override
    public boolean isItemEditable(EventObject e) {
        return true;
    }

    @Override
    public boolean shouldSelectItem(EventObject anEvent) {
        return true;
    }

    @Override
    public boolean stopItemEditing() {
        fireEditingStopped();
        return true;
    }

    @Override
    public void cancelItemEditing() {
        fireEditingCanceled();
    }

    @Override
    public void addScrollableListEditorListener(ItemEditorListener l) {
        listeners.add(ItemEditorListener.class, l);
    }

    @Override
    public void removeScrollableListEditorListener(ItemEditorListener l) {
        listeners.remove(ItemEditorListener.class, l);
    }

    /**
     * Returns an array of all the <code>ScrollableListEditorListener</code>s
     * added to this AbstractScrollableListItemEditor with
     * addScrollableListEditorListener().
     *
     * @return all of the <code>ScrollableListEditorListener</code>s added or an
     * empty array if no listeners have been added
     * @since 1.4
     */
    public ItemEditorListener[] getScrollableListEditorListener() {
        return listeners.getListeners(ItemEditorListener.class);
    }

    /**
     * Notifies all listeners that have registered interest for notification on
     * this event type. The event instance is created lazily.
     *
     * @see EventListenerList
     */
    protected void fireEditingStopped() {
        // Guaranteed to return a non-null array
        Object[] listeners = this.listeners.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 1; i >= 0; i -= 1) {
            if (listeners[i] == ItemEditorListener.class) {
                // Lazily create the event:
                if (changeEvent == null) {
                    changeEvent = new ChangeEvent(this);
                }
                ((ItemEditorListener) listeners[i]).editingStopped(changeEvent);
            }
        }
    }

    /**
     * Notifies all listeners that have registered interest for notification on
     * this event type. The event instance is created lazily.
     *
     * @see EventListenerList
     */
    protected void fireEditingCanceled() {
        // Guaranteed to return a non-null array
        Object[] listeners = this.listeners.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 1; i >= 0; i -= 1) {
            if (listeners[i] == ItemEditorListener.class) {
                // Lazily create the event:
                if (changeEvent == null) {
                    changeEvent = new ChangeEvent(this);
                }
                ((ItemEditorListener) listeners[i]).editingCanceled(changeEvent);
            }
        }
    }
}
