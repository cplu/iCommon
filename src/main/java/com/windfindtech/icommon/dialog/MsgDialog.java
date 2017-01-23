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
import com.windfindtech.icommon.dialog.callback.MsgCallback;


/**
 * 确认框，仅包含确认/取消按钮
 *
 * @author cplu
 */
public class MsgDialog extends BaseDialog {
	private static final String MESSAGE = "message";
	private String m_message;
	private MsgCallback m_callback;

	public static MsgDialog create(long id, String message, FragmentManager manager) {
		MsgDialog dialog = new MsgDialog();
		Bundle bundle = dialog.getBaseBundle(id);
		bundle.putString(MESSAGE, message);
		dialog.setArguments(bundle);
		showDialog(manager, dialog, "MsgDialog");
		return dialog;
	}

	/**
	 *
	 */
	public MsgDialog() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setStyle(STYLE_NO_TITLE, R.style.dialog_notitlebar_transparent);
		Bundle bundle = getArguments();
		m_message = bundle.getString(MESSAGE);
		m_callback = (MsgCallback) getCallback();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
	                         @Nullable Bundle savedInstanceState) {
		m_root = super.onCreateView(inflater, container, savedInstanceState);

		Button accept_btn = (Button) m_root.findViewById(R.id.btn_accept);
		accept_btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
				m_callback.onAccept(m_uniqueID);
			}
		});

		TextView msgTxt = (TextView) m_root.findViewById(R.id.dlg_message);
		msgTxt.setText(m_message);


		return m_root;
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
//		m_callback.onConfirmResult(m_uniqueID, m_positive);
		super.onDismiss(dialog);
	}

	@Override
	protected int getLayoutId() {
		return R.layout.dialog_alert;
	}
}
