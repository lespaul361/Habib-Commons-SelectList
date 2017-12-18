/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.habibsweb.commons.components;

import com.habibsweb.commons.components.event.ItemEditorListener;
import java.awt.Dimension;
import javax.swing.JComponent;
import static com.habibsweb.commons.components.list.ScrollableListModel.*;
import com.habibsweb.commons.components.event.ScrollableListItemSelectionEvent;
import static com.habibsweb.commons.components.event.ScrollableListItemSelectionEvent.UNSELECTED;
import com.habibsweb.commons.components.event.ScrollableListItemSelectionListener;
import com.habibsweb.commons.components.event.ScrollableListModelEvent;
import com.habibsweb.commons.components.event.ScrollableListModelListener;
import com.habibsweb.commons.components.event.SelectionBoxEvent;
import com.habibsweb.commons.components.event.SelectionBoxListener;
import com.habibsweb.commons.components.list.DefaultScrollableListModel;
import com.habibsweb.commons.components.list.DefaultScrollableListRenderer;
import com.habibsweb.commons.components.list.DefaultScrollableListTransferHandler;
import com.habibsweb.commons.components.list.ScrollableListItemEditor;
import com.habibsweb.commons.components.list.ScrollableListModel;
import com.habibsweb.commons.components.list.ScrollableListRenderer;
import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.dnd.DropTarget;
import static java.awt.event.ItemEvent.SELECTED;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.Transient;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EventListener;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DropMode;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;
import javax.swing.event.ChangeEvent;
import javax.swing.event.EventListenerList;

/**
 * A <code>JComponent</code> extension to support drawing a list
 *
 * @author Charles Hamilton
 */
public class ScrollableList extends JComponent implements ScrollableListModelListener,
        ItemEditorListener, Serializable {
///
/// Variables
///

    /**
     * A property constant
     */
    public static final String PROP_DATAMODEL = "MODEL";

    /**
     * A property constant
     */
    public static final String PROP_LISTRENDERER = "RENDERER";

    /**
     * A property constant
     */
    public static final String PROP_LISTEDITOR = "EDITOR";

    /**
     * the <code>ScrollableListModel</code> of the <code>ScrollableList</code>
     */
    private ScrollableListModel dataModel;
    /**
     * The <code>ScrollableListRenderer</code> of the
     * <code>ScrollableList</code>
     */
    private ScrollableListRenderer listRenderer;
    /**
     * The <code>ScrollableListItemEditor</code> of the
     * <code>ScrollableList</code>
     */
    private ScrollableListItemEditor listEditor;

    /**
     * The component for editing the list
     */
    protected transient Component editorComponent;
    private transient ScrollableListItemEditor editor;

    /**
     * A Map of the child controls and their values.
     */
    protected final Map<Component, Object> childControls = new HashMap<>();
    private final EventListenerList eventList = new EventListenerList();
    private int editingItemIndex = -1;
    private PropertyChangeListener editorRemover = null;

    /**
     * The <code>JPanel</code> that has the list values
     */
    protected final JPanel pnlMain = new JPanel() {

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (!paintedComponents && this.getComponentCount() != 0) {
                paintedComponents = true;
                if (doUpDateList) {
                    updateItems();
                }
            } else if (!paintedComponents && this.getComponentCount() == 0 && getModel().getItemCount() > 0) {
                paintedComponents = true;
                initListObjects();
            }
            if (this.getComponentCount() != 0) {
                if (getHorizontalScrollBar().isVisible()) {
                    getHorizontalScrollBar().setUnitIncrement(Integer.parseInt(String.valueOf(this.WIDTH / 2)));
                }
                if (getVerticalScrollBar().isVisible()) {
                    Component c = (Component) childControls.keySet().toArray()[0];
                    getVerticalScrollBar().setUnitIncrement(c.getHeight());
                }
            }
        }

    };
    private final JPanel pnlControl = new JPanel();
    private final JScrollPane scrollPane = new JScrollPane(pnlMain);
    private Font fontTitle;
    private JLabel lblTitle = null;
    private int titleAlignment = TITLE_ALIGN_CENTER;
    private String titleText = null;
    private Box titleBox = null;

    /**
     * variable for drag and drop
     */
    protected boolean dragAndDropEnabled = false;

    /**
     * A <code>SelectionBox</code> component
     */
    protected SelectionBox selectionBox;
    private List<Object> preSelectionBoxSelections = new ArrayList<>();
    private Map<Component, List<MouseListener>> childRemovedListeners = new HashMap<>();

    /**
     * Align the title to the left
     */
    public static final int TITLE_ALIGN_LEFT = -1;

    /**
     * Align the title to the center
     */
    public static final int TITLE_ALIGN_CENTER = 0;

    /**
     * Align the title to the right
     */
    public static final int TITLE_ALIGN_RIGHT = 1;

    /**
     * flag for telling the update routine there is pending changes
     */
    boolean doUpDateList = false;
    /**
     * flag for changed made before control is loaded
     */
    boolean paintedComponents = false;
    /**
     * flag for stopping events from firing
     */
    boolean allowFireChange = true;

    /**
     * The drop mode for drag and drop
     */
    protected DropMode dropMode = DropMode.USE_SELECTION;

    private final transient PropertyChangeSupport propertyChangeSupport = new java.beans.PropertyChangeSupport(this);
    private final PropertyChangeListener rendererPropertyChangeListener = new PropertyChangeListener() {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            rendererPropertyChangeHandler(evt);
        }
    };

    // This class tracks changes in the keyboard focus state. It is used
    // when editing to determine when to cancel the edit.
    // If focus switches to a component outside of the ScrollableList, but in the
    // same window, this will cancel editing.
    class ItemEditorRemover implements PropertyChangeListener {

        KeyboardFocusManager focusManager;

        public ItemEditorRemover(KeyboardFocusManager fm) {
            this.focusManager = fm;
        }

        public void propertyChange(PropertyChangeEvent ev) {
            if (!isEditing() || getClientProperty("terminateEditOnFocusLost") != Boolean.TRUE) {
                return;
            }

            Component c = focusManager.getPermanentFocusOwner();
            while (c != null) {
                if (c == ScrollableList.this) {
                    // focus remains inside the table
                    return;
                } else if ((c instanceof Window)
                        || (c instanceof Applet && c.getParent() == null)) {
                    if (c == SwingUtilities.getRoot(ScrollableList.this)) {
                        if (!getEditor().stopItemEditing()) {
                            getEditor().cancelItemEditing();
                        }
                    }
                    break;
                }
                c = c.getParent();
            }
        }
    }

