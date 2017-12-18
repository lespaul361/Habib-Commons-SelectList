/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.habibsweb.commons.components;

import com.habibsweb.commons.commonroutines.TextFieldContextMenu.DefaultContextMenu;
import com.habibsweb.commons.components.list.ScrollableListRenderer;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.beans.ConstructorProperties;
import java.io.Serializable;
import java.util.EventObject;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JTextField;

/**
 *
 * @author Charles Hamilton
 */
public class DefaultScrollableListEditor extends AbstractScrollableListItemEditor {

    /**
     * The Swing component being edited.
     */
    protected JComponent editorComponent;
    /**
     * The delegate class which handles all methods sent from the
     * <code>CellEditor</code>.
     */
    protected DefaultScrollableListEditor.EditorDelegate delegate;
    /**
     * An integer specifying the number of clicks needed to start editing. Even
     * if <code>clickCountToStart</code> is defined as zero, it will not
     * initiate until a click occurs.
     */
    protected int clickCountToStart = 2;

//
//  Constructors
//
    /**
     * Constructs a <code>DefaultScrollableListEditor</code> that uses a text
     * field.
     *
     * @param textField a <code>JTextField</code> object
     */
    @ConstructorProperties({"component"})
    public DefaultScrollableListEditor(final JTextField textField) {
        DefaultContextMenu mnu =new DefaultContextMenu();
        mnu.add(textField);
        editorComponent = textField;
        editorComponent.setEnabled(true);
        editorComponent.setFocusable(true);
        editorComponent.setRequestFocusEnabled(true);
        this.clickCountToStart = 2;
        delegate = new DefaultScrollableListEditor.EditorDelegate() {
            @Override
            public void setValue(Object value) {
                textField.setText((value != null) ? value.toString() : "");
                textField.selectAll();
            }

            @Override
            public Object getItemEditorValue() {
                return textField.getText();
            }
        };
        textField.addActionListener(delegate);
    }

    /**
     * Constructs a <code>DefaultScrollableListEditor</code> object that uses a
     * check box.
     *
     * @param checkBox a <code>JCheckBox</code> object
     */
    public DefaultScrollableListEditor(final JCheckBox checkBox) {
        editorComponent = checkBox;
        delegate = new DefaultScrollableListEditor.EditorDelegate() {
            public void setValue(Object value) {
                boolean selected = false;
                if (value instanceof Boolean) {
                    selected = ((Boolean) value).booleanValue();
                } else if (value instanceof String) {
                    selected = value.equals("true");
                }
                checkBox.setSelected(selected);
            }

            public Object getCellEditorValue() {
                return Boolean.valueOf(checkBox.isSelected());
            }
        };
        checkBox.addActionListener(delegate);
        checkBox.setRequestFocusEnabled(false);
    }

    /**
     * Constructs a <code>DefaultScrollableListEditor</code> object that uses a
     * combo box.
     *
     * @param comboBox a <code>JComboBox</code> object
     */
    public DefaultScrollableListEditor(final JComboBox comboBox) {
        editorComponent = comboBox;
        comboBox.putClientProperty("JComboBox.isTableCellEditor", Boolean.TRUE);
        delegate = new DefaultScrollableListEditor.EditorDelegate() {
            public void setValue(Object value) {
                comboBox.setSelectedItem(value);
            }

            public Object getCellEditorValue() {
                return comboBox.getSelectedItem();
            }

            public boolean shouldSelectItem(EventObject anEvent) {
                if (anEvent instanceof MouseEvent) {
                    MouseEvent e = (MouseEvent) anEvent;
                    return e.getID() != MouseEvent.MOUSE_DRAGGED;
                }
                return true;
            }

            public boolean stopItemEditing() {
                if (comboBox.isEditable()) {
                    // Commit edited value.
                    comboBox.actionPerformed(new ActionEvent(
                            DefaultScrollableListEditor.this, 0, ""));
                }
                return super.stopItemEditing();
            }
        };
        comboBox.addActionListener(delegate);
    }

//
// Modify
//
    @Override
    public int getNumberOfClicksToStart() {
        return clickCountToStart;
    }

