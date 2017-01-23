package com.windfindtech.icommon.fragment.navigate;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ExifInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.windfindtech.icommon.R;
import com.windfindtech.icommon.R2;
import com.windfindtech.icommon.bitmap.DrawableManager;
import com.windfindtech.icommon.dialog.EditConfigDialog;
import com.windfindtech.icommon.dialog.callback.EditCallback;
import com.windfindtech.icommon.fragment.management.TagUser;
import com.windfindtech.icommon.iCommon;
import com.windfindtech.icommon.jsondata.user.UserData;
import com.windfindtech.icommon.mvp.model.MLocalReceiver;
import com.windfindtech.icommon.mvp.model.MType;
import com.windfindtech.icommon.mvp.model.local.ReservedMessageManager;
import com.windfindtech.icommon.mvp.model.local.UsrDataManager;
import com.windfindtech.icommon.mvp.presenter.navigate.AccountInfoPresenter;
import com.windfindtech.icommon.mvp.view.navigate.IAccountInfoView;
import com.windfindtech.icommon.view.CircleImageDrawable;

import org.pmw.tinylog.Logger;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 个人中心-->个人信息
 *
 * @author cplu
 */
public class AccountInfoFragment extends NavigateFragment<IAccountInfoView, AccountInfoPresenter>
	implements IAccountInfoView, EditCallback<String> {
	//private static final int WEATHER_RETRIEVAL_DELAY = 5 * 60 * 1000;
	private static final String AVATAR_CACHE_DIR = "avatar_cache_directory";
	//private static final String AVATAR_CACHE_FILE_NAME = "avatar.jpg";

	//    private static final int AVATAR_SIZE_LIMIT = 128 * 1024;
	private static final int AVATAR_WIDTH_LIMIT = 512;
	private static final int AVATAR_HEIGHT_LIMIT = 512;

	@BindView(R2.id.layout_avatar)
	LinearLayout m_avatarLayout;
	@BindView(R2.id.account_avatar_image)
	ImageView m_avatarImage;
	@BindView(R2.id.log_out_btn)
	Button m_logoutBtn;
	//    private ImageView m_gender_image;
//    private TextView m_account_info_nickname;
//    private TextView m_account_info_signature;
	//private TextView m_weather_view;
//	private EditConfigDialog m_userconfigDlg;
//	private long[] m_editID = new long[6];  /// 6 dialog id at most
//	private static final long EDIT

	//    private boolean m_avatar_finish;
//	private boolean m_bImageCaptureFromActivityResult = false;
//	private NavListAdapter.LineItem m_item;
	//    private Bitmap m_avatarBitmap;

	public AccountInfoFragment() {
		setPageName("AccountInfoView");
	}

	@Override
	protected int getLayoutID() {
		return R.layout.fragment_account_info;
	}

	@Override
	protected int getTitleID() {
		return R.string.title_account_info;
	}

	@Override
	protected int getNotificationType() {
		return MType.WS_RECEIVER
		       | MType.RESERVED_MESSAGE_NOTIFIER;
	}

	@Override
	protected AccountInfoPresenter createPresenter() {
		return new AccountInfoPresenter();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		m_listTags = new String[]{
			null,   // 昵称
			null,   // 个人签名
			TagUser.FRG_ACCOUNT_DETAIL,
			null,
			TagUser.FRG_CHANGE_PASSWORD,
			null,   // 预留信息
		};
	}

	@Override
	protected NavListAdapter createNavAdapter() {
		return new AccountInfoAdapter(this.getActivity(), R.layout.list_item_with_icon);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		onAvatar(DrawableManager.instance().getAssetBitmap("avatar_waiting_icon.jpg"));

	}

	@OnClick(R2.id.layout_avatar)
	public void onAvatarClick() {
		pickUserAvatar();
	}

	@OnClick(R2.id.log_out_btn)
	public void onLogoutBtnClick() {
		m_primaryCallback.onLogout();
	}

	//@TargetApi(Build.VERSION_CODES.KITKAT)
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Logger.debug("test: onActivityResult " + getClass().getSimpleName());
		if (requestCode == RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK) {
//			m_bImageCaptureFromActivityResult = true;
			parseImageCaptureResult(data);
		}
	}

	protected void clearData() {
		try {
			((AccountInfoAdapter) m_adapter).clearDataItemDescriptions();
			m_adapter.notifyDataSetChanged();
		} catch (Exception e) {
			Logger.error(e);
		}
	}

	@Override
	public void onUserData(UserData userData) {
		try {
			((AccountInfoAdapter) m_adapter).setDataItemDescriptions(userData.getNickName(), userData.getSignature());
			m_adapter.notifyDataSetChanged();
		} catch (Exception e) {
			return;
		}
	}

