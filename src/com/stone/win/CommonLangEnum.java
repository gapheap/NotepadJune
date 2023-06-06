package com.stone.win;

import java.util.ArrayList;
import java.util.List;

/**
 * 语言
 * 
 * @see com.stone.win.comp.CommonMenuEnum
 * @author: stone
 * @date 2023-6-6 13:42:33
 */
public enum CommonLangEnum {
    edit("Edit", "编辑"), copy("Copy", "复制"), paste("Paste", "粘贴"), cut("Cut", "剪切"), delete("Delete", "删除"), 
    selectAll("Select All", "全选"), wordCount("Word Count", "字数统计"), findReplace("Find/Replace", "查找替换"), 
    settings("Settings", "设置"), plugins_readme("Plugins Readme", "插件开发说明"), version("Version", "版本"), 
    buildTime("Build Time", "构建时间"), home("Home", "主页"), close("Close", "关闭"), 
    closeRight("Close Right", "关闭右侧"), closeOther("Close Other", "关闭非当前"), closeAll("Close All", "关闭所有"), 
    options("Options", "可选项"), appearance("Appearance", "外观"), tools("Tools", "工具栏"), 
    keyMap("Key Map", "快捷键"), function("Function", "功能"), language("Language", "语言"), 
    fontSize("Font Size", "文字大小"), hideTools("Hide Tools", "隐藏工具栏"), dbclickTab("Dbclick Close Tab", "双击关闭标签"), 
    autoReload("Auto Reload File", "自动检测文件"), autoTail("Auto File Tail", "自动定位到文件尾"), 
    reload("Reload", "重新读取文件"), toUpperCase("To Upper Case", "转成大写"), toLowerCase("To Lower Case", "转成小写"), 
    notepadName("NotepadJune", "六月记事本"), wordNumber("Word Number", "字数"), reloadFile("Reload File", "重新加载文件"), 
    openDirectory("Open Folder", "打开所在目录"), hintReload("Are you sure you want to reload the current file and lose your changes?", "重新加载文件将不保存当前的变更，是否继续操作？"), 
    base64Encode("Base64 Encode", "Base64编码"), base64Decode("Base64 Decode", "Base64解码"), 
    warning("Error, Please check the data or contact the plugin author.", "出错，请检查数据或联系插件作者。"), 
    findWhat("Find What", "查找目标"), replaceWhat("Replace What", "替换为"), find("Find", "查找"), 
    replace("Replace", "替换"), replaceAll("Replace All", "全部替换"), caseSensitive("Match Case", "区分大小写"), 
    wholeWord("Whole Word", "全字匹配"), backward("Backward Direction", "反向查找"), 
    hindReplace("%d matches were replaced", "已替换%d处"), enterLineNo("Enter line number", "输入行号"), 
    jumpTo("Go to", "跳转"), cencel("Cencel", "取消"),
    hideToolbarItem("Hide toolbar item", "隐藏工具栏按钮"),
    shortCutTitle("Serial Number, Name, Shortcut", "序号, 名称, 快捷键"),
    aboutContent("Free version, Some function to be continued: such as print,syntax highlighting,multi-line edit etc.", "免费版本，待实现功能：打印、语法高亮、列块模式等"),

    ;

    private String[] data;

    CommonLangEnum(String... data) {
        List<String> list = new ArrayList<>();
        for (String str : data) {
            list.add(str);
        }
        this.data = list.toArray(new String[list.size()]);
    }

    public String[] getData() {
        return data;
    }

    public void setData(String[] data) {
        this.data = data;
    }

}