///
/// Constructors
///
    /**
     * Constructs a new <code>ScrollableList</code>
     */
    public ScrollableList() {
        super();
        initScrollableList(new DefaultScrollableListModel(this), null, null);
    }

    /**
     * Constructs a new <code>ScrollableList</code>
     *
     * @param items an array of items
     */
    public ScrollableList(Object[] items) {
        super();
        initScrollableList(new DefaultScrollableListModel(items, this), null, null);
    }

    /**
     * Constructs a new <code>ScrollableList</code>
     *
     * @param model the model for the <code>ScrollableList</code>
     */
    public ScrollableList(ScrollableListModel model) {
        super();
        initScrollableList(model, null, null);
    }

    /**
     * Constructs a new <code>ScrollableList</code>
     *
     * @param items an <code>List</code> of items
     */
    public ScrollableList(List<Object> items) {
        super();
        initScrollableList(new DefaultScrollableListModel(items, this), null, null);
    }

    /**
     * Constructs a new <code>ScrollableList</code>
     *
     * @param items an <code>List</code> of items
     * @param renderer a <code>ScrollableListRenderer</code> to render this list
     * items
     */
    public ScrollableList(List<Object> items, ScrollableListRenderer renderer) {
        super();
        initScrollableList(new DefaultScrollableListModel(items, this), null, renderer);
    }

    /**
     * Constructs a new <code>ScrollableList</code>
     *
     * @param model the model for the <code>ScrollableList</code>
     * @param renderer a <code>ScrollableListRenderer</code> to render this list
     * items
     */
    public ScrollableList(ScrollableListModel model, ScrollableListRenderer renderer) {
        super();
        initScrollableList(model, null, renderer);
    }

    /**
     * Constructs a new <code>ScrollableList</code>
     *
     * @param items an array of items
     * @param renderer a <code>ScrollableListRenderer</code> to render this list
     * items
     */
    public ScrollableList(Object[] items, ScrollableListRenderer renderer) {
        super();
        initScrollableList(new DefaultScrollableListModel(items, this), null, renderer);
    }

    /**
     * Constructs a new <code>ScrollableList</code>
     *
     * @param items an <code>List</code> of items
     * @param renderer a <code>ScrollableListRenderer</code> to render this list
     * items
     * @param editor the <code>ScrollableLiestItemEditor</code> for editing
     * items in the <code>ScrollableLisst</code>
     */
    public ScrollableList(List<Object> items, ScrollableListRenderer renderer, ScrollableListItemEditor editor) {
        super();
        initScrollableList(new DefaultScrollableListModel(items, this), editor, renderer);
    }

    /**
     * Constructs a new <code>ScrollableList</code>
     *
     * @param model the model for the <code>ScrollableList</code>
     * @param renderer a <code>ScrollableListRenderer</code> to render this list
     * items
     * @param editor the <code>ScrollableLiestItemEditor</code> for editing
     * items in the <code>ScrollableLisst</code>
     */
    public ScrollableList(ScrollableListModel model, ScrollableListRenderer renderer, ScrollableListItemEditor editor) {
        super();
        initScrollableList(model, editor, renderer);
    }

    /**
     * Constructs a new <code>ScrollableList</code>
     *
     * @param items an array of items
     * @param renderer a <code>ScrollableListRenderer</code> to render this list
     * items
     * @param editor the <code>ScrollableLiestItemEditor</code> for editing
     * items in the <code>ScrollableLisst</code>
     */
    public ScrollableList(Object[] items, ScrollableListRenderer renderer, ScrollableListItemEditor editor) {
        super();
        initScrollableList(new DefaultScrollableListModel(items, this), editor, renderer);
    }

///
/// Geters and Setters
///
    /**
     * Sets the model to use for this <code>ScrollableList</code>
     *
     * @param model the model to use for this <code>ScrollableList</code>
     */
    public void setModel(ScrollableListModel model) {
        com.habibsweb.commons.components.list.ScrollableListModel oldDataModel = this.dataModel;
        this.dataModel = model;
        if (selectionBox != null) {
            if (model.getSelectionMode() == ScrollableListModel.SELECTION_MODE_SINGLE) {
                selectionBox.setAllowSelectionBox(false);
            } else {
                selectionBox.setAllowSelectionBox(true);
            }
        }
        model.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName() == ScrollableListModel.PROP_SELECTIONMODE) {
                    if (model.getSelectionMode() == ScrollableListModel.SELECTION_MODE_SINGLE) {
                        selectionBox.setAllowSelectionBox(false);
                    } else {
                        selectionBox.setAllowSelectionBox(true);
                    }
                }
            }
        });
        propertyChangeSupport.firePropertyChange(PROP_DATAMODEL, oldDataModel, dataModel);
        pnlMain.removeAll();
        childControls.clear();
        revalidate();
        repaint();
        allowFireChange = false;
        initListObjects();
        allowFireChange = true;
    }

    /**
     * Gets the model used for this <code>ScrollableList</code>
     *
     * @return the model used for this <code>ScrollableList</code>
     */
    public ScrollableListModel getModel() {
        return this.dataModel;
    }

    /**
     * Sets the <code>ScrollableListRenderer</code> used to draw the items.
     * Returned <code>Components</code> must implements
     * <code>ScrollableListRenderer</code>
     *
     * @param renderer the <code>ScrollableListRenderer</code> used to draw the
     * items
     */
    public void setRenderer(ScrollableListRenderer renderer) {
        com.habibsweb.commons.components.list.ScrollableListRenderer oldListRenderer = this.listRenderer;
        this.listRenderer = renderer;
        propertyChangeSupport.firePropertyChange(PROP_LISTRENDERER, oldListRenderer, listRenderer);
        pnlMain.removeAll();
        childControls.clear();
        initListObjects();
    }

    /**
     * Gets the <code>ScrollableListRenderer</code> used to draw the items
     *
     * @return the <code>ScrollableListRenderer</code> used to draw the items
     */
    public ScrollableListRenderer getRenderer() {
        return this.listRenderer;
    }

    /**
     * Sets the font for the title
     *
     * @param font the font for the title
     */
    public void setTitleFont(Font font) {
        this.fontTitle = font;
        updateTitle();
    }

    /**
     * Gets the font for the title
     *
     * @return the font for the title
     */
    public Font getTitleFont() {
        if (this.fontTitle == null) {
            this.fontTitle = Font.decode(null);
            this.fontTitle = this.fontTitle.deriveFont(20.0f);
            int style = this.fontTitle.getStyle();
            style = style | Font.BOLD;
            this.fontTitle = this.fontTitle.deriveFont(style);
        }
        return this.fontTitle;
    }

    /**
     * Sets the alignment for the title
     *
     * @param alignment the alignment for the title
     */
    public void setTitleAlignment(int alignment) {
        this.titleAlignment = alignment;
        updateTitle();
    }

    /**
     * Gets the alignment for the title
     *
     * @return the alignment for the title
     */
    public int getTitleAlignment() {
        return this.titleAlignment;
    }

    /**
     * Gets the text of the title
     *
     * @return the text of the title. Null allowed.
     */
    public String getTitleText() {
        return titleText;
    }

    /**
     * Sets the text of the title
     *
     * @param titleText the text of the title
     */
    public void setTitleText(String titleText) {
        if (titleText != null && titleText != "") {
            this.titleText = titleText;
        } else {
            this.titleText = null;
        }

        updateTitle();
    }

    /**
     * Gets the selection mode used
     *
     * @return the selection mode used
     */
    public int getSelectionMode() {
        return getModel().getSelectionMode();
    }

    /**
     * Sets the selection mode used
     *
     * @param selectionMode the selection mode used
     */
    public void setSelectionMode(int selectionMode) {
        getModel().setSelectionMode(selectionMode);
    }

    /**
     * Gets the index of an item
     *
     * @param item item to find the index for
     * @return the index of an item
     */
    public int getItemIndex(Object item) {
        return this.getModel().getItemIndex(item);
    }

    /**
     * Gets the <code>ScrollableListItemEditor</code> for editing list items
     *
     * @return the <code>ScrollableListItemEditor</code> for editing list items
     */
    public ScrollableListItemEditor getEditor() {
        if (this.listEditor == null) {
            this.setEditor(new DefaultScrollableListEditor(new JTextField()));
        }
        return this.listEditor;
    }

    /**
     * Sets the <code>ScrollableListItemEditor</code> for editing list items
     *
     * @param editor the <code>ScrollableListItemEditor</code> for editing list
     * items
     */
    public void setEditor(ScrollableListItemEditor editor) {
        com.habibsweb.commons.components.list.ScrollableListItemEditor oldListEditor = this.listEditor;
        this.listEditor = editor;
        propertyChangeSupport.firePropertyChange(PROP_LISTEDITOR, oldListEditor, listEditor);
    }

    /**
     * Sets if this <code>ScrollableList</code> supports drag and drop
     *
     * @param b enables or disables drag and drop
     */
    public void setDragEnabled(boolean b) {
        this.dragAndDropEnabled = b;
        getRenderer().setDragEnabled(b);
        setTransferHandler(new DefaultScrollableListTransferHandler());

        Iterator it = childControls.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            ScrollableListRenderer renderer = (ScrollableListRenderer) pair.getKey();
            if (renderer instanceof JComponent) {
                renderer.setDragEnabled(b);
                if (b) {
                    ((JComponent) renderer).setTransferHandler(new TransferHandler("text"));
                } else {
                    ((JComponent) renderer).setTransferHandler(null);
                }
            }
        }
        setDropTarget(new DropTarget());
    }

