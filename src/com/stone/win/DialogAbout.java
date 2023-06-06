package com.stone.win;

import java.awt.Frame;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * 关于弹窗
 * 
 * @author: stone
 * @date 2023-6-6 13:43:10
 */
public class DialogAbout extends JDialog {

    private static final long serialVersionUID = -488145333092243860L;

    public DialogAbout(Frame owner, String title, boolean modal) {
        super(owner, true);
        this.setTitle(title);
        init();
        this.setVisible(true);
    }

    private void init() {
        JLabel label1 = new JLabel();
        JLabel label2 = new JLabel();
        JLabel label3 = new JLabel();
        TextAreaEdit jTextArea1 = new TextAreaEdit(true, false);
        label1.setText(CommonUtil.getShowText(CommonLangEnum.version) + ": 1.0.1");
        label2.setText(CommonUtil.getShowText(CommonLangEnum.buildTime) + ": 2023-05-23 17:00:00");
        label3.setText(CommonUtil.getShowText(CommonLangEnum.home) + ":");
        jTextArea1.setLineWrap(true);
        jTextArea1.setText(CommonUtil.getShowText(CommonLangEnum.aboutContent));
        JPanel jPanel1 = new JPanel();

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(34, 34, 34).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(jTextArea1, javax.swing.GroupLayout.PREFERRED_SIZE, 321, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(0, 14, Short.MAX_VALUE)).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(label1, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(label3, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(label2, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE))).addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(22, 22, 22).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(label2, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE).addGroup(layout.createSequentialGroup().addComponent(label1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(label3, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jTextArea1, javax.swing.GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE).addContainerGap())
        );

        this.setContentPane(jPanel1);
        this.setSize(400, 300);
        this.setLocationRelativeTo(null);
    }
}
