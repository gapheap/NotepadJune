package com.stone.win;

/**
 * 配置
 * 
 * @author: stone
 * @date 2023-6-6 13:41:30
 */
public class CommonConfig {
    /**
     * 语言 0-English 1-简体中文
     */
    private int lang = 1;
    private int fontSize = 16;
    /**
     * 是否隐藏工具栏
     */
    private boolean hideTools = false;
    /**
     * 显示的工具栏项
     */
    private String showToolNames = "undo,cut,saveAll,save,addNew,find_Replace,copy,paste,redo,open";
    /**
     * 是否双击关闭
     */
    private boolean doubleClickClose = false;
    /**
     * 是否自动文件更新
     */
    private boolean autoReload = false;
    /**
     * 是否自动文件尾
     */
    private boolean autoTail = false;

    public int getLang() {
        return lang;
    }

    public void setLang(int lang) {
        this.lang = lang;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public boolean isHideTools() {
        return hideTools;
    }

    public void setHideTools(boolean hideTools) {
        this.hideTools = hideTools;
    }

    public String getShowToolNames() {
        return showToolNames;
    }

    public void setShowToolNames(String showToolNames) {
        this.showToolNames = showToolNames;
    }

    public boolean isDoubleClickClose() {
        return doubleClickClose;
    }

    public void setDoubleClickClose(boolean doubleClickClose) {
        this.doubleClickClose = doubleClickClose;
    }

    public boolean isAutoReload() {
        return autoReload;
    }

    public void setAutoReload(boolean autoReload) {
        this.autoReload = autoReload;
    }

    public boolean isAutoTail() {
        return autoTail;
    }

    public void setAutoTail(boolean autoTail) {
        this.autoTail = autoTail;
    }

}
