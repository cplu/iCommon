package com.windfindtech.icommon.fragment.navigate;

import android.content.Context;

import com.windfindtech.icommon.R;
import com.windfindtech.icommon.jsondata.enumtype.Gender;

import java.util.ArrayList;

public class AccountDetailAdapter extends NavListAdapter {
	public static final int EMAIL_ITEM_INDEX = 0;
	public static final int REALNAME_ITEM_INDEX = 1;
	public static final int AGE_ITEM_INDEX = 2;
	public static final int GENDER_INDEX_ITEM = 3;

	public AccountDetailAdapter(Context context, int line_item_id) {
		super(context, line_item_id);

		ArrayList<LineItem> items = new ArrayList<>();
		/// "email"
		items.add(new LineItem(0,
			context.getString(R.string.email_lineitem),
			context.getString(R.string.user_data_not_set),
			R.drawable.btn_arrow_forward));
		/// "real name"
		items.add(new LineItem(0,
			context.getString(R.string.realname_lineitem),
			context.getString(R.string.user_data_not_set),
			R.drawable.btn_arrow_forward));
		/// "age"
		items.add(new LineItem(0,
			context.getString(R.string.age_lineitem),
			context.getString(R.string.user_data_not_set),
			R.drawable.btn_arrow_forward));
		/// "gender"
		items.add(new LineItem(0,
			context.getString(R.string.gender_lineitem),
			context.getString(R.string.user_data_not_set),
			R.drawable.btn_arrow_forward));

		setData(items, false);
	}

	/**
	 * Set descriptions in this adapter
	 * Warning: This method does NOT call notifydatasetchanged
	 *
	 * @param email
	 * @param real_name
	 * @param age
	 * @param gender
	 */
	public void setDataItemDescriptions(String email, String real_name, int age, Gender gender) throws Exception {
		setDataDescription(EMAIL_ITEM_INDEX, email, false);
		setDataDescription(REALNAME_ITEM_INDEX, real_name, false);
		setDataDescription(AGE_ITEM_INDEX, String.valueOf(age), false);
		String gender_string;
		if (gender == Gender.Female) {
			gender_string = m_ctx.getString(R.string.female);
		} else if (gender == Gender.Male) {
			gender_string = m_ctx.getString(R.string.male);
		} else {
			gender_string = "";
		}
		setDataDescription(GENDER_INDEX_ITEM, gender_string, false);
	}

	@Override
	public void clearAll() {

	}
}
