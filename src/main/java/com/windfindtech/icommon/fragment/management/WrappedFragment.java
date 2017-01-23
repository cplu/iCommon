package com.windfindtech.icommon.fragment.management;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.View;

import com.windfindtech.icommon.iCommon;
import com.windfindtech.icommon.mvp.model.MLocalReceiver;
import com.windfindtech.icommon.mvp.presenter.IFragmentPresenter;
import com.windfindtech.icommon.mvp.view.IFragmentView;
import com.windfindtech.icommon.util.Gather;

import java.lang.reflect.Field;

import butterknife.ButterKnife;

/**
 * Created by cplu on 2015/8/18.
 * This class deals with the following issues:
 * 1 bug fixed: IllegalStateException: Activity has been destroyed
 * 2 back key handling
 * 3 deal with network related events.
 * If derived class want to subscribe to typical kind of events, it should set arguments in Constructor, for example:
 * Bundle bundle = new Bundle();
 * bundle.putInt(SUBSCRIBER_NETWORK, true);
 * setArguments(bundle);
 * Default, no event handler is registered and m_broadcastReceiver is null
 */
public abstract class WrappedFragment<ViewType extends IFragmentView, PresenterType extends IFragmentPresenter<ViewType>>
	extends Fragment implements IFragmentView {
	//	protected static final String LocalNotificationKey = "localnotifier:subscriber";
//	protected static final String localNotifierRefreshKey = "localnotifier:subscriber";
	protected static final String BundleIntKey = "bundle_key:int";
	protected static final String BundleParamKey = "bundle_key:param";
	private static final String PAGE_VIEW_NAME_KEY = "windfindtech:page_view";
//	private BroadcastReceiver m_broadcastReceiver;

	protected String m_pageName;    /// name of the page, for statistics usage
	protected long m_viewStart;     /// time of page view start
	protected PrimaryCallback m_primaryCallback;
//	protected long m_viewStop;      /// time of page view stop

	protected PresenterType m_presenter;
	protected boolean isStarted = false;
//	private Unbinder unbinder;

	public WrappedFragment() {
		m_presenter = createPresenter();
	}

	/**
	 * return notification type (MType) of this fragment
	 * derived classes should override this to provide types they are concerned about
	 *
	 * @return
	 */
	protected int getNotificationType() {
		return 0;
	}

	/**
	 * set int value to bundle
	 *
	 * @param value this value is a parameter passed to fragment before onCreate/onCreateView
	 */
	protected void setBundleInt(int value) {
		Bundle bundle = getArguments();
		if (bundle == null) {
			bundle = new Bundle();
			bundle.putInt(BundleIntKey, value);
			setArguments(bundle);
		} else {
			bundle.putInt(BundleIntKey, value);
		}
	}

	protected int getBundleInt(boolean clear) {
		Bundle bundle = getArguments();
		int value = -1;
		if (bundle != null) {
			value = bundle.getInt(BundleIntKey, -1);
			if (clear) {
				bundle.putInt(BundleIntKey, -1);
			}
		}
		return value;
	}

	public void prepareParams(Parcelable params) {
		Bundle bundle = getArguments();
		if (bundle == null) {
			bundle = new Bundle();
			bundle.putParcelable(BundleParamKey, params);
			setArguments(bundle);
		} else {
			bundle.putParcelable(BundleParamKey, params);
		}
	}

	public void prepareParams(Parcelable[] params) {
		Bundle bundle = getArguments();
		if (bundle == null) {
			bundle = new Bundle();
			bundle.putParcelableArray(BundleParamKey, params);
			setArguments(bundle);
		} else {
			bundle.putParcelableArray(BundleParamKey, params);
		}
	}

	public Parcelable getParcelableParams() {
		Bundle bundle = getArguments();
		if (bundle != null) {
			return bundle.getParcelable(WrappedFragment.BundleParamKey);
		}
		return null;
	}

	public Parcelable[] getParcelableArrayParams() {
		Bundle bundle = getArguments();
		if (bundle != null) {
			return bundle.getParcelableArray(WrappedFragment.BundleParamKey);
		}
		return null;
	}

	public void prepareParams(String[] args) {
		Bundle bundle = getArguments();
		if (bundle == null) {
			bundle = new Bundle();
			bundle.putStringArray(BundleParamKey, args);
			setArguments(bundle);
		} else {
			bundle.putStringArray(BundleParamKey, args);
		}
	}

	public String[] getStringArrayParams() {
		Bundle bundle = getArguments();
		if (bundle != null) {
			return bundle.getStringArray(WrappedFragment.BundleParamKey);
		}
		return null;
	}

	public void setPageName(String pageName) {
		m_pageName = pageName;
	}

	protected abstract PresenterType createPresenter();

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			m_primaryCallback = (PrimaryCallback) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + " must implement PrimaryCallback");
		}
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		ButterKnife.bind(this, view);
		if (savedInstanceState != null) {
			m_pageName = savedInstanceState.getString(PAGE_VIEW_NAME_KEY);
		}
	}

	@Override
	public void onStart() {
		super.onStart();
		isStarted = true;
		m_presenter.attach((ViewType) this, getNotificationType());
	}

	@Override
	public void onResume() {
		super.onResume();
		if (m_pageName != null) {
			m_viewStart = System.currentTimeMillis();
		} else {
			m_viewStart = 0;
		}
	}

	@Override
	public void onPause() {
		if (m_pageName != null) {
			if (m_viewStart > 0) {
				Gather.instance().addPageView(m_pageName, m_viewStart);
				m_viewStart = 0;
			} else {
				Gather.instance().addPageView(m_pageName);
			}
		}
		super.onPause();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString(PAGE_VIEW_NAME_KEY, m_pageName);
	}

	@Override
	public void onStop() {
		m_presenter.detach();
		isStarted = false;
		super.onStop();
	}

	@Override
	public void onDestroyView() {
//		unbinder.unbind();
		super.onDestroyView();
	}

	@Override
	public void onNetworkChanged(@MLocalReceiver.NetworkStatus int status) {
	}

	@Override
	public void onWifiChanged(@MLocalReceiver.WifiStatus int status) {
	}

	@Override
	public void onWSChanged(@MLocalReceiver.WebserviceStatus int status) {
	}

	/**
	 * bug fix:
	 * http://stackoverflow.com/questions/15207305/getting-the-error-java-lang-illegalstateexception-activity-has-been-destroyed
	 */
	private static final Field sChildFragmentManagerField;

	static {
		Field f = null;
		try {
			f = Fragment.class.getDeclaredField("mChildFragmentManager");
			f.setAccessible(true);
		} catch (NoSuchFieldException e) {
//			Log.e(LOGTAG, "Error getting mChildFragmentManager field", e);
		}
		sChildFragmentManagerField = f;
	}

	@Override
	public void onDetach() {
		if (sChildFragmentManagerField != null) {
			try {
				sChildFragmentManagerField.set(this, null);
			} catch (Exception e) {
//            throw new RuntimeException(e);
			}
		}
		super.onDetach();
	}

	/**
	 * when back key pressed, how this fragment should behave
	 * derived class should override this method to implement specific logic
	 *
	 * @return true if back key event is resolved, false otherwise
	 */
	public boolean triggerBackkey() {
		return false;
//		FragmentManager fragmentManager = getChildFragmentManager();
//		if (fragmentManager.getBackStackEntryCount() > 0) {
//			try {
//				fragmentManager.popBackStackImmediate();
//				return true;
//			} catch (Exception e) {
//				return false;
//			}
//		} else {
//			return false;
//		}
	}

	/**
	 * when key is pressed, how this fragment should behave
	 * @param keyCode
	 * @param event
	 * @return
	 */
	public boolean triggerKey(int keyCode, KeyEvent event) {
		return false;
	}

	/**
	 * Check if activity is destroyed
	 * {@link Activity#isDestroyed()} could only be called on api level 17 or above
	 *
	 * @return
	 */
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
	protected boolean isActivityDestroyed() {
		if (getActivity() != null) {
			if (iCommon.isApiLevelAbove17() && getActivity().isDestroyed()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Check if activity is finishing
	 *
	 * @return true if activity is finishing, or {@link Fragment#getActivity()} returns null, false otherwise
	 */
	protected boolean isActivityFinishing() {
		if (getActivity() != null) {
			return getActivity().isFinishing();
		} else {
			return true;
		}
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
	}
}
