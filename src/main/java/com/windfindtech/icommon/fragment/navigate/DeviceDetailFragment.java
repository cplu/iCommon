package com.windfindtech.icommon.fragment.navigate;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.windfindtech.icommon.R;
import com.windfindtech.icommon.R2;
import com.windfindtech.icommon.dialog.ConfirmDialog;
import com.windfindtech.icommon.dialog.callback.ConfirmCallback;
import com.windfindtech.icommon.iCommon;
import com.windfindtech.icommon.jsondata.enumtype.DeviceStatus;
import com.windfindtech.icommon.jsondata.webservice.DeviceData;
import com.windfindtech.icommon.mvp.presenter.navigate.DeviceDetailPresenter;
import com.windfindtech.icommon.mvp.view.navigate.IDeviceDetailView;

import org.pmw.tinylog.Logger;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.OnClick;

public class DeviceDetailFragment extends NavigateFragment<IDeviceDetailView, DeviceDetailPresenter>
	implements IDeviceDetailView, ConfirmCallback {
	private static final int DURATION_CHANGING_STATUS = 3000;
	private static final int DLG_DEVICE_DELETE = 10;    /// should be different from any value of DeviceStatus.ordinal
//    private static final int DURATION_TOAST = 2000;
//    private DeviceInfoListener m_infoListener;
//    private boolean m_bSelf = false;

	@BindView(R2.id.device_nick_name)
	EditText m_deviceNickNameEdit;
	@BindView(R2.id.device_nick_name_edit)
	ImageButton m_deviceNickNameEditImg;
	@BindView(R2.id.device_nick_name_confirm)
	Button m_deviceNickNameConfirmBtn;
	@BindView(R2.id.device_status_view)
	TextView m_statusView;
	@BindView(R2.id.auth_date_value)
	TextView m_authDate;
	@BindView(R2.id.login_duration_value)
	TextView m_loginDuration;
	@BindView(R2.id.login_location_value)
	TextView m_loginLocation;
	@BindView(R2.id.mac_value)
	TextView m_macAddr;
	@BindView(R2.id.approve_btn)
	TextView m_approveBtn;
	@BindView(R2.id.deny_btn)
	TextView m_denyBtn;
	@BindView(R2.id.delete_btn)
	TextView m_deleteBtn;

	//private RelativeLayout m_operation_region;    /// 操作区域

	//private EditConfigDialog m_device_config_dlg;
//	private ConfirmDialog m_confirmDlg;

	public DeviceDetailFragment() {
	}

	@Override
	protected int getLayoutID() {
		return R.layout.fragment_account_device_detail;
	}

	@Override
	protected int getTitleID() {
		return R.string.title_account_device_detail;
	}

//    public void set_info_listener(DeviceInfoListener info_listener) {
//        m_infoListener = info_listener;
//    }

	private void rollback_nickname() {
		m_deviceNickNameEdit.setText(m_presenter.getRollbackName());
	}

	private void rollback_ui_to_edit_pen() {
		if (m_deviceNickNameConfirmBtn.isShown()) {
			m_deviceNickNameConfirmBtn.setVisibility(View.GONE);
			m_deviceNickNameEditImg.setVisibility(View.VISIBLE);
			m_deviceNickNameEdit.setEnabled(false);
			//m_deviceNickNameEdit.clearFocus();
			//m_deviceNickNameEdit.setSelectAllOnFocus(true);
			rollback_nickname();
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

//        if(m_deviceInfo == null){
//            m_userInterface.onFragmentNaviBack(false);
//        }
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		m_backBtnTxt.setText(getString(R.string.title_account_devices));

		DeviceData deviceData = (DeviceData) getParcelableParams();
		if (deviceData != null) {
			m_presenter.setDeviceItem(deviceData);
			if (deviceData.getName() != null) {
				m_deviceNickNameEdit.setText(deviceData.getName());
			} else {
				m_deviceNickNameEdit.setText(getString(R.string.device_name_unknown));
			}
			if (deviceData.getLastLoginTime() != null) {
				SimpleDateFormat date_fmt = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
				date_fmt.setTimeZone(TimeZone.getDefault());
				m_authDate.setText(date_fmt.format(deviceData.getLastLoginTime()));
			} else {
				m_authDate.setText(getString(R.string.device_auth_date_unknown));
			}
			if (deviceData.getLastLoginDuration() > 0) {
				String result = iCommon.getTimeDurationBy(getActivity(), deviceData.getLastLoginDuration());
				m_loginDuration.setText(result);
			} else {
				m_loginDuration.setText(getString(R.string.device_login_duration_unknown));
			}
			if (deviceData.getLastLoginLocation() != null && !deviceData.getLastLoginLocation().equals("Unknown")) {
				m_loginLocation.setText(deviceData.getLastLoginLocation());
			} else {
				m_loginLocation.setText(getString(R.string.device_login_location_unknown));
			}
			if (deviceData.getMac() != null) {
				m_macAddr.setText(deviceData.getMac());
			} else {
				m_macAddr.setText(getString(R.string.device_mac_unknown));
			}
			update_ui_according_to_status(deviceData.getStatus());
		} else {
			m_primaryCallback.onNaviBack();
		}
	}

	@Override
	protected NavListAdapter createNavAdapter() {
		return null;
	}

	@OnClick(R2.id.device_nick_name_edit)
	public void onNickEditClick() {
		try {
			m_deviceNickNameConfirmBtn.setVisibility(View.VISIBLE);
			m_deviceNickNameEditImg.setVisibility(View.GONE);
			m_deviceNickNameEdit.setEnabled(true);
			m_presenter.storeRollbackName(m_deviceNickNameEdit.getText().toString());
			m_deviceNickNameEdit.requestFocus();
			m_deviceNickNameEdit.selectAll();
			iCommon.showInputMethod(getActivity(), m_deviceNickNameEdit);
		} catch (Exception e) {

		}
	}

	@OnClick(R2.id.device_nick_name_confirm)
	public void onNickConfirmClick() {
		m_deviceNickNameConfirmBtn.setVisibility(View.GONE);
		m_deviceNickNameEditImg.setVisibility(View.VISIBLE);
		m_deviceNickNameEdit.setEnabled(false);
		//m_deviceNickNameEdit.clearFocus();
		iCommon.hideInputMethod(getActivity());
		final String new_nick_name = m_deviceNickNameEdit.getText().toString();
		if (new_nick_name == null || new_nick_name.length() < 2) {
			iCommon.showToast(getActivity(), R.string.device_nick_name_invalid);
			rollback_nickname();
		} else {
			m_presenter.changeDeviceName(new_nick_name);
		}
	}

	@OnClick(R2.id.approve_btn)
	public void onApproveBtnClick() {
		if (!m_presenter.isApproved()) {
			confirm_to_change_status(getActivity().getString(R.string.device_status_confirm_approve), DeviceStatus.approved);
		}
	}

	@OnClick(R2.id.deny_btn)
	public void onDenyBtnClick() {
		if (!m_presenter.isDenied()) {
			if (m_presenter.isSelf()) {
				/// do not deny self
				iCommon.showToast(getActivity(), R.string.device_status_operation_denied);
				return;
			}
			confirm_to_change_status(getActivity().getString(R.string.device_status_confirm_deny), DeviceStatus.denied);
		}
	}

	@OnClick(R2.id.delete_btn)
	public void onDelBtnClick() {
		if (m_presenter.isSelf()) {
			/// do not delete self
			iCommon.showToast(getActivity(), R.string.device_status_operation_denied);
			return;
		}
		confirm_to_delete(getActivity().getString(R.string.device_status_confirm_delete));
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	protected DeviceDetailPresenter createPresenter() {
		return new DeviceDetailPresenter();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

//    @Override
//    protected void onLayoutTouch(View v, MotionEvent event){
//        if(event.getAction() == MotionEvent.ACTION_DOWN) {
//            rollback_ui_to_edit_pen();
//        }
//    }

	private void confirm_to_change_status(String title, final DeviceStatus status) {
		if (isStarted) {
			/// use status.ordinal as dialog id
			ConfirmDialog.create(status.ordinal(), title, getChildFragmentManager());
		}
	}

	private void confirm_to_delete(String title) {
		if (isStarted) {
			/// use status.ordinal as dialog id
			ConfirmDialog.create(DLG_DEVICE_DELETE, title, getChildFragmentManager());
		}
	}

	private void change_device_status(final DeviceStatus status) {
		m_primaryCallback.onShowBottomToast(getString(R.string.device_status_changing), DURATION_CHANGING_STATUS);
		m_deviceNickNameEditImg.setEnabled(false);
		m_approveBtn.setEnabled(false);
		m_denyBtn.setEnabled(false);
		m_deleteBtn.setEnabled(false);
		m_presenter.changeDeviceStatus(status);

	}

	private void delete_device() {
		m_primaryCallback.onShowBottomToast(getString(R.string.device_deleting), DURATION_CHANGING_STATUS);
		m_deviceNickNameEditImg.setEnabled(false);
		m_approveBtn.setEnabled(false);
		m_denyBtn.setEnabled(false);
		m_deleteBtn.setText(getString(R.string.device_btn_deleting));
		m_deleteBtn.setEnabled(false);
		m_presenter.deleteDevice();

	}

	/**
	 * Warning: this will only change the visibility of buttons, not availability(enabled/disabled)
	 *
	 * @param status
	 */
	private void update_ui_according_to_status(DeviceStatus status) {
//		m_deviceInfo.setStatus(status);

		//m_deleteBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX, getActivity().getResources().getDimension(R.dimen.default_text_size));
		if (status == DeviceStatus.pending) {
			m_statusView.setText(getString(R.string.device_status_pending));
			m_statusView.setTextColor(getResources().getColor(R.color.device_pending));
			m_approveBtn.setVisibility(View.VISIBLE);
			m_denyBtn.setVisibility(View.VISIBLE);
			m_deleteBtn.setVisibility(View.VISIBLE);
		} else if (status == DeviceStatus.approved) {
			m_statusView.setText(getString(R.string.device_approved));
			m_statusView.setTextColor(getResources().getColor(R.color.device_approve));
			m_approveBtn.setVisibility(View.GONE);
			m_denyBtn.setVisibility(View.VISIBLE);
			m_deleteBtn.setVisibility(View.VISIBLE);
		} else if (status == DeviceStatus.denied) {
			m_statusView.setText(getString(R.string.device_denied));
			m_statusView.setTextColor(getResources().getColor(R.color.device_deny));
			m_approveBtn.setVisibility(View.VISIBLE);
			m_denyBtn.setVisibility(View.GONE);
			m_deleteBtn.setVisibility(View.VISIBLE);
		}
	}

//    public void set_device_info(DeviceData device_info, boolean is_self) {
//        m_deviceInfo = device_info;
//        m_bSelf = is_self;
//    }

	@Override
	public void onPause() {
		rollback_ui_to_edit_pen();
		super.onPause();
	}

	@Override
	public void onDestroy() {
//        if (m_device_config_dlg != null) {
//            m_device_config_dlg.cancel();
//            m_device_config_dlg = null;
//        }
		super.onDestroy();
	}

	@Override
	protected boolean onLayoutTouch(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			rollback_ui_to_edit_pen();
		}
		return true;
	}

	@Override
	public void onNameChanged(String new_nick_name) {
		if (isStarted) {
			m_deviceNickNameEdit.setText(new_nick_name);
		}
	}

	@Override
	public void onNameUnchanged() {
		if (isStarted) {
			iCommon.showToast(getActivity(), R.string.device_nick_name_change_failed);
			rollback_nickname();
		}
	}

	@Override
	public void onDeviceStatus(DeviceStatus status, boolean changed) {
		if (isStarted) {
			m_deviceNickNameEditImg.setEnabled(true);
			m_approveBtn.setEnabled(true);
			m_denyBtn.setEnabled(true);
			m_deleteBtn.setEnabled(true);
			update_ui_according_to_status(status);
			if (changed) {
				m_primaryCallback.onShowBottomToast(getString(R.string.device_status_changed), 0);
			} else {
				m_primaryCallback.onShowBottomToast(getString(R.string.device_status_change_failed), 0);
			}
		}
	}

	@Override
	public void onDeleteResult(boolean success) {
		if (isStarted) {
			if (success) {
				m_deleteBtn.setText(getString(R.string.device_btn_deleted));
				m_primaryCallback.onShowBottomToast(getString(R.string.device_status_deleted), 0);
			} else {
				m_deviceNickNameEditImg.setEnabled(true);
				m_approveBtn.setEnabled(true);
				m_denyBtn.setEnabled(true);
				m_deleteBtn.setText(getString(R.string.device_delete));
				m_deleteBtn.setEnabled(true);
				m_primaryCallback.onShowBottomToast(getString(R.string.device_delete_failed), 0);
			}
		}
	}

	@Override
	public void onConfirmResult(long id, boolean isOK) {
		if (!isOK) {
			return;
		}
		/// id is used as device status in ordinal
		if (id == DLG_DEVICE_DELETE) {
			delete_device();
		} else {
			try {
				DeviceStatus status = DeviceStatus.values()[(int) id];
				if (status != null) {
					change_device_status(status);
				}
			} catch (Exception e) {
				Logger.error(e);
			}
		}
	}
}
