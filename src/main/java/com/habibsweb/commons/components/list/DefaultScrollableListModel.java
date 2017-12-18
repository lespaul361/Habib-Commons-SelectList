/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.habibsweb.commons.components.list;

import com.habibsweb.commons.components.ScrollableList;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Charles Hamilton
 */
public class DefaultScrollableListModel extends AbstractScrollableListModel
        implements Serializable {

    private final ScrollableList scrollableList;

    /**
     * Constructs a new <code>DefaultScrollableListModel</code>
     *
     * @param scrollableList the <code>ScrollableList</code> this is attached to
     */
    public DefaultScrollableListModel(ScrollableList scrollableList) {
        this(new Object[0], scrollableList);
    }

    /**
     * Constructs a new <code>DefaultScrollableListModel</code> and fills the
     * items
     *
     * @param items an <code>Array</code> of items to be added to the model
     * @param scrollableList the <code>ScrollableList</code> this is attached to
     */
    public DefaultScrollableListModel(Object[] items, ScrollableList scrollableList) {
        this(Arrays.asList(items), scrollableList);
    }

    /**
     * Constructs a new <code>DefaultScrollableListModel</code> and fills the
     * items
     *
     * @param items a <code>List</code> of items to be added to the model
     * @param scrollableList the <code>ScrollableList</code> this is attached to
     */
    public DefaultScrollableListModel(List<Object> items, ScrollableList scrollableList) {
        super.items.addAll(items);
        this.scrollableList = scrollableList;
        modelListeners.add(scrollableList);
    }

    @Override
    public boolean isItemEditable(int itemIndex) {
        return false;
    }

}
