package com.stone.win;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.DefaultListModel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEdit;

/**
 * 编辑器
 * 
 * @author: stone
 * @date 2023-6-6 13:44:14
 */
public class TextAreaEdit extends JTextArea implements MouseListener {
    private static final long serialVersionUID = 33043834875583545L;
    DefaultListModel<Character> listModel = new DefaultListModel<>();
    private boolean selectText = false;
    int indexF, indexS;
    private UndoManager undoManager = new UndoManager();

    private String charset;
    private boolean showLineNumber = true;
    private boolean readonly = false;

    private JPopupMenu pm = null;
    private JMenuItem copy = null;
    private JMenuItem paste = null;

    private JMenuItem cut = null;
    private JMenuItem delete = null;

    private JMenuItem selectAll = null;

    private JMenuItem count = null;
    private JMenuItem upperCase = null;
    private JMenuItem lowerCase = null;

    private JMenuItem base64Encode = null;
    private JMenuItem base64Decode = null;

    public TextAreaEdit(Notepad obj, String charset) {
        super();
        this.charset = charset;
        init();
        this.setFont(CommonUtil.getFont());
        new DropTarget(this, DnDConstants.ACTION_COPY_OR_MOVE, new DropTargetAdapter() {
            @Override
            public void drop(DropTargetDropEvent dtde) {
                obj.dragAction(dtde);
            };
        });
    }

    public TextAreaEdit(boolean readonly, boolean showLineNumber) {
        super();
        this.showLineNumber = showLineNumber;
        this.readonly = readonly;
        init();
    }

    public void setShowLineNumber(boolean isShow) {
        this.showLineNumber = isShow;
    }

    public boolean getShowLineNumber() {
        return this.showLineNumber;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (getShowLineNumber()) {
            drawLineNumber(g);
        }
    }

    protected void drawLineNumber(Graphics g) {
        Document docu = getDocument();
        Element element = docu.getDefaultRootElement();
        int rows = element.getElementCount();
        int numwidth = getstrLength(rows + "", this.getFont());
        g.setColor(Color.GRAY);
        setMargin(new Insets(0, numwidth, 0, 0));
        g.fillRect(0, 0, numwidth, getHeight());
        int fontheight = getRowHeight();
        g.setColor(Color.WHITE);
        for (int row = 0; row <= rows; ++row) {
            int zitimind = fontheight / 4;
            int ypianyi = fontheight * row;
            g.drawString(row + "", 0, ypianyi - zitimind);
        }
    }

//    private int getNumLength(int num) {
//        String str = String.valueOf(num);
//        return str.length();
//    }

    private int getstrLength(String text, Font font) {
        return CommonUtil.stringWidth(text, font);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        //
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON3) {
            copy.setEnabled(isCanCopy());
            if (!readonly) {
                paste.setEnabled(isClipboardString());
                cut.setEnabled(isCanCopy());
                delete.setEnabled(isCanCopy());
            }
            pm.show(this, e.getX(), e.getY());
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // 
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // 
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // 
    }

