package com.stone.win;

import java.awt.event.KeyEvent;

import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;

import com.stone.win.consumer.IException;

/**
 * 行定位弹窗
 * 
 * @author: stone
 * @date 2023-6-6 13:43:26
 */
public class DialogJumpLine extends javax.swing.JDialog {

    private static final long serialVersionUID = 2399050974398722767L;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JTextField jTextField1;

    public DialogJumpLine(java.awt.Frame parent, JTextArea textArea) {
        super(parent, false);
        initComponents(textArea);

        this.setLocationRelativeTo(parent);
        this.setResizable(false);
        this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        this.setVisible(true);
    }

    private void initComponents(JTextArea textArea) {
        JPanel jPanel1 = new JPanel();

        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        jLabel1.setText(CommonUtil.getShowText(CommonLangEnum.enterLineNo) + ":  ");

        jButton1.setText(CommonUtil.getShowText(CommonLangEnum.jumpTo));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jumpToLine(textArea);
            }
        });

        jButton2.setText(CommonUtil.getShowText(CommonLangEnum.cencel));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                close();
            }
        });
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    jumpToLine(textArea);
                }
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(45, 45, 45).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 304, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel1))).addGroup(layout.createSequentialGroup().addGap(80, 80, 80).addComponent(jButton1).addGap(43, 43, 43).addComponent(jButton2))).addContainerGap(51, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(48, 48, 48).addComponent(jLabel1).addGap(18, 18, 18).addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(60, 60, 60).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jButton1).addComponent(jButton2)).addContainerGap(98, Short.MAX_VALUE))
        );


        this.setContentPane(jPanel1);
        this.setSize(400, 300);
        this.setLocationRelativeTo(null);
    }

    private void close() {
        this.dispose();
    }

    private void jumpToLine(JTextArea textArea) {
        IException i = () -> {
            String str = jTextField1.getText().trim();
            Integer pos = 0;
            try {
                pos = Integer.valueOf(str);
            } catch (NumberFormatException e) {
                //do nothing
            }
            if (pos == 0) {
                return;
            }
            int max = textArea.getLineCount();
            pos = pos > max ? max : pos;
            try {
                textArea.setCaretPosition(textArea.getLineStartOffset(pos - 1));
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
            close();
        };
        i.exec();
    }

}
