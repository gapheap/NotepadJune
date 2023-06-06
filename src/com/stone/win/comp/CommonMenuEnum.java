package com.stone.win.comp;

import java.util.HashSet;
import java.util.Set;

import com.stone.win.CommonUtil;

/**
 * 菜单语言
 * 
 * @author: stone
 * @date 2023-6-6 13:44:36
 */
public enum CommonMenuEnum {
    file(true, "file", "File,文件"), addNew("addNew", "New,新建"), open("open", "Open,打开"), save("save", "Save,保存"), saveAs("saveAs", "Save As,另存为"), saveAll("saveAll", "Save All,全部保存"), reload("reload", "Reload,重新读取文件"), exit(false, "exit", "Exit,退出"),

    edit(true, "edit", "Edit,编辑"), copy("copy", "Copy,复制"), paste("paste", "Paste,粘贴"), cut("cut", "Cut,剪切"), delete("delete", "Delete,删除"), undo("undo", "Undo,撤销"), redo("redo", "Redo,恢复"), selectAll("selectAll", "Select All,全选"),

    search(true, "search", "Search,搜索"), jumpToLine("jumpToLine", "Jump To Line,行定位"), find_Replace("find_Replace", "Find/Replace,查找替换"),

    encoding(true, "encoding", "Encoding,编码"), settings(true, "settings", "Settings,设置"), options(false, "options", "Options,可选项"),

    tools_(true, "tools_", "Tools,工具"), wordCount("wordCount", "Word Count,字数统计"), toUpperCase("toUpperCase", "To Upper Case,转成大写"), toLowerCase("toLowerCase", "To Lower Case,转成小写"), md5("md5", "MD5,MD5"), base64Encode("base64Encode", "Base64 Encode,Base64编码"), base64Decode("base64Decode", "Base64 Decode,Base64解码"), unicodeTo("unicodeTo", "Unicode To,Unicode To"), toUnicode("toUnicode", "To Unicode,To Unicode"),

    plugins(true, "plugins", "Plugins,插件"), plugins_manage(false, "plugins_manage", "Plugins Directory,插件目录"), plugins_readme(false, "plugins_readme", "Plugins Readme,插件开发说明"),

    help(true, "help", "Help,帮助"), about(false, "about", "About,关于"),

    enlarge("enlarge", "Enlarge,放大"), reduce("reduce", "Reduce,缩小"),

    ;

    private String method;
    private String text;
    public static final int TYPE_MENU = 1;
    public static final int TYPE_EXCUDE_TOOL = 2;
    /**
     * 类型 0-普通菜单项 1-菜单 2-不放入工具栏的菜单项
     */
    private int type = 0;

    /**
     * 普通菜单项
     * <默认构造函数>
     */
    CommonMenuEnum(String method, String text) {
        this.method = method;
        this.text = text;
    }

    /**
     * 菜单和不放入工具栏的菜单项
     * <默认构造函数>
     */
    CommonMenuEnum(boolean isMenu, String method, String text) {
        int type = isMenu ? TYPE_MENU : TYPE_EXCUDE_TOOL;
        this.method = method;
        this.text = text;
        this.type = type;
    }

    public String getMethod() {
        return method;
    }

    public String getText() {
        return text;
    }

    public int getType() {
        return type;
    }

    public static void check() {
        Set<String> set = new HashSet<>();
        for (CommonMenuEnum e : CommonMenuEnum.values()) {
            if (set.contains(e.getMethod())) {
                throw new RuntimeException("菜单方法名重复:" + e.getMethod());
            }
            CommonUtil.menuLangMap.put(e.getMethod(), e.getText());
            set.add(e.getMethod());
        }
    }

    public static void main(String[] args) {
        //生成方法代码
        for (CommonMenuEnum x : CommonMenuEnum.values()) {
            if (CommonMenuEnum.TYPE_MENU == x.getType()) {
                continue;
            }
            String str = "public void " + x.getMethod() + "(){}";
            System.out.println(str);
        }
    }

}
