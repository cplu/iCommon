package com.windfindtech.icommon.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.windfindtech.icommon.R;
import com.windfindtech.icommon.dialog.callback.BtnNCallback;

/**
 * Created by cplu on 2015/10/21.
 */
public class LoginFailedDialog extends BaseDialog {
	private BtnNCallback<String> m_callback;
	public static final String TAG = "LoginFailedDialog";
//	private final BtnNCallback<String> m_callback;

	public static LoginFailedDialog create(long id, String title, FragmentManager manager) {
		LoginFailedDialog dialog = new LoginFailedDialog();
		Bundle bundle = dialog.getBaseBundle(id);
		dialog.setArguments(bundle);
		showDialog(manager, dialog, TAG);
		return dialog;
	}

	public LoginFailedDialog() {

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setStyle(STYLE_NO_TITLE, R.style.dialog_notitlebar_transparent);
		m_callback = (BtnNCallback<String>) getCallback();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
	                         @Nullable Bundle savedInstanceState) {
		m_root = super.onCreateView(inflater, container, savedInstanceState);
		Button btn_retry = (Button) m_root.findViewById(R.id.btn_retry);
		btn_retry.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
				m_callback.onNthClicked(m_uniqueID, 0, "retry");
			}
		});
		Button btn_change_password = (Button) m_root.findViewById(R.id.btn_change_password);
		btn_change_password.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
				m_callback.onNthClicked(m_uniqueID, 1, "change_password");
			}
		});

		return m_root;
	}

	@Override
	protected int getLayoutId() {
		return R.layout.dialog_login_failed;
	}
}
