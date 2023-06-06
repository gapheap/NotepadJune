package com.stone.win;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JTextArea;

import com.stone.win.comp.CommonMenuEnum;
import com.stone.win.comp.SButton;
import com.stone.win.comp.SMenu;
import com.stone.win.comp.SMenuItem;

/**
 * 工具类
 * 
 * @author: stone
 * @date 2023-6-6 13:42:58
 */
public class CommonUtil {
    /**
     * 是否打印日志到控制台
     */
    public static final boolean isLog = true;
    /**
     * 配置文件名称
     */
    public static final String CONFIG_FILE_NAME = "cfg.ini";
    /**
     * 菜单map
     */
    public static final Map<String, String> menuLangMap = new HashMap<>();
    /**
     * 显示的工具栏按钮集合
     */
    public static final Set<String> showToolNames = new HashSet<>();
    /**
     * 初始化生成的菜单及工具栏，可改变语言和文字大小
     */
    public static final Set<Object> menuAndToolbar = new HashSet<>();
    /**
     * 其他方式生成的菜单，不可改变语言，可改变文字大小
     */
    public static final Set<Object> menuAndToolbarAttach = new HashSet<>();

    /**
     * 插件jar包路径集合
     */
    public static final List<String> jarPathList = new ArrayList<>();
    /**
     * 未最大化
     */
    public static boolean notFullScreen = false;
    /**
     * 配置
     */
    public static CommonConfig config;
    /**
     * 主界面初始宽度
     */
    public static final int DEFAULT_WIDTH = 1080;
    /**
     * 主界面初始高度
     */
    public static final int DEFAULT_HEIGHT = 720;
    /**
     * 当前工作目录
     */
    public static final String USER_DIR = System.getProperty("user.dir");
    /**
     * 热键集合
     */
    public static final Map<CommonMenuEnum, String> hotKeyMap = new LinkedHashMap<>();
    static {
        hotKeyMap.put(CommonMenuEnum.save, "Ctrl+S");
        hotKeyMap.put(CommonMenuEnum.undo, "Ctrl+Z");
        hotKeyMap.put(CommonMenuEnum.redo, "Ctrl+Y");
        hotKeyMap.put(CommonMenuEnum.open, "Ctrl+O");
        hotKeyMap.put(CommonMenuEnum.addNew, "Ctrl+N");
        hotKeyMap.put(CommonMenuEnum.find_Replace, "Ctrl+F");
        hotKeyMap.put(CommonMenuEnum.jumpToLine, "Ctrl+G");
        hotKeyMap.put(CommonMenuEnum.exit, "Alt+F4");
        config = new CommonConfig();
        readConfig();

        String[] hideToolNames = config.getShowToolNames().split(",");
        for (String e : hideToolNames) {
            showToolNames.add(e.trim());
        }
    }