///
/// Public Methods
///
    /**
     * Get the item located at the specified index.
     *
     * @param index the index of the item
     * @return the item located at the specified index.
     * @throws ArrayIndexOutOfBoundsException if the index is not in the array
     */
    public Object getItemAt(int index) throws ArrayIndexOutOfBoundsException {
        return getModel().getItemIndex(index);
    }

    /**
     * Sets the value at the specified index.
     *
     * @param value the new value
     * @param index the index of the item to set
     * @throws ArrayIndexOutOfBoundsException if the index is not in the array
     */
    public void setItemAt(Object value, int index) throws ArrayIndexOutOfBoundsException {
        getModel().setValueAt(value, index);
    }

    /**
     * Sets the selected status of an item
     *
     * @param index the index of the item to change the selection status on
     * @param selected the selection status
     * @throws ArrayIndexOutOfBoundsException if the index is not in the array
     */
    public void setItemSelectionAt(int index, boolean selected) throws ArrayIndexOutOfBoundsException {
        getModel().setItemSelectionAt(index, selected);
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                if (!paintedComponents && pnlMain.getComponentCount() == 0) {
                    doUpDateList = true;
                }
            }
        });

    }

    /**
     * Sets the selected status of an item
     *
     * @param item the item to change the selection status on
     * @param selected the selection status
     * @throws ArrayIndexOutOfBoundsException if the index is not in the array
     */
    public void setItemSelection(Object item, boolean selected) throws ArrayIndexOutOfBoundsException {
        getModel().setItemSelection(item, selected);
        if (!paintedComponents) {
            doUpDateList = true;
        }
    }

    /**
     * Gets an array of the currently selected items
     *
     * @return an array of the currently selected items
     */
    public Object[] getSelectedItems() {
        return getModel().getSelectedItems();
    }

    /**
     * Adds an item to the <code>ScrollableList</code>
     *
     * @param item to be added to the <code>ScrollableList</code>
     */
    public void addItem(Object item) {
        this.dataModel.addItem(item);
    }

    /**
     * Removes an item to the <code>ScrollableList</code>
     *
     * @param item to be removed from the <code>ScrollableList</code>
     */
    public void removeItem(Object item) {
        this.dataModel.removeItem(item);
    }

    /**
     * Removes an item to the <code>ScrollableList</code>
     *
     * @param index of the item to be removed from the
     * <code>ScrollableList</code>
     */
    public void removeItem(int index) {
        removeItem(this.dataModel.getValueAt(index));
    }

    /**
     * Prepares the renderer by querying the data model for the value and
     * selection state
     *
     * @param renderer the <code>ScrollableListRenderer</code> to prepare
     * @param index the index of the item to render
     * @return the <code>Component</code> under the event location
     * @throws java.lang.Exception exception in processing
     */
    public Component prepareRenderer(ScrollableListRenderer renderer, int index) throws Exception {
        Object value = this.getModel().getValueAt(index);
        Component component = renderer.getScrollableListItemRendererComponent(this, value,
                false, index);
        if (!(component instanceof ScrollableListRenderer)) {
            throw new Exception("Component must implement ScrollableListRenderer");
        }
        childControls.put(component, getModel().getValueAt(index));
        childComponentMouseAdapter adpt = new childComponentMouseAdapter(this);
        component.addMouseListener(adpt);
        component.addMouseMotionListener(adpt);
        return component;
    }

    /**
     * Prepares the editor by querying the data model for the value and
     * selection state
     *
     * @param editor the <code>ScrollableListItemEditor</code> to prepare
     * @param index the index of the item to edit
     * @return the <code>ScrollableListItemEditor</code> to prepare
     */
    public Component prepareEditor(ScrollableListItemEditor editor, int index) {
        Object value = getModel().getValueAt(index);
        boolean isSelected = Arrays.asList(getModel().getSelectedItems()).contains(value);
        Component comp = editor.getScrollableListItemEditorComponent(this, value, index, isSelected);
        if (comp instanceof JComponent) {
            JComponent jComponent = (JComponent) comp;
            if (jComponent.getClientProperty("nextFocuse") == null) {
                jComponent.setNextFocusableComponent(this);
            }

        }
        return comp;
    }

    /**
     * Adds a listener to listen for selection changes
     *
     * @param l a listener to listen for selection changes
     */
    public void addScrollableListItemSelectionListener(ScrollableListItemSelectionListener l) {
        this.eventList.add(ScrollableListItemSelectionListener.class, l);
    }

    /**
     * Removes a listener to listen for selection changes
     *
     * @param l a listener to listen for selection changes
     */
    public void removeScrollableListItemSelectionListener(ScrollableListItemSelectionListener l) {
        this.eventList.remove(ScrollableListItemSelectionListener.class, l);
    }

    /**
     * Selects all the items in the list if the selection mode is not
     * SELECTION_MODE_SINGLE
     */
    public void selectAll() {
        if (getSelectionMode() == SELECTION_MODE_SINGLE) {
            System.out.println("ScrollableList cannot use selectAll when the "
                    + "selection mode is SELECTION_MODE_SINGLE");
            return;
        }
        List<Object> selectedValues = Arrays.asList(getModel().getSelectedItems());
        for (int i = 0; i < getModel().getItemCount(); i++) {
            Object value = getModel().getValueAt(i);
            if (!selectedValues.contains(value)) {
                Component component = getComponentFromItem(value);
                ((ScrollableListRenderer) component).setSelected(true);
                getModel().setItemSelection(childControls.get(component), true);
                fireSelectionListeners(SELECTED, i);
            }
        }
    }

    /**
     * Returns true if a cell is being edited.
     *
     * @return true if the table is editing a cell
     */
    public boolean isEditing() {
        return this.editorComponent != null;
    }

    /**
     * Discards the editor object and frees the real estate it used for cell
     * rendering.
     */
    public void removeEditor() {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().
                removePropertyChangeListener("permanentFocusOwner", editorRemover);
        editorRemover = null;

        ScrollableListItemEditor editor = getEditor();
        if (editor != null) {
            editor.removeScrollableListEditorListener(this);
            if (editorComponent != null) {
                Component focusOwner
                        = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
                boolean isFocusOwnerInTheTable = focusOwner != null
                        ? SwingUtilities.isDescendingFrom(focusOwner, this) : false;
                pnlMain.remove(editorComponent);
                if (isFocusOwnerInTheTable) {
                    requestFocusInWindow();
                }
            }

            //setEditor(null);
            editingItemIndex = -1;
            this.editorComponent = null;
            JPanel glass = (JPanel) pnlMain.getRootPane().getGlassPane();
            glass.setVisible(false);
            glass.removeMouseListener(glass.getMouseListeners()[0]);
            glass.removeAll();
            repaint(scrollPane.getVisibleRect());
        }
    }

    /**
     * Shows the editor to edit an item
     *
     * @param index the index of the item to edit
     * @param e an event object
     * @return true if editor is shown
     */
    public boolean editItemAt(int index, EventObject e) {
        if (this.listEditor != null && !this.listEditor.isItemEditable(e)) {
            return false;
        }

        if (!getModel().isItemEditable(index)) {
            return false;
        }

        if (editorRemover == null) {
            KeyboardFocusManager fm
                    = KeyboardFocusManager.getCurrentKeyboardFocusManager();
            editorRemover = new ItemEditorRemover(fm);
            fm.addPropertyChangeListener("permanentFocusOwner", editorRemover);

        }

        ScrollableListItemEditor edit = getEditor();
        if (getModel().isItemEditable(index)) {
            editorComponent = prepareEditor(edit, index);
            if (editorComponent == null) {
                removeEditor();
                return false;
            }
            //editorComponent.setBounds(getEditorBounds(index));

            try {
                editorComponent.setFont(this.getFont());
            } catch (Exception ex) {
                ex.printStackTrace(System.out);
            }

            setUpGlassPane(index);
            editorComponent.validate();
            editorComponent.repaint();

            setEditor(edit);
            edit.addScrollableListEditorListener(this);
            editingItemIndex = index;
            return true;
        }

        return false;
    }

    /**
     * Shows the editor to edit an item
     *
     * @param index the index of the item to edit
     * @return true if editor is shown
     */
    public boolean editItemAt(int index) {
        return editItemAt(index, null);
    }

    /**
     * Sets the value at the specified index.
     *
     * @param value the new value
     * @param index the index of the item to set
     * @throws ArrayIndexOutOfBoundsException if the index is not in the array
     */
    public void setItemValueAt(Object value, int index) {
        getModel().setValueAt(value, index);
    }

    /**
     * Returns the horizontal scroll bar that controls the viewport's horizontal
     * view position.
     *
     * @return the <code>horizontalScrollBar</code> property
     * @see #setHorizontalScrollBar
     */
    @Transient
    public JScrollBar getHorizontalScrollBar() {
        return scrollPane.getHorizontalScrollBar();
    }

    /**
     * Adds the scrollbar that controls the viewport's horizontal view position
     * to the scrollpane. This is usually unnecessary, as
     * <code>JScrollPane</code> creates horizontal and vertical scrollbars by
     * default.
     *
     * @param horizontalScrollBar the horizontal scrollbar to be added
     * @see #getHorizontalScrollBar
     *
     */
    public void setHorizontalScrollBar(JScrollBar horizontalScrollBar) {
        JScrollBar old = getHorizontalScrollBar();
        scrollPane.setHorizontalScrollBar(horizontalScrollBar);
        firePropertyChange("horizontalScrollBar", old, horizontalScrollBar);
    }

    /**
     * Returns the vertical scroll bar that controls the viewports vertical view
     * position.
     *
     * @return the <code>verticalScrollBar</code> property
     * @see #setVerticalScrollBar
     */
    @Transient
    public JScrollBar getVerticalScrollBar() {
        return scrollPane.getVerticalScrollBar();
    }

    /**
     * Adds the scrollbar that controls the viewports vertical view position to
     * the scrollpane. This is usually unnecessary, as <code>JScrollPane</code>
     * creates vertical and horizontal scrollbars by default.
     *
     * @param verticalScrollBar the new vertical scrollbar to be added
     * @see #getVerticalScrollBar
     *
     */
    public void setVerticalScrollBar(JScrollBar verticalScrollBar) {
        JScrollBar old = getVerticalScrollBar();
        scrollPane.setVerticalScrollBar(verticalScrollBar);
        firePropertyChange("verticalScrollBar", old, verticalScrollBar);
    }
