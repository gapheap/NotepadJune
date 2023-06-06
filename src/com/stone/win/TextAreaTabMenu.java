package com.stone.win;

import java.awt.Font;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;

/**
 * 编辑器选项卡右键菜单
 * 
 * @author: stone
 * @date 2023-6-6 13:44:28
 */
public class TextAreaTabMenu extends JPopupMenu {
    private static final long serialVersionUID = -6081556530761202116L;
    JTabbedPane tabbedPane;
    private int selectIndex;
    private int tabCount;

    private JMenuItem closeTabItem = new JMenuItem();
    private JMenuItem closeOtherMenuItem = new JMenuItem();
    private JMenuItem closeAllMenuItem = new JMenuItem();
    private JMenuItem closeRightMenuItem = new JMenuItem();
    private JMenuItem reloadMenuItem = new JMenuItem();
    private JMenuItem openDirectoryMenuItem = new JMenuItem();


    public TextAreaTabMenu(JTabbedPane tabbedPane) {
        this.tabbedPane = tabbedPane;
        this.setFocusable(false);
        initTabbedMenuListeners();
    }

    private void initTabbedMenuListeners() {
        closeTabItem.setText(CommonUtil.getShowText(CommonLangEnum.close));
        closeOtherMenuItem.setText(CommonUtil.getShowText(CommonLangEnum.closeOther));
        closeAllMenuItem.setText(CommonUtil.getShowText(CommonLangEnum.closeAll));
        closeRightMenuItem.setText(CommonUtil.getShowText(CommonLangEnum.closeRight));
        reloadMenuItem.setText(CommonUtil.getShowText(CommonLangEnum.reloadFile));
        openDirectoryMenuItem.setText(CommonUtil.getShowText(CommonLangEnum.openDirectory));
        Font font = CommonUtil.getFont();
        closeTabItem.setFont(font);
        closeOtherMenuItem.setFont(font);
        closeAllMenuItem.setFont(font);
        closeRightMenuItem.setFont(font);
        reloadMenuItem.setFont(font);
        openDirectoryMenuItem.setFont(font);

        this.closeTabItem.addActionListener(e -> closeTabAction());
        this.closeOtherMenuItem.addActionListener(e -> closeOtherTabAction());
        this.closeAllMenuItem.addActionListener(e -> closeAllTabAction());
        this.closeRightMenuItem.addActionListener(e -> closeRightTabAction());
        this.reloadMenuItem.addActionListener(e -> reloadTabAction());
        this.openDirectoryMenuItem.addActionListener(e -> openDirectoryTabAction());
    }

    private void closeTabAction() {
        tabbedPane.remove(selectIndex);
    }

    private void closeOtherTabAction() {
        if (tabCount > selectIndex + 1) {
            for (int i = tabCount - 1; i >= selectIndex + 1; i--) {
                tabbedPane.removeTabAt(i);
            }
        }
        for (int i = 0; i <= selectIndex - 1; i++) {
            tabbedPane.removeTabAt(0);
        }
    }

    private void closeAllTabAction() {
        for (int i = tabCount - 1; i >= 0; i--) {
            tabbedPane.removeTabAt(i);
        }
    }

    private void closeRightTabAction() {
        int start = selectIndex + 1;
        for (int i = tabCount - 1; i >= start; i--) {
            tabbedPane.removeTabAt(i);
        }
    }

    private void reloadTabAction() {
        TextAreaTab tab = (TextAreaTab) tabbedPane.getSelectedComponent();
        if (tab == null || tab.getFilePath() == null) {
            return;
        }
        String title = CommonUtil.getShowText(CommonLangEnum.reload);
        String hint = CommonUtil.getShowText(CommonLangEnum.hintReload);
        int i = JOptionPane.showConfirmDialog(this, hint, title, JOptionPane.YES_NO_OPTION);
        if (i == 0) {
            CommonUtil.openFile(new File(tab.getFilePath()), tab.getjTextArea1(), tab.getCharset());
        }
    }

    private void openDirectoryTabAction() {
        TextAreaTab tab = (TextAreaTab) tabbedPane.getSelectedComponent();
        if (tab == null || tab.getFilePath() == null) {
            return;
        }
        try {
            Runtime.getRuntime().exec("explorer.exe /select, " + tab.getFilePath());
        } catch (Exception e) {
            CommonUtil.log(e);
        }
    }

    public void showTabbedMenu(MouseEvent e) {
        this.removeAll();
        createTabbedMenu();
        selectIndex = tabbedPane.getSelectedIndex();
        tabCount = tabbedPane.getTabCount();
        if (selectIndex >= 0) {
            this.closeTabItem.setEnabled(true);
        }
        if (tabCount == 1) {
            this.closeOtherMenuItem.setEnabled(false);
            this.closeAllMenuItem.setEnabled(false);
            this.closeRightMenuItem.setEnabled(false);
        } else {
            this.closeOtherMenuItem.setEnabled(true);
            this.closeAllMenuItem.setEnabled(true);
            this.closeRightMenuItem.setEnabled(true);
            if (selectIndex == tabCount - 1) {
                this.closeRightMenuItem.setEnabled(false);
            }
        }
        TextAreaTab x = (TextAreaTab) tabbedPane.getSelectedComponent();
        if (x == null || x.getFilePath() == null) {
            this.reloadMenuItem.setEnabled(false);
            this.openDirectoryMenuItem.setEnabled(false);
        } else {
            this.reloadMenuItem.setEnabled(true);
            this.openDirectoryMenuItem.setEnabled(true);
        }
        this.show(e.getComponent(), e.getX(), e.getY());
    }

    private void createTabbedMenu() {
        this.add(closeTabItem);
        this.add(new JPopupMenu.Separator());
        this.add(closeOtherMenuItem);
        this.add(closeAllMenuItem);
        this.add(new JPopupMenu.Separator());
        this.add(closeRightMenuItem);
        this.add(new JPopupMenu.Separator());
        this.add(reloadMenuItem);
        this.add(openDirectoryMenuItem);
    }
}
