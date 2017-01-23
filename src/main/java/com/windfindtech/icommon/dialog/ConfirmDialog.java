package com.windfindtech.icommon.dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.windfindtech.icommon.R;
import com.windfindtech.icommon.R2;
import com.windfindtech.icommon.dialog.callback.ConfirmCallback;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * 确认框，仅包含确认/取消按钮
 *
 * @author cplu
 */
public class ConfirmDialog extends BaseDialog {
	private static final String TITLE = "title";
	//    private ConfirmCallback m_callback;
	private String m_header;
	private ConfirmCallback m_callback;
	@BindView(R2.id.btn_positive)
	Button m_positiveBtn;
	@BindView(R2.id.btn_negative)
	Button m_negativeBtn;
	@BindView(R2.id.dlg_header_hint)
	TextView m_headerTxt;
//	private boolean m_positive = false;
//    private boolean m_bPositive = false;

	public static ConfirmDialog create(long id, String title, FragmentManager manager) {
		ConfirmDialog dialog = new ConfirmDialog();
		Bundle bundle = dialog.getBaseBundle(id);
		bundle.putString(TITLE, title);
		dialog.setArguments(bundle);
		showDialog(manager, dialog, "ConfirmDialog");
		return dialog;
	}

	/**
	 *
	 */
	public ConfirmDialog() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setStyle(STYLE_NO_TITLE, R.style.dialog_notitlebar_transparent);
		Bundle bundle = getArguments();
		m_header = bundle.getString(TITLE);
		m_callback = (ConfirmCallback) getCallback();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
	                         @Nullable Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);

		m_headerTxt.setText(m_header);
		return m_root;
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
//		m_callback.onConfirmResult(m_uniqueID, m_positive);
		super.onDismiss(dialog);
	}

	@Override
	protected int getLayoutId() {
		return R.layout.dialog_confirm;
	}

	@OnClick(R2.id.btn_positive)
	public void onPositiveClick() {
		dismiss();
		m_callback.onConfirmResult(m_uniqueID, true);
	}

	@OnClick(R2.id.btn_negative)
	public void onNegativeClick() {
		dismiss();
		m_callback.onConfirmResult(m_uniqueID, false);
	}
}