///
/// Super overrides
///

    @Override
    public void setFont(Font font) {
        super.setFont(font);
        if (getModel() != null) {
            updateItems();
        }
    }

    @Override
    public boolean hasFocus() {
        if (!isDisplayable()) {
            return false;
        }
        try {
            Point mouseLoc = MouseInfo.getPointerInfo().getLocation();
            Point listLoc = this.getLocationOnScreen();
            Rectangle rec = new Rectangle(listLoc, this.getSize());
            if (rec.contains(mouseLoc)) {
                return true;
            }
        } catch (java.awt.IllegalComponentStateException ex) {
            //buggy not showing on the screen error
            return false;
        } catch (Exception e) {
            e.printStackTrace(System.out);
            return false;
        }

        return false;
    }

///
/// Protected Methods
///
    /**
     * Method to recreate the items when a change affecting the items has
     * changed
     */
    protected void updateItems() {
        if (!paintedComponents) {
            doUpDateList = true;
            return;
        }
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                ScrollableListModel model = getModel();
                List<Object> values = Arrays.asList(model.getItems());
                Iterator it = childControls.entrySet().iterator();
                Object[] selectedItems = model.getSelectedItems();
                List<Object> listSelected = Arrays.asList(selectedItems);
                while (it.hasNext()) {
                    Map.Entry pair = (Map.Entry) it.next();
                    if (listSelected.contains(pair.getValue())) {
                        ((ScrollableListRenderer) pair.getKey()).setSelected(true);
                        if (doUpDateList) {
                            fireSelectionListeners(ScrollableListItemSelectionEvent.SELECTED, model.getItemIndex(pair.getValue()));
                        }
                    }
                }
                revalidate();
                repaint();
            }
        });

    }

    private void initListObjects() {
        ScrollableListModel model = getModel();
        if (model != null && selectionBox != null) {
            if (model.getSelectionMode() == ScrollableListModel.SELECTION_MODE_SINGLE) {
                selectionBox.setAllowSelectionBox(false);
            } else {
                selectionBox.setAllowSelectionBox(true);
            }
        }

        int pnlCount = pnlMain.getComponentCount();
        if (pnlMain.getComponentCount() == 0 && model != null && model.getItemCount() > 0) {
            Object value = null;
            Component comp = null;
            ScrollableListRenderer renderer = getRenderer();
            for (int i = 0; i < getModel().getItemCount(); i++) {
                value = getModel().getValueAt(i);
                try {
                    comp = prepareRenderer(renderer, i);
                    if (dragAndDropEnabled) {
                        if (comp instanceof JComponent) {
                            JComponent c = (JComponent) comp;
                            ((ScrollableListRenderer) comp).setDragEnabled(true);
                            c.setTransferHandler(new TransferHandler("text"));
                        }
                    }
                    pnlMain.add(comp);
                } catch (Exception e) {
                    e.printStackTrace(System.out);
                }

            }
            updateItems();
        }
    }

    /**
     * Method to recreate the items when a change affecting the title has
     * changed
     */
    protected void updateTitle() {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                if (titleBox != null) {
                    try {
                        pnlControl.remove(titleBox);
                    } catch (Exception e) {
                    }

                }

                if (titleText == null) {
                    return;
                }
                lblTitle = new JLabel();
                lblTitle.setFont(getTitleFont());
                lblTitle.setText(getTitleText());
                titleBox = Box.createHorizontalBox();
                switch (titleAlignment) {
                    case TITLE_ALIGN_CENTER:
                        titleBox.add(Box.createGlue());
                        titleBox.add(lblTitle);
                        titleBox.add(Box.createGlue());
                        break;
                    case TITLE_ALIGN_RIGHT:
                        titleBox.add(Box.createGlue());
                        titleBox.add(lblTitle);
                        titleBox.add(Box.createHorizontalStrut(10));
                        break;
                    case TITLE_ALIGN_LEFT:
                        titleBox.add(Box.createHorizontalStrut(10));
                        titleBox.add(lblTitle);
                        titleBox.add(Box.createGlue());
                        break;
                }
                pnlControl.add(titleBox, BorderLayout.NORTH);
            }
        });

    }

    /**
     * Gets a <code>Rectangle</code> with the location and size for the editor
     *
     * @param index the index of the item to be edited
     * @return a <code>Rectangle</code> with the location and size for the
     * editor
     */
    protected Rectangle getEditorBounds(int index) {
        Component comp = getComponentFromItem(getModel().getValueAt(index));
        Rectangle rect = comp.getBounds();
        ScrollableListRenderer render = (ScrollableListRenderer) comp;
        int top = render.getTopMargin() * index;
        int left = render.getLeftMargin();
        int bottom = render.getBottomMargin();
        int right = render.getRightMargin();
        int x, y, w, h;
        double rx, ry, rh, rw;
        rx = rect.getX();
        ry = rect.getY();
        rh = rect.getHeight();
        rw = rect.getWidth();
        rx = rx + left;
        ry = ry + top;
        x = Math.round((float) rx);
        y = Math.round((float) ry);
        w = Math.round((float) rw);
        h = Math.round((float) rh);

        w = pnlMain.getWidth() - (x + 5);
        return new Rectangle(x, y, w, h);
    }

