package com.stone.win.comp;

import javax.swing.JButton;

import com.stone.win.CommonUtil;
import com.stone.win.Notepad;

/**
 * 工具栏按钮
 * 
 * @author: stone
 * @date 2023-6-6 13:44:41
 */
public class SButton extends JButton {

    private static final long serialVersionUID = 3552271177290614872L;
    private String name;

    public SButton(Notepad obj, String name) {
        super();
        CommonUtil.menuAndToolbar.add(this);
        this.name = name;
        this.setText(CommonUtil.menuLangMap.get(name).split(",")[CommonUtil.config.getLang()]);
        this.setFocusable(false);
        this.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        this.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        this.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CommonUtil.bindMethod(obj, name);
            }
        });
    }

    public String getName() {
        return name;
    }

}
