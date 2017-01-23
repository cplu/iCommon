package com.windfindtech.icommon.dialog.callback;

/**
 * 对话框回调接口
 *
 * @author cplu
 */
public interface ConfirmCallback extends BaseCallback{
    /**
     * ok button clicked
     * @param id
     * @param isOK      true if OK button is clicked, false if cancel button is clicked
     */
    void onConfirmResult(long id, boolean isOK);
}
