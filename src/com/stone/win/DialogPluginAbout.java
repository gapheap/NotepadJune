package com.stone.win;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridLayout;

import javax.swing.JDialog;
import javax.swing.JLabel;

/**
 * 插件关于弹窗
 * 
 * @author: stone
 * @date 2023-6-6 13:43:32
 */
public class DialogPluginAbout extends JDialog {

    private static final long serialVersionUID = 1665564371773398764L;

    public DialogPluginAbout(Frame owner, String title, boolean modal, String[] arr) {
        super(owner, true);
        this.setTitle(title);
        init(arr);
        this.setVisible(true);
    }

    private void init(String[] arr) {
        GridLayout layout = new GridLayout(arr.length, 1, 1, 1);
        this.setLayout(layout);
        for (String str : arr) {
            JLabel l = new JLabel();
            l.setText("   " + str);
            this.add(l, BorderLayout.WEST);
        }
        this.setSize(400, 300);
        this.setLocationRelativeTo(null);
    }
}
