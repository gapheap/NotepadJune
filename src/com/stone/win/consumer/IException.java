package com.stone.win.consumer;

import com.stone.win.CommonUtil;
import com.stone.win.NotepadException;

/**
 * 切面异常处理
 * 
 * @author: stone
 * @date 2023-6-6 13:44:55
 */
public interface IException {

    default void exec() {
        try {
            zfunction();
        } catch (NotepadException e) {
            return;
        } catch (Exception e) {
            CommonUtil.log(e);
        }
    }

    void zfunction();

}
