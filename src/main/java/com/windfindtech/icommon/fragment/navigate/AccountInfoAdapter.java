package com.windfindtech.icommon.fragment.navigate;


import android.content.Context;

import com.windfindtech.icommon.R;

import java.util.ArrayList;

/**
 * 用户信息界面Adapter
 *
 * @author cplu
 */
public class AccountInfoAdapter extends NavListAdapter {
    public static final int NICKNAME_ITEM_INDEX = 0;
    public static final int SIGN_ITEM_INDEX = 1;
    public static final int ACCOUNT_DETAIL_INDEX = 2;
    //    public static final int EMPTY_INDEX = 3;
    public static final int CHANGE_PASSWORD_INDEX = 4;
    public static final int RESERVED_MSG_INDEX = 5;

    public AccountInfoAdapter(Context context, int line_item_id) {
        super(context, line_item_id);
        ArrayList<LineItem> items = new ArrayList<>();
        /// "nickname"
        items.add(new LineItem(0,
                context.getString(R.string.nickname_lineitem),
                context.getString(R.string.user_data_not_set),
                R.drawable.btn_arrow_forward));
        /// "signature"
        items.add(new LineItem(0,
                context.getString(R.string.sign_lineitem),
                context.getString(R.string.user_data_not_set),
                R.drawable.btn_arrow_forward));
        /// "details"
        items.add(new LineItem(0,
                context.getString(R.string.details_lineitem),
                context.getString(R.string.details_lineitem_description),
                R.drawable.btn_arrow_forward));
        /// separator white space
        items.add(null);
        /// "change password"
        items.add(new LineItem(0,
                context.getString(R.string.changepassword_lineitem),
                "",
                R.drawable.btn_arrow_forward));
        /// "personal message"
        items.add(new LineItem(0,
                context.getString(R.string.personal_message_lineitem),
                context.getString(R.string.personal_message_description),
                R.drawable.btn_arrow_forward));

        setData(items, false);
    }

    /**
     * Set descriptions in this adapter
     * Warning: This method does NOT call notifydatasetchanged
     *
     * @param nick_name
     * @param signature
     */
    public void setDataItemDescriptions(String nick_name, String signature) throws Exception {
        setDataDescription(NICKNAME_ITEM_INDEX,
                nick_name != null ? nick_name : m_ctx.getString(R.string.user_data_not_set),
                false);
        setDataDescription(SIGN_ITEM_INDEX,
                signature != null ? signature : m_ctx.getString(R.string.user_data_not_set),
                false);
    }

    /**
     * clearAll descriptions in this adapter
     * Warning: This method does NOT call notifydatasetchanged
     */
    public void clearDataItemDescriptions() throws Exception {
        setDataDescription(NICKNAME_ITEM_INDEX, null, false);
        setDataDescription(SIGN_ITEM_INDEX, null, false);
    }
}