    /**
     * 准备jar包路径
     * 
     * @author: stone
     * @date 2023-6-6 14:17:28
     */
    public static void preparePluginsJarPath() {
        String path = CommonUtil.getPluginDir();
        File file = new File(path);
        if (file.exists() && file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File f : files) {
                    CommonUtil.jarPathList.add(f.getAbsolutePath());
                }
            }
        }
    }

    /**
     * 获取插件目录
     * 
     * @author: stone
     * @date 2023-6-6 14:19:11
     * @return
     */
    public static String getPluginDir() {
        return CommonUtil.USER_DIR + File.separator + "plugins";
    }

    /**
     * 根据jar包名称获取不含扩展名的名称
     * 
     * @author: stone
     * @date 2023-6-6 14:22:10
     * @param jarName
     * @return
     */
    public static String getNameExcludeExt(String jarName) {
        if (jarName.endsWith(".jar")) {
            return jarName.substring(0, jarName.length() - 4);
        }
        return "";
    }

    /**
     * 读取配置到内存
     * 
     * @author: stone
     * @date 2023-6-6 15:09:34
     */
    public static void readConfig() {
        File file = new File(CommonUtil.USER_DIR + "/" + CommonUtil.CONFIG_FILE_NAME);
        if (file.exists() && file.isFile()) {
            Map<String, String> map = CommonUtil.file2map(file);
            CommonUtil.map2Config(map);
        }
    }

    /**
     * 非菜单语言获取
     * 
     * @author: stone
     * @date 2023-6-6 15:12:26
     * @param lang
     * @return
     */
    public static String getShowText(CommonLangEnum lang) {
        return lang.getData()[config.getLang()];
    }

    /**
     * 菜单语言获取
     * 
     * @author: stone
     * @date 2023-6-6 15:12:36
     * @param data
     * @return
     */
    public static String getShowTextMenu(CommonMenuEnum data) {
        return data.getText().split(",")[config.getLang()];
    }

    /**
     * 写文件
     * 
     * @author: stone
     * @date 2023-6-6 15:12:54
     * @param file
     * @param data
     * @param encoding
     */
    public static void writeStringToFile(final File file, final String data, final String encoding) {
        OutputStream out = null;
        try {
            out = new FileOutputStream(file, false);
            out.write(data.getBytes(encoding));
            out.close();
        } catch (IOException e) {
            log(e);
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (final IOException ioe) {
                // ignore
            }
        }
    }

    /**
     * 获取行号宽度
     * 
     * @author: stone
     * @date 2023-6-6 15:13:23
     * @param str
     * @param font
     * @return
     */
    public static int stringWidth(String str, Font font) {
        FontRenderContext frc = new FontRenderContext(new AffineTransform(), true, true);
        Rectangle rec = font.getStringBounds(str, frc).getBounds();
        int i = (int) rec.getWidth();
        return i < 30 ? 30 : i;
    }

    /**
     * 获取字体
     * 
     * @author: stone
     * @date 2023-6-6 15:14:54
     * @return
     */
    public static Font getFont() {
        return new Font("宋体", Font.PLAIN, config.getFontSize());
    }

    /**
     * 重新加载菜单(含工具栏)字体
     * 
     * @author: stone
     * @date 2023-6-6 15:15:07
     */
    public static void reloadMenuFont() {
        Font font = getFont();
        Iterator<Object> it = menuAndToolbar.iterator();
        while (it.hasNext()) {
            Object tmp = it.next();
            if (tmp instanceof JMenu) {
                JMenu x = (JMenu) tmp;
                x.setFont(font);
            } else if (tmp instanceof JMenuItem) {
                JMenuItem x = (JMenuItem) tmp;
                x.setFont(font);
            } else if (tmp instanceof JButton) {
                JButton x = (JButton) tmp;
                x.setFont(font);
            }
        }
        Iterator<Object> it2 = menuAndToolbarAttach.iterator();
        while (it2.hasNext()) {
            Object tmp = it2.next();
            if (tmp instanceof JMenu) {
                JMenu x = (JMenu) tmp;
                x.setFont(font);
            } else if (tmp instanceof JMenuItem) {
                JMenuItem x = (JMenuItem) tmp;
                x.setFont(font);
            }
        }
    }

    /**
     * 根据名称获取语言
     * 
     * @author: stone
     * @date 2023-6-6 15:17:47
     * @param map
     * @param name
     * @return
     */
    private static String getShowText(Map<String, String> map, String name) {
        return map.get(name).split(",")[CommonUtil.config.getLang()];
    }

    /**
     * 重新渲染菜单(含工具栏)语言
     * 
     * @author: stone
     * @date 2023-6-6 15:17:02
     */
    public static void reloadMenuText() {
        Iterator<Object> it = menuAndToolbar.iterator();
        while (it.hasNext()) {
            Object tmp = it.next();
            if (tmp instanceof SMenu) {
                SMenu x = (SMenu) tmp;
                x.setText(getShowText(menuLangMap, x.getName()));
            } else if (tmp instanceof JMenuItem) {
                SMenuItem x = (SMenuItem) tmp;
                x.setText(getShowText(menuLangMap, x.getName()));
            } else if (tmp instanceof JButton) {
                SButton x = (SButton) tmp;
                x.setText(getShowText(menuLangMap, x.getName()));
            }
        }
    }

    /**
     * 打开文件
     * 
     * @author: stone
     * @date 2023-6-6 15:18:11
     * @param file
     * @param jTextArea1
     * @param charset
     */
    public static void openFile(File file, JTextArea jTextArea1, String charset) {
        FileInputStream fis = null;
        InputStreamReader reader = null;
        BufferedReader in = null;
        try {
            fis = new FileInputStream(file);
            reader = new InputStreamReader(fis, charset);
            in = new BufferedReader(reader);
            String info = in.readLine();
            jTextArea1.setText("");
            while (info != null) {
                jTextArea1.append(info + "\n");
                info = in.readLine();
            }
        } catch (Exception e) {
            log(e);
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException ex) {
                    //do nothing
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ex) {
                    //do nothing
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ex) {
                    //do nothing
                }
            }
        }
    }

    /**
     * 设置文本编辑区域行列属性
     * 
     * @author: stone
     * @date 2023-6-6 15:18:28
     * @param jTextArea1
     */
    public static void setRowsAndColumns(JTextArea jTextArea1) {
        if (notFullScreen) {
            jTextArea1.setColumns(125);
            jTextArea1.setRows(28);
        } else {
            jTextArea1.setColumns(230);
            jTextArea1.setRows(52);
        }
    }

    /**
     * base64编码
     * 
     * @author: stone
     * @date 2023-6-6 15:18:43
     * @param param
     * @param charset
     * @return
     */
    public static String base64Encode(String param, String charset) {
        if (param == null || param.trim().length() == 0) {
            return null;
        }
        try {
            return new String(Base64.getEncoder().encode(param.getBytes(charset)), charset);
        } catch (UnsupportedEncodingException e) {
            log(e);
        }
        return null;
    }

    /**
     * base64解码
     * 
     * @author: stone
     * @date 2023-6-6 15:18:54
     * @param param
     * @param charset
     * @return
     */
    public static String base64Decode(String param, String charset) {
        if (param == null || param.trim().length() == 0) {
            return null;
        }
        try {
            return new String(Base64.getDecoder().decode(param), charset);
        } catch (UnsupportedEncodingException e) {
            log(e);
        }
        return null;
    }

    /**
     * 日志打印到控制台
     * 
     * @author: stone
     * @date 2023-6-6 15:19:03
     * @param e
     */
    public static void log(Exception e) {
        if (isLog) {
            e.printStackTrace();
        }
    }

    /**
     * 提醒弹窗
     * 
     * @author: stone
     * @date 2023-6-6 15:19:12
     * @param title
     * @param content
     */
    public static void showWarning(String title, String content) {
        JDialog dialog = new JDialog();
        dialog.setTitle(title);
        JLabel label = new JLabel();
        label.setFont(CommonUtil.getFont());
        label.setText(content);
        dialog.setLayout(new FlowLayout());
        dialog.add(label);
        dialog.setSize(300, 100);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    /**
     * 给菜单绑定响应方法
     * 
     * @author: stone
     * @date 2023-6-6 15:19:21
     * @param obj
     * @param methodName
     */
    public static void bindMethod(Notepad obj, String methodName) {
        try {
            Class<?> clazz = obj.getClass();
            Method method = clazz.getDeclaredMethod(methodName);
            method.invoke(obj);
        } catch (NotepadException e) {
            return;
        } catch (InvocationTargetException e) {
            Throwable exp = e.getTargetException();
            boolean b = exp != null && exp instanceof NotepadException;
            if (b) {
                return;
            }
            CommonUtil.log(e);
        } catch (Exception e) {
            CommonUtil.log(e);
        }
    }

    /**
     * 中文转unicode
     * 
     * @author: stone
     * @date 2023-6-6 15:19:42
     * @param param
     * @return
     */
    public static String wordToUnicode(String param) {
        char[] charArr = param.toCharArray();
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < charArr.length; i++) {
            result.append("\\u");
            String hex = Integer.toHexString(charArr[i]);
            if (hex.length() <= 2) {
                result.append("00");
            }
            result.append(hex);
        }
        return result.toString();
    }

    /**
     * unicode转中文
     * 
     * @author: stone
     * @date 2023-6-6 15:19:53
     * @param param
     * @return
     */
    public static String unicodeToWord(String param) {
        Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");
        Matcher matcher = pattern.matcher(param);
        char ch;
        String result = null;
        while (matcher.find()) {
            ch = (char) Integer.parseInt(matcher.group(2), 16);
            result = param.replace(matcher.group(1), ch + "");
        }
        return result;
    }

    /**
     * md5
     * 
     * @author: stone
     * @date 2023-6-6 15:20:03
     * @param param
     * @param charset
     * @return
     */
    public static String md5(String param, String charset) {
        try {
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.update(param.getBytes(charset));
            byte s[] = m.digest();
            StringBuffer result = new StringBuffer();
            for (int i = 0; i < s.length; i++) {
                result.append(Integer.toHexString((0x000000FF & s[i]) | 0xFFFFFF00).substring(6));
            }
            return result.toString();
        } catch (Exception e) {
            CommonUtil.log(e);
        }
        return null;
    }

    /**
     * 空字符串判断包括null "" " "
     * 
     * @author: stone
     * @date 2023-6-6 15:20:16
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }

    /**
     * 根据KeyCode获取常量
     * 
     * @author: stone
     * @date 2023-6-6 15:20:52
     * @param key
     * @return
     */
    public static Integer getCode(String key) {
        key = key.toUpperCase();
        Integer code = null;
        switch (key) {
            case "F1":
                code = KeyEvent.VK_F1;
                break;
            case "F2":
                code = KeyEvent.VK_F2;
                break;
            case "F3":
                code = KeyEvent.VK_F3;
                break;
            case "F4":
                code = KeyEvent.VK_F4;
                break;
            case "F5":
                code = KeyEvent.VK_F5;
                break;
            case "F6":
                code = KeyEvent.VK_F6;
                break;
            case "F7":
                code = KeyEvent.VK_F7;
                break;
            case "F8":
                code = KeyEvent.VK_F8;
                break;
            case "F9":
                code = KeyEvent.VK_F9;
                break;
            case "F10":
                code = KeyEvent.VK_F10;
                break;
            case "F11":
                code = KeyEvent.VK_F11;
                break;
            case "F12":
                code = KeyEvent.VK_F12;
                break;
            case "A":
                code = KeyEvent.VK_A;
                break;
            case "B":
                code = KeyEvent.VK_B;
                break;
            case "C":
                code = KeyEvent.VK_C;
                break;
            case "D":
                code = KeyEvent.VK_D;
                break;
            case "E":
                code = KeyEvent.VK_E;
                break;
            case "F":
                code = KeyEvent.VK_F;
                break;
            case "G":
                code = KeyEvent.VK_G;
                break;
            case "H":
                code = KeyEvent.VK_H;
                break;
            case "I":
                code = KeyEvent.VK_I;
                break;
            case "J":
                code = KeyEvent.VK_J;
                break;
            case "K":
                code = KeyEvent.VK_K;
                break;
            case "L":
                code = KeyEvent.VK_L;
                break;
            case "M":
                code = KeyEvent.VK_M;
                break;
            case "N":
                code = KeyEvent.VK_N;
                break;
            case "O":
                code = KeyEvent.VK_O;
                break;
            case "P":
                code = KeyEvent.VK_P;
                break;
            case "Q":
                code = KeyEvent.VK_Q;
                break;
            case "R":
                code = KeyEvent.VK_R;
                break;
            case "S":
                code = KeyEvent.VK_S;
                break;
            case "T":
                code = KeyEvent.VK_T;
                break;
            case "U":
                code = KeyEvent.VK_U;
                break;
            case "V":
                code = KeyEvent.VK_V;
                break;
            case "W":
                code = KeyEvent.VK_W;
                break;
            case "X":
                code = KeyEvent.VK_X;
                break;
            case "Y":
                code = KeyEvent.VK_Y;
                break;
            case "Z":
                code = KeyEvent.VK_Z;
                break;
            case "`":
                code = KeyEvent.VK_BACK_QUOTE;
                break;
            case "0":
                code = KeyEvent.VK_0;
                break;
            case "1":
                code = KeyEvent.VK_1;
                break;
            case "2":
                code = KeyEvent.VK_2;
                break;
            case "3":
                code = KeyEvent.VK_3;
                break;
            case "4":
                code = KeyEvent.VK_4;
                break;
            case "5":
                code = KeyEvent.VK_5;
                break;
            case "6":
                code = KeyEvent.VK_6;
                break;
            case "7":
                code = KeyEvent.VK_7;
                break;
            case "8":
                code = KeyEvent.VK_8;
                break;
            case "9":
                code = KeyEvent.VK_9;
                break;
            case "-":
                code = KeyEvent.VK_MINUS;
                break;
            case "=":
                code = KeyEvent.VK_EQUALS;
                break;
            case "!":
                code = KeyEvent.VK_EXCLAMATION_MARK;
                break;
            case "@":
                code = KeyEvent.VK_AT;
                break;
            case "#":
                code = KeyEvent.VK_NUMBER_SIGN;
                break;
            case "$":
                code = KeyEvent.VK_DOLLAR;
                break;
            case "^":
                code = KeyEvent.VK_CIRCUMFLEX;
                break;
            case "&":
                code = KeyEvent.VK_AMPERSAND;
                break;
            case "*":
                code = KeyEvent.VK_ASTERISK;
                break;
            case "(":
                code = KeyEvent.VK_LEFT_PARENTHESIS;
                break;
            case ")":
                code = KeyEvent.VK_RIGHT_PARENTHESIS;
                break;
            case "_":
                code = KeyEvent.VK_UNDERSCORE;
                break;
            case "+":
                code = KeyEvent.VK_PLUS;
                break;
            case "\t":
                code = KeyEvent.VK_TAB;
                break;
            case "\n":
                code = KeyEvent.VK_ENTER;
                break;
            case "[":
                code = KeyEvent.VK_OPEN_BRACKET;
                break;
            case "]":
                code = KeyEvent.VK_CLOSE_BRACKET;
                break;
            case "\\":
                code = KeyEvent.VK_BACK_SLASH;
                break;
            case ";":
                code = KeyEvent.VK_SEMICOLON;
                break;
            case ":":
                code = KeyEvent.VK_COLON;
                break;
            case "'":
                code = KeyEvent.VK_QUOTE;
                break;
            case "\"":
                code = KeyEvent.VK_QUOTEDBL;
                break;
            case ",":
                code = KeyEvent.VK_COMMA;
                break;
            case ".":
                code = KeyEvent.VK_PERIOD;
                break;
            case "/":
                code = KeyEvent.VK_SLASH;
                break;
            case " ":
                code = KeyEvent.VK_SPACE;
                break;
            case "SPACE":
                code = KeyEvent.VK_SPACE;
                break;
            case "BACK":
                code = KeyEvent.VK_BACK_SPACE;
                break;
            case "ENTER":
                code = KeyEvent.VK_ENTER;
                break;
            case "TAB":
                code = KeyEvent.VK_TAB;
                break;
            case "CTRL":
                code = KeyEvent.VK_CONTROL;
                break;
            case "ALT":
                code = KeyEvent.VK_ALT;
                break;
            case "WIN":
                code = KeyEvent.VK_OPEN_BRACKET;
                break;
            case "ESC":
                code = KeyEvent.VK_ESCAPE;
                break;
            case "DELETE":
                code = KeyEvent.VK_DELETE;
                break;
            case "UP":
                code = KeyEvent.VK_UP;
                break;
            case "DOWN":
                code = KeyEvent.VK_DOWN;
                break;
            case "LEFT":
                code = KeyEvent.VK_LEFT;
                break;
            case "RIGHT":
                code = KeyEvent.VK_RIGHT;
                break;
            case "HOME":
                code = KeyEvent.VK_HOME;
                break;
            case "END":
                code = KeyEvent.VK_END;
                break;
            case "PGUP":
                code = KeyEvent.VK_PAGE_UP;
                break;
            case "PGDN":
                code = KeyEvent.VK_PAGE_DOWN;
                break;
            case "OPTION":
                code = KeyEvent.VK_CLOSE_BRACKET;
                break;
        }
        return code;
    }

    /**
     * 封装重复字符串
     * 
     * @author: stone
     * @date 2023-6-6 15:22:04
     * @param src
     * @param n
     * @return
     */
    public static String repeatString(String src, int n) {
        final int len = src.length();
        final char[] srcArr = src.toCharArray();
        char[] resultArr = new char[n * len];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < len; j++) {
                resultArr[i * len + j] = srcArr[j];
            }
        }
        return String.valueOf(resultArr);
    }

    /**
     * 配置转字符串
     * 
     * @author: stone
     * @date 2023-6-6 15:22:33
     * @return
     */
    public static String configToString() {
        Field[] declaredFields = config.getClass().getDeclaredFields();
        StringBuffer content = new StringBuffer();
        try {
            for (Field field : declaredFields) {
                boolean b = field.isAccessible();
                field.setAccessible(true);
                String key = field.getName();
                Object valueObj;
                valueObj = field.get(config);
                if (valueObj != null && !"NULL".equals(String.valueOf(valueObj))) {
                    String value = String.valueOf(valueObj);
                    content.append(key).append("=").append(value).append("\r\n");
                }
                field.setAccessible(b);
            }
        } catch (IllegalArgumentException | IllegalAccessException e) {
            CommonUtil.log(e);
        }
        return content.toString();
    }

    /**
     * map存入配置
     * 
     * @author: stone
     * @date 2023-6-6 15:22:46
     * @param map
     */
    public static void map2Config(Map<String, String> map) {
        Field[] declaredFields = config.getClass().getDeclaredFields();
        try {
            for (Field field : declaredFields) {
                boolean b = field.isAccessible();
                field.setAccessible(true);
                String key = field.getName();
                if (map.containsKey(key)) {
                    Class<?> clazz = field.getType();
                    if (Integer.class.equals(clazz) || int.class.equals(clazz)) {
                        field.setInt(config, Integer.valueOf(map.get(key)));
                    } else if (Long.class.equals(clazz) || long.class.equals(clazz)) {
                        field.setLong(config, Long.valueOf(map.get(key)));
                    } else if (String.class.equals(clazz)) {
                        field.set(config, map.get(key));
                    } else if (Boolean.class.equals(clazz) || boolean.class.equals(clazz)) {
                        field.setBoolean(config, "true".equalsIgnoreCase(map.get(key)));
                    } else {
                        throw new RuntimeException("do not support property type");
                    }
                }
                field.setAccessible(b);
            }
        } catch (IllegalArgumentException | IllegalAccessException e) {
            CommonUtil.log(e);
        }
    }

    /**
     * 配置文件转map
     * 
     * @author: stone
     * @date 2023-6-6 15:23:02
     * @param file
     * @return
     */
    public static Map<String, String> file2map(File file) {
        FileInputStream fis = null;
        InputStreamReader reader = null;
        BufferedReader in = null;
        Map<String, String> map = new HashMap<>();
        try {
            fis = new FileInputStream(file);
            reader = new InputStreamReader(fis, CommonEncodingEnum.UTF8.getCharset());
            in = new BufferedReader(reader);
            String info = in.readLine();
            while (info != null) {
                int idx = info.indexOf("=");
                if (idx != -1) {
                    map.put(info.substring(0, idx), info.substring(idx + 1));
                    info = in.readLine();
                }
            }
        } catch (Exception e) {
            log(e);
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException ex) {
                    //do nothing
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ex) {
                    //do nothing
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ex) {
                    //do nothing
                }
            }
        }
        return map;
    }

    /**
     * 保存内存配置到文件
     * 
     * @author: stone
     * @date 2023-6-6 15:23:15
     */
    public static void saveConfig() {
        String data = configToString();
        File file = new File(CommonUtil.USER_DIR + "/" + CommonUtil.CONFIG_FILE_NAME);
        CommonUtil.writeStringToFile(file, data, CommonEncodingEnum.UTF8.getCharset());
    }

    /**
     * set转符号分割的字符串
     * 
     * @author: stone
     * @date 2023-6-6 15:23:26
     * @param set
     * @param separator
     * @return
     */
    public static String join4set(Set<String> set, final String separator) {
        if (set == null) {
            return null;
        }
        Iterator<String> iterator = set.iterator();
        if (!iterator.hasNext()) {
            return "";
        }
        final Object first = iterator.next();
        if (!iterator.hasNext()) {
            return Objects.toString(first, "");
        }

        final StringBuilder buf = new StringBuilder(256);
        if (first != null) {
            buf.append(first);
        }

        while (iterator.hasNext()) {
            if (separator != null) {
                buf.append(separator);
            }
            final Object obj = iterator.next();
            if (obj != null) {
                buf.append(obj);
            }
        }
        return buf.toString();
    }

    /**
     * 逗号分割字符串转Object对象数组
     * 
     * @author: stone
     * @date 2023-6-6 15:23:46
     * @param param
     * @return
     */
    public static Object[] string2ObjectArr(String param) {
        if (isEmpty(param)) {
            return null;
        }
        String[] stringArr = param.split(",");
        Object[] objectArr = new Object[stringArr.length];
        for (int i = 0; i < stringArr.length; i++) {
            objectArr[i] = stringArr[i].trim();
        }
        return objectArr;
    }

}
