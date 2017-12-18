/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.habibsweb.commons.components.list;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A class for doing drag and drop with the <code>ScrollableList</code>
 *
 * @author David Hamilton
 */
public class DefaultScrollableListTransferable implements Transferable {

    private List<DataFlavor> flavors = new ArrayList<>();

    /**
     * Constructs a new <code>DefaultScrollableListTransferable</code>
     *
     * @param data the data to transfer
     */
    public DefaultScrollableListTransferable(DefaultScrollableListDataFlavor data) {
        flavors.add(data);
    }

    /**
     * Constructs a new <code>DefaultScrollableListTransferable</code>
     *
     * @param datas the data to transfer
     */
    public DefaultScrollableListTransferable(DefaultScrollableListDataFlavor[] datas) {
        flavors.addAll(Arrays.asList(datas));
    }

    /**
     * Constructs a new <code>DefaultScrollableListTransferable</code>
     *
     * @param datas the data to transfer
     */
    public DefaultScrollableListTransferable(List<DefaultScrollableListDataFlavor> datas) {
        flavors.addAll(datas);
    }

    @Override
    public DataFlavor[] getTransferDataFlavors() {
        return flavors.toArray(new DefaultScrollableListDataFlavor[flavors.size()]);
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        boolean supported = false;

        for (DataFlavor mine : getTransferDataFlavors()) {

            if (mine.equals(flavor)) {

                supported = true;
                break;

            }

        }

        return supported;

    }

    @Override
    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
        return flavors.toArray(new DefaultScrollableListDataFlavor[flavors.size()]);
    }

}
