/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.habibsweb.commons.components;

import com.habibsweb.commons.components.event.SelectionBoxEvent;
import com.habibsweb.commons.components.event.SelectionBoxListener;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import javax.swing.JComponent;
import javax.swing.JLayer;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.event.EventListenerList;

/**
 * This class add support for making a selection box on a
 * <code>JComponent</code>. This class will add a <code>JLayer</code> and a
 * <code>LayerUI</code> to the <code>JComponent</code> to handle drawing the
 * selection box.
 *
 * @author David Hamilton
 */
public class SelectionBox {
///
/// Variables and Constants
///

    /**
     * A property change constant
     */
    public static final String PROP_SELECTIONBOXCOLOR = "PROP_SELECTIONBOXCOLOR";

    /**
     * A property change constant
     */
    public static final String PROP_SELECTIONLAYUI = "PROP_SELECTIONLAYUI";

    /**
     * A property change constant
     */
    public static final String PROP_BLOCKMOUSE = "PROP_BLOCKMOUSE";

    /**
     * A property change constant
     */
    public static final String PROP_ALLOWSELECTIONBOX = "PROP_ALLOWSELECTIONBOX";
    public static final String PROP_SELECTIONSCROLLSPEED = "PROP_SELECTIONSCROLLSPEED";

    private EventListenerList eventList = new EventListenerList();
    private final transient PropertyChangeSupport propertyChangeSupport = new java.beans.PropertyChangeSupport(this);
    private MouseMotionListener[] mouseMotionListeners = null;
    private MouseListener[] mouseListeners = null;
    private Point mouseStartPoint = null;
    private Point scrollableMouseStartPoint = null;
    private boolean allowSelectionBox = true;
    private final int COMPONENT_SCROLL_NONE = 0;
    private final int COMPONENT_SCROLL_JSCROLLPANE = 1;
    private int componentScroll = COMPONENT_SCROLL_NONE;
    private Dimension viewPortDimension = null;
    private Rectangle viewPortStartRectangle = null;
    private boolean blockMouse = true;
    private Color selectionBoxColor = new Color(0, 102, 204);
    private int selectionScrollSpeed = 100;
    private Timer tmrScrollCheck = null;

    /**
     * The component to draw the <code>SelectionBox</code> on.
     */
    protected final JComponent component;
    /**
     * The container holding the component
     */
    protected final Container container;
    /**
     * The <code>JLayer</code> added to the container
     */
    protected JLayer selectionLayer = null;
    /**
     * The <code>AbstractSelectionLayerUI</code> that connects to the component
     */
    protected AbstractSelectionLayerUI selectionLayerUI = null;
    /**
     * a <code>ComponentAdapter</code> that builds the <code>JLayer</code> when
     * the component does its first resize
     */

