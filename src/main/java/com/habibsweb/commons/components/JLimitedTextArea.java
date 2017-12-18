/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.habibsweb.commons.components;

import javax.swing.JTextArea;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

/**
 *Creates a <code>JTextArea</code> that can allow setting a maximum number of 
 * characters
 * 
 * @author Charles Hamilton
 */
public class JLimitedTextArea extends JTextArea {

    private int maxLen = -1;

    /**
     * Constructs a new JLimitedTextArea. A default model is set, the initial
     * string is null, rows/columns are set to 0, and there is no maximum limit.
     */
    public JLimitedTextArea() {
        super();
    }

    /**
     * Constructs a new JLimitedTextArea with the specified text displayed. A
     * default model is created, rows/columns are set to 0, and there is no
     * maximum limit.
     *
     * @param text the text to be displayed, or null
     */
    public JLimitedTextArea(String text) {
        super(text);
    }

    /**
     * Constructs a new empty JLimitedTextArea with the specified number of rows
     * and columns. A default model is created, and the initial string is null.
     *
     * @param rows the number of rows &gt;= 0
     * @param columns the number of columns &gt;= 0
     * @exception IllegalArgumentException if the rows or columns arguments are
     * negative.
     */
    public JLimitedTextArea(int rows, int columns) {
        super(rows, columns);
    }

    /**
     * Constructs a new JLimitedTextArea with the specified text and number of
     * rows and columns. A default model is created.
     *
     * @param text the text to be displayed, or null
     * @param rows the number of rows &gt;= 0
     * @param columns the number of columns &gt;= 0
     * @exception IllegalArgumentException if the rows or columns arguments are
     * negative.
     */
    public JLimitedTextArea(String text, int rows, int columns) {
        super(text, rows, columns);
    }

    /**
     * Constructs a new JLimitedTextArea with the given document model, and
     * defaults for all of the other arguments (null, 0, 0).
     *
     * @param doc the model to use
     */
    public JLimitedTextArea(Document doc) {
        super(doc);
    }

    /**
     * Constructs a new JLimitedTextArea with the specified number of rows and
     * columns, and the given model. All of the constructors feed through this
     * constructor.
     *
     * @param doc the model to use, or create a default one if null
     * @param text the text to be displayed, null if none
     * @param rows the number of rows &gt;= 0
     * @param columns the number of columns &gt;= 0
     * @exception IllegalArgumentException if the rows or columns arguments are
     * negative.
     */
    public JLimitedTextArea(Document doc, String text, int rows, int columns) {
        super(doc, text, rows, columns);
    }

    /**
     * Constructs a new JLimitedTextArea with a specified maximum input length.
     * A default model is set, the initial string is null, rows/columns are set
     * to 0.
     *
     * @param maxLength Maximum length of the input allowed
     */
    public JLimitedTextArea(int maxLength) {
        super();
        this.maxLen = maxLength;
        this.setDocument(new JTextFieldLimit(maxLength));
    }

    /**
     ** Constructs a new JLimitedTextArea with a specified text and maximum
     * input length. A default model is set, the initial string is null,
     * rows/columns are set to 0.
     *
     * @param text the text to be displayed, or null
     * @param maxLength maximum length of the input allowed
     */
    public JLimitedTextArea(String text, int maxLength) {
        super(text);

        this.maxLen = maxLength;
        this.setDocument(new JTextFieldLimit(maxLength));
    }

    /**
     * Constructs a new empty JLimitedTextArea with the specified number of rows
     * and columns. The maximum input length is specified. A default model is
     * created, and the initial string is null.
     *
     * @param rows the number of rows &gt;= 0
     * @param columns the number of columns &gt;= 0
     * @param maxLength maximum length of the input allowed
     * @exception IllegalArgumentException if the rows or columns arguments are
     * negative.
     */
    public JLimitedTextArea(int rows, int columns, int maxLength) {
        super(rows, columns);
        this.maxLen = maxLength;
        this.setDocument(new JTextFieldLimit(maxLength));
    }

    /**
     * Constructs a new JLimitedTextArea with the specified text and number of
     * rows and columns. A default model is created. The maximum length of the
     * input is specified
     *
     * @param text the text to be displayed, or null
     * @param rows the number of rows &gt;= 0
     * @param columns the number of columns &gt;= 0
     * @param maxLength maximum length of the input allowed
     * @exception IllegalArgumentException if the rows or columns arguments are
     * negative.
     */
    public JLimitedTextArea(String text, int rows, int columns, int maxLength) {
        super(text, rows, columns);
        this.maxLen = maxLength;
        this.setDocument(new JTextFieldLimit(maxLength));
    }

    /**
     * Constructs a new JLimitedTextArea with the given document model, and
     * defaults for all of the other arguments (null, 0, 0). The maximum length
     * of the input is specified
     *
     * @param doc the model to use
     * @param maxLength maximum length of the input allowed
     */
    public JLimitedTextArea(Document doc, int maxLength) {
        super(doc);
        this.maxLen = maxLength;
        this.setDocument(new JTextFieldLimit(maxLength));
    }

    /**
     * Constructs a new JLimitedTextArea with the specified number of rows and
     * columns, and the given model. The maximum length of the input is
     * specified
     *
     * @param doc the model to use, or create a default one if null
     * @param text the text to be displayed, null if none
     * @param rows the number of rows &gt;= 0
     * @param columns the number of columns &gt;= 0
     * @param maxLength maximum length of the input allowed
     * @exception IllegalArgumentException if the rows or columns arguments are
     * negative.
     */
    public JLimitedTextArea(Document doc, String text, int rows, int columns, int maxLength) {
        super(doc, text, rows, columns);
        this.maxLen = maxLength;
        this.setDocument(new JTextFieldLimit(maxLength));
    }

}

class JTextFieldLimit extends PlainDocument {

    private int limit;

    JTextFieldLimit(int limit) {
        super();
        this.limit = limit;
    }

    JTextFieldLimit(int limit, boolean upper) {
        super();
        this.limit = limit;
    }

    public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
        if (str == null) {
            return;
        }

        if ((getLength() + str.length()) <= limit) {
            super.insertString(offset, str, attr);
        }
    }
}
