package com.windfindtech.icommon.mvp.model.local;

import com.windfindtech.icommon.jsondata.enumtype.Gender;
import com.windfindtech.icommon.jsondata.user.UserData;
import com.windfindtech.icommon.mvp.model.MNotifierManager;
import com.windfindtech.icommon.webservice.WSManager;

import rx.Observable;

/**
 * Created by cplu on 2016/8/1.
 */
public class UsrDataManager extends MNotifierManager<UserData> {
	public static UsrDataManager usrDat = new UsrDataManager();

	public UsrDataManager() {
		super();
	}

	public void setGender(Gender gender) {
		UserData data = m_mNotifier.getData();
		if(data != null) {
			data.setGender(gender);
		}
	}

	public void setEmail(String email) {
		UserData data = m_mNotifier.getData();
		if(data != null) {
			data.setEmail(email);
		}
	}

	public void setRealName(String realName) {
		UserData data = m_mNotifier.getData();
		if(data != null) {
			data.setRealName(realName);
		}
	}

	public void setAge(int age) {
		UserData data = m_mNotifier.getData();
		if(data != null) {
			data.setAge(age);
		}
	}

	public void setNickName(String nickName) {
		UserData data = m_mNotifier.getData();
		if(data != null) {
			data.setNickName(nickName);
		}
	}

	public void setSignature(String signature) {
		UserData data = m_mNotifier.getData();
		if(data != null) {
			data.setSignature(signature);
		}
	}

	@Override
	public Observable<UserData> getNetworkObservable() {
		return WSManager.instance().getAccountInfo();
	}
}
