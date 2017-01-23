package com.windfindtech.icommon.fragment.management;

import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.windfindtech.icommon.R;
import com.windfindtech.icommon.iCommon;

import org.pmw.tinylog.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by cplu on 2016/04/21.
 * This class arranges fragments shown on activity
 * Each fragment should extend WrappedFragment, and has a public constructor with no parameters
 */
public class FragmentBox {
	private FragmentManager m_fragmentManager;
	public static final int ACTIVITY_CONTENT_ID = android.R.id.content;

	private HashMap<String, Class> m_fragmentTags;

	private static final int[] DEFAULT_ANIMATION_RESOURCE_ID = {R.anim.slide_right_enter, R.anim.alpha_exit, R.anim.alpha_enter, R.anim.slide_right_exit};

	public FragmentBox(FragmentActivity activity, HashMap<String, Class> fragmentTags) {
		m_fragmentManager = activity.getSupportFragmentManager();
		m_fragmentTags = fragmentTags;
		/// add all user fragments
		m_fragmentTags.putAll(TagUser.s_FragmentTags);
	}

	/**
	 * change to fragment with tag, without passing params
	 * @param tag               tag associated with target fragment
	 * @param addToBackstack    whether the changing should be added to back stack
	 * @param animations        animations the fragment will perform. If this is null, use DEFAULT_ANIMATION_RESOURCE_ID
	 * @throws Exception
	 */
	public void changeToFragmentByTag(String tag, boolean addToBackstack, int[] animations) throws Exception {
		WrappedFragment fragment = prepareFragment(tag);
		replaceFragment(tag, fragment, addToBackstack, animations != null ? animations : DEFAULT_ANIMATION_RESOURCE_ID);
	}

	/**
	 * change to fragment with tag
	 * other overrides of this function take different types of params
	 * @param tag               tag associated with target fragment
	 * @param params            params passed to the fragment
	 * @param addToBackstack    whether the changing should be added to back stack
	 * @param animations        animations the fragment will perform. If this is null, use DEFAULT_ANIMATION_RESOURCE_ID
	 * @throws Exception
	 */
	public void changeToFragmentByTag(String tag, Parcelable params, boolean addToBackstack, int[] animations) throws Exception {
		WrappedFragment fragment = prepareFragment(tag);
		if (params != null) {
			fragment.prepareParams(params);
		}
		replaceFragment(tag, fragment, addToBackstack, animations != null ? animations : DEFAULT_ANIMATION_RESOURCE_ID);
	}

	/**
	 * change to fragment with tag
	 * other overrides of this function take different types of params
	 * @param tag               tag associated with target fragment
	 * @param params            params passed to the fragment
	 * @param addToBackstack    whether the changing should be added to back stack
	 * @param animations        animations the fragment will perform. If this is null, use DEFAULT_ANIMATION_RESOURCE_ID
	 * @throws Exception
	 */
	public void changeToFragmentByTag(String tag, Parcelable[] params, boolean addToBackstack, int[] animations) throws Exception {
		WrappedFragment fragment = prepareFragment(tag);
		if (params != null) {
			fragment.prepareParams(params);
		}
		replaceFragment(tag, fragment, addToBackstack, animations != null ? animations : DEFAULT_ANIMATION_RESOURCE_ID);
	}

	/**
	 * change to fragment with tag
	 * other overrides of this function take different types of params
	 * @param tag               tag associated with target fragment
	 * @param params            params passed to the fragment
	 * @param addToBackstack    whether the changing should be added to back stack
	 * @param animations        animations the fragment will perform. If this is null, use DEFAULT_ANIMATION_RESOURCE_ID
	 * @throws Exception
	 */
	public void changeToFragmentByTag(String tag, String[] params, boolean addToBackstack, int[] animations) throws Exception {
		WrappedFragment fragment = prepareFragment(tag);
		if (params != null) {
			fragment.prepareParams(params);
		}
		replaceFragment(tag, fragment, addToBackstack, animations != null ? animations : DEFAULT_ANIMATION_RESOURCE_ID);
	}

	/**
	 * get fragment by tag or create a new one by default constructor
	 * @param tag
	 * @return
	 * @throws Exception
	 */
	private WrappedFragment prepareFragment(String tag) throws Exception{
		WrappedFragment fragment = (WrappedFragment) m_fragmentManager.findFragmentByTag(tag);
		if (fragment == null) {
			Logger.debug("recreate fragment with tag " + tag);
			Class c = m_fragmentTags.get(tag);
//			Method m = c.getConstructor().newInstance(null);
			/// WrappedFragment must have a default constructor without parameters
			fragment = (WrappedFragment) c.getConstructor().newInstance();
		}
		return fragment;
	}

	/**
	 * replace fragment with tag
	 *
	 * @param tag             tag associated with target fragment
	 * @param fragment
	 * @param addToBackstack  whether the changing should be added to back stack
	 * @param animations      animations for transaction, 4 in all representing enter, exit, popEnter and popExit
	 */
	private void replaceFragment(String tag, WrappedFragment fragment, boolean addToBackstack, int[] animations) {
		if (!fragment.isAdded()) {
			FragmentTransaction transaction = m_fragmentManager.beginTransaction();
			if (iCommon.getApiLevel() >= 13) {
				transaction.setCustomAnimations(animations[0], animations[1], animations[2], animations[3]);
			} else {
				transaction.setCustomAnimations(animations[0], animations[1]);
			}
			Fragment current = m_fragmentManager.findFragmentById(ACTIVITY_CONTENT_ID);
			if(current != null) {
				transaction.hide(current);
			}
			transaction.add(ACTIVITY_CONTENT_ID, fragment, tag);

			if (addToBackstack) {
				transaction.addToBackStack(null);
			}
			transaction.commit();
		}
	}

	public void popbackToRoot() throws Exception {
		m_fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
	}

	public boolean naviBack() throws Exception {
		if (m_fragmentManager.getBackStackEntryCount() > 0) {
			try {
				m_fragmentManager.popBackStackImmediate();
			} catch (Exception e) {
				Logger.error(e);
			}
			return true;
		}
		return false;
	}

	/**
	 * Get available fragment that currently shown on top
	 *
	 * @return
	 */
	public WrappedFragment getCurrentFragment() {
		List<Fragment> frgList = m_fragmentManager.getFragments();
		if (frgList != null && frgList.size() > 0) {
			ListIterator<Fragment> it = frgList.listIterator(frgList.size());
			while (it.hasPrevious()) {
				Fragment fragment = it.previous();
				if(fragment!= null && fragment instanceof WrappedFragment) {
					return (WrappedFragment)fragment;
				}
			}
			return null;
		} else {
			return null;
		}
	}
}
