/**
 *
 */
package com.windfindtech.icommon.fragment.navigate;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.windfindtech.icommon.R;
import com.windfindtech.icommon.R2;
import com.windfindtech.icommon.view.recycler.MVPAdapter;
import com.windfindtech.icommon.view.recycler.MVPViewHolder;

import butterknife.BindView;

/**
 * @author cplu</br>
 * the base adapter for {@link NavigateFragment}
 * Modified at 2016/08/26
 * derived classes of {@link NavigateFragment} is not forced to have an adapter extending NavListAdapter, neither even to have an adapter (if no list view is required)
 */
public class NavListAdapter extends MVPAdapter<NavListAdapter.LineItem, NavListAdapter.BaseItemHolder> {

	protected static final int TYPE_LINEITEM_VALID = 0;
	protected static final int TYPE_LINEITEM_SPACE = 1;
	protected int m_lineItemID;

	public NavListAdapter(Context context, int line_item_id) {
		super(context);
		m_lineItemID = line_item_id;
	}

	@Override
	public int getItemViewType(int position) {
		if(getData(position) != null) {
			return TYPE_LINEITEM_VALID;
		} else {
			return TYPE_LINEITEM_SPACE;
		}
	}

	@Override
	public BaseItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		if(viewType == TYPE_LINEITEM_VALID) {
			return new LineItemHolder(getInflated(parent, m_lineItemID));
		} else {
			return new BaseItemHolder(getInflated(parent, R.layout.list_item_space));
		}
	}

	public void clearAll() {

	}

	/**
	 * set the hint number to right of "name" in LineItem
	 *
	 * @param item_index
	 * @param num
	 * @param invalidate true if DataSet changes should be notified, false otherwise
	 */
	public void setDataHintNumber(int item_index, int num, boolean invalidate) {
		LineItem data = getData(item_index);
		if(data != null) {
			data.m_hintNumber = num;
			if (invalidate) {
				notifyItemChanged(item_index);
			}
		}
	}

	/**
	 * set m_description in LineItem
	 *
	 * @param item_index
	 * @param description
	 * @param invalidate  true if DataSet changes should be notified, false otherwise
	 */
	public void setDataDescription(int item_index, String description, boolean invalidate) {
		LineItem data = getData(item_index);
		if(data != null) {
			data.m_description = description;
			if (invalidate) {
				notifyItemChanged(item_index);
			}
		}
	}

//    public void set_adapter_listener(TemplateAdapterListener templateAdapterListener) {
//        m_listener = templateAdapterListener;
//    }

//    public void set_ui_data_callback(UserUIDataCallback user_ui_data_callback) {
//        m_user_ui_data_callback = user_ui_data_callback;
//    }


	/**
	 * base item holder, with empty body
	 */
	protected class BaseItemHolder extends MVPViewHolder<LineItem>{

		public BaseItemHolder(View itemView) {
			super(itemView);
		}

		@Override
		public void bindData(int position, LineItem data) {

		}
	}

	/**
	 * basic line item holder, with a common line item layout
	 */
	protected class LineItemHolder extends BaseItemHolder {
		@BindView(R2.id.list_item_icon_view)
		public ImageView m_icon;
		@BindView(R2.id.list_item_name_view)
		public TextView m_nameView;
		@BindView(R2.id.list_item_hint_view)
		public TextView m_hintView;
		@BindView(R2.id.list_item_description_view)
		public TextView m_descriptionView;
		@BindView(R2.id.list_item_forward_btn)
		public ImageView m_forwardBtn;

		public LineItemHolder(View view) {
			super(view);
		}

		@Override
		public void bindData(int position, LineItem data) {
			if (data.m_iconID > 0) {
				//DrawableManager.instance().loadBitmap(m_ctx, item.m_icon_id, holder.m_icon);
				m_icon.setImageResource(data.m_iconID);
			}
			m_nameView.setText(data.m_name);
			if (data.m_description != null) {
				m_descriptionView.setText(data.m_description);
			} else {
				m_descriptionView.setText(m_ctx.getResources().getString(R.string.user_data_not_set));
			}
			if (data.m_hintNumber > 0) {
				m_hintView.setText(String.valueOf(data.m_hintNumber));
				m_hintView.setVisibility(View.VISIBLE);
			} else {
				m_hintView.setVisibility(View.INVISIBLE);
			}
			if (data.m_forwardBtnID > 0) {
				//DrawableManager.instance().loadBitmap(m_ctx, item.m_forwardBtnID, holder.m_forwardBtn);
				m_forwardBtn.setImageResource(data.m_forwardBtnID);
			}
		}
	}

	protected static class LineItem {
		public int m_iconID;
		public String m_name;
		public int m_hintNumber;
		public String m_description;
		public int m_forwardBtnID;
		//public NavigateFragment m_ui_fragment;
//    public boolean m_could_navi_back;   /// whether user could navi back to this fragment after navi forward by this item

		public LineItem(int icon_id, String name, String description, int forward_btn_id) {
			m_iconID = icon_id;
			m_name = name;
			m_hintNumber = 0;
			m_description = description;
			m_forwardBtnID = forward_btn_id;
			//m_ui_fragment = ui_fragment;
//        m_could_navi_back = could_nava_back;
			//m_b_space_placeholder_above = b_space_placeholder;
		}
		//public boolean m_b_space_placeholder_above;	/// if a space placeholder is above the item
	}

	protected static class LineItemWithIconSelected extends LineItem {
		public int m_iconIDSelected;

		public LineItemWithIconSelected(int icon_id, int icon_id_selected, String name, String description, int forward_btn_id) {
			super(icon_id, name, description, forward_btn_id);
			m_iconIDSelected = icon_id_selected;
		}
	}

}