//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//
//        refresh_all_user_data();
//    }

//	@Override
//	public void onStart() {
//		super.onStart();
//		Boolean hasMessage = (Boolean)MNotifierManager.get(MType.RESERVED_MESSAGE_NOTIFIER).getOrUpdateData();
//		if(hasMessage != null) {
//			onReservedMessage(hasMessage);
//		}
//	}

	@Override
	public void performItemClick(final int position, long id) {
		if (m_adapter == null) {
			return;
		}
		if (!isWebserviceLoggedIn()) {
			return;
		}
		final UserData userData = UsrDataManager.usrDat.getData();
		if (userData == null) {
			return;
		}

		NavListAdapter.LineItem item = m_adapter.getData(position);
		if (position == AccountInfoAdapter.NICKNAME_ITEM_INDEX
		    || position == AccountInfoAdapter.SIGN_ITEM_INDEX
			) {
			EditConfigDialog.create(position, item.m_name, get_info_item_content(position, userData)
				, ".+", R.string.invalid_input_text
				, getChildFragmentManager());
		} else if (position == AccountInfoAdapter.RESERVED_MSG_INDEX) {
			EditConfigDialog.create(position, getString(R.string.personal_message_dialog_title)   /// title
				, null  /// default
				, ".{3,20}"
				, R.string.personal_message_invalid_hint,  /// invalid hint
				getChildFragmentManager());
		} else {
			super.performItemClick(position, id);
		}
	}

	@Override
	public void onDestroyView() {
//        if (m_rootView != null) {
//            ViewGroup parentViewGroup = (ViewGroup) m_rootView.getParent();
//            if (parentViewGroup != null) {
//                parentViewGroup.removeView(m_rootView);
//            }
//        }
		super.onDestroyView();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	private int to_exif_orientation(int orientation) {
		switch (orientation) {
			case 90:
				return ExifInterface.ORIENTATION_ROTATE_90;
			case 180:
				return ExifInterface.ORIENTATION_ROTATE_180;
			case 270:
				return ExifInterface.ORIENTATION_ROTATE_270;
			default:
				return ExifInterface.ORIENTATION_NORMAL;
		}
	}

	private int get_exif_rotation(int orientation) {
		switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				return 90;
			case ExifInterface.ORIENTATION_ROTATE_180:
				return 180;
			case ExifInterface.ORIENTATION_ROTATE_270:
				return 270;
			default:
				return 0;
		}
	}

	private void refresh_all_user_data() {
		m_presenter.getUserData();
//		if (!m_bImageCaptureFromActivityResult) {
//		Logger.debug("test: getUserAvatar");
		m_presenter.getUserAvatar();
//		} else {
		/// here we reset m_bImageCaptureFromActivityResult to allow refreshing avatar next time
//			m_bImageCaptureFromActivityResult = false;
//		}
	}

	private void clear_all_user_data() {
		if (isStarted) {
			clearData();
			m_avatarImage.setImageDrawable(new CircleImageDrawable(getActivity(), "avatar_waiting_icon.jpg"));
//            m_gender_image.setImageDrawable(getResources().getDrawable(R.drawable.icon_boy));
		}
	}

	@Override
	public void onWSChanged(int status) {
		if (status == MLocalReceiver.WS_LOGGED_IN) {
			Logger.debug("test: onWSChanged " + getClass().getSimpleName());
			Boolean hasReservedMessage = ReservedMessageManager.reservedMsgNotifier.getData();
			m_adapter.setDataHintNumber(AccountInfoAdapter.RESERVED_MSG_INDEX,
				(hasReservedMessage != null && hasReservedMessage) ? 0 : 1, true);
			refresh_all_user_data();
		} else {
			m_adapter.setDataHintNumber(AccountInfoAdapter.RESERVED_MSG_INDEX, 0, true);
			clear_all_user_data();
		}
//        if (m_adapter != null) {
//            m_adapter.notifyDataSetChanged();
//        }
	}

	@Override
	public void onReservedMessage(Boolean hasReservedMessage) {
		m_adapter.setDataHintNumber(AccountInfoAdapter.RESERVED_MSG_INDEX, hasReservedMessage ? 0 : 1, true);
	}

	private String get_info_item_content(int index, UserData config) {
		if (index == AccountInfoAdapter.NICKNAME_ITEM_INDEX) {
			return config.getNickName();
		} else if (index == AccountInfoAdapter.SIGN_ITEM_INDEX) {
			return config.getSignature();
		}
		return null;
	}

	@Override
	public boolean avatarConcerned() {
		return true;
	}

	@Override
	public void onAvatar(Bitmap bitmap) {
		try {
//            m_avatarImage.setRotation(rotation);
//            m_avatarBitmap = drawable.getBitmap();
			Logger.debug("test:onAvatar in " + getClass().getSimpleName() + " with stack " + Thread.currentThread().getStackTrace()[3]);
			m_avatarImage.setImageDrawable(new CircleImageDrawable(getActivity(), bitmap)
				.setBorderColor(R.color.avatar_border));
//            m_account_info_layout.setBackgroundDrawable(drawable);
		} catch (Exception e) {
			Logger.error(e);
		}
	}

	@Override
	public void onUpdateAccountInfoStatus(UserData userData, int position, boolean success) {
		if (success) {
			if (position == AccountInfoAdapter.NICKNAME_ITEM_INDEX) {
				UsrDataManager.usrDat.setNickName(userData.getNickName());
			} else if (position == AccountInfoAdapter.SIGN_ITEM_INDEX) {
				UsrDataManager.usrDat.setSignature(userData.getSignature());
			}
			if (isStarted) {
				/// update list
				onUserData(userData);
//                                        m_account_info_nickname.setText(user_config.getNickName());
//                                        m_account_info_signature.setText(user_config.getSignature() == null ? getResources().getString(R.string.not_set) : user_config.getSignature());

				///
				iCommon.showToast(getActivity(), R.string.account_info_update_success, m_adapter.getData(position).m_name);
			}
		} else {
			if (isStarted) {
				iCommon.showToast(getActivity(), R.string.account_info_update_failed);
			}
		}
	}

	@Override
	public void onReservedMsgSent(boolean success, String reservedMsg) {
		if (success) {
			iCommon.showToast(getActivity(), String.format(getString(R.string.personal_message_update_success), reservedMsg));
		} else {
			iCommon.showToast(getActivity(), R.string.personal_message_update_failed);
		}
	}

	@Override
	public void onEditResult(long id, boolean success, String result) {
		/// here id is the position of item in list
		/// should clone a copy of user_config to ensure that the value remains unchanged unless it is successfully updated to web server
		final UserData userDataOrigin = UsrDataManager.usrDat.getData();
		if (!success || userDataOrigin == null) {
			return;
		}
		final UserData userData = (UserData) userDataOrigin.clone();
		if (id == AccountInfoAdapter.NICKNAME_ITEM_INDEX) {
			userData.setNickName(result);
			m_presenter.updateAccountInfo(userData, AccountInfoAdapter.NICKNAME_ITEM_INDEX);
		} else if (id == AccountInfoAdapter.SIGN_ITEM_INDEX) {
			userData.setSignature(result);
			m_presenter.updateAccountInfo(userData, AccountInfoAdapter.SIGN_ITEM_INDEX);
		} else if (id == AccountInfoAdapter.RESERVED_MSG_INDEX) {
			m_presenter.sendReservedMsg(result);
		}
	}
}
