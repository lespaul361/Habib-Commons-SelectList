/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.habibsweb.commons.components.list;

import com.habibsweb.commons.components.ScrollableList;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

/**
 *
 * @author Charles Hamilton
 */
public class DefaultScrollableListRenderer extends JLabel implements ScrollableListRenderer {

    /**
     * Constant for property change
     */
    public static final String PROP_FOREGROUND = "PROP_FOREGROUND";

    /**
     * Constant for property change
     */
    public static final String PROP_BACKGROUND = "PROP_BACKGROUND";

    /**
     * Constant for property change
     */
    public static final String PROP_SELECTEDFOREGROUND = "PROP_SELECTEDFOREGROUND";

    /**
     * Constant for property change
     */
    public static final String PROP_SELECTEDBACKGROUND = "PROP_SELECTEDBACKGROUND";

    /**
     * Constant for property change
     */
    public static final String PROP_MOUSEOVERFOREGROUND = "PROP_MOUSEOVERFOREGROUND";

    /**
     * Constant for property change
     */
    public static final String PROP_MOUSEOVERBACKGROUND = "PROP_MOUSEOVERBACKGROUND";

    private boolean isSelected = false;
    private final ScrollableList scrollableList;
    private int marginTop = 0;
    private int marginBottom = 0;
    private int marginLeft = 5;
    private int marginRight = 5;
    private Color foreground;
    private Color background;
    private Color selectedForeground;
    private Color selectedBackground;
    private Color mouseOverForeground;
    private Color mouseOverBackground;
    private boolean dragEnabled = false;
    private final transient PropertyChangeSupport propertyChangeSupport = new java.beans.PropertyChangeSupport(this);

    /**
     * Constructs a new <code>ScrollableListRenderer</code> using
     * <code>JLabel</code>
     *
     * @param scrollableList the <code>ScrollableList</code> that this renderer
     * is for
     */
    public DefaultScrollableListRenderer(final ScrollableList scrollableList) {
        super();
        this.scrollableList = scrollableList;
        selectedForeground = Color.WHITE;
        selectedBackground = new Color(51, 153, 255);
        mouseOverForeground = java.awt.SystemColor.activeCaptionBorder;
        mouseOverBackground = scrollableList.getBackground();
        foreground = scrollableList.getForeground();
        background = scrollableList.getBackground();

        setOpaque(true);
        addMouseListener(new MouseAdapter() {

            @Override
            public void mouseExited(MouseEvent e) {
                if (!scrollableList.isEditing()) {
                    MouseLeave(e);
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if (!scrollableList.isEditing()) {
                    MouseEnter(e);
                }
            }

        });
    }

    @Override
    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        if (propertyChangeSupport != null) {
            propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
        }
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        if (propertyChangeSupport != null) {
            propertyChangeSupport.addPropertyChangeListener(listener);
        }

    }

    @Override
    public Component getScrollableListItemRendererComponent(final ScrollableList scrollableList,
            Object value, boolean isSelected, int index) {
        String val = value.toString();
        DefaultScrollableListRenderer ret = new DefaultScrollableListRenderer(scrollableList);

        ret.setForeground(foreground);
        ret.setBackground(background);
        ret.setMouseOverBackground(mouseOverBackground);
        ret.setMouseOverForeground(mouseOverForeground);
        ret.setSelectedBackground(selectedBackground);
        ret.setSelectedForeground(selectedForeground);
        ret.setText(val);
        ret.setFont(scrollableList.getFont());
        ret.isSelected = isSelected;
        ret.setLabelColors(scrollableList, isSelected, false);
        ret.setBorder(BorderFactory.createEmptyBorder(ret.marginTop,
                ret.marginLeft, ret.marginBottom, ret.marginRight));
        ret.setDragEnabled(dragEnabled);

        return ret;
    }

    @Override
    public void upDateValue(Object value) {
        String newValue = String.valueOf(value);
        this.setText(newValue);
    }

    @Override
    public void setSelected(boolean b) {
        this.isSelected = b;
        setLabelColors(this.scrollableList, isSelected, false);
    }

