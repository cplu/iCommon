package com.windfindtech.icommon.fragment.navigate;

import android.content.Context;

import com.windfindtech.icommon.R;
import com.windfindtech.icommon.util.Utils;

import java.util.ArrayList;

public class AppAboutAdapter extends NavListAdapter {

	public static final int USER_AGREEMENT_INDEX = 0;
	public static final int VERSION_UPDATE_INDEX = 1;
//	public enum DeviceState{
//		uncertain,
//		accepted,
//		rejected
//	}

	public AppAboutAdapter(Context context, int line_item_id) {
		super(context, line_item_id);
		ArrayList<LineItem> items = new ArrayList<>();
		/// "user agreement"
		items.add(new LineItem(0,
			context.getString(R.string.useragreement_lineitem),
			context.getString(R.string.invalid_description),
			R.drawable.btn_arrow_forward));
		/// "version update"
		items.add(new LineItem(0,
			context.getString(R.string.versionupdate_lineitem),
			Utils.s_appVersionName,
			R.drawable.btn_arrow_forward));

		setData(items, false);
	}
}