    @Override
    public void getNumberOfClicksToStart(int clickCount) {
        clickCountToStart = clickCount;
    }

//
//  Override the implementations of the superclass, forwarding all methods
//  from the ScrollableListItemEditor interface to our delegate.
//
    @Override
    public Object getItemEditorValue() {
        return delegate.getItemEditorValue();
    }

    @Override
    public boolean isItemEditable(EventObject anEvent) {
        return delegate.isItemEditable(anEvent);
    }

    @Override
    public boolean shouldSelectItem(EventObject anEvent) {
        return delegate.shouldSelectItem(anEvent);
    }

    @Override
    public boolean stopItemEditing() {
        return delegate.stopItemEditing();
    }

    /**
     * Forwards the message from the <code>ScrollableListItemEditor</code> to
     * the <code>delegate</code>.
     *
     * @see EditorDelegate#cancelItemEditing
     */
    @Override
    public void cancelItemEditing() {
        delegate.cancelItemEditing();
    }

//
//  Implementing the ScrollableListItemEditor Interface
//
    @Override
    public Component getScrollableListItemEditorComponent(ScrollableList scrollableList,
            Object value, int index, boolean isSelected) {
        delegate.setValue(value);
        if (editorComponent instanceof JCheckBox) {
            //in order to avoid a "flashing" effect when clicking a checkbox
            //in a table, it is important for the editor to have as a border
            //the same border that the renderer has, and have as the background
            //the same color as the renderer has. This is primarily only
            //needed for JCheckBox since this editor doesn't fill all the
            //visual space of the table cell, unlike a text field.
            ScrollableListRenderer renderer = scrollableList.getRenderer();
            Component c = renderer.getScrollableListItemRendererComponent(scrollableList,
                    value, isSelected, index);
            if (c != null) {
                editorComponent.setOpaque(true);
                editorComponent.setBackground(c.getBackground());
                if (c instanceof JComponent) {
                    editorComponent.setBorder(((JComponent) c).getBorder());
                }
            } else {
                editorComponent.setOpaque(false);
            }
        }
        return editorComponent;
    }

//
//  Implementing the ScrollableListItemEditor Interface
//
    /**
     * Implements the <code>ScrollableListItemEditor</code> interface.
     */
//<editor-fold defaultstate="collapsed" desc="EditorDelegate">
    /**
     * The protected <code>EditorDelegate</code> class.
     */
    protected class EditorDelegate implements ActionListener, ItemListener, Serializable {

        /**
         * The value of this cell.
         */
        protected Object value;

        /**
         * Returns the value of this cell.
         *
         * @return the value of this cell
         */
        public Object getItemEditorValue() {
            return value;
        }

        /**
         * Sets the value of this cell.
         *
         * @param value the new value of this cell
         */
        public void setValue(Object value) {
            this.value = value;
        }

        /**
         * Returns true if <code>anEvent</code> is <b>not</b> a
         * <code>MouseEvent</code>. Otherwise, it returns true if the necessary
         * number of clicks have occurred, and returns false otherwise.
         *
         * @param anEvent the event
         * @return true if cell is ready for editing, false otherwise
         */
        public boolean isItemEditable(EventObject anEvent) {
            if (anEvent instanceof MouseEvent) {
                return ((MouseEvent) anEvent).getClickCount() >= clickCountToStart;
            }
            return true;
        }

        /**
         * Returns true to indicate that the editing cell may be selected.
         *
         * @param anEvent the event
         * @return true
         * @see #isItemEditable
         */
        public boolean shouldSelectItem(EventObject anEvent) {
            return true;
        }

        /**
         * Returns true to indicate that editing has begun.
         *
         * @param anEvent the event
         * @return true to indicate that editing has begun
         */
        public boolean startItemEditing(EventObject anEvent) {
            return true;
        }

        /**
         * Stops editing and returns true to indicate that editing has stopped.
         * This method calls <code>fireEditingStopped</code>.
         *
         * @return true
         */
        public boolean stopItemEditing() {
            fireEditingStopped();
            return true;
        }

        /**
         * Cancels editing. This method calls <code>fireEditingCanceled</code>.
         */
        public void cancelItemEditing() {
            fireEditingCanceled();
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            DefaultScrollableListEditor.this.stopItemEditing();
        }

        @Override
        public void itemStateChanged(ItemEvent e) {
            DefaultScrollableListEditor.this.stopItemEditing();
        }

    }
//</editor-fold>
}
