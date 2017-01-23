package com.windfindtech.icommon.fragment.navigate;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.windfindtech.icommon.R;
import com.windfindtech.icommon.R2;
import com.windfindtech.icommon.fragment.management.TagUser;
import com.windfindtech.icommon.jsondata.enumtype.DeviceStatus;
import com.windfindtech.icommon.jsondata.webservice.DeviceData;
import com.windfindtech.icommon.mvp.model.MType;
import com.windfindtech.icommon.mvp.presenter.navigate.AccountDevicesPresenter;
import com.windfindtech.icommon.mvp.view.navigate.IAccountDevicesView;
import com.windfindtech.icommon.view.recycler.MVPAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnTouch;

/**
 * 个人中心-->设备管理
 */
public class AccountDevicesFragment extends NavigateFragment<IAccountDevicesView, AccountDevicesPresenter>
	implements IAccountDevicesView {

	private static final String SELECTED_TAB_KEY = "selected_tab";
	@BindView(R2.id.device_pending_tab_btn)
	Button m_pendingTabBtn;
	@BindView(R2.id.device_approved_tab_btn)
	Button m_approvedTabBtn;
	@BindView(R2.id.device_denied_tab_btn)
	Button m_deniedTabBtn;
	@BindView(R2.id.devices_loading)
	ProgressBar m_loadingProgressBar;

	private DeviceStatus m_selectedTabStatus = DeviceStatus.pending;
	private boolean m_bChecked;

	private AccountDevicesAdapter m_accountDevicesAdapter;

	public AccountDevicesFragment() {
		setPageName("DeviceManagementView");
//        m_data_observer = new DataSetObserver() {
//            @Override
//            public void onChanged() {
//                super.onChanged();
//                m_user_ui_data_callback.on_data_changed(
//                        new UserUIData(UserUIData.DataType.DevicePendingNumber,
//                                ((AccountDevicesAdapter)m_adapter).get_pending_device_count()));
//            }
//        };
	}

	@Override
	protected int getLayoutID() {
		return R.layout.fragment_account_devices;
	}

	@Override
	protected int getTitleID() {
		return R.string.title_account_devices;
	}

	@Override
	protected int getNotificationType() {
		return MType.WS_RECEIVER
		       | MType.DEVICE_NOTIFIER | MType.DEVICE_UPDATE_BIT;
	}

	@Override
	protected AccountDevicesPresenter createPresenter() {
		return new AccountDevicesPresenter();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	protected NavListAdapter createNavAdapter() {
		return null;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		m_accountDevicesAdapter = new AccountDevicesAdapter(getActivity());
		if(m_listRecycled != null) {
			m_listRecycled.setAdapter(m_accountDevicesAdapter);
			m_accountDevicesAdapter.setOnItemClickListener(new MVPAdapter.OnItemClickListener() {
				@Override
				public void onItemClick(View view, int position, long id) {
					performItemClick(position, id);
				}
			});
		}

		if (savedInstanceState != null) {
			// Restore last state for m_bChecked position.
			m_selectedTabStatus = DeviceStatus.values()[savedInstanceState.getInt(SELECTED_TAB_KEY, DeviceStatus.pending.ordinal())];
		} else {
			//m_selectedTabStatus = DeviceStatus.pending;
		}

	}

	@OnTouch(R2.id.device_pending_tab_btn)
	public boolean onPendingTabTouch(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			select_devices_focused(DeviceStatus.pending);
			return true;
		} else {
			return false;
		}
	}

	@OnTouch(R2.id.device_approved_tab_btn)
	public boolean onApprovedTabTouch(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			select_devices_focused(DeviceStatus.approved);
			return true;
		} else {
			return false;
		}
	}

	@OnTouch(R2.id.device_denied_tab_btn)
	public boolean onDeniedTabTouch(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			select_devices_focused(DeviceStatus.denied);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		select_devices_focused(m_selectedTabStatus);
//        if(m_parent_layout != null) {
//            m_parent_layout.prepare_swipe_header(m_rootView, new SwipeRefreshListener() {
//                @Override
//                public void on_refreshing() {
//
//                }
//            });
//        }
	}

//    @Override
//    protected boolean is_swipe_refresh_enabled(){
//        return false;
//    }

//	@Override
//	public void onListItemClick(ListView l, View v, int position, long id)
//	{
//		// user config item is clicked
//		m_adapter.perform_click(position, m_ui_interface);
//	}

	private void select_devices_focused(DeviceStatus status) {
		m_selectedTabStatus = status;
		m_pendingTabBtn.setBackgroundResource(R.drawable.button_light_normal);
		m_pendingTabBtn.setTextColor(getResources().getColor(R.color.default_fg_color));
		m_approvedTabBtn.setBackgroundResource(R.drawable.button_light_normal);
		m_approvedTabBtn.setTextColor(getResources().getColor(R.color.default_fg_color));
		m_deniedTabBtn.setBackgroundResource(R.drawable.button_light_normal);
		m_deniedTabBtn.setTextColor(getResources().getColor(R.color.default_fg_color));

		switch (status) {
			case pending:
				m_pendingTabBtn.setBackgroundResource(R.drawable.button_light_pressed);
				m_pendingTabBtn.setTextColor(getResources().getColor(R.color.default_bg_color));
				break;
			case approved:
				m_approvedTabBtn.setBackgroundResource(R.drawable.button_light_pressed);
				m_approvedTabBtn.setTextColor(getResources().getColor(R.color.default_bg_color));
				break;
			case denied:
				m_deniedTabBtn.setBackgroundResource(R.drawable.button_light_pressed);
				m_deniedTabBtn.setTextColor(getResources().getColor(R.color.default_bg_color));
				break;
			default:
				break;
		}

		m_accountDevicesAdapter.setShownStatus(status);
	}

	@Override
	public void performItemClick(final int position, long id) {
		final DeviceData item = m_accountDevicesAdapter.getDeviceItemByIndex(position);
		if (item != null) {
			m_primaryCallback.onForward(TagUser.FRG_DEVICE_DETAIL, item, null);
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(SELECTED_TAB_KEY, m_selectedTabStatus.ordinal());
	}

//    public void setEventsInterface(UnresolvedEventsInterface unresolvedEventsInterface) {
//        m_evt_interface = unresolvedEventsInterface;
//    }

	@Override
	public void onDestroyView() {
		if (m_loadingProgressBar != null) {
			m_loadingProgressBar.setVisibility(View.GONE);
		}
		super.onDestroyView();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onDeviceListChanged(ArrayList<DeviceData>[] devices) {
		if (devices != null) {
			if (m_selectedTabStatus == DeviceStatus.pending && !m_bChecked) {
				m_bChecked = true;
				if (devices[m_selectedTabStatus.ordinal()].size() == 0) {
					select_devices_focused(DeviceStatus.approved);
				}
			}
			m_loadingProgressBar.setVisibility(View.GONE);
			m_accountDevicesAdapter.setDevices(devices);
		}
	}
}
