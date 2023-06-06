package com.stone.win.consumer;

import com.stone.win.CommonUtil;
import com.stone.win.NotepadException;

/**
 * 切面保存配置
 * 
 * @author: stone
 * @date 2023-6-6 13:44:59
 */
public interface ISaveConfig {
    default void exec() {
        try {
            zfunction();
            CommonUtil.saveConfig();
        } catch (NotepadException e) {
            return;
        } catch (Exception e) {
            CommonUtil.log(e);
        }
    }

    void zfunction();
}
