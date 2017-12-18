/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.habibsweb.commons.components.event;

import com.habibsweb.commons.components.list.ScrollableListModel;
import java.util.EventObject;

/**
 * ScrollableListModelEvent is used to notify listeners that a
 * <code>ScrollableListModel</code> has changed. The model event describes
 * changes to a ScrollableListModel.
 *
 * @author Charles Hamilton
 */
public class ScrollableListModelEvent extends EventObject {

    /**
     * Identifies the addition of a new item.
     */
    public static final int INSERT = 1;
    /**
     * Identifies a change to existing item.
     */
    public static final int UPDATE = 0;
    /**
     * Identifies the removal of an item.
     */
    public static final int DELETE = -1;

    /**
     * Identifies the sort of the list.
     */
    public static final int SORTED = 3;

    /**
     * Identified a selection change on an item
     */
    public static final int SELECTION_CHANGED = 4;

    /**
     * The type of event
     */
    protected int type;

    /**
     * The first item index
     */
    protected int firstItem;

    /**
     * The last item index
     */
    protected int lastItem;

    /**
     * Constructs a new <code>ScrollableListModelEvent</code> with the source
     * that is updated.
     *
     * @param source the <code>ScrollableListModel</code> that is used
     */
    public ScrollableListModelEvent(ScrollableListModel source) {
        this(source, 0, Integer.MAX_VALUE, UPDATE);
    }

    /**
     * Constructs a new <code>ScrollableListModelEvent</code> with the source
     * and item range that is updated.
     *
     * @param source the <code>ScrollableListModel</code> that is used
     * @param firstItem the first item affected
     * @param lastItem the last item affected
     */
    public ScrollableListModelEvent(ScrollableListModel source, int firstItem, int lastItem) {
        this(source, firstItem, lastItem, UPDATE);
    }

    /**
     * Constructs a new <code>ScrollableListModelEvent</code> with the source
     * and item range and the change made.
     *
     * @param source the <code>ScrollableListModel</code> that is used
     * @param firstItem the first item affected
     * @param lastItem the last item affected
     * @param type the type of change that is made<p>
     *     /**
     * Identifies the addition of a new item. INSERT = 1<br>
     * UPDATE = 0<br>
     * DELETE = -1
     *
     */
    public ScrollableListModelEvent(ScrollableListModel source, int firstItem, int lastItem, int type) {
        super(source);
        this.firstItem = firstItem;
        this.lastItem = lastItem;
        this.type = type;
    }

    @Override
    public String toString() {
        return super.toString(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object getSource() {
        return super.getSource(); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Gets the type of change
     *
     * @return <code>int</code>
     * <p>
     *
     * INSERT = 1<br>
     *
     * UPDATE = 0<br>
     *
     * DELETE = -1
     */
    public int getType() {
        return type;
    }

    /**
     * Gets the index of the first item for the event
     *
     * @return <code>int</code>
     */
    public int getFirstItem() {
        return firstItem;
    }

    /**
     * Gets the index of the first item for the event
     *
     * @return <code>int</code>
     */
    public int getLastItem() {
        return lastItem;
    }
}
