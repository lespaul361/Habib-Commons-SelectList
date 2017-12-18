/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.habibsweb.commons.components;

import com.habibsweb.commons.components.event.ItemEditorListener;
import java.util.EventListener;
import java.util.EventObject;

/**
 *
 * @author Charles Hamilton
 */
public interface ItemEditor extends EventListener{

    /**
     * Returns the value contained in the editor.
     *
     * @return the value contained in the editor
     */
    public Object getItemEditorValue();

    /**
     * Asks the editor if it can start editing using <code>anEvent</code>.
     * <code>anEvent</code> is in the invoking component coordinate system. The
     * editor can not assume the Component returned by
     * <code>getScrollableListItemEditorComponent</code> is installed. This
     * method is intended for the use of client to avoid the cost of setting up
     * and installing the editor component if editing is not possible. If
     * editing can be started this method returns true.
     *
     * @param anEvent the event the editor should use to consider whether to
     * begin editing or not
     * @return true if editing can be started
     * @see #shouldSelectItem
     */
    public boolean isItemEditable(EventObject anEvent);

    /**
     * Returns true if the editing item should be selected, false otherwise.
     * Typically, the return value is true, because is most cases the editing
     * cell should be selected. However, it is useful to return false to keep
     * the selection from changing for some types of edits. eg. A table that
     * contains a column of check boxes, the user might want to be able to
     * change those checkboxes without altering the selection. (See Netscape
     * Communicator for just such an example) Of course, it is up to the client
     * of the editor to use the return value, but it doesn't need to if it
     * doesn't want to.
     *
     * @param anEvent the event the editor should use to start editing
     * @return true if the editor would like the editing cell to be selected;
     * otherwise returns false
     * @see #isItemEditable
     */
    public boolean shouldSelectItem(EventObject anEvent);
    
    /**
     * Tells the editor to stop editing and accept any partially edited
     * value as the value of the editor.  The editor returns false if
     * editing was not stopped; this is useful for editors that validate
     * and can not accept invalid entries.
     *
     * @return  true if editing was stopped; false otherwise
     */
    public boolean stopItemEditing();

    /**
     * Tells the editor to cancel editing and not accept any partially
     * edited value.
     */
    public void cancelItemEditing();
    
    /**
     * Adds a listener to the list that's notified when the editor
     * stops, or cancels editing.
     *
     * @param   l               the ItemEditorListener
     */
    public void addScrollableListEditorListener(ItemEditorListener l);
    
        /**
     * Removes a listener from the list that's notified
     *
     * @param   l               the ItemEditorListener
     */
    public void removeScrollableListEditorListener(ItemEditorListener l);
}
