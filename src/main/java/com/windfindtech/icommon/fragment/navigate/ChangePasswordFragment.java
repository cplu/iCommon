package com.windfindtech.icommon.fragment.navigate;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;

import com.windfindtech.icommon.R;
import com.windfindtech.icommon.R2;
import com.windfindtech.icommon.iCommon;
import com.windfindtech.icommon.jsondata.webservice.BaseResponse;
import com.windfindtech.icommon.jsondata.webservice.WSErrorResponse;
import com.windfindtech.icommon.mvp.presenter.navigate.ChangePasswordPresenter;
import com.windfindtech.icommon.mvp.view.navigate.IChangePasswordView;

import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnEditorAction;

/**
 * 个人中心-->个人信息-->修改密码
 */
public class ChangePasswordFragment extends NavigateFragment<IChangePasswordView, ChangePasswordPresenter> implements IChangePasswordView {
	private static final int DURATION_CHANGING_PASSWORD = 3000;
	//    private static final int DURATION_TOAST = 2000;
	@BindView(R2.id.current_password_edit)
	EditText m_oldPasswordEdit;
	@BindView(R2.id.new_password_edit)
	EditText m_newPasswordEdit;
	@BindView(R2.id.confirm_password_edit)
	EditText m_confirmPasswordEdit;
	@BindView(R2.id.confirm_password_btn)
	Button m_confirmBtn;

	private static final int passwordLengthMin = 4;
//	private static final int passwordLengthMax = 10;
	private static Pattern validPasswordPattern = Pattern.compile(String.format("[^\\.|/\\\\]{%d,}", passwordLengthMin));

	public ChangePasswordFragment() {
	}

	@Override
	protected ChangePasswordPresenter createPresenter() {
		return new ChangePasswordPresenter();
	}

	@Override
	protected int getLayoutID() {
		return R.layout.fragment_change_password;
	}

	@Override
	protected int getTitleID() {
		return R.string.title_change_password;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		m_confirmBtn.setEnabled(true);
	}

	@Override
	protected NavListAdapter createNavAdapter() {
		return null;
	}

	@OnEditorAction(R2.id.confirm_password_edit)
	public boolean onPasswordConfirmEditor(int actionId, KeyEvent event) {
		if (actionId == EditorInfo.IME_ACTION_DONE
		    || actionId == EditorInfo.IME_ACTION_NEXT
		    || actionId == EditorInfo.IME_ACTION_GO) {
			start_change_password();
		}
		return false;
	}

	@OnClick(R2.id.confirm_password_btn)
	public void onPasswordConfirmBtn() {
		start_change_password();
	}

	@Override
	public void onResume() {
		super.onResume();
		m_oldPasswordEdit.setText("");
		m_newPasswordEdit.setText("");
		m_confirmPasswordEdit.setText("");
	}

	private void start_change_password() {
		String old_pw = m_oldPasswordEdit.getText().toString();
		final String new_pw = m_newPasswordEdit.getText().toString();
		String confirm_pw = m_confirmPasswordEdit.getText().toString();
		if (old_pw.isEmpty()
		    || new_pw.isEmpty()
		    || confirm_pw.isEmpty()) {
			m_primaryCallback.onShowBottomToast(getString(R.string.password_empty_hint), 0);
			return;
		}
		if (!validPasswordPattern.matcher(new_pw).matches()) {
			iCommon.showToast(getActivity(), String.format(getString(R.string.change_password_invalid), passwordLengthMin));
			return;
		}
		if (new_pw.equals(confirm_pw)) {
			m_confirmBtn.setEnabled(false);
			m_oldPasswordEdit.setEnabled(false);
			m_newPasswordEdit.setEnabled(false);
			m_confirmPasswordEdit.setEnabled(false);
			m_primaryCallback.onShowBottomToast(getString(R.string.changing_password), DURATION_CHANGING_PASSWORD);
			m_presenter.changePassword(old_pw, new_pw);

		} else {
			iCommon.showToast(getActivity(), R.string.password_not_match);
			return;
		}
	}

	@Override
	public void onChangePasswordSuccess(BaseResponse ret) {
		if (isStarted) {
			m_primaryCallback.onShowBottomToast(getString(R.string.password_change_success), 0);
//                                    m_userInterface.onFragmentNaviBack(false);
			/// clearAll input text for all EditText
			m_oldPasswordEdit.setText("");
			m_newPasswordEdit.setText("");
			m_confirmPasswordEdit.setText("");
			/// enable all ui controls
			m_confirmBtn.setEnabled(true);
			m_oldPasswordEdit.setEnabled(true);
			m_newPasswordEdit.setEnabled(true);
			m_confirmPasswordEdit.setEnabled(true);
		}

	}

	@Override
	public void onChangePasswordFailed(WSErrorResponse reason) {
		if (isStarted) {
			String to_show = reason != null ?
				reason.getMessage()
				: getString(com.windfindtech.icommon.R.string.change_password_failed_on_exception);
			m_primaryCallback.onShowBottomToast(to_show, 0);
			/// enable all ui controls
			m_confirmBtn.setEnabled(true);
			m_oldPasswordEdit.setEnabled(true);
			m_newPasswordEdit.setEnabled(true);
			m_confirmPasswordEdit.setEnabled(true);
		}
	}
}
