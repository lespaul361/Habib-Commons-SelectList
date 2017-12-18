/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.habibsweb.commons.components.event;

import java.awt.Rectangle;
import java.util.EventObject;

/**
 *
 * @author David Hamilton
 */
public class SelectionBoxEvent extends EventObject {

    /**
     * The selection has just started
     */
    public static final int SELECTION_STARTED = 0;
    /**
     * The size of the selection area has changed
     */
    public static final int SELECTION_SIZE_CHANGED = 1;
    /**
     * The mouse has been released and the selection box is complete
     */
    public static final int SELECTION_ENDED = 2;

    private final Rectangle selectionBounds;
    private final int eventType;

    /**
     * Constructs a new <code>SelectionBoxSeletionEvent</code>
     *
     * @param source the source component of the <code>SelectionBox</code>
     * @param eventType the type of event
     * <ul>
     * <li>
     * SELECTION_STARTED = 0
     * </li>
     * <li>
     * ELECTION_SIZE_CHANGED = 1
     * </li>
     * <li>
     * SELECTION_ENDED = 2
     * </li>
     * </ul>
     * @param selectionBounds the area of the selection box
     */
    public SelectionBoxEvent(Object source, int eventType,
            Rectangle selectionBounds) {
        super(source);
        this.selectionBounds = selectionBounds;
        this.eventType = eventType;
    }

    /**
     * Gets the area of the selection box
     *
     * @return the area of the selection box
     */
    public Rectangle getSelectionBounds() {
        return selectionBounds;
    }

    /**
     * Gets the event type of this event
     *
     * @return the eventType
     * <br>
     * <br>
     * <ul>
     * <li>
     * SELECTION_STARTED = 0
     * </li>
     * <li>
     * ELECTION_SIZE_CHANGED = 1
     * </li>
     * <li>
     * SELECTION_ENDED = 2
     * </li>
     * </ul>
     */
    public int getEventType() {
        return eventType;
    }

}
