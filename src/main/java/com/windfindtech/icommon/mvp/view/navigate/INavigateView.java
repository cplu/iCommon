package com.windfindtech.icommon.mvp.view.navigate;

import android.graphics.Bitmap;

import com.windfindtech.icommon.jsondata.user.UserData;
import com.windfindtech.icommon.jsondata.webservice.DeviceData;
import com.windfindtech.icommon.jsondata.webservice.VersionCheck;
import com.windfindtech.icommon.mvp.view.IFragmentView;

import java.util.ArrayList;

/**
 * Created by cplu on 2016/7/29.
 */
public interface INavigateView extends IFragmentView {
	/**
	 * whether this fragment needs avatar,
	 * classes implementing INavigateView should override this to indicate that they need to use avatar
	 * @return  default to false
	 */
	boolean avatarConcerned();

	/**
	 * whether this fragment needs user data,
	 * classes implementing INavigateView should override this to indicate that they need to use user data
	 * @return  default to true
	 */
	boolean usrDataConcerned();

	/**
	 * notify avatar changed
	 * @param bitmap
	 */
	void onAvatar(Bitmap bitmap);

	/**
	 * notify avatar uploading success/failed
	 * @param success       success or failed
	 */
	void onAvatarUploadResult(boolean success);

	/**
	 * notify user data received
	 * @param userData
	 */
	void onUserData(UserData userData);

	/**
	 * triggered when device list has been changed
	 * @param devices
	 */
	void onDeviceListChanged(ArrayList<DeviceData>[] devices);

	/**
	 * triggered when version check is done
	 * @param versionCheck
	 */
	void onVersionCheck(VersionCheck versionCheck);

	/**
	 * notify existence of reserved message
	 * @param hasReservedMessage
	 */
	void onReservedMessage(Boolean hasReservedMessage);
}
