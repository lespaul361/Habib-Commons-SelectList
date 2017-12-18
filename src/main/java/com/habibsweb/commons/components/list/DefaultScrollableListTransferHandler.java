/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.habibsweb.commons.components.list;

import com.habibsweb.commons.components.ScrollableList;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.TransferHandler;

/**
 *
 * @author David Hamilton
 */
public class DefaultScrollableListTransferHandler extends TransferHandler {

    @Override
    public int getSourceActions(JComponent c) {
        return TransferHandler.COPY_OR_MOVE;
    }

    @Override
    protected Transferable createTransferable(JComponent source) {
        ScrollableList lst = (ScrollableList) source;
        List<Object> selected = Arrays.asList(lst.getModel().getSelectedItems());
        List<DefaultScrollableListDataFlavor> flavors = new ArrayList<>();
        for (Object sel : selected) {
            flavors.add(new DefaultScrollableListDataFlavor(sel));
        }

        DefaultScrollableListTransferable t = new DefaultScrollableListTransferable(flavors);
        return t;
    }

    @Override
    protected void exportDone(JComponent source, Transferable data, int action) {
        @SuppressWarnings("unchecked")
        ScrollableList sourceList = (ScrollableList) source;
         if (action == TransferHandler.MOVE) {
            DefaultScrollableListModel listModel = (DefaultScrollableListModel) sourceList
                    .getModel();
            for (Object selectedItem : listModel.getSelectedItems()) {
                listModel.removeItem(selectedItem);
            }
        }
    }

    @Override
    public boolean canImport(TransferHandler.TransferSupport support) {
        if (!support.isDrop()) {
            return false;
        }
        return support.isDataFlavorSupported(DataFlavor.stringFlavor);
    }
    
    
}
