package com.windfindtech.icommon.fragment.navigate;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.windfindtech.icommon.R;
import com.windfindtech.icommon.R2;
import com.windfindtech.icommon.fragment.management.TagUser;
import com.windfindtech.icommon.jsondata.webservice.BaseResponse;
import com.windfindtech.icommon.jsondata.webservice.WSErrorResponse;
import com.windfindtech.icommon.mvp.presenter.navigate.DeviceRegisterPresenter;
import com.windfindtech.icommon.mvp.view.navigate.IDeviceRegisterView;
import com.windfindtech.icommon.rx.RXTimerTask;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;

public class DeviceRegisterFragment extends NavigateFragment<IDeviceRegisterView, DeviceRegisterPresenter>
	implements IDeviceRegisterView {
	private static final int DURATION_REGISTERING = 3000;
//    private static final int DURATION_TOAST = 2000;
//    private static final int REG_CODE_LENGTH = 6;
//	private final DeviceRegisterPresenter m_presenter;

	@BindView(R2.id.master_device_register_layout)
	LinearLayout m_registerLayout;
	@BindView(R2.id.master_device_confirm_layout)
	LinearLayout m_confirmLayout;
	@BindView(R2.id.master_device_reg_btn)
	Button m_regBtn;
	@BindView(R2.id.master_device_code)
	EditText m_regCodeEdit;
	@BindView(R2.id.master_device_submit_btn)
	Button m_codeSubmitBtn;
	@BindView(R2.id.master_device_reg_retry_btn)
	Button m_regRetryBtn;
	@BindView(R2.id.go_to_device_manager_btn)
	Button m_gotoDeviceManagerBtn;
	//private MainDeviceRegInterface m_reg_interface;

	public DeviceRegisterFragment() {
//	    m_presenter = new DeviceRegisterPresenter(this);
	}

	@Override
	protected DeviceRegisterPresenter createPresenter() {
		return new DeviceRegisterPresenter();
	}

	@Override
	protected int getLayoutID() {
		return R.layout.fragment_master_device_register;
	}

	@Override
	protected int getTitleID() {
		return R.string.title_master_device_reg;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = super.onCreateView(inflater, container, savedInstanceState);
		return rootView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		m_gotoDeviceManagerBtn.setVisibility(View.GONE);

	}

	@Override
	protected NavListAdapter createNavAdapter() {
		return null;
	}

	@OnClick(R2.id.master_device_reg_btn)
	public void onRegBtnClick() {
		acquire_register_code();
	}

	@OnClick(R2.id.master_device_submit_btn)
	public void onSubmitBtnClick() {
		String code = m_regCodeEdit.getText().toString();
		if (!TextUtils.isEmpty(code)) {
			m_codeSubmitBtn.setEnabled(false);
			m_primaryCallback.onShowBottomToast(getString(R.string.master_device_confirming), DURATION_REGISTERING);
			m_presenter.changeMasterDevice(code);

		} else {
			m_primaryCallback.onShowBottomToast(getString(R.string.master_device_code_not_empty), 0);
		}
	}

	@OnClick(R2.id.master_device_reg_retry_btn)
	public void onRetryBtnClick() {
		acquire_register_code();
	}

	@OnClick(R2.id.go_to_device_manager_btn)
	public void onForwardBtnClick() {
		m_primaryCallback.onNaviBack();
		m_primaryCallback.onForward(TagUser.FRG_ACCOUNT_DEVICE, null);
	}

	private void change_to_device_confirm_layout() {
		m_registerLayout.setVisibility(View.GONE);
		m_confirmLayout.setVisibility(View.VISIBLE);
	}

	private void change_to_device_register_layout() {
		m_confirmLayout.setVisibility(View.GONE);
		m_registerLayout.setVisibility(View.VISIBLE);
	}

	private void acquire_register_code() {
		m_regBtn.setEnabled(false);
		m_regRetryBtn.setEnabled(false);
		m_primaryCallback.onShowBottomToast(getString(R.string.master_device_acquiring_code), DURATION_REGISTERING);
		m_presenter.deviceRegister();

	}

	private void enable_reg_btn_after() {
		final int seconds = 15;
		RXTimerTask.createAndStart(1000, seconds, new Subscriber<Long>() {
			@Override
			public void onCompleted() {
				if (isStarted) {
					m_regBtn.setText(getString(R.string.master_device_register_btn));
					m_regRetryBtn.setText(getString(R.string.master_device_register_retry));
					m_regBtn.setEnabled(true);
					m_regRetryBtn.setEnabled(true);
				}
			}

			@Override
			public void onError(Throwable throwable) {
				if (isStarted) {
					m_regBtn.setText(getString(R.string.master_device_register_btn));
					m_regRetryBtn.setText(getString(R.string.master_device_register_retry));
					m_regBtn.setEnabled(true);
					m_regRetryBtn.setEnabled(true);
				}
			}

			@Override
			public void onNext(Long value) {
				if (isStarted) {
					m_regBtn.setText(String.format(getString(R.string.seconds_left), seconds - 1 - value));
					m_regRetryBtn.setText(String.format(getString(R.string.seconds_left), seconds - 1 - value));
				}
			}
		});
	}

	@Override
	public void onDeviceChangeSuccess(BaseResponse ret) {
		if (isStarted) {
			m_codeSubmitBtn.setEnabled(true);
			m_gotoDeviceManagerBtn.setVisibility(View.VISIBLE);
			m_primaryCallback.onShowBottomToast(getString(R.string.master_device_confirmed), 0);
		}
	}

	@Override
	public void onDeviceChangeFailed(WSErrorResponse reason) {
		if (isStarted) {
			m_codeSubmitBtn.setEnabled(true);
			m_regRetryBtn.setVisibility(View.VISIBLE);
			m_primaryCallback.onShowBottomToast(getString(R.string.master_device_confirm_failed), 0);
		}
	}

	@Override
	public void onDeviceRegisterSuccess(BaseResponse ret) {
		if (isStarted) {
			enable_reg_btn_after();
			m_primaryCallback.onShowBottomToast(getString(R.string.master_device_acquiring_code_success), 0);
			change_to_device_confirm_layout();
		}
	}

	@Override
	public void onDeviceRegisterFailed(WSErrorResponse reason) {
		if (isStarted) {
			enable_reg_btn_after();
			m_primaryCallback.onShowBottomToast(getString(R.string.master_device_acquiring_code_failed), 0);
		}
	}
}
