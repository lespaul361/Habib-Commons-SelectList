/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.habibsweb.commons.components;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JPanel;

/**
 * A panel that is sized to an image and shows the image in the background. The
 * default layout is null.
 *
 * @author David Hamilton
 */
public class ImagePanel extends JPanel {

    private Image image;

    /**
     * Constructs a new <code>ImagePanel</code> with the size based on the image
     * provided.
     *
     * @param image the image for the background
     */
    public ImagePanel(Image image) {
        this.image = image;
        Dimension size = new Dimension(image.getWidth(null), image.getHeight(null));
        setSize(size);
        setPreferredSize(size);
        setMaximumSize(size);
        setMaximumSize(size);
        setLayout(null);
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.drawImage(image, 0, 0, null);
    }

}