///
/// Private Methods
/// 
    private void initScrollableList(ScrollableListModel model, ScrollableListItemEditor editor, ScrollableListRenderer renderer) {
        this.setRenderer(renderer);
        this.setEditor(editor);

        if (getRenderer() == null) {
            this.setRenderer(new DefaultScrollableListRenderer(this));
        }

        if (getEditor() == null) {
            this.setEditor(new DefaultScrollableListEditor(new JTextField()));
        }

        Font font = Font.decode(null);
        font = font.deriveFont(16.0f);
        if (font.isBold()) {
            int fontStyle = font.getStyle();
            fontStyle ^= Font.BOLD;
            font = font.deriveFont(fontStyle);
        }

        setFont(font);
        boolean t = getFont().isBold();
        setOpaque(true);
        setVisible(true);
        setBackground(Color.WHITE);
        setForeground(Color.BLACK);
        setPreferredSize(new Dimension(50, 100));

        pnlMain.setLayout(new BoxLayout(pnlMain, BoxLayout.Y_AXIS));
        pnlMain.setBackground(getBackground());
        pnlMain.setForeground(getForeground());
        pnlMain.setSize(new Dimension(100, 100));
        pnlMain.setOpaque(true);
        pnlMain.setVisible(true);

        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVisible(true);

        pnlControl.setLayout(new BorderLayout());
        pnlControl.setBackground(getBackground());
        pnlControl.setForeground(getForeground());
        pnlControl.setOpaque(true);
        pnlControl.setVisible(true);
        pnlControl.setLocation(new Point(0, 0));
        pnlControl.setSize(new Dimension(100, 100));
        pnlControl.add(scrollPane, BorderLayout.CENTER);
        pnlControl.add(scrollPane, BorderLayout.CENTER);

        selectionBox = new SelectionBox(scrollPane, pnlControl);
        selectionBox.setAllowSelectionBox(false);
        selectionBox.addSelectionBoxListener(new SelectionBoxListener() {

            @Override
            public void selectionBoxSelectionChange(SelectionBoxEvent e) {
                selectionBoxSelectionChangeHandler(e);
            }
        });

        setModel(model);
        model.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName() == ScrollableListModel.PROP_SELECTIONMODE) {
                    if (model.getSelectionMode() == ScrollableListModel.SELECTION_MODE_SINGLE) {
                        selectionBox.setAllowSelectionBox(false);
                    } else {
                        selectionBox.setAllowSelectionBox(true);
                    }
                }
            }
        });
        this.setLayout(new BorderLayout());
        this.add(pnlControl, BorderLayout.CENTER);
        validate();
        IsKeyPressed.main(new String[]{});
        IsKeyPressed.addListener(new ControlAPressedListener() {

            @Override
            public void controlAPressed() {
                if (hasFocus()) {
                    selectAll();
                }
            }
        });

    }

    private void selectionBoxSelectionChangeHandler(SelectionBoxEvent e) {
        switch (e.getEventType()) {
            case SelectionBoxEvent.SELECTION_STARTED:
                preSelectionBoxSelections.clear();
                preSelectionBoxSelections.addAll(Arrays.asList(getModel().getSelectedItems()));
                removeChildMouseListeners();
                allowFireChange = false;
                break;
            case SelectionBoxEvent.SELECTION_SIZE_CHANGED:
                Rectangle area = e.getSelectionBounds();
                Iterator it = childControls.entrySet().iterator();
                boolean preSelected = false;
                while (it.hasNext()) {
                    Map.Entry pair = (Map.Entry) it.next();
                    Component renderer = (Component) pair.getKey();
                    if (area.contains(renderer.getBounds()) || area.intersects(renderer.getBounds())) {
                        ((ScrollableListRenderer) renderer).setSelected(true);
                        getModel().setItemSelection(pair.getValue(), true);
                    } else {
                        preSelected = false;
                        for (Object value : preSelectionBoxSelections) {
                            Component comp = getComponentFromItem(value);
                            if (comp == renderer) {
                                preSelected = true;
                                break;
                            }
                        }
                        if (!preSelected) {
                            ((ScrollableListRenderer) renderer).setSelected(false);
                            getModel().setItemSelection(pair.getValue(), false);
                        }
                    }
                }
                break;
            case SelectionBoxEvent.SELECTION_ENDED:
                allowFireChange = true;
                reattachChildMouseListeners();
                break;
        }
    }

    private void removeChildMouseListeners() {
        childRemovedListeners.clear();
        Iterator it = childControls.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            Component renderer = (Component) pair.getKey();
            List<MouseListener> listeners = new ArrayList<>();
            listeners.addAll(Arrays.asList(renderer.getMouseListeners()));
            childRemovedListeners.put(renderer, listeners);
            for (MouseListener l : listeners) {
                renderer.removeMouseListener(l);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void reattachChildMouseListeners() {
        Iterator it = childRemovedListeners.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Component, List<MouseListener>> pair = (Map.Entry) it.next();
            Component renderer = pair.getKey();
            List<MouseListener> listeners = pair.getValue();
            for (MouseListener l : listeners) {
                renderer.addMouseListener(l);
            }
        }
    }

    private void setUpGlassPane(int index) {
        Rectangle rect = getEditorBounds(index);
        JPanel glass = (JPanel) scrollPane.getRootPane().getGlassPane();
        Point convertedPoint = SwingUtilities.convertPoint(pnlMain, rect.x, rect.y, glass);
        MouseAdapter mouseAdapter = new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                ChangeEvent evt = new ChangeEvent(this);
                editingStopped(evt);
            }

        };
        int x, y, width, height, hs1, hs2, vs1, vs2;

        glass.addMouseListener(mouseAdapter);
        glass.setLayout(new BoxLayout((JPanel) glass, BoxLayout.Y_AXIS));
        y = convertedPoint.y + 1;
        x = convertedPoint.x - 1;
        width = rect.width + 4;
        height = rect.height + 4;
        hs2 = (glass.getWidth() - width) - x;
        hs1 = x;
        vs1 = y;
        vs2 = glass.getHeight() - (y + height);

        editorComponent.setPreferredSize(new Dimension(width, height));
        glass.add(Box.createVerticalStrut(vs1));
        Box box = Box.createHorizontalBox();
        box.setPreferredSize(new Dimension(width, height));
        box.add(Box.createHorizontalStrut(hs1));
        box.add(editorComponent);
        box.add(Box.createHorizontalStrut(hs2));
        glass.add(box);
        glass.add(Box.createVerticalStrut(vs2));

        glass.setVisible(true);
        glass.revalidate();
    }

    //sets up for a single selection
    private void selectOneItem(MouseEvent evt) {
        class ChangeSelectionRunnable implements Runnable {

            final Component c;
            final int selMode;
            final boolean isSelected;

            public ChangeSelectionRunnable(Component c, int selectionMode, boolean isSelected) {
                this.c = c;
                this.selMode = selectionMode;
                this.isSelected = isSelected;
            }

            @Override
            public void run() {
                ((ScrollableListRenderer) c).setSelected(isSelected);
                getModel().setItemSelection(childControls.get(c), isSelected);
                fireSelectionListeners(selMode, getModel().getItemIndex(childControls.get(c)));
            }

        }
        Component component = (Component) evt.getSource();
        ScrollableListRenderer renderer = (ScrollableListRenderer) component;
        Component c = null;
        if (getModel().getSelectedItems().length > 0) {
            c = getComponentFromItem(getModel().getSelectedItems()[0]);
        }

        if (c != null) {
            try {
                ChangeSelectionRunnable rnUnselect = new ChangeSelectionRunnable(c,
                        ScrollableListItemSelectionEvent.UNSELECTED, false);
                SwingUtilities.invokeAndWait(rnUnselect);
            } catch (Exception e) {
                e.printStackTrace(System.out);
            }

        }
        Object o = childControls.get(component);
        getModel().setItemSelection(childControls.get(component), true);
        ((ScrollableListRenderer) component).setSelected(true);
        fireSelectionListeners(SELECTED, getModel().getItemIndex(childControls.get(component)));
    }

    //sets up for selection with shift or control key pressed
    private void selectMultiKeyDown(MouseEvent evt) {
        if (!IsKeyPressed.isCtrPressed() && !IsKeyPressed.isShiftPressed()) {
            clearSelectedItems();
            selectOneItem(evt);
            return;
        }

        Component c = (Component) evt.getSource();
        ScrollableListRenderer renderer = (ScrollableListRenderer) c;
        if (IsKeyPressed.isCtrPressed()) {
            boolean s = !Arrays.asList(getModel().getSelectedItems()).contains(childControls.get(c));
            ((ScrollableListRenderer) c).setSelected(s);
            getModel().setItemSelection(childControls.get(c), s);
            if (!s) {
                fireSelectionListeners(UNSELECTED, getModel().getItemIndex(childControls.get(c)));
            } else {
                fireSelectionListeners(SELECTED, getModel().getItemIndex(childControls.get(c)));
            }
        } else if (IsKeyPressed.isShiftPressed()) {
            if (getModel().getSelectedItems().length == 0) {
                selectOneItem(evt);
                return;
            }
            Object valueFirst = getModel().getSelectedItems()[0];
            int index1 = getModel().getItemIndex(valueFirst);
            Object valueSecond = childControls.get(c);
            int index2 = getModel().getItemIndex(valueSecond);
            clearSelectedItems();
            int i1, i2;
            if (index1 < index2) {
                i1 = index1;
                i2 = index2;
            } else {
                i1 = index2;
                i2 = index1;
            }
            for (int i = i1; i < i2 + 1; i++) {
                Component component = getComponentFromItem(getModel().getValueAt(i));
                ((ScrollableListRenderer) component).setSelected(true);
                getModel().setItemSelection(childControls.get(component), true);
                fireSelectionListeners(SELECTED, i);
            }
        }
    }

    //sets up for selection with shift or control key pressed
    private void selectMultiNoDown(MouseEvent evt) {
        Component c = (Component) evt.getSource();
        ScrollableListRenderer renderer = (ScrollableListRenderer) c;
        boolean s = !Arrays.asList(getModel().getSelectedItems()).contains(childControls.get(c));
        ((ScrollableListRenderer) c).setSelected(s);
        getModel().setItemSelection(childControls.get(c), s);

        if (!s) {
            fireSelectionListeners(UNSELECTED, getModel().getItemIndex(childControls.get(c)));
        } else {
            fireSelectionListeners(SELECTED, getModel().getItemIndex(childControls.get(c)));
        }
    }

    private void clearSelectedItems() {
        if (getModel().getSelectedItems().length > 0) {
            for (Object selectedItem : getModel().getSelectedItems()) {
                Component c = getComponentFromItem(selectedItem);
                ScrollableListRenderer renderer = (ScrollableListRenderer) c;
                int index = getModel().getItemIndex(selectedItem);
                renderer.setSelected(false);
                getModel().setItemSelection(selectedItem, false);
                fireSelectionListeners(UNSELECTED, index);
            }
        }
    }

    private void fireSelectionListeners(int mode, int index) {
        ScrollableListItemSelectionEvent evt
                = new ScrollableListItemSelectionEvent(this, mode, index);
        ScrollableListItemSelectionListener[] listeners
                = eventList.getListeners(ScrollableListItemSelectionListener.class
                );
        for (int i = listeners.length - 1;
                i >= 0; i--) {
            listeners[i].scrollableListItemSelectionChanged(evt);
        }
    }

    private Component getComponentFromItem(Object item) {
        Iterator it = childControls.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            if (pair.getValue() == item) {
                return (Component) pair.getKey();
            }
        }
        return null;
    }

    private Component getComponentFromIndex(int index) {
        return pnlMain.getComponent(index);
    }

    private void rendererPropertyChangeHandler(PropertyChangeEvent evt) {
        pnlMain.removeAll();
        childControls.clear();
        initListObjects();
    }

    /**
     * Discards the editor object and frees the real estate it used for cell
     * rendering.
     *
     * @param evt a <code>ScrollableListModelEvent</code> with info about the
     * change
     */
    //<editor-fold defaultstate="collapsed" desc="ScrollableListModelListener Implementation">
    @Override
    public void scrollableListModelChanged(ScrollableListModelEvent evt) {
        if (getModel() != evt.getSource()) {
            return;
        }
        if (!allowFireChange) {
            return;
        }
        if (!this.paintedComponents) {
            doUpDateList = true;
            return;
        }
        switch (evt.getType()) {
            case ScrollableListModelEvent.DELETE:
                SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        for (int i = evt.getFirstItem(); i == evt.getLastItem(); i++) {
                            Component c = getComponentFromIndex(i);
                            pnlMain.remove(c);
                            childControls.remove(c);
                        }
                    }
                });
                break;
            case ScrollableListModelEvent.INSERT:
                SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        Component comp = null;
                        int first, last;
                        if (evt.getFirstItem() < evt.getLastItem()) {
                            first = evt.getFirstItem();
                            last = evt.getLastItem();
                        } else {
                            first = evt.getLastItem();
                            last = evt.getFirstItem();
                        }

                        for (int i = first; i <= last; i++) {
                            try {
                                comp = prepareRenderer(getRenderer(), i);
                                Object value = getModel().getValueAt(i);
                                childControls.put(comp, value);
                                pnlMain.add(comp);
                            } catch (Exception e) {
                                e.printStackTrace(System.out);
                            }

                        }

                        revalidate();
                        repaint();
                    }
                });
                break;

            case ScrollableListModelEvent.UPDATE:
                Component comp = null;
                Object value = null;
                ScrollableListRenderer renderer = null;

                int first;
                int last;
                if (evt.getFirstItem() < evt.getLastItem()) {
                    first = evt.getFirstItem();
                    last = evt.getLastItem();
                } else {
                    first = evt.getLastItem();
                    last = evt.getFirstItem();
                }
                for (int i = first; i <= last; i++) {
                    value = getModel().getValueAt(i);
                    comp = getComponentFromIndex(i);
                    renderer = (ScrollableListRenderer) comp;
                    childControls.replace(comp, value);
                    renderer.upDateValue(value);
                }
                this.revalidate();
                this.repaint();
                break;
            case ScrollableListModelEvent.SORTED:
                pnlMain.removeAll();

                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        Object v = null;
                        Component c = null;
                        for (int i = 0; i < getModel().getItemCount(); i++) {
                            v = getModel().getValueAt(i);
                            if (v != null) {
                                c = getComponentFromItem(v);
                                if (c != null) {
                                    pnlMain.add(c, i);
                                }
                            }

                        }
                    }
                });
                pnlMain.validate();
                break;
        }
    }
    //</editor-fold>

