package com.windfindtech.icommon.dialog.callback;

/**
 * 对话框回调接口
 *
 * @author cplu
 */
public interface ChoiceCallback extends BaseCallback{
	/**
     * choice dialog result callback
     * @param id            id of dialog
     * @param success       chosen or not
     * @param choice        the chosen one
     */
    void onChoiceResult(long id, boolean success, int choice);
}
