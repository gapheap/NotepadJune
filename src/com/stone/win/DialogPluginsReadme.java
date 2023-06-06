package com.stone.win;


import java.awt.Frame;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * 插件开发说明弹窗
 * 
 * @author: stone
 * @date 2023-6-6 13:43:42
 */
public class DialogPluginsReadme extends JDialog {

    private static final long serialVersionUID = -488145333092243860L;

    public DialogPluginsReadme(Frame owner, String title, boolean modal) {
        super(owner, true);
        this.setTitle(title);
        init();
        this.setVisible(true);
    }

    private void init() {
        JPanel jPanel1 = new JPanel();

        JLabel label1 = new JLabel();
        TextAreaEdit jTextArea1 = new TextAreaEdit(true, false);

        JScrollPane jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        label1.setText(CommonUtil.getShowText(CommonLangEnum.plugins_readme));
        String content = "package: plugins\r\n" + "class name: Tool\r\n" + "static methods: menu and others\r\n" + "source code example:\r\n------------------------------------\r\n" + "package plugins;\r\n" + "import java.util.HashMap;\r\n" + "import java.util.Map;\r\n" + "public class Tool {\r\n" + "    public static List<String> menu() {\r\n" + "        List<String> list = new ArrayList<>();\r\n" + "        /*菜单名, 类型：1-文字转换 2-弹窗, 方法名*/\r\n" + "        list.add(\"Convert, 1, convert\");\r\n" + "        list.add(\"About, 2, about\");\r\n" + "        return list;\r\n" + "    }" + "    public static String convert(String src) {\r\n" + "        if (src == null || src.trim().length() == 0) {\r\n" + "            return \"\";\r\n" + "        }\r\n" + "        if (Character.isUpperCase(src.charAt(0))) {\r\n" + "            return src.toLowerCase();\r\n" + "        } else {\r\n" + "            return src.toUpperCase();\r\n" + "        }\r\n" + "    }\r\n" + "    public static String about() {\r\n" + "        return \"Author: stone\\r\\nVersion: 1.0.1\\r\\nLicense: GPL\\r\\nHome: \\r\\n\";\r\n" + "    }\r\n" + "}\r\n------------------------------------";
        jTextArea1.setText(content);


        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(34, 34, 34).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 321, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(0, 14, Short.MAX_VALUE)).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(label1, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                ).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                )).addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(22, 22, 22).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(label1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                )).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE).addContainerGap())
        );

        this.setContentPane(jPanel1);
        this.setSize(400, 300);
        this.setLocationRelativeTo(null);
    }
}