    ///Listeners
    /**
     * Sets up the layer when the component is actually created
     */
    private ComponentAdapter initAdapter = new ComponentAdapter() {

        @Override
        public void componentResized(ComponentEvent e) {
            if (e.getComponent().getSize().width > 0 || e.getComponent().getSize().height > 0) {
                initLayer();
            }
        }

    };
    /**
     * a <code>MouseAdapter</code> that initializes the
     * <code>SelectionBox</code> to start drawing
     */
    protected MouseAdapter dragInitAdapter = new MouseAdapter() {

        @Override
        public void mousePressed(MouseEvent e) {
            if (!isAllowSelectionBox()) {
                return;
            }
            mouseStartPoint = e.getPoint();
            if (component instanceof JScrollPane) {
                JScrollPane scrollPane = (JScrollPane) component;
                scrollableMouseStartPoint = getViewPortStart(mouseStartPoint);
                viewPortStartRectangle = getCurrentDisplayedRectangle();
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (!isAllowSelectionBox()) {
                return;
            }
            if (isBlockMouse()) {
                mouseMotionListeners = component.getMouseMotionListeners();
                mouseListeners = component.getMouseListeners();
                for (MouseMotionListener l : mouseMotionListeners) {
                    component.removeMouseMotionListener(l);
                }
                for (MouseListener l : mouseListeners) {
                    component.removeMouseListener(l);
                }
            } else {
                component.removeMouseMotionListener(dragInitAdapter);
                component.removeMouseListener(dragInitAdapter);
            }
            if (component instanceof JScrollPane) {
                Rectangle maxBounds = new Rectangle(selectionLayer.getLocation(),
                        selectionLayer.getSize());
                JScrollPane scroll = (JScrollPane) component;
                if (scroll.getHorizontalScrollBar().isVisible()) {
                    maxBounds.height -= scroll.getHorizontalScrollBar().getHeight();
                }
                if (scroll.getVerticalScrollBar().isVisible()) {
                    maxBounds.width -= scroll.getVerticalScrollBar().getWidth();
                }
                selectionLayerUI.setMaximumRectangle(maxBounds);
            }
            component.addMouseListener(dragAdapter);
            component.addMouseMotionListener(dragAdapter);
            selectionLayerUI.setIsEnableDrawing(true);
            fireEvents(component, SelectionBoxEvent.SELECTION_STARTED, null);
        }

    };
    /**
     * a <code>MouseAdapter</code> that handles refreshing the
     * <code>SelectionBox</code> drawing area as well as the end of the drawing
     */
    protected MouseAdapter dragAdapter = new MouseAdapter() {

        @Override
        public void mouseDragged(MouseEvent e) {
            if (componentScroll != COMPONENT_SCROLL_NONE) {
                if (tmrScrollCheck == null || !tmrScrollCheck.isRunning()) {
                    if (scrollToNewLocation(e.getPoint())) {
                        //start timer
                        tmrScrollCheck = new Timer(getSelectionScrollSpeed(), new ActionListener() {

                            @Override
                            public void actionPerformed(ActionEvent e) {
                                Point mouseLoc = MouseInfo.getPointerInfo().getLocation();
                                SwingUtilities.convertPointFromScreen(mouseLoc, component);
                                if (!scrollToNewLocation(mouseLoc)) {
                                    tmrScrollCheck.stop();
                                }
                                Rectangle rec = getPaintRectangle(mouseLoc);
                                selectionLayerUI.setCurrentRectangle(rec);
                                container.repaint();
                                fireEvents(component, SelectionBoxEvent.SELECTION_SIZE_CHANGED,
                                        getSelectionArea(mouseLoc));
                            }
                        });
                        tmrScrollCheck.start();
                    }
                } else {
                    return;
                }
            }
            Rectangle rec = getPaintRectangle(e.getPoint());
            selectionLayerUI.setCurrentRectangle(rec);
            container.repaint();
            if (componentScroll != COMPONENT_SCROLL_NONE) {
                rec = getSelectionArea(e.getPoint());
            }
            fireEvents(component, SelectionBoxEvent.SELECTION_SIZE_CHANGED, rec);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            selectionLayerUI.setIsEnableDrawing(false);
            container.repaint();
            if (isBlockMouse()) {
                for (MouseListener l : mouseListeners) {
                    component.addMouseListener(l);
                }
                for (MouseMotionListener l : mouseMotionListeners) {
                    component.addMouseMotionListener(l);
                }
            }
            component.removeMouseListener(dragAdapter);
            component.removeMouseMotionListener(dragAdapter);
            fireEvents(component, SelectionBoxEvent.SELECTION_ENDED, selectionLayerUI.getCurrentRectangle());
            if (tmrScrollCheck != null && tmrScrollCheck.isRunning()) {
                tmrScrollCheck.stop();
            }
            tmrScrollCheck = null;
            mouseStartPoint = null;
            scrollableMouseStartPoint = null;
            viewPortStartRectangle = null;
            viewPortDimension = null;

        }

    };

///
/// Constructor
///
    /**
     * Constructs a new <code>SelectionBox</code> class.
     *
     * @param component the component to show <code>SelectionBox</code> on.
     * @param container the container holding the component
     */
    public SelectionBox(JComponent component, Container container) {
        this.component = component;
        component.addComponentListener(initAdapter);
        this.container = container;
        if (component instanceof JScrollPane) {
            componentScroll = COMPONENT_SCROLL_JSCROLLPANE;
        }
    }

///
/// Public Functions
///
    /**
     * Adds a <code>SelectionBoxListener</code> to the <code>SelectionBox</code>
     *
     * @param l a <code>SelectionBoxListener</code>
     */
    public void addSelectionBoxListener(SelectionBoxListener l) {
        eventList.add(SelectionBoxListener.class, l);
    }

    /**
     * Removes a <code>SelectionBoxListener</code> to the
     * <code>SelectionBox</code>
     *
     * @param l a <code>SelectionBoxListener</code>
     */
    public void removeSelectionBoxListener(SelectionBoxListener l) {
        eventList.remove(SelectionBoxListener.class, l);
    }

    /**
     * Adds a listener for property changes
     *
     * @param l a listener for property changes
     */
    public void addPropertyChangeListener(PropertyChangeListener l) {
        propertyChangeSupport.addPropertyChangeListener(l);
    }

    /**
     * Removes a listener for property changes
     *
     * @param l a listener for property changes
     */
    public void removePropertyChangeListener(PropertyChangeListener l) {
        propertyChangeSupport.removePropertyChangeListener(l);
    }
///
/// Getters and Setters
///

    /**
     * Gets the <code>SelectionBox</code> <code>Color</code>
     *
     * @return the <code>SelectionBox</code> <code>Color</code>
     */
    public Color getSelectionBoxColor() {
        return selectionBoxColor;
    }

    /**
     * Sets the <code>SelectionBox</code> <code>Color</code>
     *
     * @param selectionBoxColor the <code>SelectionBox</code> <code>Color</code>
     */
    public void setSelectionBoxColor(Color selectionBoxColor) {
        if (selectionBoxColor == null) {
            throw new NullPointerException("selectionBoxColor must have a color value");
        }
        java.awt.Color oldSelectionBoxColor = this.selectionBoxColor;
        this.selectionBoxColor = selectionBoxColor;
        getSelectionLayerUI().setSelectionBoxColor(selectionBoxColor);
        propertyChangeSupport.firePropertyChange(PROP_SELECTIONBOXCOLOR, oldSelectionBoxColor, selectionBoxColor);
    }

    /**
     * Gets the <code>AbstractSelectionLayerUI</code> used for drawing the
     * <code>SelectionBox</code>
     *
     * @return the <code>AbstractSelectionLayerUI</code> used for drawing the
     * <code>SelectionBox</code>
     */
    public AbstractSelectionLayerUI getSelectionLayerUI() {
        if (selectionLayerUI == null) {
            selectionLayerUI = (AbstractSelectionLayerUI) new DefaultSelectionLayerUI();
        }
        return selectionLayerUI;
    }

    /**
     * Sets the <code>AbstractSelectionLayerUI</code> used for drawing the
     * <code>SelectionBox</code>
     *
     * @param selectionLayUI the <code>AbstractSelectionLayerUI</code> used for
     * drawing the <code>SelectionBox</code>
     */
    public void setSelectionLayerUI(AbstractSelectionLayerUI selectionLayUI) {
        javax.swing.plaf.LayerUI oldSelectionLayUI = this.selectionLayerUI;
        this.selectionLayerUI = selectionLayUI;
        propertyChangeSupport.firePropertyChange(PROP_SELECTIONLAYUI, oldSelectionLayUI, selectionLayUI);
    }

    /**
     * Checks if other <code>MouseAdapter</code> should be temporarily removed
     *
     * @return if other <code>MouseAdapter</code> should be temporarily removed
     */
    public boolean isBlockMouse() {
        return blockMouse;
    }

    /**
     * Sets if other <code>MouseAdapter</code> should be temporarily removed
     *
     * @param isBlockMouse if other <code>MouseAdapter</code> should be
     * temporarily removed
     */
    public void setBlockMouse(boolean isBlockMouse) {
        boolean oldIsBlockMouse = this.blockMouse;
        this.blockMouse = isBlockMouse;
        propertyChangeSupport.firePropertyChange(PROP_BLOCKMOUSE, oldIsBlockMouse, isBlockMouse);
    }

    /**
     * Gets a <code>Rectangle</code> built from 2 points
     *
     * @param p1 a point to use for the <code>Rectangle</code>
     * @param p2 a point to use for the <code>Rectangle</code>
     * @return a <code>Rectangle</code> built from 2 points
     */
    public static Rectangle getRectangle(Point p1, Point p2) {
        int x;
        int y;
        int w;
        int h;
        if (p1.x < p2.x) {
            x = p1.x;
            w = p2.x - p1.x;
        } else {
            x = p2.x;
            w = p1.x - p2.x;
        }

        if (p1.y < p2.y) {
            y = p1.y;
            h = p2.y - p1.y;
        } else {
            y = p2.y;
            h = p1.y - p2.y;
        }

        return new Rectangle(x, y, w, h);
    }

    /**
     * Gets if a <code>SelectionBox</code> should be allowed to draw
     *
     * @return the allowSelectionBox
     */
    public boolean isAllowSelectionBox() {
        return allowSelectionBox;
    }

    /**
     * Gets if a <code>SelectionBox</code> should be allowed to draw
     *
     * @param allowSelectionBox the allowSelectionBox to set
     */
    public void setAllowSelectionBox(boolean allowSelectionBox) {
        boolean oldAllowSelectionBox = this.allowSelectionBox;
        this.allowSelectionBox = allowSelectionBox;
        propertyChangeSupport.firePropertyChange(PROP_ALLOWSELECTIONBOX, oldAllowSelectionBox, allowSelectionBox);
    }

    /**
     * Gets the speed that out of bound drag selection will scroll if scrolling
     * is available
     *
     * @return the selectionScrollSpeed
     */
    public int getSelectionScrollSpeed() {
        return selectionScrollSpeed;
    }

    /**
     * Sets the speed that out of bound drag selection will scroll if scrolling
     * is available
     *
     * @param selectionScrollSpeed the selectionScrollSpeed to set
     */
    public void setSelectionScrollSpeed(int selectionScrollSpeed) {
        int oldSelectionScrollSpeed = this.selectionScrollSpeed;
        this.selectionScrollSpeed = selectionScrollSpeed;
        propertyChangeSupport.firePropertyChange(PROP_SELECTIONSCROLLSPEED, oldSelectionScrollSpeed, selectionScrollSpeed);
    }
///
/// Protected Methods
///

    /**
     * Fires the listeners attached to this <code>SelectionBox</code>
     *
     * @param source the component this <code>SelectionBox</code> is attached to
     * @param eventType the type of this event
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
     * @param area a rectangle showing the current selected area. Maybe null.
     */
    protected void fireEvents(Object source, int eventType, Rectangle area) {
        SelectionBoxEvent evt = new SelectionBoxEvent(source, eventType, area);
        SelectionBoxListener[] listeners = eventList.getListeners(SelectionBoxListener.class);
        for (int i = listeners.length - 1; i > -1; i--) {
            listeners[i].selectionBoxSelectionChange(evt);
        }
    }

///
/// Private Methods
///
    @SuppressWarnings("unchecked")
    private void initLayer() {
        component.removeComponentListener(initAdapter);
        component.addMouseMotionListener(dragInitAdapter);
        component.addMouseListener(dragInitAdapter);
        selectionLayerUI = getSelectionLayerUI();
        selectionLayer = new JLayer(component, selectionLayerUI);
        container.add(selectionLayer);

    }

    private Rectangle getPaintRectangle(Point currentPoint) {
        switch (componentScroll) {
            case COMPONENT_SCROLL_NONE:
                return getRectangle(currentPoint, mouseStartPoint);
            case COMPONENT_SCROLL_JSCROLLPANE:
                JScrollPane scrollPane = (JScrollPane) component;
                if (!scrollPane.getHorizontalScrollBar().isVisible()
                        && !scrollPane.getVerticalScrollBar().isVisible()) {
                    return getRectangle(currentPoint, mouseStartPoint);
                }

                return getRectangle(getScrolledStartPoint(), currentPoint);
        }
        return getRectangle(currentPoint, mouseStartPoint);
    }

    private Point getScrolledStartPoint() {
        Rectangle recDisplayed = getCurrentDisplayedRectangle();
        Rectangle recStart = viewPortStartRectangle;
        Point start = scrollableMouseStartPoint;
        int x = 0;
        int y = 0;
                //convert from drawing location to component location
        //set the starting rectangle as a home reference ie point (0,0)
        x = start.x - recStart.x;
        y = start.y - recStart.y;
        if (recDisplayed.x != recStart.x) {
            if (recDisplayed.x > recStart.x) {
                x -= (recDisplayed.x - recStart.x);
            } else {
                x += (recStart.x - recDisplayed.x);
            }
        }
        if (recDisplayed.y != recStart.y) {
            if (recDisplayed.y > recStart.y) {
                y -= (recDisplayed.y - recStart.y);
            } else {
                y += (recStart.y + recDisplayed.y);
            }
        }
        //if value is less than 0 then it is off screen so go to edge
        if (x < 0) {
            x = 0;
        }

        if (y < 0) {
            y = 0;
        }
        return new Point(x, y);
    }

    private Rectangle getSelectionArea(Point currentPoint) {
        Point ret = getCurrentDisplayedRectangle().getLocation();
        ret.x += currentPoint.x;
        ret.y += currentPoint.y;
        return getRectangle(ret, scrollableMouseStartPoint);
    }

    private boolean scrollToNewLocation(Point currentPoint) {
        boolean ret = false;
        JScrollPane scrollPane = (JScrollPane) component;
        if (isScrollVert(currentPoint)) {
            ret = true;
            JScrollBar vBar = scrollPane.getVerticalScrollBar();
            if (isOutBoundsBottom(currentPoint)) {
                if (vBar.getValue() < vBar.getMaximum()) {
                    vBar.setValue(vBar.getValue() + vBar.getUnitIncrement());
                }
            } else if (isOutBoundsTop(currentPoint)) {
                if (vBar.getValue() > 0) {
                    vBar.setValue(vBar.getValue() - vBar.getUnitIncrement());
                }
            }

        }
        if (isScrollHor(currentPoint)) {
            ret = true;
            JScrollBar hBar = scrollPane.getHorizontalScrollBar();
            if (isOutBoundsLeft(currentPoint)) {
                if (hBar.getValue() < hBar.getMaximum()) {
                    hBar.setValue(hBar.getValue() + hBar.getUnitIncrement());
                }
            } else if (isOutBoundsRight(currentPoint)) {
                if (hBar.getValue() > 0) {
                    hBar.setValue(hBar.getValue() - hBar.getUnitIncrement());
                }
            }
        }
        return ret;
    }

    /**
     * Gets the area of the <code>JViewPort</code> that is currently visible
     *
     * @return the area of the <code>JViewPort</code> that is currently visible
     */
    private Rectangle getCurrentDisplayedRectangle() {
        int x = 0;
        int y = 0;

        JScrollPane sp = (JScrollPane) component;
        if (sp.getVerticalScrollBar().isVisible()) {
            y = sp.getVerticalScrollBar().getValue();
        }
        if (sp.getHorizontalScrollBar().isVisible()) {
            x = sp.getHorizontalScrollBar().getValue();
        }
        return new Rectangle(new Point(x, y), sp.getVisibleRect().getSize());
    }

    private Point getViewPortStart(Point currentPoint) {
        JScrollPane scrollPane = (JScrollPane) component;
        Point ret = SwingUtilities.convertPoint(container,
                currentPoint, scrollPane.getViewport());
        if (scrollPane.getVerticalScrollBar().isVisible()) {
            ret.y += scrollPane.getVerticalScrollBar().getValue();
        }
        if (scrollPane.getHorizontalScrollBar().isVisible()) {
            ret.x += scrollPane.getHorizontalScrollBar().getValue();
        }

        return ret;
    }

    private Dimension getViewPortDimension() {
        if (viewPortDimension != null) {
            return viewPortDimension;
        }
        JScrollPane scrollPane = (JScrollPane) component;
        viewPortDimension = scrollPane.getViewport().getComponent(0).getSize();
        int w = 0;
        int h = 0;

        for (Component c : scrollPane.getViewport().getComponents()) {
            if (c.getLocation().x + c.getSize().width > w) {
                w = c.getLocation().x + c.getSize().width;
            }
            if (c.getLocation().y + c.getSize().height > h) {
                h = c.getLocation().y + c.getSize().height;
            }
        }
        viewPortDimension = new Dimension(w, h);
        return viewPortDimension;
    }

    private boolean isScrollVert(Point currentPoint) {
        JScrollPane scrollPane = (JScrollPane) component;
        if (scrollPane.getVerticalScrollBar().isVisible()) {
            Rectangle recVisible = getCurrentDisplayedRectangle();
            Dimension viewPortSize = getViewPortDimension();
            if (isOutBoundsBottom(currentPoint)) {
                //scroll down
                if ((recVisible.y + recVisible.height) < viewPortSize.height) {
                    return true;
                }
            } else if (isOutBoundsTop(currentPoint)) {
                //scroll up
                if (recVisible.y > 0) {
                    return true;
                }

            }
        }
        return false;
    }

    private boolean isScrollHor(Point currentPoint) {
        JScrollPane scrollPane = (JScrollPane) component;
        if (scrollPane.getVerticalScrollBar().isVisible()) {
            Rectangle recVisible = scrollPane.getVisibleRect();
            Dimension viewPortSize = getViewPortDimension();
            if (isOutBoundsRight(currentPoint)) {
                //scroll right
                if ((recVisible.x + recVisible.width) < viewPortSize.width) {
                    return true;
                }
            } else if (isOutBoundsLeft(currentPoint)) {
                //scroll left
                if (recVisible.x > 0) {
                    return true;
                }

            }
        }
        return false;
    }

    private boolean isOutBoundsTop(Point currentPoint) {
        return currentPoint.y < 0;
    }

    private boolean isOutBoundsBottom(Point currentPoint) {
        return currentPoint.y > (component.getLocation().y + component.getHeight());
    }

    private boolean isOutBoundsLeft(Point currentPoint) {
        return currentPoint.x < component.getLocation().x;
    }

    private boolean isOutBoundsRight(Point currentPoint) {
        return currentPoint.x > (component.getLocation().y + component.getWidth());
    }
}
