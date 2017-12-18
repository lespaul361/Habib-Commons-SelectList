/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.habibsweb.commons.components;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;
import javax.swing.text.PlainDocument;

/**
 * *Creates a <code>JTextArea</code> that can allow setting a maximum number of
 * characters and will accept only a numeric input
 *
 * @author Charles Hamilton
 */
public class JIntTextArea extends JLimitedTextArea {

    /**
     * Constructs a new JIntTextArea. A default model is set, the initial string
     * is null, and rows/columns are set to 0.
     */
    public JIntTextArea() {
        PlainDocument doc = (PlainDocument) this.getDocument();
        doc.setDocumentFilter(new MyIntFilter());
    }

    /**
     * Constructs a new JIntTextArea with the specified text displayed. A
     * default model is created, rows/columns are set to 0, and there is no
     * maximum limit.
     *
     * @param text the text to be displayed, or null
     */
    public JIntTextArea(String text) {
        super(text);
        PlainDocument doc = (PlainDocument) this.getDocument();
        doc.setDocumentFilter(new MyIntFilter());
    }

    /**
     * Constructs a new empty JIntTextArea with the specified number of rows and
     * columns. A default model is created, and the initial string is null.
     *
     * @param rows the number of rows &gt;= 0
     * @param columns the number of columns &gt;= 0
     * @exception IllegalArgumentException if the rows or columns arguments are
     * negative.
     */
    public JIntTextArea(int rows, int columns) {
        super(rows, columns);
        PlainDocument doc = (PlainDocument) this.getDocument();
        doc.setDocumentFilter(new MyIntFilter());
    }

    /**
     * Constructs a new JIntTextArea with the specified text and number of rows
     * and columns. A default model is created.
     *
     * @param text the text to be displayed, or null
     * @param rows the number of rows &gt;= 0
     * @param columns the number of columns &gt;= 0
     * @exception IllegalArgumentException if the rows or columns arguments are
     * negative.
     */
    public JIntTextArea(String text, int rows, int columns) {
        super(text, rows, columns);
        PlainDocument doc = (PlainDocument) this.getDocument();
        doc.setDocumentFilter(new MyIntFilter());
    }

    /**
     * Constructs a new JIntTextArea with the given document model, and defaults
     * for all of the other arguments (null, 0, 0).
     *
     * @param doc the model to use
     */
    public JIntTextArea(Document doc) {
        super(doc);
        PlainDocument doc2 = (PlainDocument) this.getDocument();
        doc2.setDocumentFilter(new MyIntFilter());
    }

    /**
     * Constructs a new JIntTextArea with the specified number of rows and
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
    public JIntTextArea(Document doc, String text, int rows, int columns) {
        super(doc, text, rows, columns);
        PlainDocument doc2 = (PlainDocument) this.getDocument();
        doc2.setDocumentFilter(new MyIntFilter());
    }

    /**
     * Constructs a new JIntTextArea with a specified maximum input length. A
     * default model is set, the initial string is null, rows/columns are set to
     * 0.
     *
     * @param maxLength Maximum length of the input allowed
     */
    public JIntTextArea(int maxLength) {
        super(maxLength);
        PlainDocument doc = (PlainDocument) this.getDocument();
        doc.setDocumentFilter(new MyIntFilter());
    }

    /**
     ** Constructs a new JIntTextArea with a specified text and maximum input
     * length. A default model is set, the initial string is null, rows/columns
     * are set to 0.
     *
     * @param text the text to be displayed, or null
     * @param maxLength maximum length of the input allowed
     */
    public JIntTextArea(String text, int maxLength) {
        super(text, maxLength);
        PlainDocument doc = (PlainDocument) this.getDocument();
        doc.setDocumentFilter(new MyIntFilter());
    }

    /**
     * Constructs a new empty JIntTextArea with the specified number of rows and
     * columns. The maximum input length is specified. A default model is
     * created, and the initial string is null.
     *
     * @param rows the number of rows &gt;= 0
     * @param columns the number of columns &gt;= 0
     * @param maxLength maximum length of the input allowed
     * @exception IllegalArgumentException if the rows or columns arguments are
     * negative.
     */
    public JIntTextArea(int rows, int columns, int maxLength) {
        super(rows, columns, maxLength);
        PlainDocument doc = (PlainDocument) this.getDocument();
        doc.setDocumentFilter(new MyIntFilter());
    }

    /**
     * Constructs a new JIntTextArea with the specified text and number of rows
     * and columns. A default model is created. The maximum length of the input
     * is specified
     *
     * @param text the text to be displayed, or null
     * @param rows the number of rows &gt;= 0
     * @param columns the number of columns &gt;= 0
     * @param maxLength maximum length of the input allowed
     * @exception IllegalArgumentException if the rows or columns arguments are
     * negative.
     */
    public JIntTextArea(String text, int rows, int columns, int maxLength) {
        super(text, rows, columns, maxLength);
        PlainDocument doc = (PlainDocument) this.getDocument();
        doc.setDocumentFilter(new MyIntFilter());
    }

    /**
     * Constructs a new JIntTextArea with the given document model, and defaults
     * for all of the other arguments (null, 0, 0). The maximum length of the
     * input is specified
     *
     * @param doc the model to use
     * @param maxLength maximum length of the input allowed
     */
    public JIntTextArea(Document doc, int maxLength) {
        super(doc, maxLength);
        PlainDocument doc2 = (PlainDocument) this.getDocument();
        doc2.setDocumentFilter(new MyIntFilter());
    }

    /**
     * Constructs a new JIntTextArea with the specified number of rows and
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
    public JIntTextArea(Document doc, String text, int rows, int columns, int maxLength) {
        super(doc, text, rows, columns, maxLength);
        PlainDocument doc2 = (PlainDocument) this.getDocument();
        doc2.setDocumentFilter(new MyIntFilter());
    }

}

class MyIntFilter extends DocumentFilter {

    @Override
    public void insertString(FilterBypass fb, int offset, String string,
            AttributeSet attr) throws BadLocationException {

        Document doc = fb.getDocument();
        StringBuilder sb = new StringBuilder();
        sb.append(doc.getText(0, doc.getLength()));
        sb.insert(offset, string);

        if (test(sb.toString())) {
            super.insertString(fb, offset, string, attr);
        } else {
            // warn the user and don't allow the insert
        }
    }

    public boolean test(String text) {
        try {
            if (text.equalsIgnoreCase("-")) {
                return true;
            }
            Integer.parseInt(text);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text,
            AttributeSet attrs) throws BadLocationException {

        Document doc = fb.getDocument();
        StringBuilder sb = new StringBuilder();
        sb.append(doc.getText(0, doc.getLength()));
        sb.replace(offset, offset + length, text);

        if (test(sb.toString())) {
            super.replace(fb, offset, length, text, attrs);
        } else {
            // warn the user and don't allow the insert
        }

    }

    @Override
    public void remove(FilterBypass fb, int offset, int length)
            throws BadLocationException {
        Document doc = fb.getDocument();
        StringBuilder sb = new StringBuilder();
        sb.append(doc.getText(0, doc.getLength()));
        sb.delete(offset, offset + length);

        if (test(sb.toString())) {
            super.remove(fb, offset, length);
        } else {
            // warn the user and don't allow the insert
        }

    }
}
