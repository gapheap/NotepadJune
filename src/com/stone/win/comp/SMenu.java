package com.stone.win.comp;

import javax.swing.JMenu;

import com.stone.win.CommonUtil;

/**
 * 菜单
 * 
 * @author: stone
 * @date 2023-6-6 13:44:46
 */
public class SMenu extends JMenu {

    private static final long serialVersionUID = -3009107348830964658L;
    private String name;

    public SMenu(String name) {
        super();
        CommonUtil.menuAndToolbar.add(this);
        this.name = name;
        this.setText(CommonUtil.menuLangMap.get(name).split(",")[CommonUtil.config.getLang()]);
    }

    public String getName() {
        return name;
    }

}
