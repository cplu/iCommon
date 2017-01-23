package com.windfindtech.icommon.mvp.model.local;

import com.windfindtech.icommon.jsondata.enumtype.DeviceStatus;
import com.windfindtech.icommon.jsondata.webservice.DeviceData;
import com.windfindtech.icommon.mvp.model.MNotifierManager;
import com.windfindtech.icommon.webservice.WSManager;

import java.util.ArrayList;
import java.util.Iterator;

import rx.Observable;

/**
 * Created by cplu on 2015/3/10.
 * Modified by cplu on 2016/07/28:
 *  proxy of MNotifier, with devices
 */
public class DeviceManager extends MNotifierManager<ArrayList<DeviceData>[]> {

	public static DeviceManager deviceNotifier = new DeviceManager();

	/**
	 * device section list is in type ArrayList<ArrayList<AccountDeviceItem>>
	 * each index of the list is the representation of a certain list of devices with a certain status
	 * should never be null, and contains at least DeviceStatus.values().length members, each of which is empty ArrayList as default
	 */

	//    private ArrayList<ArrayList<AccountDeviceItem>> m_deviceSections;
//	public enum Status {
//		unready,
//		inprogress,
//		ready
//	}

//	private Status m_status = Status.unready;

	@SuppressWarnings("unchecked")
	private DeviceManager() {
		super();
	}

	/**
	 * delete device from m_deviceSections
	 *
	 * @param id
	 * @param deviceStatus
	 */
	public void deleteDevice(String id, DeviceStatus deviceStatus) {
		ArrayList<DeviceData>[] devices = m_mNotifier.getData();
		if(devices != null) {
			ArrayList<DeviceData> itemList = devices[deviceStatus.ordinal()];
			Iterator<DeviceData> it = itemList.iterator();
			while (it.hasNext()) {
				if (it.next().getId().equals(id)) {
					it.remove();
					m_mNotifier.notifySubscribers();
					return;
				}
			}
		}
//        m_deviceSections.get(deviceStatus.ordinal()).remove()
	}

	public void changeDeviceStatus(String id, DeviceStatus currentStatus, DeviceStatus newStatus) {
		ArrayList<DeviceData>[] devices = m_mNotifier.getData();
		if(devices != null) {
			if (currentStatus != newStatus) {
				ArrayList<DeviceData> currentStatusList = devices[currentStatus.ordinal()];
				Iterator<DeviceData> it = currentStatusList.iterator();
				while (it.hasNext()) {
					DeviceData item = it.next();
					if (item.getId().equals(id)) {
						currentStatusList.remove(item);
						devices[newStatus.ordinal()].add(item);
						m_mNotifier.notifySubscribers();
						return;
					}
				}
				//report_pending_device_number();
				//m_evt_interface.on_events_number(UnresolvedEventsInterface.EventType.DEVICE_NUMBER, m_device_sections.get(DeviceStatus.pending.ordinal()).size());
			}
		}
	}

	public int getDevicePendingNumber() {
		return m_mNotifier.getData() != null ? m_mNotifier.getData()[DeviceStatus.pending.ordinal()].size() : 0;
	}

	@Override
	public Observable<ArrayList<DeviceData>[]> getNetworkObservable() {
		return WSManager.instance().doGetDeviceList();
	}
}
