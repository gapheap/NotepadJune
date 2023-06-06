package com.stone.win;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.KeyEventPostProcessor;
import java.awt.KeyboardFocusManager;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import com.stone.win.comp.CommonMenuEnum;
import com.stone.win.comp.SButton;
import com.stone.win.comp.SMenu;
import com.stone.win.comp.SMenuItem;
import com.stone.win.consumer.IException;

/**
 * 主类
 * 
 * @author: stone
 * @date 2023-6-6 13:43:59
 */
public class Notepad extends javax.swing.JFrame {
    private KeyboardFocusManager manager;
    private int TAB_IDX = 0;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JTabbedPane jTabbedPane5;
    private javax.swing.JToolBar jToolBar1;
    private static final long serialVersionUID = 7764641280959582600L;
    private Map<Integer, CommonMenuEnum> ctrlMap = new HashMap<>();
    private Map<Integer, CommonMenuEnum> altMap = new HashMap<>();
    private Map<Integer, CommonMenuEnum> keyMap = new HashMap<>();

    public Notepad() {
        this.setTitle(CommonUtil.getShowText(CommonLangEnum.notepadName));
        CommonUtil.preparePluginsJarPath();
        initComponents();
        CommonUtil.reloadMenuFont();
        bindHotKey();
        bindDrag();
        initTimer();
    }

    /**
     * 绑定热键
     * 
     * @author: stone
     * @date 2023-6-6 14:16:20
     */
    private void bindHotKey() {
        Iterator<Entry<CommonMenuEnum, String>> it = CommonUtil.hotKeyMap.entrySet().iterator();
        while (it.hasNext()) {
            Entry<CommonMenuEnum, String> entry = it.next();
            String value = entry.getValue();
            if (value.toUpperCase().startsWith("CTRL")) {
                int idx = value.indexOf("+");
                if (idx != -1) {
                    ctrlMap.put(CommonUtil.getCode(value.substring(idx + 1)), entry.getKey());
                }
            } else if (value.toUpperCase().startsWith("ALT")) {
                int idx = value.indexOf("\\+");
                if (idx != -1) {
                    altMap.put(CommonUtil.getCode(value.substring(idx + 1)), entry.getKey());
                }
            } else {
                keyMap.put(CommonUtil.getCode(value), entry.getKey());
            }
        }
        Notepad notepad = this;
        manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventPostProcessor(new KeyEventPostProcessor() {
            @Override
            public boolean postProcessKeyEvent(KeyEvent e) {
                if (e.getID() == KeyEvent.KEY_RELEASED) {
                    Component focus = notepad.getFocusOwner();
                    if (focus == null) {
                        return false;
                    }
                    int keyCode = e.getKeyCode();
                    if (e.isControlDown()) {
                        if (ctrlMap.containsKey(keyCode)) {
                            CommonMenuEnum tmp = ctrlMap.get(keyCode);
                            CommonUtil.bindMethod(notepad, tmp.getMethod());
                        }
                    } else if (e.isAltDown()) {
                        if (altMap.containsKey(keyCode)) {
                            CommonMenuEnum tmp = altMap.get(keyCode);
                            CommonUtil.bindMethod(notepad, tmp.getMethod());
                        }
                    } else {
                        if (keyMap.containsKey(keyCode)) {
                            CommonMenuEnum tmp = altMap.get(keyCode);
                            CommonUtil.bindMethod(notepad, tmp.getMethod());
                        }
                    }
                }
                return false;
            }
        });
    }

    /**
     * 插件目录加载
     * 
     * @author: stone
     * @date 2023-6-6 14:21:19
     * @param menu
     */
    private void addPluginsMenu(SMenu menu) {
        for (String str : CommonUtil.jarPathList) {
            File file = new File(str);
            addSinglePlugin(menu, file);
        }
    }

