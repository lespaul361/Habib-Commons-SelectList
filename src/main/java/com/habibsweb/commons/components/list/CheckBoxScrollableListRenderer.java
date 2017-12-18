/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.habibsweb.commons.components.list;

import com.habibsweb.commons.components.ScrollableList;
import java.awt.Component;
import javax.swing.JCheckBox;

/**
 *
 * @author Charles Hamilton
 */
public class CheckBoxScrollableListRenderer extends JCheckBox implements ScrollableListRenderer {

    private boolean isSelected = false;
    private ScrollableList scrollableList;
    private int marginTop = 0;
    private int marginBottom = 0;
    private int marginLeft = 5;
    private int marginRight = 0;
    private boolean blnDragNDrop=false;

    @Override
    public Component getScrollableListItemRendererComponent(ScrollableList scrollableList, Object value, boolean isSelected, int index) {
        CheckBoxScrollableListRenderer renderer = new CheckBoxScrollableListRenderer();
        renderer.scrollableList = scrollableList;
        renderer.upDateValue(value);
        renderer.setOpaque(true);
        renderer.setEnabled(true);
        renderer.setText(String.valueOf(value));
        renderer.setSelected(isSelected);
        renderer.setBackground(scrollableList.getBackground());
        renderer.setForeground(scrollableList.getForeground());
        renderer.setFont(scrollableList.getFont());
        renderer.setDragEnabled(blnDragNDrop);
        return renderer;
    }

    @Override
    public void setSelected(boolean b) {
        super.setSelected(b);
        isSelected = b;
    }

    @Override
    public void upDateValue(Object value) {
        this.setText(String.valueOf(value));
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

    @Override
    public void setDragEnabled(boolean b) {
        this.blnDragNDrop=b;
        this.setDragEnabled(b);
    }

    @Override
    public boolean getDragEnabled() {
        return this.blnDragNDrop;
    }

    
}
