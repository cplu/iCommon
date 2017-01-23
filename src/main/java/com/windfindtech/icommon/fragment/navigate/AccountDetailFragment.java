package com.windfindtech.icommon.fragment.navigate;

import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import com.windfindtech.icommon.R;
import com.windfindtech.icommon.dialog.GenderChoiceDialog;
import com.windfindtech.icommon.dialog.EditConfigDialog;
import com.windfindtech.icommon.dialog.callback.ChoiceCallback;
import com.windfindtech.icommon.dialog.callback.EditCallback;
import com.windfindtech.icommon.iCommon;
import com.windfindtech.icommon.jsondata.enumtype.Gender;
import com.windfindtech.icommon.jsondata.user.UserData;
import com.windfindtech.icommon.jsondata.webservice.WSErrorResponse;
import com.windfindtech.icommon.mvp.model.MLocalReceiver;
import com.windfindtech.icommon.mvp.model.MType;
import com.windfindtech.icommon.mvp.model.local.UsrDataManager;
import com.windfindtech.icommon.mvp.presenter.navigate.AccountDetailPresenter;
import com.windfindtech.icommon.mvp.view.navigate.IAccountDetailView;

import org.pmw.tinylog.Logger;

/**
 * 个人中心-->个人信息-->详细信息
 */