    /**
     * 单个插件目录加载
     * 
     * @author: stone
     * @date 2023-6-6 14:21:32
     * @param menu
     * @param file
     */
    private void addSinglePlugin(SMenu menu, File file) {
        String jarName = file.getName();
        if (!jarName.endsWith(".jar")) {
            return;
        }
        try {
            CommonJarLoader loader = CommonJarLoader.getInstance();
            String url = "jar:file:///" + CommonUtil.USER_DIR + "/plugins/" + jarName + "!/";
            loader.addURL(url);
            Class<?> aClass = Class.forName("plugins.Tool", true, loader);
            Object instance = aClass.newInstance();
            Object strip = aClass.getDeclaredMethod("menu").invoke(instance);
            @SuppressWarnings("unchecked") List<String> list = (List<String>) strip;

            JMenu menu1 = new JMenu();
            CommonUtil.menuAndToolbarAttach.add(menu1);
            menu1.setFont(CommonUtil.getFont());
            menu1.setText(CommonUtil.getNameExcludeExt(jarName));
            for (String entry : list) {
                String key = entry.split(",")[0];
                JMenuItem tmp = new javax.swing.JMenuItem();
                CommonUtil.menuAndToolbarAttach.add(tmp);
                tmp.setFont(CommonUtil.getFont());
                tmp.setText(key);
                tmp.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        pluginClickActionPerformed(evt, jarName, entry);
                    }
                });
                menu1.add(tmp);
            }
            menu.add(menu1);
        } catch (Exception e) {
            CommonUtil.log(e);
        }
    }

    /**
     * 插件事件绑定
     * 
     * @author: stone
     * @date 2023-6-6 14:24:16
     * @param evt
     * @param jarName
     * @param pluginMenu
     */
    private void pluginClickActionPerformed(java.awt.event.ActionEvent evt, String jarName, String pluginMenu) {
        if (pluginMenu == null || pluginMenu.trim().length() == 0) {
            return;
        }
        String[] arr = pluginMenu.split(",");
        if (arr.length < 3) {
            return;
        }
        String type = arr[1].trim();
        if ("1".equals(type)) {
            IException i = () -> {
                String param = getSelectedText();
                String result = call(jarName, arr[2].trim(), param);
                replaceSelectedText(result);
            };
            i.exec();
        } else if ("2".equals(type)) {
            String result = call(jarName, arr[2].trim(), null);
            if (result == null) {
                return;
            }
            String[] content = result.trim().split("\\r?\\n|\\r");
            new DialogPluginAbout(this, CommonUtil.getNameExcludeExt(jarName), true, content);
        }
    }

    /**
     * 获取当前编辑的TextArea
     * 
     * @author: stone
     * @date 2023-6-6 14:25:17
     * @return
     */
    private TextAreaEdit getSelectedTextArea() {
        TextAreaTab tabPanel = getSelectedTab();
        TextAreaEdit textArea = tabPanel.getjTextArea1();
        if (textArea == null) {
            throw new NotepadException();
        }
        return textArea;
    }

    /**
     * 获取当前TextArea选中的文本，未选文本则返回全部文本
     * 
     * @author: stone
     * @date 2023-6-6 14:25:55
     * @return
     */
    private String getSelectedText() {
        TextAreaEdit textArea = getSelectedTextArea();
        String param = textArea.getSelectedText();
        if (param == null || param.trim().length() == 0) {
            param = textArea.getText();
            textArea.setSelectText(false);
        } else {
            textArea.setSelectText(true);
        }
        if (param == null || param.trim().length() == 0) {
            throw new NotepadException();
        }
        return param;
    }

    /**
     * 替换当前TextArea选中的文本，未选中替换全部
     * 
     * @author: stone
     * @date 2023-6-6 14:27:43
     * @param result
     */
    public void replaceSelectedText(String result) {
        if (CommonUtil.isEmpty(result)) {
            return;
        }
        TextAreaEdit textArea = getSelectedTextArea();
        if (textArea.isSelectText()) {
            textArea.replaceSelection(result);
        } else {
            textArea.setText(result);
        }
    }

    /**
     * 粘贴追加文本
     * 
     * @author: stone
     * @date 2023-6-6 14:30:11
     * @param result
     */
    public void insertText4Paste(String result) {
        if (CommonUtil.isEmpty(result)) {
            return;
        }
        TextAreaEdit textArea = getSelectedTextArea();
        if (textArea.isSelectText()) {
            textArea.replaceSelection(result);
        } else {
            textArea.insert(result, textArea.getCaretPosition());
        }
    }

    /**
     * 插件事件响应
     * 
     * @author: stone
     * @date 2023-6-6 14:32:32
     * @param fileName
     * @param method
     * @param param
     * @return
     */
    private String call(String fileName, String method, String param) {
        try {
            CommonJarLoader loader = CommonJarLoader.getInstance();
            String url = "jar:file:///" + CommonUtil.USER_DIR + "/plugins/" + fileName + "!/";
            loader.addURL(url);
            Class<?> aClass = Class.forName("plugins.Tool", true, loader);
            Object instance = aClass.newInstance();
            Object strip = null;
            if (param == null) {
                strip = aClass.getDeclaredMethod(method).invoke(instance);
            } else {
                strip = aClass.getDeclaredMethod(method, String.class).invoke(instance, param);
            }
            return String.valueOf(strip);
        } catch (Exception e) {
            CommonUtil.showWarning(method, CommonUtil.getShowText(CommonLangEnum.warning));
            CommonUtil.log(e);
        }
        return null;
    }

    /**
     * 主界面渲染，包括菜单、工具栏、编辑区、状态栏等
     * 
     * @author: stone
     * @date 2023-6-6 14:33:27
     */
    private void initComponents() {
        jToolBar1 = new javax.swing.JToolBar();
        jTabbedPane5 = new javax.swing.JTabbedPane();
        jMenuBar1 = new javax.swing.JMenuBar();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
        });
        jTabbedPane5.setFont(CommonUtil.getFont());

        MouseListener[] listeners = jTabbedPane5.getMouseListeners();
        for (MouseListener listener : listeners) {
            jTabbedPane5.removeMouseListener(listener);
        }

        TextAreaTabMenu tabbedMenu = new TextAreaTabMenu(jTabbedPane5);
        jTabbedPane5.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int index = jTabbedPane5.indexAtLocation(e.getX(), e.getY());
                if (index == -1) {
                    return;
                }
                if (e.getModifiers() == MouseEvent.BUTTON1_MASK && e.getClickCount() == 2) {
                    if (CommonUtil.config.isDoubleClickClose()) {
                        jTabbedPane5.removeTabAt(index);
                        return;
                    }
                }
                if (e.getModifiers() == MouseEvent.BUTTON1_MASK) {
                    if (index > -1) {
                        jTabbedPane5.setSelectedIndex(index);
                    }
                } else if (e.getModifiers() == MouseEvent.BUTTON3_MASK) {
                    jTabbedPane5.setSelectedIndex(index);
                    tabbedMenu.showTabbedMenu(e);
                }
            }
        });

        initMenu();
        initToolbarAuto();
        this.setJMenuBar(jMenuBar1);
        this.add(jToolBar1, BorderLayout.NORTH);
        this.add(jTabbedPane5, BorderLayout.CENTER);

        this.setSize(CommonUtil.DEFAULT_WIDTH, CommonUtil.DEFAULT_HEIGHT);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    /**
     * 窗口最大化与窗口恢复响应
     * 
     * @author: stone
     * @date 2023-6-6 14:34:51
     * @param evt
     */
    private void formComponentResized(java.awt.event.ComponentEvent evt) {
        int width = this.getWidth();
        CommonUtil.notFullScreen = width == CommonUtil.DEFAULT_WIDTH;
        jMenuBar1.setSize(width, jMenuBar1.getHeight());
        jToolBar1.setSize(width, jToolBar1.getHeight());
        int count = jTabbedPane5.getTabCount();
        for (int i = 0; i < count; i++) {
            TextAreaTab x = (TextAreaTab) jTabbedPane5.getComponentAt(i);
            TextAreaEdit jTextArea1 = x.getjTextArea1();
            CommonUtil.setRowsAndColumns(jTextArea1);
        }
    }

    /**
     * 隐藏工具栏
     * 
     * @author: stone
     * @date 2023-6-6 14:35:43
     * @param b
     */
    public void hideTools(boolean b) {
        this.jToolBar1.setVisible(!b);
    }

    /**
     * 工具栏按钮加载
     * 
     * @author: stone
     * @date 2023-6-6 14:35:51
     */
    public void reloadToolbarButtons() {
        int count = jToolBar1.getComponentCount();
        for (int i = 0; i < count; i++) {
            SButton button = (SButton) jToolBar1.getComponent(i);
            if (CommonUtil.showToolNames.contains(button.getName())) {
                button.setVisible(true);
            } else {
                button.setVisible(false);
            }
        }
    }

    /**
     * 主方法
     * 
     * @author: stone
     * @date 2023-6-6 14:36:32
     * @param args
     */
    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                 UnsupportedLookAndFeelException e) {
            CommonUtil.log(e);
        }
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Notepad();
            }
        });
    }

    /**
     * 新增选项卡，包括未保存和打开文件
     * 
     * @author: stone
     * @date 2023-6-6 14:39:02
     * @param isFile
     */
    private void addTab(boolean isFile) {
        File file = null;
        if (isFile) {
            file = openFile();
            if (file == null) {
                return;
            }
        }
        TextAreaTab jPanel1 = new TextAreaTab(this);
        if (isFile) {
            validSameFile(file);
            CommonUtil.openFile(file, jPanel1.getjTextArea1(), jPanel1.getCharset());
            jPanel1.setFilePath(file.getAbsolutePath());
            jTabbedPane5.addTab(file.getName(), jPanel1);
        } else {
            int tmp = TAB_IDX + 1;
            TAB_IDX = tmp;
            jTabbedPane5.addTab("New" + tmp, jPanel1);
        }
        jTabbedPane5.setSelectedComponent(jPanel1);
    }

    /**
     * 拖动打开文件
     * 
     * @author: stone
     * @date 2023-6-6 14:40:15
     * @param file
     */
    private void openFile4Drag(File file) {
        if (!file.exists() || file.isDirectory()) {
            return;
        }
        validSameFile(file);
        TextAreaTab jPanel1 = new TextAreaTab(this);
        CommonUtil.openFile(file, jPanel1.getjTextArea1(), jPanel1.getCharset());
        jPanel1.setFilePath(file.getAbsolutePath());
        jTabbedPane5.addTab(file.getName(), jPanel1);
        jTabbedPane5.setSelectedComponent(jPanel1);
    }

    /**
     * 已打开文件相同文件校验
     * 
     * @author: stone
     * @date 2023-6-6 14:40:51
     * @param file
     */
    private void validSameFile(File file) {
        int count = jTabbedPane5.getTabCount();
        if (count == 0) {
            return;
        }
        for (int i = 0; i < count; i++) {
            TextAreaTab tabPanel = (TextAreaTab) jTabbedPane5.getComponent(i);
            String filePath = tabPanel.getFilePath();
            if (filePath != null && filePath.equals(file.getAbsolutePath())) {
                jTabbedPane5.setSelectedIndex(i);
                throw new NotepadException();
            }
        }
    }

    /**
     * 打开文件
     * 
     * @author: stone
     * @date 2023-6-6 14:41:22
     * @return
     */
    private File openFile() {
        JFileChooser fileChooser = new JFileChooser();
        //fileChooser.setFileFilter(new FileNameExtensionFilter("文本文件", "txt"));
        int returnValue = fileChooser.showOpenDialog(getContentPane());
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            return file;
        }
        return null;
    }

    /**
     * 保存文件
     * 
     * @author: stone
     * @date 2023-6-6 14:41:32
     * @param tabPanel
     * @param index
     */
    private void saveFile(TextAreaTab tabPanel, int index) {
        if (tabPanel == null || !tabPanel.isChanged()) {
            return;
        }
        String path = tabPanel.getFilePath();
        String content = tabPanel.getjTextArea1().getText();
        File file = null;
        if (path == null) {
            FileDialog save = new FileDialog(this, "保存文件对话框", FileDialog.SAVE);
            save.setVisible(true);
            if (save.getFile() != null) {
                file = new File(save.getDirectory(), save.getFile());
                jTabbedPane5.setTitleAt(index, file.getName());
            } else {
                return;
            }
        } else {
            file = new File(path);
        }
        CommonUtil.writeStringToFile(file, content, tabPanel.getCharset());
        tabPanel.setFilePath(file.getAbsolutePath());
        tabPanel.setChanged(false);
    }

    /**
     * 编码切换
     * 
     * @author: stone
     * @date 2023-6-6 14:42:22
     * @param charset
     */
    private void charsetChange(String charset) {
        TextAreaTab tabPanel = getSelectedTab();
        String oldCharset = tabPanel.getCharset();
        tabPanel.setCharset(charset);
        TextAreaEdit textArea = tabPanel.getjTextArea1();
        String oldText = textArea.getText();
        byte[] data = tabPanel.getData();
        try {
            if (data == null) {
                data = oldText.getBytes(oldCharset);
                tabPanel.setData(data);
            }
            String text = new String(data, charset);
            textArea.setText(text);
        } catch (UnsupportedEncodingException e) {
            CommonUtil.log(e);
        }
    }

    /**
     * 当前使用编码显示
     * 
     * @author: stone
     * @date 2023-6-6 14:42:34
     * @param evt
     * @param menu
     */
    private void charsetShowUse(MenuEvent evt, JMenu menu) {
        TextAreaTab tabPanel = getSelectedTab();
        String oldCharset = tabPanel.getCharset();
        int count = menu.getItemCount();
        for (int i = 0; i < count; i++) {
            JMenuItem item = menu.getItem(i);
            String text = item.getText();
            int idx = text.indexOf("(");
            if (idx != -1) {
                text = text.substring(0, idx);
                if (oldCharset.equals(text)) {
                    return;
                } else {
                    item.setText(text);
                    continue;
                }
            }
            if (oldCharset.equals(text)) {
                item.setText(text + "(current use)");
            }
        }
    }

    /**
     * 初始化菜单
     * 
     * @author: stone
     * @date 2023-6-6 14:42:55
     */
    private void initMenu() {
        CommonMenuEnum.check();
        addMenu(CommonMenuEnum.file, CommonMenuEnum.addNew, CommonMenuEnum.open, CommonMenuEnum.save, CommonMenuEnum.saveAs, CommonMenuEnum.saveAll, CommonMenuEnum.reload, CommonMenuEnum.exit);
        addMenu(CommonMenuEnum.edit, CommonMenuEnum.copy, CommonMenuEnum.paste, CommonMenuEnum.cut, CommonMenuEnum.delete, CommonMenuEnum.undo, CommonMenuEnum.redo, CommonMenuEnum.selectAll);
        addMenu(CommonMenuEnum.search, CommonMenuEnum.jumpToLine, CommonMenuEnum.find_Replace);
        SMenu menuEncoding = addMenu(CommonMenuEnum.encoding);
        addInUseShow(menuEncoding);
        initEncodingMenu(menuEncoding);

        addMenu(CommonMenuEnum.settings, CommonMenuEnum.options);
        addMenu(CommonMenuEnum.tools_, CommonMenuEnum.wordCount, CommonMenuEnum.toUpperCase, CommonMenuEnum.toLowerCase, CommonMenuEnum.md5, CommonMenuEnum.base64Encode, CommonMenuEnum.base64Decode, CommonMenuEnum.unicodeTo, CommonMenuEnum.toUnicode);

        SMenu menuPlugins = addMenu(CommonMenuEnum.plugins);
        addPluginsMenu(menuPlugins);
        addMenuItem(menuPlugins, CommonMenuEnum.plugins_manage, CommonMenuEnum.plugins_readme);

        addMenu(CommonMenuEnum.help, CommonMenuEnum.about);
    }

    /**
     * 显示使用编码事件绑定
     * 
     * @author: stone
     * @date 2023-6-6 14:43:35
     * @param menu
     */
    private void addInUseShow(JMenu menu) {
        menu.addMenuListener(new MenuListener() {
            @Override
            public void menuSelected(MenuEvent e) {
                IException i = () -> {
                    charsetShowUse(e, menu);
                };
                i.exec();
            }

            @Override
            public void menuDeselected(MenuEvent e) {
                //
            }

            @Override
            public void menuCanceled(MenuEvent e) {
                //
            }
        });
    }

    /**
     * 添加菜单
     * 
     * @author: stone
     * @date 2023-6-6 14:44:02
     * @param items
     * @return
     */
    private SMenu addMenu(CommonMenuEnum... items) {
        if (items == null || items.length == 0) {
            return null;
        }
        int len = items.length;
        SMenu menu = new SMenu(items[0].getMethod());
        if (len > 1) {
            for (int i = 1; i < len; i++) {
                SMenuItem menuItem = new SMenuItem(this, items[i].getMethod());
                menu.add(menuItem);
            }
        }
        jMenuBar1.add(menu);
        return menu;
    }

    /**
     * 添加菜单项
     * 
     * @author: stone
     * @date 2023-6-6 14:44:10
     * @param menu
     * @param items
     */
    private void addMenuItem(SMenu menu, CommonMenuEnum... items) {
        if (items == null || items.length == 0) {
            return;
        }
        for (CommonMenuEnum item : items) {
            SMenuItem jMenuItem = new SMenuItem(this, item.getMethod());
            menu.add(jMenuItem);
        }
    }

    /**
     * 添加编码菜单的菜单项
     * 
     * @author: stone
     * @date 2023-6-6 14:44:32
     * @param menu
     */
    private void initEncodingMenu(SMenu menu) {
        for (CommonEncodingEnum e : CommonEncodingEnum.values()) {
            JMenuItem item = new JMenuItem();
            CommonUtil.menuAndToolbarAttach.add(item);
            item.setText(e.getCharset());
            menu.add(item);
            item.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    String str = item.getText();
                    IException i = () -> {
                        charsetChange(str);
                    };
                    i.exec();
                }
            });
        }
    }

    /**
     * 根据菜单生成工具栏
     * 
     * @author: stone
     * @date 2023-6-6 14:45:09
     */
    private void initToolbarAuto() {
        for (CommonMenuEnum item : CommonMenuEnum.values()) {
            if (item.getType() == 0) {
                SButton button = new SButton(this, item.getMethod());
                jToolBar1.add(button);
                if (CommonUtil.showToolNames.contains(item.getMethod())) {
                    jToolBar1.setVisible(true);
                } else {
                    jToolBar1.setVisible(false);
                }
            }
        }
        reloadToolbarButtons();
        hideTools(CommonUtil.config.isHideTools());
    }

    //----手动添加工具栏目前未使用
    public void initToolbarManual() {
        addToolbarButton(CommonMenuEnum.addNew, CommonMenuEnum.open, CommonMenuEnum.save, CommonMenuEnum.saveAll);
        addToolbarSeparator();
        addToolbarButton(CommonMenuEnum.undo, CommonMenuEnum.redo);
        addToolbarSeparator();
        addToolbarButton(CommonMenuEnum.find_Replace);
        addToolbarSeparator();
        addToolbarButton(CommonMenuEnum.enlarge, CommonMenuEnum.reduce);
        reloadToolbarButtons();
    }

    private void addToolbarButton(CommonMenuEnum... items) {
        for (CommonMenuEnum item : items) {
            if (item.getType() == 0) {
                SButton button = new SButton(this, item.getMethod());
                jToolBar1.add(button);
                if (CommonUtil.showToolNames.contains(item.getMethod())) {
                    button.setVisible(true);
                } else {
                    button.setVisible(false);
                }
            }
        }
    }

    private void addToolbarSeparator() {
        jToolBar1.add(new JToolBar.Separator());
    }
    //----手动添加工具栏目前未使用 end---

    /**
     * 获取当前选项卡
     * 
     * @author: stone
     * @date 2023-6-6 14:51:20
     * @return
     */
    private TextAreaTab getSelectedTab() {
        TextAreaTab tab = (TextAreaTab) jTabbedPane5.getSelectedComponent();
        if (tab == null) {
            throw new NotepadException();
        }
        return tab;
    }

    /**
     * 获取当前选项卡仅打开文件或保存过的
     * 
     * @author: stone
     * @date 2023-6-6 14:51:45
     * @return
     */
    private TextAreaTab getSelectedFileTab() {
        TextAreaTab tab = (TextAreaTab) jTabbedPane5.getSelectedComponent();
        if (tab == null || tab.getFilePath() == null) {
            throw new NotepadException();
        }
        return tab;
    }

    /**
     * 绑定拖动事件
     * 
     * @author: stone
     * @date 2023-6-6 14:54:34
     */
    private void bindDrag() {
        new DropTarget(this, DnDConstants.ACTION_COPY_OR_MOVE, new DropTargetAdapter() {
            @Override
            public void drop(DropTargetDropEvent dtde) {
                dragAction(dtde);
            };
        });
    }

    /**
     * 拖动事件响应
     * 
     * @author: stone
     * @date 2023-6-6 14:56:45
     * @param dtde
     */
    public void dragAction(DropTargetDropEvent dtde) {
        IException i = () -> {
            if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
                Transferable tr = dtde.getTransferable();
                Object obj;
                try {
                    obj = tr.getTransferData(DataFlavor.javaFileListFlavor);
                } catch (UnsupportedFlavorException | IOException e) {
                    throw new RuntimeException(e);
                }
                @SuppressWarnings("unchecked") List<File> list = (List<File>) (obj);
                for (File file : list) {
                    openFile4Drag(file);
                    dtde.dropComplete(true);
                }
            } else {
                dtde.rejectDrop();
            }

        };
        i.exec();
    }

    /**
     * 定时器
     * 
     * @author: stone
     * @date 2023-6-6 14:57:03
     */
    private void initTimer() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                autoFileCheck();
            }
        }, 1000, 1000);
    }

    /**
     * 自动文件检测及自动跳转到文件尾
     * 
     * @author: stone
     * @date 2023-6-6 14:15:23
     */
    private void autoFileCheck() {
        if (CommonUtil.config.isAutoReload()) {
            reload();
            if (CommonUtil.config.isAutoTail()) {
                tail();
            }
        }
    }

    /**
     * 主界面字体重新加载
     * 
     * @author: stone
     * @date 2023-6-6 14:57:11
     */
    public void reloadFont() {
        jTabbedPane5.setFont(CommonUtil.getFont());
        CommonUtil.reloadMenuFont();
    }

    //--以下是菜单方法-------------------------------------
    //--必须是public方法并且名称与CommonMenuEnum定义的method相同---
    public void addNew() {
        addTab(false);
    }

    public void open() {
        addTab(true);
    }

    public void save() {
        TextAreaTab tabPanel = getSelectedTab();
        int index = jTabbedPane5.getSelectedIndex();
        saveFile(tabPanel, index);
    }

    public void saveAs() {
        TextAreaTab tabPanel = getSelectedTab();
        int index = jTabbedPane5.getSelectedIndex();
        String path = tabPanel.getFilePath();
        if (path == null) {
            saveFile(tabPanel, index);
            return;
        } else {
            String content = tabPanel.getjTextArea1().getText();
            FileDialog save = new FileDialog(this, "保存文件对话框", FileDialog.SAVE);
            save.setVisible(true);
            if (save.getFile() != null) {
                File file = new File(save.getDirectory(), save.getFile());
                CommonUtil.writeStringToFile(file, content, tabPanel.getCharset());
            }
        }
    }

    public void saveAll() {
        int count = jTabbedPane5.getTabCount();
        for (int i = 0; i < count; i++) {
            TextAreaTab tabPanel = (TextAreaTab) jTabbedPane5.getComponent(i);
            saveFile(tabPanel, i);
        }
    }

    public void reload() {
        IException i = () -> {
            TextAreaTab tab = getSelectedFileTab();
            String title = CommonUtil.getShowText(CommonLangEnum.reload);
            String hint = CommonUtil.getShowText(CommonLangEnum.hintReload);
            int position = tab.getjTextArea1().getCaretPosition();
            if (tab.isHasWarn()) {
                CommonUtil.openFile(new File(tab.getFilePath()), tab.getjTextArea1(), tab.getCharset());
            } else {
                tab.setHasWarn(true);
                int result = JOptionPane.showConfirmDialog(null, hint, title, JOptionPane.YES_NO_OPTION);
                if (result == 0) {
                    CommonUtil.openFile(new File(tab.getFilePath()), tab.getjTextArea1(), tab.getCharset());
                }
            }
            tab.getjTextArea1().setCaretPosition(position);
        };
        i.exec();
    }

    public void tail() {
        IException i = () -> {
            TextAreaTab tab = getSelectedFileTab();
            TextAreaEdit textArea = tab.getjTextArea1();
            textArea.setCaretPosition(textArea.getText().length());
        };
        i.exec();
    }

    public void exit() {
        System.exit(0);
    }

    public void copy() {
        IException i = () -> {
            String str = getSelectedTextArea().getSelectedText();
            if (CommonUtil.isEmpty(str)) {
                return;
            }
            Clipboard clipboard = this.getToolkit().getSystemClipboard();
            clipboard.setContents(new StringSelection(str), null);
        };
        i.exec();
    }

    public void paste() {
        IException i = () -> {
            Clipboard clipboard = this.getToolkit().getSystemClipboard();
            Transferable content = clipboard.getContents(this);
            String result = "";
            try {
                if (content.getTransferData(DataFlavor.stringFlavor) instanceof String) {
                    result = (String) content.getTransferData(DataFlavor.stringFlavor);
                }
            } catch (Exception e) {
                CommonUtil.log(e);
            }
            insertText4Paste(result);
        };
        i.exec();
    }

    public void cut() {
        IException i = () -> {
            TextAreaEdit textArea = getSelectedTextArea();
            String str = textArea.getSelectedText();
            if (CommonUtil.isEmpty(str)) {
                return;
            }
            Clipboard clipboard = this.getToolkit().getSystemClipboard();
            clipboard.setContents(new StringSelection(str), null);
            textArea.replaceSelection("");
        };
        i.exec();
    }

    public void delete() {
        IException i = () -> {
            TextAreaEdit textArea = getSelectedTextArea();
            String str = textArea.getSelectedText();
            if (CommonUtil.isEmpty(str)) {
                return;
            }
            textArea.replaceSelection("");
        };
        i.exec();
    }

    public void undo() {
        IException i = () -> {
            TextAreaTab tab = getSelectedTab();
            TextAreaEdit textArea = tab.getjTextArea1();
            if (textArea.getUndoManager().canUndo()) {
                textArea.getUndoManager().undo();
            }
        };
        i.exec();
    }

    public void redo() {
        IException i = () -> {
            TextAreaTab tab = getSelectedTab();
            TextAreaEdit textArea = tab.getjTextArea1();
            if (textArea.getUndoManager().canRedo()) {
                textArea.getUndoManager().redo();
            }
        };
        i.exec();
    }

    public void selectAll() {
        IException i = () -> {
            TextAreaEdit textArea = getSelectedTextArea();
            textArea.selectAll();
        };
        i.exec();
    }

    public void jumpToLine() {
        IException i = () -> {
            TextAreaEdit textArea = getSelectedTextArea();
            new DialogJumpLine(this, textArea);
        };
        i.exec();
    }

    public void find_Replace() {
        IException i = () -> {
            TextAreaEdit textArea = getSelectedTextArea();
            new DialogFindReplace(this, textArea);
        };
        i.exec();
    }

    public void options() {
        new DialogSettings(this, CommonUtil.getShowText(CommonLangEnum.settings), true);
    }

    public void wordCount() {
        IException i = () -> {
            TextAreaEdit textArea = getSelectedTextArea();
            String text = textArea.getSelectedText();
            if (text == null) {
                text = textArea.getText();
            }
            String result = CommonUtil.getShowText(CommonLangEnum.wordNumber) + ": " + text.length();
            CommonUtil.showWarning(CommonUtil.getShowText(CommonLangEnum.wordCount), result);
        };
        i.exec();
    }

    public void toUpperCase() {
        IException i = () -> {
            String param = getSelectedText();
            String result = param.toUpperCase();
            replaceSelectedText(result);
        };
        i.exec();
    }

    public void toLowerCase() {
        IException i = () -> {
            String param = getSelectedText();
            String result = param.toLowerCase();
            replaceSelectedText(result);
        };
        i.exec();
    }

    public void md5() {
        IException i = () -> {
            String param = getSelectedText();
            String result = CommonUtil.md5(param, getSelectedTextArea().getCharset());
            replaceSelectedText(result);
        };
        i.exec();
    }

    public void base64Encode() {
        IException i = () -> {
            String param = getSelectedText();
            String result = CommonUtil.base64Encode(param, getSelectedTextArea().getCharset());
            replaceSelectedText(result);
        };
        i.exec();
    }

    public void base64Decode() {
        IException i = () -> {
            String param = getSelectedText();
            String result = CommonUtil.base64Decode(param, getSelectedTextArea().getCharset());
            replaceSelectedText(result);
        };
        i.exec();
    }

    public void unicodeTo() {
        IException i = () -> {
            String param = getSelectedText();
            String result = CommonUtil.unicodeToWord(param);
            replaceSelectedText(result);
        };
        i.exec();

    }

    public void toUnicode() {
        IException i = () -> {
            String param = getSelectedText();
            String result = CommonUtil.wordToUnicode(param);
            replaceSelectedText(result);
        };
        i.exec();
    }

    public void plugins_manage() {
        File file = new File(CommonUtil.getPluginDir());
        if (!file.exists()) {
            file.mkdirs();
        }
        if (!file.isDirectory()) {
            //
        }
        try {
            java.awt.Desktop.getDesktop().open(file);
        } catch (IOException e) {
            CommonUtil.log(e);
        }
    }

    public void plugins_readme() {
        new DialogPluginsReadme(this, CommonUtil.getShowText(CommonLangEnum.plugins_readme), true);
    }

    public void about() {
        new DialogAbout(this, CommonUtil.getShowText(CommonLangEnum.notepadName), true);
    }

    public void enlarge() {
        IException i = () -> {
            TextAreaTab tabPanel = getSelectedTab();
            TextAreaEdit textArea = tabPanel.getjTextArea1();
            Font f = textArea.getFont();
            int size = textArea.getFont().getSize() + 1;
            textArea.setFont(new Font(f.getName(), f.getStyle(), size));
        };
        i.exec();
    }

    public void reduce() {
        IException i = () -> {
            TextAreaTab tabPanel = getSelectedTab();
            TextAreaEdit textArea = tabPanel.getjTextArea1();
            Font f = textArea.getFont();
            int size = textArea.getFont().getSize() - 1;
            textArea.setFont(new Font(f.getName(), f.getStyle(), size));
        };
        i.exec();
    }

}
