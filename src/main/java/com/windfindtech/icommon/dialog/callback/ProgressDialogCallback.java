package com.windfindtech.icommon.dialog.callback;

import com.windfindtech.icommon.dialog.callback.BaseCallback;

/**
 * 对话框回调接口
 *
 * @author cplu
 */
public interface ProgressDialogCallback extends BaseCallback{
    /**
     * cancel button clicked
     * @param id        dialog unique id
     * @param fallback  description
     */
    void onProgressDlgCancelled(long id, String fallback);
}
