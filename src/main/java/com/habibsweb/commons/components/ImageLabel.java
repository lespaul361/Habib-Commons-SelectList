/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.habibsweb.commons.components;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 * A JLabel that has the size of the provided <code>ImageIcon</code>
 *
 * @author David Hamilton
 */
public class ImageLabel extends JLabel {

    /**
     * Constructs a new <code>ImageLabel</code> with a provided
     * <code>ImageIcon</code>
     *
     * @param icon a provided <code>ImageIcon</code>
     */
    public ImageLabel(ImageIcon icon) {
        setSize(icon.getImage().getWidth(null), icon.getImage().getHeight(null));
        setIcon(icon);
        setIconTextGap(0);
        setBorder(null);
        setText(null);
        setOpaque(false);
    }

}
