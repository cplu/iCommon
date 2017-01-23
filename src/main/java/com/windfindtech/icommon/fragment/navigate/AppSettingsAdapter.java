package com.windfindtech.icommon.fragment.navigate;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.windfindtech.icommon.R;
import com.windfindtech.icommon.R2;
import com.windfindtech.icommon.config.app.AppSettings;
import com.windfindtech.icommon.util.Gather;
import com.windfindtech.icommon.view.recycler.MVPAdapter;
import com.windfindtech.icommon.view.recycler.MVPViewHolder;

import org.pmw.tinylog.Logger;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnCheckedChanged;

/**
 * “个人”界面，管理个人信息
 *
 * @author cplu
 */
public class AppSettingsAdapter extends MVPAdapter<OptionItem, OptionItemHolder> {
//	private static final int OPTION_COUNT = 1;

	public AppSettingsAdapter(Context context) {
		super(context);
		OptionItem item = new OptionItem();
		item.m_content = context.getString(R.string.join_user_experience);
		item.m_checked = false;
		ArrayList<OptionItem> items = new ArrayList<>();
		items.add(item);
		setData(items, false);
	}

	@Override
	public OptionItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		return new OptionItemHolder(getInflated(parent, R.layout.list_item_option));
	}

//    /**
//     * perform a click event on the specified item
//     *
//     * @param position     the item's index in m_sections
//     * @param ui_interface the interface for callback
//     */
//    @Override
//    public void perform_click(int position, final UserTabInterface ui_interface) {
//    }

}

class OptionItemHolder extends MVPViewHolder<OptionItem> {
	@BindView(R2.id.option_content)
	TextView m_option_content;
	@BindView(R2.id.option_item_checkbox)
	CheckBox m_checked;

	public OptionItemHolder(View itemView) {
		super(itemView);
	}

	@Override
	public void bindData(int position, OptionItem data) {
		m_option_content.setText(data.m_content);
		m_checked.setChecked(AppSettings.instance().getAppExperiencePlanJoined());
	}

	@OnCheckedChanged(R2.id.option_item_checkbox)
	public void onCheckedChanged(boolean isChecked) {
		AppSettings.instance().setAppExperiencePlanJoined(isChecked);
		try {
			if (isChecked) {
				Gather.instance().startCollection();
			} else {
				Gather.instance().stopCollection();
			}
		} catch (Exception e) {
			Logger.error(e);
		}
	}
}

class OptionItem {
	String m_content;
	boolean m_checked;
}
