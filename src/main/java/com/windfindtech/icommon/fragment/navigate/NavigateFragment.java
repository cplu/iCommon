/**
 *
 */
package com.windfindtech.icommon.fragment.navigate;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.windfindtech.icommon.R;
import com.windfindtech.icommon.R2;
import com.windfindtech.icommon.fragment.management.WrappedFragment;
import com.windfindtech.icommon.iCommon;
import com.windfindtech.icommon.jsondata.user.UserData;
import com.windfindtech.icommon.jsondata.webservice.DeviceData;
import com.windfindtech.icommon.jsondata.webservice.VersionCheck;
import com.windfindtech.icommon.mvp.model.MLocalReceiver;
import com.windfindtech.icommon.mvp.presenter.navigate.INavigatePresenter;
import com.windfindtech.icommon.mvp.view.navigate.INavigateView;
import com.windfindtech.icommon.view.recycler.MVPAdapter;

import org.pmw.tinylog.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

/**
 * @author cplu
 *         fragment template that could have a title bar and a back button inside the bar
 * modified at 2016/07/29:  rename to NavigateFragment
 */
public abstract class NavigateFragment<ViewType extends INavigateView, PresenterType extends INavigatePresenter<ViewType>>
	extends WrappedFragment<ViewType, PresenterType>
	implements INavigateView {
	public static final int RESULT_LOAD_IMAGE = 11;

	private static final String PHOTO_CAPTURED = "photoCaptured";
	protected NavListAdapter m_adapter;
	protected String[] m_listTags = new String[1];
	//	protected Handler m_handler;
	protected View m_rootView;
	@Nullable
	@BindView(R2.id.list_recycled)
	RecyclerView m_listRecycled;

	//	protected static final int ACCOUNT_INFO_CACHED_CODE = 1;
	@Nullable
	@BindView(R2.id.fragment_title_txt)
	TextView m_title;
	@Nullable
	@BindView(R2.id.fragment_back_btn)
	ImageView m_backBtn;
	@Nullable
	@BindView(R2.id.fragment_back_txt)
	TextView m_backBtnTxt;

//	protected INavigatePresenter m_naviPresenter;

	public NavigateFragment() {
//		m_naviPresenter = new NavigatePresenterImpl(this);
	}

	@LayoutRes
	protected abstract int getLayoutID();

	@StringRes
	protected abstract int getTitleID();

	@Override
	public void onDeviceListChanged(ArrayList<DeviceData>[] devices) {
	}

	@Override
	public void onVersionCheck(VersionCheck versionCheck) {
	}

	@Override
	public void onReservedMessage(Boolean hasReservedMessage) {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		m_handler = new Handler();
	}

	protected abstract NavListAdapter createNavAdapter();

	/**
	 * Warning: in NavigateFragment, onCreateView will always create a new layout once called
	 * derived classes needn't override onCreateView if it gives a valid layout id in getLayoutID
	 * all view related initial codes should be in onViewCreated
	 *
	 * @param inflater
	 * @param container
	 * @param savedInstanceState
	 * @return
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		m_rootView = inflater.inflate(getLayoutID(), container, false);
		return m_rootView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		m_rootView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				//logger.debug("touch event " + event.getAction());
				boolean ret = onLayoutTouch(v, event);
				return ret;
			}
		});

		if (m_title != null) {
			m_title.setText(getString(getTitleID()));
		}

		if (m_listRecycled != null) {
			prepareRecyclerView();
		}
	}

	/**
	 * derived classes may use this to manually bind recycler view to avoid id conflict in library project
	 * this method also calls {@link #prepareRecyclerView()} after binding
	 */
	protected void bindRecyclerView(int layoutID) {
		m_listRecycled = ButterKnife.findById(m_rootView, layoutID);
		prepareRecyclerView();
	}

	/**
	 * initialize {@link #m_listRecycled} and create adapter for it with a valid item click listener
	 */
	protected void prepareRecyclerView() {
		m_listRecycled.setLayoutManager(new LinearLayoutManager(getActivity()));
		m_adapter = createNavAdapter();
		if(m_adapter != null) {
			m_listRecycled.setAdapter(m_adapter);
			m_adapter.setOnItemClickListener(new MVPAdapter.OnItemClickListener() {
				@Override
				public void onItemClick(View view, int position, long id) {
					if (!isWebserviceLoggedIn()) {
						return;
					}
					performItemClick(position, id);
				}
			});
		}
	}

	@Optional
	@OnClick({R2.id.fragment_back_btn, R2.id.fragment_back_txt})
	public void onBackClick() {
		m_primaryCallback.onNaviBack();
	}

	/**
	 * root view of this fragment is touched
	 * return true to stop touch event from propagation
	 *
	 * @param v
	 * @param event
	 * @return
	 */
	protected boolean onLayoutTouch(View v, MotionEvent event) {
		return true;
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	public void performItemClick(int position, long id) {
		/// user config item is clicked
		if (position < m_listTags.length && m_listTags[position] != null) {
			m_primaryCallback.onForward(m_listTags[position]);
		}
	}

	@Override
	public void onPause() {
		iCommon.hideInputMethod(this.getActivity());
		super.onPause();
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	@Override
	public void onDestroy() {
		if (m_adapter != null) {
			m_adapter.clearAll();
		}
		super.onDestroy();
	}

	/**
	 * check if webservice is logged in
	 * this Fragment should subscribe to MType.WS_RECEIVER, otherwise isWebserviceLoggedIn makes no sense
	 *
	 * @return
	 */
	protected boolean isWebserviceLoggedIn() {
		int ws_status = m_presenter.getWSStatus();
		if (ws_status == MLocalReceiver.WS_NOT_LOGGED_IN) {
			iCommon.showToast(getActivity(), R.string.ws_not_logged_in);
			m_primaryCallback.onWsNotLoggedIn();
		} else if (ws_status == MLocalReceiver.WS_IN_PROGRESS) {
			iCommon.showToast(getActivity(), R.string.wft_login_inprogress);
		}
		return ws_status == MLocalReceiver.WS_LOGGED_IN;
	}

	public long getItemID(int position) {
		if (m_adapter != null) {
			return m_adapter.getItemId(position);
		}
		return -1;
	}

//	protected class UserConfigCallback implements WSCallback<UserData> {
//
//		@Override
//		public void onSuccess(int code, UserData ret) {
//			WFTConfig.instance().storeUserConfig(ret);
//			onUserInfoUpdated(ret);
//		}
//
//		@Override
//		public void onFailed(WSErrorResponse reason) {
//			on_user_info_failed(reason != null ? reason.getMessage() : null);
//		}
//	}

//	protected void on_user_info_failed(String reason) {
//
//	}
//
//	protected void onUserInfoUpdated(UserData ret) {
//
//	}

//	protected UserConfigCallback m_userConfigCallback = new UserConfigCallback();

	protected void pickUserAvatar() {
		if (m_presenter.isAvatarUploading()) {
			iCommon.showToast(getActivity(), R.string.avatar_update_in_progress);
		} else if (isWebserviceLoggedIn()) {
			try {
				Intent intent = new Intent();
				intent.setType("image/*");
				intent.setAction(Intent.ACTION_GET_CONTENT);
				intent.addCategory(Intent.CATEGORY_OPENABLE);
				/// create camera intent
				PackageManager pm = getActivity().getPackageManager();
				if (pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
					Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					File path = getAvatarExternalFile();
					takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(path));
					/// create chooser
					String pickTitle = getString(R.string.select_picture);
					Intent chooserIntent = Intent.createChooser(intent, pickTitle);
					chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{takePhotoIntent});
					startActivityForResult(chooserIntent, RESULT_LOAD_IMAGE);
				} else {
					startActivityForResult(intent, RESULT_LOAD_IMAGE);
				}
			} catch (Exception e) {
				iCommon.showToast(getActivity(), R.string.avatar_invalid);
			}
		}
	}

	protected void parseImageCaptureResult(Intent data) {
		Logger.debug("parseImageCaptureResult");
		Uri selectedImage;
		if (null != data) {
			selectedImage = data.getData();
			Logger.debug("get image from data.getData()");
		} else {
			File file = getAvatarExternalFile();
			Logger.debug("get image from external file");
			selectedImage = Uri.fromFile(file);
		}
		try {
			Pair<InputStream, Integer> pair = getBitmapInfo(selectedImage);
			m_presenter.handleImageCapture(pair.first, pair.second, true);
		} catch (Exception e) {
			Logger.error(e);
		}
//			Utils.saveBoolean(AVATAR_UPDATED_KEY, true);
	}

	/**
	 * get bitmap info from uri
	 *
	 * @param uri
	 * @return an inputstream of the bitmap data and an integer for orientation of bitmap
	 * @throws Exception
	 */
	private Pair<InputStream, Integer> getBitmapInfo(Uri uri) throws Exception {
		InputStream inputstream;
		int orientation;
		if (uri.getScheme() != null && uri.getScheme().equals("content")) {
			inputstream = getActivity().getContentResolver().openInputStream(uri);
			orientation = iCommon.getOrientation(getActivity(), uri);
		} else {
			File file = new File(uri.getPath());
			inputstream = new FileInputStream(file);
			ExifInterface exif = new ExifInterface(file.getAbsolutePath());
			int ori = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
			orientation = iCommon.parseExifOrientation(ori);
		}
		return Pair.create(inputstream, orientation);
	}

	protected File getAvatarExternalFile() {
		File path = iCommon.getAvailableExternalFile(getActivity(), Environment.DIRECTORY_PICTURES, PHOTO_CAPTURED);
		return path;
	}

	@Override
	public boolean avatarConcerned() {
		return false;
	}

	@Override
	public void onAvatar(Bitmap bitmap) {

	}

	@Override
	public boolean usrDataConcerned() {
		return true;
	}

	@Override
	public void onUserData(UserData userData) {

	}

	/**
	 * notify avatar uploading success/failed
	 *
	 * @param success success or failed
	 */
	@Override
	public void onAvatarUploadResult(boolean success) {
		if (isStarted) {
			if (success) {
				iCommon.showToast(getActivity(), R.string.avatar_uploaded);
			} else {
				iCommon.showToast(getActivity(), R.string.avatar_upload_failed);
			}
		}
	}
}
