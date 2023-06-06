package com.stone.win;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.stone.win.comp.CommonMenuEnum;
import com.stone.win.consumer.ISaveConfig;

/**
 * 设置弹窗
 * 
 * @author: stone
 * @date 2023-6-6 13:43:50
 */
public class DialogSettings extends JDialog {
    private static final long serialVersionUID = 2277520672060221631L;

    private JTabbedPane jTabbedPane1;
    private JTable jTable2;
    private DefaultTableModel model;
    private JScrollPane jScrollPane1;
    JComboBox<String> jComboBox1;
    JComboBox<String> jComboBox2;
    private Map<Object, CommonLangEnum> components = new HashMap<>();
    private Map<Object, CommonMenuEnum> toolbarContents = new HashMap<>();

    public DialogSettings(Notepad owner, String title, boolean modal) {
        super(owner, true);
        this.setTitle(title);
        this.setFont(CommonUtil.getFont());
        init(owner);
        this.setVisible(true);
    }

    private void init(Notepad owner) {
        jTabbedPane1 = new JTabbedPane();
        JPanel jPanel1 = new javax.swing.JPanel();
        JLabel jLabel1 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        JLabel jLabel2 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox<>();
        JPanel jPanel2 = new javax.swing.JPanel();
        JPanel jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        JPanel jPanel4 = new javax.swing.JPanel();
        JCheckBox jCheckBox1 = new javax.swing.JCheckBox();
        JCheckBox jCheckBox2 = new javax.swing.JCheckBox();
        JCheckBox jCheckBox3 = new javax.swing.JCheckBox();
        JCheckBox jCheckBox4 = new javax.swing.JCheckBox();

        jTabbedPane1.setTabPlacement(javax.swing.JTabbedPane.LEFT);
        jTabbedPane1.setFont(CommonUtil.getFont());

        jLabel1.setText(getShowText(jLabel1, CommonLangEnum.appearance) + ": ");
        components.put(jLabel1, CommonLangEnum.appearance);

        jLabel1.setFont(CommonUtil.getFont());
        ComboBoxModel<String> langArr = new DefaultComboBoxModel<>(new String[]{"English", "简体中文"});
        jComboBox1.setFont(CommonUtil.getFont());
        jComboBox1.setModel(langArr);
        jComboBox1.setSelectedIndex(CommonUtil.config.getLang());
        jComboBox1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ISaveConfig i = () -> {
                    CommonUtil.config.setLang(jComboBox1.getSelectedIndex());
                    owner.setTitle(CommonUtil.getShowText(CommonLangEnum.notepadName));
                    CommonUtil.reloadMenuText();
                    reText();
                };
                i.exec();
            }
        });
        jLabel2.setText(getShowText(jLabel2, CommonLangEnum.fontSize) + ": ");
        jLabel2.setFont(CommonUtil.getFont());
        ComboBoxModel<String> sizeArr = new DefaultComboBoxModel<>(new String[]{"12", "15", "16", "18", "20", "22", "24"});
        jComboBox2.setFont(CommonUtil.getFont());
        jComboBox2.setModel(sizeArr);
        String x = String.valueOf(CommonUtil.config.getFontSize());
        int idx = 0;
        int count = sizeArr.getSize();
        for (int i = 0; i < count; i++) {
            if (x.equals(sizeArr.getElementAt(i))) {
                idx = i;
                break;
            }
        }
        jComboBox2.setSelectedIndex(idx);
        jComboBox2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ISaveConfig i = () -> {
                    String selected = String.valueOf(jComboBox2.getSelectedItem());
                    CommonUtil.config.setFontSize(Integer.valueOf(selected));
                    owner.reloadFont();
                    reFontSettings();
                };
                i.exec();
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addGap(31, 31, 31).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addGroup(jPanel1Layout.createSequentialGroup().addComponent(jLabel2).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(jPanel1Layout.createSequentialGroup().addComponent(jLabel1).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))).addContainerGap(109, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addGap(17, 17, 17).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel1).addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel2).addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap(221, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab(getShowText(jPanel1, CommonLangEnum.appearance), jPanel1);
        FlowLayout jPanel2Layout = new FlowLayout();
        jPanel2.setLayout(jPanel2Layout);
        JLabel label = new JLabel();
        label.setText(getShowText(label, CommonLangEnum.hideToolbarItem) + ":" + CommonUtil.repeatString(" ", 61));
        jPanel2.add(label);
        for (CommonMenuEnum toolEnum : CommonMenuEnum.values()) {
            if (toolEnum.getType() == 0) {
                String name = toolEnum.getMethod();
                JCheckBox temp = new JCheckBox();
                temp.setText(CommonUtil.menuLangMap.get(name).split(",")[CommonUtil.config.getLang()]);
                temp.setFont(CommonUtil.getFont());
                toolbarContents.put(temp, toolEnum);
                temp.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        ISaveConfig i = () -> {
                            if (temp.isSelected()) {
                                CommonUtil.showToolNames.add(name);
                            } else {
                                CommonUtil.showToolNames.remove(name);
                            }
                            String hideToolNames = CommonUtil.join4set(CommonUtil.showToolNames, ",");
                            CommonUtil.config.setShowToolNames(hideToolNames);
                            owner.reloadToolbarButtons();
                        };
                        i.exec();
                    }
                });
                if (CommonUtil.showToolNames.contains(name)) {
                    temp.setSelected(true);
                }
                jPanel2.add(temp);
            }
        }

        jTabbedPane1.addTab(getShowText(jPanel2, CommonLangEnum.tools), jPanel2);

        model = new DefaultTableModel(getRowData(), getRowTitle());
        jTable2 = new JTable(model) {
            private static final long serialVersionUID = -4954380104692907122L;

            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        jTable2.setFont(CommonUtil.getFont());
        jScrollPane1.setViewportView(jTable2);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel3Layout.createSequentialGroup().addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 296, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(0, 8, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup().addGap(0, 0, Short.MAX_VALUE).addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jTabbedPane1.addTab(getShowText(jPanel3, CommonLangEnum.keyMap), jPanel3);

        jCheckBox1.setText(getShowText(jCheckBox1, CommonLangEnum.hideTools));
        jCheckBox1.setFont(CommonUtil.getFont());
        jCheckBox1.setSelected(CommonUtil.config.isHideTools());
        jCheckBox1.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                ISaveConfig i = () -> {
                    boolean b = jCheckBox1.isSelected();
                    CommonUtil.config.setHideTools(b);
                    ((Notepad) owner).hideTools(b);
                };
                i.exec();
            }
        });

        jCheckBox2.setText(getShowText(jCheckBox2, CommonLangEnum.dbclickTab));
        if (CommonUtil.config.isDoubleClickClose()) {
            jCheckBox2.setSelected(true);
        }
        jCheckBox2.setFont(CommonUtil.getFont());
        jCheckBox2.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                ISaveConfig i = () -> {
                    CommonUtil.config.setDoubleClickClose(jCheckBox2.isSelected());
                };
                i.exec();
            }
        });

        jCheckBox3.setText(getShowText(jCheckBox3, CommonLangEnum.autoReload));
        jCheckBox3.setFont(CommonUtil.getFont());
        if (CommonUtil.config.isAutoReload()) {
            jCheckBox3.setSelected(true);
        }
        jCheckBox3.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                ISaveConfig i = () -> {
                    CommonUtil.config.setAutoReload(jCheckBox3.isSelected());
                };
                i.exec();
            }
        });

        jCheckBox4.setText(getShowText(jCheckBox4, CommonLangEnum.autoTail));
        jCheckBox4.setFont(CommonUtil.getFont());
        if (CommonUtil.config.isAutoTail()) {
            jCheckBox4.setSelected(true);
        }
        jCheckBox4.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                ISaveConfig i = () -> {
                    CommonUtil.config.setAutoTail(jCheckBox4.isSelected());
                };
                i.exec();
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel4Layout.createSequentialGroup().addGap(16, 16, 16).addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jCheckBox4).addComponent(jCheckBox3).addComponent(jCheckBox2).addComponent(jCheckBox1)).addContainerGap(168, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel4Layout.createSequentialGroup().addContainerGap().addComponent(jCheckBox1).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jCheckBox2).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jCheckBox3).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jCheckBox4).addContainerGap(172, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab(getShowText(jPanel4, CommonLangEnum.function), jPanel4);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 380, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(0, 115, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(25, 25, 25).addComponent(jTabbedPane1).addContainerGap())
        );

        this.setContentPane(jTabbedPane1);
        this.setSize(400, 300);
        this.setLocationRelativeTo(null);
    }

    private String getShowText(Object obj, CommonLangEnum commonLangEnum) {
        components.put(obj, commonLangEnum);
        return CommonUtil.getShowText(commonLangEnum);
    }

    private Object[][] getRowData() {
        int len = CommonUtil.hotKeyMap.size();
        Object[][] rowData = new Object[len][3];
        Iterator<Entry<CommonMenuEnum, String>> it = CommonUtil.hotKeyMap.entrySet().iterator();
        int i = 0;
        while (it.hasNext()) {
            Entry<CommonMenuEnum, String> entry = it.next();
            CommonMenuEnum key = entry.getKey();
            String value = entry.getValue();
            String name = CommonUtil.menuLangMap.get(key.getMethod()).split(",")[CommonUtil.config.getLang()];
            rowData[i] = new String[]{String.valueOf(i + 1), name, value};
            i++;
        }
        return rowData;
    }

    private Object[] getRowTitle() {
        return CommonUtil.string2ObjectArr(CommonUtil.getShowText(CommonLangEnum.shortCutTitle));
    }

    private void reText() {
        model.setDataVector(getRowData(), getRowTitle());
        Iterator<Entry<Object, CommonLangEnum>> it = components.entrySet().iterator();
        while (it.hasNext()) {
            Entry<Object, CommonLangEnum> entry = it.next();
            Object obj = entry.getKey();
            entry.getValue();
            if (obj instanceof JLabel) {
                JLabel x = (JLabel) obj;
                x.setText(CommonUtil.getShowText(entry.getValue()));
            } else if (obj instanceof JCheckBox) {
                JCheckBox x = (JCheckBox) obj;
                x.setText(CommonUtil.getShowText(entry.getValue()));
            }
        }
        Iterator<Entry<Object, CommonMenuEnum>> it2 = toolbarContents.entrySet().iterator();
        while (it2.hasNext()) {
            Entry<Object, CommonMenuEnum> entry = it2.next();
            Object obj = entry.getKey();
            entry.getValue();
            if (obj instanceof JCheckBox) {
                JCheckBox x = (JCheckBox) obj;
                x.setText(CommonUtil.getShowTextMenu(entry.getValue()));
            }
        }
        int count = jTabbedPane1.getTabCount();
        for (int i = 0; i < count; i++) {
            JPanel jp = (JPanel) jTabbedPane1.getComponent(i);
            if (components.containsKey(jp)) {
                jTabbedPane1.setTitleAt(i, CommonUtil.getShowText(components.get(jp)));
            }
        }
    }

    private void reFontSettings() {
        Font font = CommonUtil.getFont();
        jTabbedPane1.setFont(font);
        jTable2.setFont(font);
        jComboBox1.setFont(font);
        jComboBox2.setFont(font);
        Iterator<Entry<Object, CommonLangEnum>> it = components.entrySet().iterator();
        while (it.hasNext()) {
            Entry<Object, CommonLangEnum> entry = it.next();
            Object obj = entry.getKey();
            entry.getValue();
            if (obj instanceof JLabel) {
                JLabel x = (JLabel) obj;
                x.setFont(font);
            } else if (obj instanceof JCheckBox) {
                JCheckBox x = (JCheckBox) obj;
                x.setFont(font);
            }
        }
        Iterator<Entry<Object, CommonMenuEnum>> it2 = toolbarContents.entrySet().iterator();
        while (it2.hasNext()) {
            Entry<Object, CommonMenuEnum> entry = it2.next();
            Object obj = entry.getKey();
            entry.getValue();
            if (obj instanceof JCheckBox) {
                JCheckBox x = (JCheckBox) obj;
                x.setFont(font);
            }
        }
    }

}