//<editor-fold defaultstate="collapsed" desc="ItemEditorListener Implementation">
    @Override
    public void editingStopped(ChangeEvent e) {
        ScrollableListItemEditor editor = getEditor();
        if (editor != null) {
            Object value = editor.getItemEditorValue();
            setItemValueAt(value, editingItemIndex);
            removeEditor();
        }
    }

    @Override
    public void editingCanceled(ChangeEvent e) {
        removeEditor();

    }
//</editor-fold>

    /**
     * A subclass of <code>TransferHandler.DropLocation</code> representing a
     * drop location for a <code>JList</code>.
     */
    public static final class DropLocation extends TransferHandler.DropLocation {

        private final int index;
        private final boolean isInsert;

        private DropLocation(Point p, int index, boolean isInsert) {
            super(p);
            this.index = index;
            this.isInsert = isInsert;
        }

        /**
         * Returns the index where dropped data should be placed in the list.
         * Interpretation of the value depends on the drop mode set on the
         * associated component. If the drop mode is either
         * <code>DropMode.USE_SELECTION</code> or <code>DropMode.ON</code>, the
         * return value is an index of a row in the list. If the drop mode is
         * <code>DropMode.INSERT</code>, the return value refers to the index
         * where the data should be inserted. If the drop mode is
         * <code>DropMode.ON_OR_INSERT</code>, the value of
         * <code>isInsert()</code> indicates whether the index is an index of a
         * row, or an insert index.
         * <p>
         * <code>-1</code> indicates that the drop occurred over empty space,
         * and no index could be calculated.
         *
         * @return the drop index
         */
        public int getIndex() {
            return index;
        }

        /**
         * Returns whether or not this location represents an insert location.
         *
         * @return whether or not this is an insert location
         */
        public boolean isInsert() {
            return isInsert;
        }

        /**
         * Returns a string representation of this drop location. This method is
         * intended to be used for debugging purposes, and the content and
         * format of the returned string may vary between implementations.
         *
         * @return a string representation of this drop location
         */
        public String toString() {
            return getClass().getName()
                    + "[dropPoint=" + getDropPoint() + ","
                    + "index=" + index + ","
                    + "insert=" + isInsert + "]";
        }
    }

    class childComponentMouseAdapter extends MouseAdapter {

        private boolean isDraging = false;
        private Point mouseDownPoint = null;
        protected final ScrollableList parent;

        public childComponentMouseAdapter(ScrollableList parent) {
            this.parent = parent;
        }

        private boolean isDragAllowed(MouseEvent evt) {
            if (dragAndDropEnabled && getModel().getSelectedItems().length > 0
                    && evt.getButton() == MouseEvent.BUTTON1) {
                Point mousePoint = evt.getPoint();
                Component comp = pnlMain.getComponentAt(mousePoint);
                Object val = childControls.get(comp);
                if (Arrays.asList(getModel().getSelectedItems()).contains(val)) {
                    return true;
                }
            }

            return false;
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (isDragAllowed(e)) {
                mouseDownPoint = e.getPoint();
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            isDraging = true;
            TransferHandler handler = new DefaultScrollableListTransferHandler();
            handler.exportAsDrag(parent, e, TransferHandler.MOVE);
        }

        @Override
        public void mouseReleased(MouseEvent evt) {
            mouseDownPoint = null;
            if (isDraging) {
                isDraging = false;
                return;
            }
            Runnable runnable = new Runnable() {
                public void run() {
                    if (!isEditing()) {
                        if (evt.getButton() == MouseEvent.BUTTON1) {
                            allowFireChange = false;
                            Component component = (Component) evt.getSource();
                            ScrollableListRenderer renderer = (ScrollableListRenderer) component;
                            switch (getModel().getSelectionMode()) {
                                case SELECTION_MODE_SINGLE:
                                    selectOneItem(evt);
                                    break;
                                case SELECTION_MODE_MULTI_KEYDOWN:
                                    selectMultiKeyDown(evt);
                                    break;
                                case SELECTION_MODE_MULTI_CLICK:
                                    selectMultiNoDown(evt);
                                    break;
                            }
                            allowFireChange = true;

                        }
                    }
                }
            };
            Thread thr = new Thread(runnable);
            thr.start();

        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (getEditor() != null) {
                if (getEditor().getNumberOfClicksToStart() != -1) {
                    if (e.getClickCount() == getEditor().getNumberOfClicksToStart()) {
                        Component component = (Component) e.getSource();
                        int index = getModel().getItemIndex(childControls.get(component));
                        editItemAt(index);
                    }
                }
            }
        }

        private boolean isStartDrag(MouseEvent evt) {
            if (mouseDownPoint != null) {
                int dx = mouseDownPoint.x;
                int dy = mouseDownPoint.y;
                int cx = evt.getPoint().x;
                int cy = evt.getPoint().y;
                if (cx > (dx + 5) || cx < (dx - 5) || cy > (dy + 5) || cy < (dy - 5)) {
                    return true;
                }
            }

            return false;
        }

    }//childComponentMouseAdapter

}//ScrollableList