    @Override
    public int getRightMargin() {
        return this.marginRight;
    }

    @Override
    public int getLeftMargin() {
        return this.marginLeft;
    }

    @Override
    public int getBottomMargin() {
        return this.marginBottom;
    }

    @Override
    public int getTopMargin() {
        return this.marginTop;
    }

    @Override
    public void setRightMargin(int value) {
        this.marginRight = value;
    }

    @Override
    public void setLeftMargin(int value) {
        this.marginLeft = value;
    }

    @Override
    public void setBottomMargin(int value) {
        this.marginBottom = value;
    }

    @Override
    public void getTopMargin(int value) {
        this.marginTop = value;
    }

    private void MouseEnter(MouseEvent evt) {
        if (!isSelected) {
            setLabelColors(scrollableList, false, true);
        }
    }

    private void MouseLeave(MouseEvent evt) {
        if (!isSelected) {
            setLabelColors(scrollableList, false, false);
        }
    }

    /**
     * Sets the <code>Color</code> of the renderer depending on its state
     *
     * @param scrollableList the <code>ScrollableList</code> this renderer is
     * attached to
     * @param isSelected if the item is selected or not
     * @param isMouseOver if the mouse is over the item or not
     */
    protected void setLabelColors(final ScrollableList scrollableList, boolean isSelected, boolean isMouseOver) {

        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                if (isSelected) {
                    setForeground(selectedForeground);
                    setBackground(selectedBackground);
                } else if (isMouseOver) {
                    setForeground(mouseOverForeground);
                    setBackground(mouseOverBackground);
                } else {
                    setForeground(foreground);
                    setBackground(background);
                }
                scrollableList.repaint();
            }
        });

    }

    /**
     * @return the selectedForeground
     */
    public Color getSelectedForeground() {
        return selectedForeground;
    }

    /**
     * @param selectedForeground the selectedForeground to set
     */
    public void setSelectedForeground(Color selectedForeground) {
        java.awt.Color oldSelectedForeground = this.selectedForeground;
        this.selectedForeground = selectedForeground;
        try {
            propertyChangeSupport.firePropertyChange(PROP_BACKGROUND, oldSelectedForeground, background);
        } catch (Exception e) {
        }
    }

    /**
     * @return the selectedBackground
     */
    public Color getSelectedBackground() {
        return selectedBackground;
    }

    /**
     * @param selectedBackground the selectedBackground to set
     */
    public void setSelectedBackground(Color selectedBackground) {
        java.awt.Color oldSelectedBackground = this.selectedBackground;
        this.selectedBackground = selectedBackground;
        try {
            propertyChangeSupport.firePropertyChange(PROP_BACKGROUND, oldSelectedBackground, background);
        } catch (Exception e) {
        }
    }

    /**
     * @return the mouseOverForeground
     */
    public Color getMouseOverForeground() {
        return mouseOverForeground;
    }

    /**
     * @param mouseOverForeground the mouseOverForeground to set
     */
    public void setMouseOverForeground(Color mouseOverForeground) {
        java.awt.Color oldMouseOverForeground = this.mouseOverForeground;
        this.mouseOverForeground = mouseOverForeground;
        try {
            propertyChangeSupport.firePropertyChange(PROP_BACKGROUND, oldMouseOverForeground, background);
        } catch (Exception e) {
        }
    }

    /**
     * @return the mouseOverBackground
     */
    public Color getMouseOverBackground() {
        return mouseOverBackground;
    }

    /**
     * @param mouseOverBackground the mouseOverBackground to set
     */
    public void setMouseOverBackground(Color mouseOverBackground) {
        java.awt.Color oldMouseOverBackground = this.mouseOverBackground;
        this.mouseOverBackground = mouseOverBackground;
        try {
            propertyChangeSupport.firePropertyChange(PROP_BACKGROUND, oldMouseOverBackground, background);
        } catch (Exception e) {
        }
    }

    @Override
    public void setDragEnabled(boolean b) {
        dragEnabled = b;
    }

    @Override
    public boolean getDragEnabled() {
        return this.dragEnabled;
    }
}
