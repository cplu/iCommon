package com.windfindtech.icommon.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.windfindtech.icommon.dialog.callback.BaseCallback;
import com.windfindtech.icommon.rx.RXLocalSubscriber;
import com.windfindtech.icommon.rx.RXTask;

import org.pmw.tinylog.Logger;

import butterknife.ButterKnife;

/**
 * Created by cplu on 2016/8/15.
 *  base dialog (fragment)
 *  derived classes should implement dialogs as DialogFragment
 *  derived classes should provide static creation method to create a DialogFragment
 *  see EditConfigDialog.create for a sample create method
 */
public abstract class BaseDialog extends DialogFragment {
	private static final String UNIQUE_ID = "UniqueID";
//	protected static final String LISTENER_TAG = "ListenerTag";
//	protected static final String DIALOG_TAG = "DialogTag";

//	protected static int DIALOG_ID = 1;

	protected View m_root;
	protected long m_uniqueID;


	protected static void showDialog(final FragmentManager manager, final BaseDialog dialog, final String tag) {
//		FragmentTransaction transaction = manager.beginTransaction();
		RXTask.run(new RXLocalSubscriber<Object>() {
			@Override
			public void onNext(Object o) {
				DialogFragment fragment = (DialogFragment) manager.findFragmentByTag(tag);
				FragmentTransaction transaction = manager.beginTransaction();
				if(fragment != null) {
					Logger.debug("try to remove fragment " + fragment);
//					fragment.dismiss();
					transaction.remove(fragment);
				}
				transaction.add(dialog, tag)
					.commitNow();
//				dialog.show(manager, tag);
			}
		});
	}

	public static void close(final FragmentManager manager, final String tag) {
		RXTask.run(new RXLocalSubscriber<Object>() {
			@Override
			public void onNext(Object o) {
				DialogFragment fragment = (DialogFragment) manager.findFragmentByTag(tag);
				if(fragment != null) {
					FragmentTransaction transaction = manager.beginTransaction();
					transaction.remove(fragment).commitNow();
				}
			}
		});
	}

	public static boolean isShowing(FragmentManager manager, String tag) {
		DialogFragment fragment = (DialogFragment) manager.findFragmentByTag(tag);
		if(fragment != null && fragment.isAdded()) {
			return true;
		} else {
			return false;
		}
	}

	public Bundle getBaseBundle(long id) {
		Bundle bundle = new Bundle();
		bundle.putLong(UNIQUE_ID, id);
		return bundle;
	}

	protected abstract int getLayoutId();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		m_uniqueID = getArguments().getLong(UNIQUE_ID);
	}

	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
	                         @Nullable Bundle savedInstanceState) {
		m_root = inflater.inflate(getLayoutId(), container, false);
		ButterKnife.bind(this, m_root);
		return m_root;
	}

	/**
	 * get callback from parent fragment, or activity if no parent fragment
	 * this should be called in onCreate
	 * @return  a callback for dialog, derived classes should cast this callback to a concrete type of BaseCallback for later use
	 */
	protected BaseCallback getCallback() {
		Fragment parent = getParentFragment();
		if(parent != null) {
			return (BaseCallback)parent;
		} else {
			return (BaseCallback)getActivity();
		}
	}
}
