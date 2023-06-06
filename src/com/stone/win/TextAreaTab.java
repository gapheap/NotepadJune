package com.stone.win;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

/**
 * 编辑器选项卡
 * 
 * @author: stone
 * @date 2023-6-6 13:44:21
 */
public class TextAreaTab extends JPanel {

    private static final long serialVersionUID = -6584403011786380302L;
    private JScrollPane jScrollPane1;
    private TextAreaEdit jTextArea1;
    private String charset = CommonEncodingEnum.UTF8.getCharset();
    private byte[] data;
    private boolean changed = false;
    private String filePath = null;
    private boolean hasWarn = false;

    public TextAreaTab(Notepad obj) {
        super();
        init(obj);
        this.setFont(CommonUtil.getFont());
    }

    private void init(Notepad obj) {
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new com.stone.win.TextAreaEdit(obj, this.charset);
        CommonUtil.setRowsAndColumns(jTextArea1);
        jScrollPane1.setViewportView(jTextArea1);
        jScrollPane1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        jScrollPane1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        this.add(jScrollPane1, BorderLayout.CENTER);

        TextAreaTab tabPanel = this;
        jTextArea1.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                tabPanel.setData(null);
                tabPanel.setChanged(true);
            }

            @Override
            public void keyPressed(KeyEvent e) {
                // 
            }

            @Override
            public void keyReleased(KeyEvent e) {
                //
            }
        });

        jScrollPane1.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                boolean isCtrl = (e.getModifiersEx() & MouseWheelEvent.CTRL_DOWN_MASK) != 0;
                int notches = e.getWheelRotation();
                if (isCtrl) {
                    if (notches < 0) {
                        obj.enlarge();
                    } else {
                        obj.reduce();
                    }
                } else {
                    if (notches < 0) {
                        //
                    } else {
                        //
                    }
                }
            }
        });
    }

    public TextAreaEdit getjTextArea1() {
        return jTextArea1;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.jTextArea1.setCharset(charset);
        this.charset = charset;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public boolean isChanged() {
        return changed;
    }

    public void setChanged(boolean changed) {
        this.changed = changed;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public JScrollPane getjScrollPane1() {
        return jScrollPane1;
    }

    public boolean isHasWarn() {
        return hasWarn;
    }

    public void setHasWarn(boolean hasWarn) {
        this.hasWarn = hasWarn;
    }

}
