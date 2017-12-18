/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.habibsweb.commons.components;

import java.awt.Insets;
import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 * A <code>JButton</code> that uses is set to the size and displays an
 * <code>ImageIcon</code>
 *
 * @author David Hamilton
 */
public class ImageButton extends JButton {

    /**
     * Constructs a new <code>ImageButton</code> from a provided
     * <code>ImageIcon</code>
     *
     * @param icon a provided <code>ImageIcon</code>
     */
    public ImageButton(ImageIcon icon) {
        setSize(icon.getImage().getWidth(null), icon.getImage().getHeight(null));
        setIcon(icon);
        setMargin(new Insets(0, 0, 0, 0));
        setBorderPainted(false);
        setBorder(null);
        setText(null);
    }
}
