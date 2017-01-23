package com.windfindtech.icommon.activity;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;

import com.windfindtech.icommon.R;
import com.windfindtech.icommon.WFTApplication;
import com.windfindtech.icommon.fragment.management.FragmentBox;
import com.windfindtech.icommon.fragment.management.PrimaryCallback;
import com.windfindtech.icommon.fragment.management.WrappedFragment;
import com.windfindtech.icommon.iCommon;
import com.windfindtech.icommon.mvp.model.MLocalReceiver;
import com.windfindtech.icommon.mvp.presenter.IActivityPresenter;
import com.windfindtech.icommon.mvp.view.IActivityView;
import com.windfindtech.icommon.rx.RXCheckDelay;
import com.windfindtech.icommon.util.Gather;

import org.pmw.tinylog.Logger;

/**
 * Created by cplu on 2016/8/2.
 */
public abstract class MVPActivity<ViewType extends IActivityView, PresenterType extends IActivityPresenter<ViewType>>
		extends FragmentActivity implements IActivityView, PrimaryCallback {

	protected PresenterType m_presenter;
	private static final long BACK_KEY_TIME_GAP = 2000;
	private RXCheckDelay m_bkKeyCheckDelay;

	/**
	 * should be initialized in derived classes
	 */
	protected FragmentBox m_fragmentBox;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		m_presenter = createPresenter();
		m_presenter.attach((ViewType)this, getNotificationType());
		m_bkKeyCheckDelay = new RXCheckDelay(BACK_KEY_TIME_GAP);
	}

	@Override
	protected void onResume() {
		super.onResume();
		WFTApplication.inForeground = true;
		Gather.instance().addAppToForeground();
	}

	@Override
	protected void onPause() {
		Gather.instance().addAppToBackground();
		WFTApplication.inForeground = false;
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		if(m_bkKeyCheckDelay != null) {
			m_bkKeyCheckDelay.stopDelayCheck();
		}
		m_presenter.detach();
		super.onDestroy();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			/// check if foreground fragment responses to back key
			WrappedFragment wrappedFragment = m_fragmentBox.getCurrentFragment();
			if (wrappedFragment != null) {
				if (wrappedFragment.triggerBackkey()) {
					return true;
				}
			}
			/// check if naviback required
			try {
				if (m_fragmentBox.naviBack()) {
					return true;
				}
			} catch (Exception e) {
				Logger.error(e);
			}

//            else {
			if(!m_bkKeyCheckDelay.isDelayElapsed()) {
				return super.onKeyDown(keyCode, event);
			}
			m_bkKeyCheckDelay.startDelayCheck();
			iCommon.showToast(this, R.string.back_key_press_hint);
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	protected abstract PresenterType createPresenter();

	/**
	 * return notification type (MType) of this activity
	 * derived classes should override this to provide types they are concerned about
	 * @return
	 */
	protected int getNotificationType() {
		return 0;
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

	@Override
	public void onForward(String tag) {
		try {
			m_fragmentBox.changeToFragmentByTag(tag, true, null);
		} catch (Exception e) {
			Logger.error(e);
		}
	}

	@Override
	public void onForward(String tag, int[] animations) {
		try {
			m_fragmentBox.changeToFragmentByTag(tag, true, animations);
		} catch (Exception e) {
			Logger.error(e);
		}
	}

	@Override
	public void onForward(String tag, String[] params, int[] animations) {
		try {
			m_fragmentBox.changeToFragmentByTag(tag, params, true, animations);
		} catch (Exception e) {
			Logger.error(e);
		}
	}

	@Override
	public void onForward(String tag, Parcelable params, int[] animations) {
		try {
			m_fragmentBox.changeToFragmentByTag(tag, params, true, animations);
		} catch (Exception e) {
			Logger.error(e);
		}
	}

	@Override
	public void onForward(String tag, Parcelable[] params, int[] animations) {
		try {
			m_fragmentBox.changeToFragmentByTag(tag, params, true, animations);
		} catch (Exception e) {
			Logger.error(e);
		}
	}

	/**
	 * show first fragment, needn't add to back stack in general
	 * @param tag
	 * @param addToBackStack
	 * @param animations
	 */
	protected void showFirstFragment(String tag, boolean addToBackStack, int[] animations) {
		try {
			m_fragmentBox.changeToFragmentByTag(tag, addToBackStack, animations);
		} catch (Exception e) {
			Logger.error(e);
		}
	}

	@Override
	public void onNaviBack() {
		try {
			m_fragmentBox.naviBack();
		} catch (Exception e) {
			Logger.error(e);
		}
	}
}
