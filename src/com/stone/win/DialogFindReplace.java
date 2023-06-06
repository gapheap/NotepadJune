package com.stone.win;

import java.awt.event.KeyEvent;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 * 查找替换弹窗
 * 
 * @author: stone
 * @date 2023-6-6 13:43:19
 */
public class DialogFindReplace extends javax.swing.JDialog {

    private static final long serialVersionUID = 4482647697980195885L;

    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;

    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JCheckBox jCheckBox3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;

    private JTextArea jTextArea1;
    private JTextArea jTextArea2;

    private JTextArea textArea = null;
    private int findIndex = 0;
    private int count = 0;

    public DialogFindReplace(java.awt.Frame parent, JTextArea textArea) {
        super(parent, false);
        initComponents();

        this.textArea = textArea;
        this.findIndex = 0;
        this.setLocationRelativeTo(parent);
        this.setResizable(false);
        this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        if (textArea.isEditable()) {
            this.jButton2.setEnabled(true);
            this.jButton3.setEnabled(true);
        } else {
            this.jButton2.setEnabled(false);
            this.jButton3.setEnabled(false);
        }
        this.setVisible(true);
    }

    private void initComponents() {
        JPanel jPanel1 = new JPanel();

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTextArea1 = new JTextArea();
        jTextArea1.setLineWrap(true);

        jTextArea2 = new JTextArea();
        jTextArea2.setLineWrap(true);

        jCheckBox1 = new javax.swing.JCheckBox();
        jCheckBox2 = new javax.swing.JCheckBox();
        jCheckBox3 = new javax.swing.JCheckBox();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();

        setTitle(CommonUtil.getShowText(CommonLangEnum.findReplace));
        jLabel1.setText(CommonUtil.getShowText(CommonLangEnum.findWhat) + ": ");
        jLabel2.setText(CommonUtil.getShowText(CommonLangEnum.replaceWhat) + ": ");
        jCheckBox1.setText(CommonUtil.getShowText(CommonLangEnum.caseSensitive));
        jCheckBox1.setSelected(true);

        jCheckBox2.setText(CommonUtil.getShowText(CommonLangEnum.wholeWord));

        jCheckBox3.setText(CommonUtil.getShowText(CommonLangEnum.backward));

        jButton1.setText(CommonUtil.getShowText(CommonLangEnum.find));
        jButton2.setText(CommonUtil.getShowText(CommonLangEnum.replace));
        jButton3.setText(CommonUtil.getShowText(CommonLangEnum.replaceAll));
        jButton4.setText(CommonUtil.getShowText(CommonLangEnum.close));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                String str = textArea.getSelectedText();
                if (str == null || "".equals(str)) {
                    findIndex = 0;
                }
                find(jTextArea1.getText(), findIndex);
            }
        });
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                String str = textArea.getSelectedText();
                if (str != null && !"".equals(str)) {
                    textArea.replaceSelection(jTextArea2.getText());
                }
            }
        });
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                count = 0;
                replaceAll(jTextArea1.getText(), jTextArea2.getText(), 0, textArea.getText().length());
            }
        });
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                close();
            }
        });

        jTextArea1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    String str = textArea.getSelectedText();
                    if (str == null || "".equals(str)) {
                        findIndex = 0;
                    }
                    find(jTextArea1.getText(), findIndex);
                }
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(22, 22, 22).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jLabel1).addComponent(jLabel2)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jTextArea2).addComponent(jTextArea1, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(28, 28, 28).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jButton2, javax.swing.GroupLayout.Alignment.TRAILING))).addGroup(layout.createSequentialGroup().addComponent(jCheckBox1).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jButton3)).addGroup(layout.createSequentialGroup().addComponent(jCheckBox3).addGap(0, 0, Short.MAX_VALUE)).addGroup(layout.createSequentialGroup().addComponent(jCheckBox2).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jButton4))).addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(26, 26, 26).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jTextArea1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel1).addComponent(jButton1)).addGap(18, 18, 18).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel2).addComponent(jTextArea2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jButton2)).addGap(18, 18, 18).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jCheckBox1).addComponent(jButton3)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jCheckBox2).addComponent(jButton4)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jCheckBox3).addContainerGap(76, Short.MAX_VALUE))
        );

        this.setContentPane(jPanel1);
        this.setSize(400, 300);
        this.setLocationRelativeTo(null);
    }

    public void find(String str, int cur) {
        int i = -1;
        String textAreaText = textArea.getText();
        if (!jCheckBox1.isSelected()) {
            i = textAreaText.toUpperCase().indexOf(str.toUpperCase(), cur);
        } else {
            i = textAreaText.indexOf(str, cur);
        }
        if (i >= 0) {
            textArea.setSelectionStart(i);
            textArea.setSelectionEnd(i + str.length());
            findIndex = ++i;
        } else {
            if (findIndex == 0) {
                return;
            } else {
                findIndex = 0;
                find(str, findIndex);
            }
        }
    }

    public void replaceAll(String fromStr, String toStr, int cur, int end) {
        if (cur > end) {
            return;
        } else {
            int i = -1;
            String textAreaText = textArea.getText();
            if (!jCheckBox1.isSelected()) {
                i = textAreaText.toUpperCase().indexOf(fromStr.toUpperCase(), cur);
            } else {
                i = textAreaText.indexOf(fromStr, cur);
            }
            if (i >= 0) {
                textArea.setSelectionStart(i);
                textArea.setSelectionEnd(i + fromStr.length());
                textArea.replaceSelection(toStr);
                cur = ++i;
                count++;
            } else {
                String hint = String.format(CommonUtil.getShowText(CommonLangEnum.hindReplace), count);
                JOptionPane.showMessageDialog(this, hint);
                return;
            }
            replaceAll(fromStr, toStr, cur, end);
        }
    }

    private void close() {
        this.dispose();
    }
}
