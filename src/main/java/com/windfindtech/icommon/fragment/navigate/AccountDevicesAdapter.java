package com.windfindtech.icommon.fragment.navigate;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.windfindtech.icommon.R;
import com.windfindtech.icommon.R2;
import com.windfindtech.icommon.jsondata.enumtype.DeviceStatus;
import com.windfindtech.icommon.jsondata.webservice.DeviceData;
import com.windfindtech.icommon.view.recycler.MVPAdapter;
import com.windfindtech.icommon.view.recycler.MVPViewHolder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.TimeZone;

import butterknife.BindView;

public class AccountDevicesAdapter extends MVPAdapter<DeviceData, AccountDevicesAdapter.AccountDeviceItemHolder> {
	private ArrayList<DeviceData>[] m_deviceSections;

	private DeviceStatus m_status = DeviceStatus.pending;
	//private DeviceDetailFragment m_detail_fragment;

	public AccountDevicesAdapter(Context context) {
		super(context);
		m_deviceSections = new ArrayList[DeviceStatus.values().length];
		for (int i = 0; i < DeviceStatus.values().length; i++) {
			m_deviceSections[i] = new ArrayList();
		}
//        m_detail_fragment = new DeviceDetailFragment();
	}

	@Override
	public AccountDeviceItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		return new AccountDeviceItemHolder(getInflated(parent, R.layout.list_item_device));
	}

//	@Override
//	public long getItemID(int position)
//	{
//		return position;
//	}
//	
//	@Override
//	public boolean isEnabled(int position)
//	{
//		return true;
//	}

	private String convert_status_string(DeviceStatus status) {
		if (status == DeviceStatus.approved) {
			return m_ctx.getString(R.string.device_status_approved);
		} else if (status == DeviceStatus.denied) {
			return m_ctx.getString(R.string.device_status_denied);
		} else if (status == DeviceStatus.pending) {
			return m_ctx.getString(R.string.device_status_pending);
		} else {
			return null;
		}
	}

	private int get_status_color(DeviceStatus status) {
		if (status == DeviceStatus.approved) {
			return m_ctx.getResources().getColor(R.color.device_approve);
		} else if (status == DeviceStatus.denied) {
			return m_ctx.getResources().getColor(R.color.device_deny);
		} else if (status == DeviceStatus.pending) {
			return m_ctx.getResources().getColor(R.color.device_pending);
		} else {
			return 0;
		}
	}

//    private void report_pending_device_number(){

	public void setDevices(ArrayList<DeviceData>[] device_list) {
		m_deviceSections = device_list;
		m_adapterData = m_deviceSections[m_status.ordinal()];
		notifyDataSetChanged();
	}
//        m_user_ui_data_callback.on_data_changed(
//                new UserUIData(UserUIData.DataType.DevicePendingNumber, m_deviceSections.get(DeviceStatus.pending.ordinal()).size()));
//    }

	public DeviceData getDeviceItemByIndex(int index) {
		try {
			return m_deviceSections[m_status.ordinal()].get(index);
		} catch (Exception e) {
			return null;
		}
	}

	public void setShownStatus(DeviceStatus status) {
		m_status = status;
		m_adapterData = m_deviceSections[m_status.ordinal()];
		notifyDataSetChanged();
	}

	/**
	 * delete device item from list of current_status
	 *
	 * @param current_status
	 * @param item
	 */
	public void deleteDeviceItemByStatus(DeviceStatus current_status, DeviceData item) {
		m_deviceSections[current_status.ordinal()].remove(item);
		if (m_status == current_status) {
			notifyDataSetChanged();
		}
	}

	/**
	 * move device item from list of current_status to list of status
	 *
	 * @param current_status status whose related list the item is about to be moved out
	 * @param status         status whose related list the item is about to be moved in
	 * @param item
	 */
	public void moveItem(DeviceStatus current_status, DeviceStatus status, DeviceData item) {
		m_deviceSections[current_status.ordinal()].remove(item);
		m_deviceSections[status.ordinal()].add(item);
		if (m_status == current_status || m_status == status) {
			notifyDataSetChanged();
		}
	}

	public ArrayList<DeviceData>[] getAllItems() {
		return m_deviceSections;
	}

//    public int get_pending_device_count() {
//        return m_deviceSections.get(DeviceStatus.pending.ordinal()).size();
//    }

	class AccountDeviceItemHolder extends MVPViewHolder<DeviceData> {
		@BindView(R2.id.device_item_icon_view)
		ImageView m_iconView;
		@BindView(R2.id.device_item_name_view)
		TextView m_nameView;
		@BindView(R2.id.device_item_date_view)
		TextView m_authDateView;
		@BindView(R2.id.device_item_state_view)
		TextView m_stateView;
		public ImageButton m_forwardBtn;

		public AccountDeviceItemHolder(View itemView) {
			super(itemView);
		}

		@SuppressLint("SetTextI18n")
		@Override
		public void bindData(int position, DeviceData data) {
			String self_str = data.isSelf() ? m_ctx.getString(R.string.self_device) : "";
			if (data.getName() != null) {
				m_nameView.setText(data.getName() + self_str);
			} else {
				m_nameView.setText(m_ctx.getString(R.string.device_name_unknown) + self_str);
			}
			m_authDateView.setTextColor(m_ctx.getResources().getColor(R.color.default_fg_color));
			m_iconView.setImageResource(R.drawable.icon_device);
			if (data.getLastLoginTime() != null) {
				if (data.isLastLoginSuccess() && data.getLastLoginDuration() == 0) {
					m_authDateView.setText(m_ctx.getString(R.string.device_login_active));
					m_authDateView.setTextColor(m_ctx.getResources().getColor(R.color.device_date_color));
					m_iconView.setImageResource(R.drawable.icon_device_active);
				} else {
					SimpleDateFormat date_fmt = new SimpleDateFormat("yyyy-MM-dd kk:mm");
					date_fmt.setTimeZone(TimeZone.getDefault());
					m_authDateView.setText(m_ctx.getString(R.string.device_auth_date_prefix) + date_fmt.format(data.getLastLoginTime()));
				}
			} else {
				m_authDateView.setText(m_ctx.getString(R.string.device_auth_date_unknown));
			}
			if (data.getStatus() != null) {
				m_stateView.setText(convert_status_string(data.getStatus()));
				m_stateView.setTextColor(get_status_color(data.getStatus()));
			} else {
				m_stateView.setText(m_ctx.getString(R.string.device_status_unknown));
			}
		}
	}
}



