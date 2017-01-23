package com.windfindtech.icommon.mvp.presenter.navigate;

import com.windfindtech.icommon.jsondata.enumtype.DeviceStatus;
import com.windfindtech.icommon.jsondata.webservice.BaseResponse;
import com.windfindtech.icommon.jsondata.webservice.DeviceData;
import com.windfindtech.icommon.jsondata.webservice.WSErrorResponse;
import com.windfindtech.icommon.mvp.model.local.DeviceManager;
import com.windfindtech.icommon.mvp.view.navigate.IDeviceDetailView;
import com.windfindtech.icommon.webservice.WSCallback;
import com.windfindtech.icommon.webservice.WSManager;

/**
 * Created by cplu on 2016/8/2.
 */
public class DeviceDetailPresenter extends INavigatePresenter<IDeviceDetailView> {
	private DeviceData m_deviceInfo;
	private String m_nickRollback;

	@Override
	protected IDeviceDetailView createDummy() {
		return IDeviceDetailView.dummy;
	}

	public void setDeviceItem(DeviceData accountDeviceItem) {
		m_deviceInfo = accountDeviceItem;
	}

	public void changeDeviceName(final String new_nick_name) {
		WSManager.instance().doChangeDeviceName(m_deviceInfo.getId(), new_nick_name, new WSCallback<BaseResponse>() {
			@Override
			public void onSuccess(BaseResponse ret) {
				m_deviceInfo.setName(new_nick_name);
				getView().onNameChanged(new_nick_name);
			}

			@Override
			public void onFailed(WSErrorResponse reason) {
				getView().onNameUnchanged();
			}
		});
	}

	public void storeRollbackName(String deviceNick) {
		m_nickRollback = deviceNick;
	}

	public String getRollbackName() {
		return m_nickRollback;
	}

	public boolean isApproved() {
		return m_deviceInfo != null && m_deviceInfo.getStatus() == DeviceStatus.approved;
	}

	public boolean isDenied() {
		return m_deviceInfo != null && m_deviceInfo.getStatus() == DeviceStatus.denied;
	}

	/**
	 * whether this device is self device ("本机")
	 * @return
	 */
	public boolean isSelf() {
		return m_deviceInfo != null && m_deviceInfo.isSelf();
	}

	public void changeDeviceStatus(final DeviceStatus status) {
		WSManager.instance().doChangeDeviceStatus(m_deviceInfo.getId(), status.toString(), new WSCallback<BaseResponse>() {

			@Override
			public void onSuccess(BaseResponse ret) {
				/// change to new status
//                if (m_infoListener != null) {
				m_deviceInfo.setStatus(status);
				DeviceManager.deviceNotifier.changeDeviceStatus(m_deviceInfo.getId(), m_deviceInfo.getStatus(), status);
//                    m_infoListener.onInfoChanged(m_deviceInfo);
//                }
				getView().onDeviceStatus(status, true);

			}

			@Override
			public void onFailed(WSErrorResponse reason) {
				/// change failed
				getView().onDeviceStatus(m_deviceInfo.getStatus(), false);
			}
		});
	}

	public void deleteDevice() {
		WSManager.instance().doDeleteDevice(m_deviceInfo.getId(), new WSCallback<BaseResponse>() {
			@Override
			public void onSuccess(BaseResponse ret) {
				DeviceManager.deviceNotifier.deleteDevice(m_deviceInfo.getId(), m_deviceInfo.getStatus());
				getView().onDeleteResult(true);
//                m_userInterface.onFragmentNaviBack(false);
			}

			@Override
			public void onFailed(WSErrorResponse reason) {
				/// change failed
				getView().onDeleteResult(false);
			}
		});
	}
}