public class AccountDetailFragment extends NavigateFragment<IAccountDetailView, AccountDetailPresenter>
	implements IAccountDetailView, EditCallback<String>, ChoiceCallback {

	//	private EditConfigDialog m_userConfigDlg;
//	private GenderChoiceDialog m_choiceDlg;

	/**
	 * regular expressions for each input item
	 */
	private static final String[] INPUT_REGS = new String[]{"^[a-zA-Z0-9_-]+[\\.a-zA-Z0-9_-]*@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$", ".+", "[0-9]{1,3}", ".+",};

	public AccountDetailFragment() {
	}

	@Override
	protected int getLayoutID() {
		return R.layout.fragment_account_detail;
	}

	@Override
	protected int getTitleID() {
		return R.string.title_account_detail;
	}

	@Override
	protected int getNotificationType() {
		return MType.WS_RECEIVER;
	}

	@Override
	protected AccountDetailPresenter createPresenter() {
		return new AccountDetailPresenter();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		m_listTags = new String[]{null,   // 电子邮箱
			null,   // 真实姓名
			null,   // 年龄
			null,   // 性别
		};
	}

	@Override
	protected NavListAdapter createNavAdapter() {
		return new AccountDetailAdapter(this.getActivity(), R.layout.list_item_with_icon);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onUserData(UserData ret) {
		try {
			((AccountDetailAdapter) m_adapter).setDataItemDescriptions(ret.getEmail(), ret.getRealName(), ret.getAge(), ret.getGender());
			m_adapter.notifyDataSetChanged();
		} catch (Exception e) {
			Logger.error(e);
		}
	}

	protected void clearData() {
		try {
			((AccountDetailAdapter) m_adapter).setDataItemDescriptions(null, null, 0, null);
			m_adapter.notifyDataSetChanged();
		} catch (Exception e) {
			Logger.error(e);
		}
	}

	@Override
	public void performItemClick(final int position, long id) {
		if (!isWebserviceLoggedIn()) {
			return;
		}
//		if (m_userConfigDlg != null) {
//			m_userConfigDlg.cancel();
//		}
		if (m_adapter == null) {
			return;
		}

		final UserData userData = UsrDataManager.usrDat.getData();
		if(userData == null) {
			return;
		}

		if (position == AccountDetailAdapter.GENDER_INDEX_ITEM) {
			String title = getString(R.string.gender_lineitem);
			GenderChoiceDialog.create(position, title, getChildFragmentManager());
		} else {
			String reg = INPUT_REGS[position];
//            String[] infos = ((AccountDetailAdapter)m_adapter).get_item_infos(position);
//            if(infos == null){
//                return;
//            }
			/// use posiion as dialog id
			EditConfigDialog.create(position, getInfoItemTitle(position), getInfoItemContent(position, userData), reg, R.string.account_info_format_error
				, position == AccountDetailAdapter.AGE_ITEM_INDEX ? EditorInfo.TYPE_CLASS_NUMBER : 0
				, getChildFragmentManager());
		}
	}


	/**
	 * get info item title
	 *
	 * @param position
	 * @return
	 */
	public String getInfoItemTitle(int position) {
		if (position == AccountDetailAdapter.EMAIL_ITEM_INDEX) {
			return getString(R.string.email_lineitem);
		} else if (position == AccountDetailAdapter.REALNAME_ITEM_INDEX) {
			return getString(R.string.realname_lineitem);
		} else if (position == AccountDetailAdapter.AGE_ITEM_INDEX) {
			return getString(R.string.age_lineitem);
		} else if (position == AccountDetailAdapter.GENDER_INDEX_ITEM) {
			return getString(R.string.gender_lineitem);
		}
		return null;
	}


	/**
	 * get info item content
	 *
	 * @param index
	 * @param config
	 * @return
	 */
	public String getInfoItemContent(int index, UserData config) {
		if (index == AccountDetailAdapter.EMAIL_ITEM_INDEX) {
			return config.getEmail();
		} else if (index == AccountDetailAdapter.REALNAME_ITEM_INDEX) {
			return config.getRealName();
		} else if (index == AccountDetailAdapter.AGE_ITEM_INDEX) {
			return String.valueOf(config.getAge());
		} else if (index == AccountDetailAdapter.GENDER_INDEX_ITEM) {
			return config.getGender() !=
			       null ? config.getGender().toString() : Gender.Male.toString();
		}
		return null;
	}

	@Override
	public void onEditResult(long id, boolean success, String result) {
		/// should clone a copy of user_config to ensure that the value remains unchanged unless it is successfully updated to web server
		final UserData userDataOrigin = UsrDataManager.usrDat.getData();
		if (!success || userDataOrigin == null) {
			return;
		}
		final UserData userData = (UserData)userDataOrigin.clone();

		if (id == AccountDetailAdapter.EMAIL_ITEM_INDEX) {
			userData.setEmail(result);
			m_presenter.updateAccountInfo((int)id, userData);
		} else if (id == AccountDetailAdapter.REALNAME_ITEM_INDEX) {
			userData.setRealName(result);
			m_presenter.updateAccountInfo((int)id, userData);
		} else if (id == AccountDetailAdapter.AGE_ITEM_INDEX) {
			try {
				userData.setAge(Integer.parseInt(result));
				m_presenter.updateAccountInfo((int)id, userData);
			} catch (NumberFormatException e) {
				Logger.debug("age is not a valid integer");
			}
		}
	}

	@Override
	public void onChoiceResult(long id, boolean success, int choice) {
		final UserData userDataOrigin = UsrDataManager.usrDat.getData();
		if (!success || userDataOrigin == null) {
			return;
		}
		final UserData userData = (UserData)userDataOrigin.clone();

		final Gender newGender = (choice == Gender.Female.ordinal()) ? Gender.Female : Gender.Male;
		userData.setGender(newGender);

		m_presenter.updateAccountInfo((int)id, userData);
	}

	@Override
	public void onDestroyView() {
//		if (m_userConfigDlg != null) {
//			m_userConfigDlg.cancel();
//			m_userConfigDlg = null;
//		}
		super.onDestroyView();
	}

	@Override
	public void onWSChanged(int status) {
		if (status == MLocalReceiver.WS_LOGGED_IN) {
			m_presenter.getUserData();
		} else {
			clearData();
		}
	}


	@Override
	public void onUpdateAccountInfoSuccess(int position, UserData userData) {
		try {
			if (position == AccountDetailAdapter.GENDER_INDEX_ITEM) {
				UsrDataManager.usrDat.setGender(userData.getGender());
			}
			if (position == AccountDetailAdapter.EMAIL_ITEM_INDEX) {
				UsrDataManager.usrDat.setEmail(userData.getEmail());
			} else if (position == AccountDetailAdapter.REALNAME_ITEM_INDEX) {
				UsrDataManager.usrDat.setRealName(userData.getRealName());
			} else if (position == AccountDetailAdapter.AGE_ITEM_INDEX) {
				UsrDataManager.usrDat.setAge(userData.getAge());
			}
			onUserData(userData);
			iCommon.showToast(getActivity(), R.string.account_update_success);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onUpdateAccountInfoFailed(WSErrorResponse reason) {
		iCommon.showToast(getActivity(), R.string.account_update_failed);
	}
}
