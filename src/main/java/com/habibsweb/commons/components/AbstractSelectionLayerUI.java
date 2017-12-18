package com.habibsweb.commons.components;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.beans.PropertyChangeSupport;
import javax.swing.JComponent;
import javax.swing.plaf.LayerUI;

/**
 * An abstract class to handle the drawing of the <code>JComponent</code> layer
 * and the selection box
 *
 * @author David Hamilton
 */
public abstract class AbstractSelectionLayerUI extends LayerUI<JComponent> {

    /**
     * Property change name
     */
    public static final String PROP_ISENABLEDRAWING = "PROP_ISENABLEDRAWING";

    /**
     * Property change name
     */
    public static final String PROP_CURRENTRECTANGLE = "PROP_CURRENTRECTANGLE";

    /**
     * Property change name
     */
    public static final String PROP_SELECTIONBOXCOLOR = "PROP_SELECTIONBOXCOLOR";

    /**
     * Property change name
     */
    public static final String PROP_MAXIMUMRECTANGLE = "PROP_MAXIMUMRECTANGLE";
    private boolean isEnableDrawing = false;
    private Rectangle currentRectangle = null;
    private Rectangle maximumRectangle = null;
    private Color selectionBoxColor = new Color(0, 102, 204);
    private final transient PropertyChangeSupport propertyChangeSupport = new java.beans.PropertyChangeSupport(this);

///
/// Getters and Setters
///
    /**
     * Sets if the selection box should be drawing or if just the default
     * drawing should be done
     *
     * @param isEnableDrawing Sets if the selection box should be drawing or if
     * just the default drawing should be done
     */
    public void setIsEnableDrawing(boolean isEnableDrawing) {
        boolean oldIsEnableDrawing = this.isEnableDrawing();
        this.isEnableDrawing = isEnableDrawing;
        propertyChangeSupport.firePropertyChange(PROP_ISENABLEDRAWING, oldIsEnableDrawing, isEnableDrawing);
    }

    /**
     * Gets if the selection box is drawing
     *
     * @return a boolean if the selection box is drawing or not
     */
    public boolean isEnableDrawing() {
        return isEnableDrawing;
    }

    /**
     * Gets the current selected area
     *
     * @return the current selected area
     */
    public Rectangle getCurrentRectangle() {
        return currentRectangle;
    }

    /**
     * Sets the area to be drawn for the selection box
     *
     * @param currentRectangle the area to be drawn for the selection box
     */
    public void setCurrentRectangle(Rectangle currentRectangle) {
        java.awt.Rectangle oldCurrentRectangle = this.currentRectangle;
        this.currentRectangle = currentRectangle;
        propertyChangeSupport.firePropertyChange(PROP_CURRENTRECTANGLE, oldCurrentRectangle, currentRectangle);
    }

    /**
     * Gets the color to be used for the selection box
     *
     * @return the color to be used for the selection box
     */
    public Color getSelectionBoxColor() {
        return selectionBoxColor;
    }

    /**
     * Sets the color to be used for the selection box
     *
     * @param selectionBoxColor the color to be used for the selection box
     */
    public void setSelectionBoxColor(Color selectionBoxColor) {
        java.awt.Color oldSelectionBoxColor = this.selectionBoxColor;
        this.selectionBoxColor = selectionBoxColor;
        propertyChangeSupport.firePropertyChange(PROP_SELECTIONBOXCOLOR, oldSelectionBoxColor, selectionBoxColor);
    }

    /**
     * Gets the maximum bounds of the <code>SelectionBox</code>
     *
     * @return the maximum bounds of the <code>SelectionBox</code>
     */
    public Rectangle getMaximumRectangle() {
        return maximumRectangle;
    }

    /**
     * Sets the maximum bounds of the <code>SelectionBox</code>
     *
     * @param maximumRectangle the maximum bounds of the
     * <code>SelectionBox</code>
     */
    public void setMaximumRectangle(Rectangle maximumRectangle) {
        java.awt.Rectangle oldMaximumRectangle = this.maximumRectangle;
        this.maximumRectangle = maximumRectangle;
        propertyChangeSupport.firePropertyChange(PROP_MAXIMUMRECTANGLE, oldMaximumRectangle, maximumRectangle);
    }

///
/// Overrides
///
    @Override
    public void paint(Graphics g, JComponent c) {
        super.paint(g, c);

        if (isEnableDrawing()) {
            Graphics2D g2 = (Graphics2D) g.create();
            currentRectangle = checkMaxBounds();
            int cx = currentRectangle.x;
            int cy = currentRectangle.y;
            int cw = currentRectangle.width;
            int ch = currentRectangle.height;

            g2.setColor(getSelectionBoxColor());
            g2.setComposite(AlphaComposite.getInstance(
                    AlphaComposite.SRC_OVER, .6f));
            g2.fillRect(cx, cy, cw, ch);
            g2.setComposite(AlphaComposite.getInstance(
                    AlphaComposite.SRC_OVER, 1f));
            g2.drawRect(cx, cy, cw, ch);
            g2.dispose();

        }

    }

///
/// Private Methods
///
    private Rectangle checkMaxBounds() {
        if (getMaximumRectangle() == null) {
            return currentRectangle;
        }
        int cx = currentRectangle.x;
        int cy = currentRectangle.y;
        int cw = currentRectangle.width;
        int ch = currentRectangle.height;
        int mx = maximumRectangle.x;
        int my = maximumRectangle.y;
        int mw = maximumRectangle.width;
        int mh = maximumRectangle.height;
        int rx, ry, rw, rh;

        rx = cx;
        ry = cy;
        rw = cw;
        rh = ch;
        if ((cx + cw) > (mx + mw)) {
            int maxRight = mx + mw;
            int curRight = cx + cw;
            rw -= (curRight - maxRight);
        }
        if ((cy + ch) > (my + mh)) {
            int maxRight = my + mh;
            int curRight = cy + ch;
            rh -= (curRight - maxRight);
        }

        return new Rectangle(rx, ry, rw, rh);
    }
}