    private void init() {
        this.addMouseListener(this);
        pm = new JPopupMenu();
        copy = new JMenuItem();
        copy.setFont(CommonUtil.getFont());
        copy.setText(CommonUtil.getShowText(CommonLangEnum.copy));
        copy.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                action(e);
            }
        });
        if (!readonly) {
            paste = new JMenuItem();
            paste.setFont(CommonUtil.getFont());
            paste.setText(CommonUtil.getShowText(CommonLangEnum.paste));
            paste.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    action(e);
                }
            });
            cut = new JMenuItem();
            cut.setFont(CommonUtil.getFont());
            cut.setText(CommonUtil.getShowText(CommonLangEnum.cut));
            cut.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    action(e);
                }
            });
            delete = new JMenuItem();
            delete.setFont(CommonUtil.getFont());
            delete.setText(CommonUtil.getShowText(CommonLangEnum.delete));
            delete.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    action(e);
                }
            });
            selectAll = new JMenuItem();
            selectAll.setFont(CommonUtil.getFont());
            selectAll.setText(CommonUtil.getShowText(CommonLangEnum.selectAll));
            selectAll.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    action(e);
                }
            });
            count = new JMenuItem();
            count.setFont(CommonUtil.getFont());
            count.setText(CommonUtil.getShowText(CommonLangEnum.wordCount));
            count.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    action(e);
                }
            });
            upperCase = new JMenuItem();
            upperCase.setFont(CommonUtil.getFont());
            upperCase.setText(CommonUtil.getShowText(CommonLangEnum.toUpperCase));
            upperCase.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    action(e);
                }
            });
            lowerCase = new JMenuItem();
            lowerCase.setFont(CommonUtil.getFont());
            lowerCase.setText(CommonUtil.getShowText(CommonLangEnum.toLowerCase));
            lowerCase.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    action(e);
                }
            });
            base64Encode = new JMenuItem();
            base64Encode.setFont(CommonUtil.getFont());
            base64Encode.setText(CommonUtil.getShowText(CommonLangEnum.base64Encode));
            base64Encode.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    action(e);
                }
            });
            base64Decode = new JMenuItem();
            base64Decode.setFont(CommonUtil.getFont());
            base64Decode.setText(CommonUtil.getShowText(CommonLangEnum.base64Decode));
            base64Decode.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    action(e);
                }
            });
        }
        pm.add(copy);
        if (!readonly) {
            pm.add(paste);
            pm.add(new JSeparator());
            pm.add(cut);
            pm.add(delete);
            pm.add(new JSeparator());
            pm.add(selectAll);
            pm.add(new JSeparator());
            pm.add(count);
            pm.add(new JSeparator());
            pm.add(upperCase);
            pm.add(lowerCase);
            pm.add(new JSeparator());
            pm.add(base64Encode);
            pm.add(base64Decode);
        }
        this.add(pm);
        initUndoRedo();
    }

    private void initUndoRedo() {
        JTextArea textPane = this;
        this.getDocument().addUndoableEditListener(new UndoableEditListener() {
            @Override
            public void undoableEditHappened(UndoableEditEvent uee) {
                undoManager.addEdit((UndoableEdit) uee.getEdit());
                if (textPane.getText().length() == 0) {
                    //listModel.addElement(Character.valueOf());
                } else {
                    listModel.addElement(textPane.getText().toString().charAt(textPane.getText().length() - 1));
                }
                textPane.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyTyped(KeyEvent ke) {
                        for (int k = indexS + 1; k < listModel.getSize(); k++) {
                            listModel.removeElementAt(k);
                        }
                    }
                });
                indexF = listModel.getSize() - 1;
                indexS = listModel.getSize() - 1;
            }
        });
    }

    public boolean isClipboardString() {
        boolean b = false;
        Clipboard clipboard = this.getToolkit().getSystemClipboard();
        Transferable content = clipboard.getContents(this);
        try {
            if (content.getTransferData(DataFlavor.stringFlavor) instanceof String)
                b = true;
        } catch (Exception e) {
        }
        return b;
    }

    public boolean isCanCopy() {
        boolean b = false;
        int start = this.getSelectionStart();
        int end = this.getSelectionEnd();
        if (start != end)
            b = true;
        return b;
    }

    public void action(ActionEvent e) {
        String str = e.getActionCommand();
        if (str.equals(copy.getText())) {
            this.copy();
        } else {
            if (readonly) {
                return;
            }
            if (str.equals(paste.getText())) {
                this.paste();
            } else if (str.equals(cut.getText())) {
                this.cut();
            } else if (str.equals(delete.getText())) {
                this.replaceSelection("");
            } else if (str.equals(selectAll.getText())) {
                this.selectAll();
            } else if (str.equals(count.getText())) {
                String text = this.getSelectedText();
                if (text == null) {
                    text = this.getText();
                }
                String result = CommonUtil.getShowText(CommonLangEnum.wordNumber) + ": " + text.length();
                CommonUtil.showWarning(CommonUtil.getShowText(CommonLangEnum.wordCount), result);
            } else if (str.equals(upperCase.getText())) {
                String selected = this.getSelectedText();
                if (selected == null || selected.length() == 0) {
                    this.setText(this.getText().toUpperCase());
                } else {
                    String tmp = selected.toUpperCase();
                    this.replaceSelection(tmp);
                }
            } else if (str.equals(lowerCase.getText())) {
                String selected = this.getSelectedText();
                if (selected == null || selected.length() == 0) {
                    this.setText(this.getText().toLowerCase());
                } else {
                    String tmp = selected.toLowerCase();
                    this.replaceSelection(tmp);
                }
            } else if (str.equals(base64Encode.getText())) {
                String selected = this.getSelectedText();
                if (selected == null || selected.length() == 0) {
                    String result = CommonUtil.base64Encode(this.getText(), charset);
                    if (result != null) {
                        this.setText(result);
                    }
                } else {
                    String result = CommonUtil.base64Encode(selected, charset);
                    this.replaceSelection(result);
                }
            } else if (str.equals(base64Decode.getText())) {
                String selected = this.getSelectedText();
                if (selected == null || selected.length() == 0) {
                    String result = CommonUtil.base64Decode(this.getText(), charset);
                    if (result != null) {
                        this.setText(result);
                    }
                } else {
                    String result = CommonUtil.base64Decode(selected, charset);
                    this.replaceSelection(result);
                }
            }
        }
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public UndoManager getUndoManager() {
        return undoManager;
    }

    public boolean isSelectText() {
        return selectText;
    }

    public void setSelectText(boolean selectText) {
        this.selectText = selectText;
    }

}
