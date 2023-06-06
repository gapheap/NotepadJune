package com.stone.win.comp;

import javax.swing.JMenuItem;

import com.stone.win.CommonUtil;
import com.stone.win.Notepad;

/**
 * 菜单项
 * 
 * @author: stone
 * @date 2023-6-6 13:44:50
 */
public class SMenuItem extends JMenuItem {
    private static final long serialVersionUID = -8521710055143108683L;
    private String name;

    public SMenuItem(Notepad obj, String name) {
        super();
        CommonUtil.menuAndToolbar.add(this);
        this.name = name;
        this.setText(CommonUtil.menuLangMap.get(name).split(",")[CommonUtil.config.getLang()]);

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
