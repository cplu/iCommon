package com.windfindtech.icommon.dialog.callback;

/**
 * 对话框回调接口
 *
 * @author cplu
 */
public interface EditCallback<Type> extends BaseCallback{
	/**
     * notify result of edit dialog
     * @param id        unique id for dialog
     * @param success
     * @param result
     */
    void onEditResult(long id, boolean success, Type result);
}
