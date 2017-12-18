/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.habibsweb.commons.components;

import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.util.List;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * Creates a <code>JDialog</code> with a non visible frame so that a icon with
 * show in the icon bar
 *
 * @author David Hamilton
 */
public class JDialogFakeParent extends JDialog {

    /**
     * Constructs a new <code>JDialogFakeParent</code> component
     *
     * @param iconImages Images to use for the <code>JDialog</code>
     */
    public JDialogFakeParent(List<? extends Image> iconImages) {
        super(new DummyFrame("", iconImages));
    }

    /**
     * Constructs a new <code>JDialogFakeParent</code> component
     *
     * @param iconImages Images to use for the <code>JDialog</code>
     * @param modal specifies whether dialog blocks user input to other
     * top-level windows when shown. If {@code true}, the modality type property
     * is set to {@code DEFAULT_MODALITY_TYPE}, otherwise the dialog is
     * modeless.
     */
    public JDialogFakeParent(List<? extends Image> iconImages, boolean modal) {
        super(new DummyFrame("", iconImages), modal);
    }

    /**
     * Constructs a new <code>JDialogFakeParent</code> component
     *
     * @param iconImages Images to use for the <code>JDialog</code>
     * @param title the {@code String} to display in the dialog's title bar
     */
    public JDialogFakeParent(List<? extends Image> iconImages, String title) {
        super(new DummyFrame("", iconImages), title);
    }

    /**
     * Constructs a new <code>JDialogFakeParent</code> component
     *
     * @param iconImages Images to use for the <code>JDialog</code>
     * @param title the {@code String} to display in the dialog's title bar
     * @param modal specifies whether dialog blocks user input to other
     * top-level windows when shown. If {@code true}, the modality type property
     * is set to {@code DEFAULT_MODALITY_TYPE}, otherwise the dialog is
     * modeless.
     */
    public JDialogFakeParent(List<? extends Image> iconImages, String title,
            boolean modal) {
        super(new DummyFrame("", iconImages), title, modal);
    }

    /**
     * Constructs a new <code>JDialogFakeParent</code> component
     *
     * @param iconImages Images to use for the <code>JDialog</code>
     * @param title the {@code String} to display in the dialog's title bar
     * @param modal specifies whether dialog blocks user input to other
     * top-level windows when shown. If {@code true}, the modality type property
     * is set to {@code DEFAULT_MODALITY_TYPE}, otherwise the dialog is
     * modeless.
     * @param gc the {@code GraphicsConfiguration} of the target screen device;
     * if {@code null}, the default system {@code GraphicsConfiguration} is
     * assumed
     */
    public JDialogFakeParent(List<? extends Image> iconImages, String title,
            boolean modal, GraphicsConfiguration gc) {
        super(new DummyFrame("", iconImages), title, modal, gc);
    }

    /**
     * Constructs a new <code>JDialogFakeParent</code> component
     *
     * @param iconImages Images to use for the <code>JDialog</code>
     * @param modalityType specifies whether dialog blocks input to other
     * windows when shown. {@code null} value and unsupported modality types are
     * equivalent to {@code MODELESS}
     */
    public JDialogFakeParent(List<? extends Image> iconImages,
            ModalityType modalityType) {
        super(new DummyFrame("", iconImages), modalityType);
    }

    /**
     * Constructs a new <code>JDialogFakeParent</code> component
     *
     * @param iconImages Images to use for the <code>JDialog</code>
     * @param title the {@code String} to display in the dialog's title bar
     * @param modalityType specifies whether dialog blocks input to other
     * windows when shown. {@code null} value and unsupported modality types are
     * equivalent to {@code MODELESS}
     */
    public JDialogFakeParent(List<? extends Image> iconImages, String title,
            ModalityType modalityType) {
        super(new DummyFrame("", iconImages), title, modalityType);
    }

    /**
     * Constructs a new <code>JDialogFakeParent</code> component
     *
     * @param iconImages Images to use for the <code>JDialog</code>
     * @param title the {@code String} to display in the dialog's title bar
     * @param modalityType specifies whether dialog blocks input to other
     * windows when shown. {@code null} value and unsupported modality types are
     * equivalent to {@code MODELESS}
     * @param gc the {@code GraphicsConfiguration} of the target screen device;
     * if {@code null}, the default system {@code GraphicsConfiguration} is
     * assumed
     */
    public JDialogFakeParent(List<? extends Image> iconImages, String title,
            ModalityType modalityType, GraphicsConfiguration gc) {
        super(new DummyFrame("", iconImages), title, modalityType, gc);
    }

    @Override
    public void setVisible(boolean b) {
        super.setVisible(b);
        if (!b) {
            if (super.getParent() instanceof DummyFrame) {
                SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            ((DummyFrame) getParent()).dispose();
                        } catch (Exception e) {
                            e.printStackTrace(System.out);
                        }

                    }
                });
            }
        }
    }

}

class DummyFrame extends JFrame {

    DummyFrame(String title, List<? extends Image> iconImages) {
        super(title);
        setUndecorated(true);
        setVisible(true);
        setLocationRelativeTo(null);
        setIconImages(iconImages);
    }
}
