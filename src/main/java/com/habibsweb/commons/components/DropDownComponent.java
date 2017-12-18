/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.habibsweb.commons.components;

import java.awt.Component;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JWindow;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.plaf.metal.MetalComboBoxIcon;

/**
 *
 * @author David Hamilton
 */
public class DropDownComponent extends JComponent implements ActionListener, AncestorListener {

    protected final JComponent dropDownComp;
    protected final JComponent visibleComp;
    protected final JButton arrow;
    protected JWindow popupWindow;

    public DropDownComponent(JComponent dropDownComp, JComponent visibleComp) {
        this.dropDownComp = dropDownComp;
        this.visibleComp = visibleComp;

        this.arrow = new JButton(new MetalComboBoxIcon());
        Insets insets = arrow.getMargin();
        arrow.setMargin(new Insets(insets.top, 1, insets.bottom, 1));
        arrow.addActionListener(this);
        addAncestorListener(this);
        initComponent();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //build popup     
        popupWindow = new JWindow(getFrame(null));
        popupWindow.getContentPane().add(dropDownComp);
        popupWindow.addWindowFocusListener(new WindowAdapter() {
            public void windowLostFocus(WindowEvent evt) {
                popupWindow.setVisible(false);
            }
        });
        popupWindow.pack();

        // show the popup window
        Point pt = visibleComp.getLocationOnScreen();
        System.out.println("pt = " + pt);
        pt.translate(visibleComp.getWidth() - popupWindow.getWidth(), visibleComp.getHeight());
        System.out.println("pt = " + pt);
        popupWindow.setLocation(pt);
        popupWindow.toFront();
        popupWindow.setVisible(true);
        popupWindow.requestFocusInWindow();
    }

    @Override
    public void ancestorAdded(AncestorEvent event) {
        hidePopup();
    }

    @Override
    public void ancestorRemoved(AncestorEvent event) {
        hidePopup();
    }

    @Override
    public void ancestorMoved(AncestorEvent event) {
        if (event.getSource() != popupWindow) {
            hidePopup();
        }
    }

    public void hidePopup() {
        if (this.popupWindow != null && this.popupWindow.isVisible()) {
            popupWindow.setVisible(false);
        }
    }

    protected Frame getFrame(Component comp) {
        if (comp == null) {
            comp = this;
        }

        if (comp.getParent() instanceof Frame) {
            return (Frame) comp.getParent();
        }

        return getFrame(comp.getParent());
    }

    private void initComponent() {
        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        setLayout(gbl);

        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbl.setConstraints(this.visibleComp, gbc);
        add(this.visibleComp);

        gbc.weightx = 0;
        gbc.gridx++;
        gbl.setConstraints(this.arrow, gbc);
        add(this.arrow);
    }

}
