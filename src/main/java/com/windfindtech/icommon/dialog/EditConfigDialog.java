package com.windfindtech.icommon.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.windfindtech.icommon.R;
import com.windfindtech.icommon.dialog.callback.EditCallback;
import com.windfindtech.icommon.iCommon;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 简单编辑框，含一个输入框和确认/取消按钮
 *
 * @author cplu
 */
public class EditConfigDialog extends BaseDialog {
	private static final String TITLE = "title";
	private static final String OLD_VALUE = "old_value";
	private static final String VALID_REG = "valid_reg";
	private static final String INVALID_HINT_ID = "invalid_hint_id";
	private static final String INPUT_TYPE = "input_type";
	private String m_header;
	private EditText m_editText;
	private String m_fallbackValue;
	private String m_validReg;
	private int m_invalidInputHintID;
	private int m_inputType = 0;
	private EditCallback<String> m_callback;

	/**
	 *
	 * @param id                unique id for dialog, use for distinguishing dialogs in the same fragment
	 * @param title
	 * @param old_value
	 * @param valid_reg         输入框接受的输入表达式
	 * @param invalid_hint_id
	 * @param input_type
	 * @param manager
	 * @return  dialog unique id through app
	 */
	public static EditConfigDialog create(long id, String title, String old_value, String valid_reg, int invalid_hint_id, int input_type
		, FragmentManager manager) {
		EditConfigDialog dialog = new EditConfigDialog();
		Bundle bundle = dialog.getBaseBundle(id);
		bundle.putString(TITLE, title);
		bundle.putString(OLD_VALUE, old_value);
		bundle.putString(VALID_REG, valid_reg);
		bundle.putInt(INVALID_HINT_ID, invalid_hint_id);
		if(input_type > 0) {
			bundle.putInt(INPUT_TYPE, input_type);
		}
		dialog.setArguments(bundle);
		showDialog(manager, dialog, "EditConfigDialog");
		return dialog;
	}

	public static EditConfigDialog create(long id, String title, String old_value, String valid_reg, int invalid_hint_id
		, FragmentManager manager) {
		return create(id, title, old_value, valid_reg, invalid_hint_id, 0, manager);
	}

	/**
	 *
	 */
	public EditConfigDialog() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setStyle(STYLE_NO_TITLE, R.style.dialog_notitlebar_transparent);
		Bundle bundle = getArguments();
		m_header = bundle.getString(TITLE);
		m_fallbackValue = bundle.getString(OLD_VALUE);
		m_validReg = bundle.getString(VALID_REG);
		m_invalidInputHintID = bundle.getInt(INVALID_HINT_ID);
		m_inputType = bundle.getInt(INPUT_TYPE, 0);
		m_callback = (EditCallback)getCallback();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
	                         @Nullable Bundle savedInstanceState) {
		m_root = super.onCreateView(inflater, container, savedInstanceState);

		TextView headerTxt = (TextView) m_root.findViewById(R.id.dlg_header_hint);
		headerTxt.setText(m_header);

		m_editText = (EditText) m_root.findViewById(R.id.dialog_edit_text);
		if (m_inputType != 0) {
			m_editText.setInputType(m_inputType);
		}
		m_editText.post(new Runnable() {
			@Override
			public void run() {
				InputMethodManager inputManager = (InputMethodManager)
					getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
				inputManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
			}
		});
		m_editText.setText(m_fallbackValue);
		Button positive_btn = (Button) m_root.findViewById(R.id.btn_positive);
		positive_btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String value = m_editText.getText().toString();
				if (value.equals(m_fallbackValue)) {
					dismiss();
					/// value not changed
					m_callback.onEditResult(m_uniqueID, false, m_fallbackValue);
					return;
				}
				Pattern pattern = Pattern.compile(m_validReg);
				Matcher matcher = pattern.matcher(value);
				if (matcher.matches()) {
					dismiss();
					m_callback.onEditResult(m_uniqueID, true, value);
				} else {
					iCommon.showToast(EditConfigDialog.this.getContext(), m_invalidInputHintID);
				}
			}
		});
		Button negative_btn = (Button) m_root.findViewById(R.id.btn_negative);
		negative_btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
				m_callback.onEditResult(m_uniqueID, false, m_fallbackValue);
			}
		});

		return m_root;
	}

	@Override
	protected int getLayoutId() {
		return R.layout.dialog_edit;
	}

//	public void onDismiss(DialogInterface dialog) {
//		FragmentTransaction transaction = getFragmentManager().beginTransaction();
//		transaction.remove(m_callback);
//		transaction.commit();
//		super.onDismiss(dialog);
//	}
}