//<editor-fold defaultstate="collapsed" desc="Key Press">
class IsKeyPressed {

    private static boolean shiftPressed = false;
    private static boolean ctrPressed = false;
    private static boolean aPressed = false;
    private static List<ControlAPressedListener> selectAllListeners = new ArrayList<>();

    public static boolean isShiftPressed() {
        synchronized (IsKeyPressed.class) {
            return shiftPressed;
        }
    }

    public static boolean isCtrPressed() {
        synchronized (IsKeyPressed.class) {
            return ctrPressed;
        }
    }

    public static boolean isAPressed() {
        synchronized (IsKeyPressed.class) {
            return aPressed;
        }
    }

    public static void addListener(ControlAPressedListener l) {
        selectAllListeners.add(l);
    }

    public static void main(String[] args) {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {

            @Override
            public boolean dispatchKeyEvent(KeyEvent ke) {
                synchronized (IsKeyPressed.class) {
                    switch (ke.getID()) {
                        case KeyEvent.KEY_PRESSED:
                            if (ke.getKeyCode() == KeyEvent.VK_SHIFT) {
                                shiftPressed = true;
                            }
                            if (ke.getKeyCode() == KeyEvent.VK_CONTROL) {
                                ctrPressed = true;
                            }
                            if (ke.getKeyCode() == KeyEvent.VK_A) {
                                aPressed = true;
                            }
                            if (ctrPressed && aPressed) {
                                fireSelectAllListeners();
                            }
                            break;

                        case KeyEvent.KEY_RELEASED:
                            if (ke.getKeyCode() == KeyEvent.VK_SHIFT) {
                                shiftPressed = false;
                            }
                            if (ke.getKeyCode() == KeyEvent.VK_CONTROL) {
                                ctrPressed = false;
                            }
                            if (ke.getKeyCode() == KeyEvent.VK_A) {
                                aPressed = false;
                            }
                            break;
                    }
                    return false;
                }
            }
        });
    }

    private static void fireSelectAllListeners() {
        for (ControlAPressedListener l : selectAllListeners) {
            l.controlAPressed();
        }
    }
}

interface ControlAPressedListener extends EventListener {

    public void controlAPressed();
}
//</editor-fold>
