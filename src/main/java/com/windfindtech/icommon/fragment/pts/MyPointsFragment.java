package com.windfindtech.icommon.fragment.pts;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.windfindtech.icommon.R;
import com.windfindtech.icommon.R2;
import com.windfindtech.icommon.bitmap.DrawableManager;
import com.windfindtech.icommon.fragment.management.TagUser;
import com.windfindtech.icommon.fragment.management.WrappedFragment;
import com.windfindtech.icommon.jsondata.points.MyAchievementItem;
import com.windfindtech.icommon.jsondata.points.MyPointsItem;
import com.windfindtech.icommon.jsondata.points.MyRankItem;
import com.windfindtech.icommon.mvp.model.MLocalReceiver;
import com.windfindtech.icommon.mvp.presenter.pts.MyPointsPresenter;
import com.windfindtech.icommon.mvp.view.pts.IMyPointsView;
import com.windfindtech.icommon.view.recycler.MVPAdapter;
import com.windfindtech.icommon.view.recycler.MVPViewHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by py on 2016/8/23.
 * 我的积分
 */
public class MyPointsFragment extends WrappedFragment<IMyPointsView, MyPointsPresenter> implements IMyPointsView {


	@BindView(R2.id.my_points_back)
	ImageView m_naviBack;
	@BindView(R2.id.my_points_value)
	TextView m_pointsValue;

	@BindView(R2.id.recycler_my_points)
	RecyclerView m_recyclerView;
	@BindView(R2.id.swipe_refresh_layout)
	SwipeRefreshLayout m_refreshLayout;

	//	@BindView(R2.id.layout_achievement)
//	RelativeLayout m_achievementLayout;
//	@BindView(R2.id.layout_points_rule)
//	RelativeLayout m_pointsRuleLayout;
	private MyPointsAdapter m_myPointsAdapter;
	private View m_rootView;
	private MyAchievementItem[] m_achievementData;

	@Override
	protected MyPointsPresenter createPresenter() {
		return new MyPointsPresenter();
	}

	public MyPointsFragment() {
		setPageName("MyPointsFragment");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		m_rootView = inflater.inflate(R.layout.fragment_my_points, container, false);
		return m_rootView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		m_rootView.setBackgroundDrawable(DrawableManager.instance().getAssetImage("bg_user_fragment.png"));
		m_naviBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				m_primaryCallback.onNaviBack();
			}
		});
//		m_pointsValue.setText(getString(R.string.pts_undefined));
		m_myPointsAdapter = new MyPointsAdapter(getContext());
		m_recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        MyPointsItem[] m_datas = m_presenter.getCompletedValue();
//        m_myPointsAdapter.setData(m_datas, false);
		m_recyclerView.setAdapter(m_myPointsAdapter);
		m_myPointsAdapter.setOnItemClickListener(new MVPAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(View view, int position, long id) {
				if (m_myPointsAdapter.isPointItem(position)) {
					m_primaryCallback.onForward(TagUser.FRG_POINTS_RECORDS);
				} else if (m_myPointsAdapter.isAchievementItem(position) && m_achievementData != null && m_achievementData.length > 0) {
					m_primaryCallback.onForward(TagUser.FRG_MY_ACHIEVEMENT, m_achievementData, null);
				} else if (m_myPointsAdapter.isPtRule(position)) {
					m_primaryCallback.onForward(TagUser.FRG_POINTS_RULES);
				}
			}
		});

//		TextView m_achievementPrefix = (TextView) m_achievementLayout.findViewById(R.id.my_points_item_prefix);
//		ImageView m_achievementBtn = (ImageView) m_achievementLayout.findViewById(R.id.my_points_item_forward_btn);
//		TextView m_achievementValue = (TextView) m_achievementLayout.findViewById(R.id.my_points_item_value);
//		TextView m_pointsRulePrefix = (TextView) m_pointsRuleLayout.findViewById(R.id.my_points_item_prefix);
//		ImageView m_pointsRuleBtn = (ImageView) m_pointsRuleLayout.findViewById(R.id.my_points_item_forward_btn);
//		TextView m_pointsRuleValue = (TextView) m_pointsRuleLayout.findViewById(R.id.my_points_item_value);
//		m_achievementPrefix.setText("达成成就");
//		m_pointsRulePrefix.setText("积分规则");
//		m_achievementBtn.setVisibility(View.VISIBLE);
//		m_pointsRuleBtn.setVisibility(View.VISIBLE);
//		m_achievementValue.setText("已完成2次");
//		m_pointsRuleValue.setText("如何获得、用处");

		MyRankItem item = (MyRankItem) getParcelableParams();
		m_presenter.setMyRankItem(item);
		if (item != null) {
			m_pointsValue.setText(String.valueOf(item.getPoints()));
		}

		m_presenter.retrieveMyPoints();
		m_presenter.retrieveMyAchievements();

		m_refreshLayout.setColorSchemeResources(R.color.default_blue, R.color.blue_purple, R.color.purple_blue);
		m_refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				if (MLocalReceiver.NETWORK_OK == m_presenter.getNetworkStatus()) {
					m_presenter.retrieveMyPoints();
					m_presenter.retrieveMyAchievements();
					m_refreshLayout.setRefreshing(true);
				}
				m_refreshLayout.postDelayed(new Runnable() {
					@Override
					public void run() {
//                        iCommon.showToast(getActivity(), R.string.cube_ptr_refresh_complete);
						m_refreshLayout.setRefreshing(false);
					}
				}, new Random().nextInt(2000) + 200);
			}
		});
	}

	@OnClick(R2.id.points_records)
	public void onRecordsForward() {
		m_primaryCallback.onForward(TagUser.FRG_POINTS_RECORDS);
	}

	@OnClick(R2.id.points_store)
	public void onStoreForward() {
		m_primaryCallback.onForward(TagUser.FRG_POINTS_STORE);
	}

//	@OnClick(R2.id.layout_achievement)
//	public void onAchievementBtnClick() {
//		m_primaryCallback.onForward(Tag.User.FRG_MY_ACHIEVEMENT);
//	}
//
//	@OnClick(R2.id.layout_points_rule)
//	public void onPointsRuleBtnClick() {
//		m_primaryCallback.onForward(TagUser.FRG_POINTS_RULES);
//	}

	@Override
	public void onMyPointsResult(MyPointsItem[] ret) {
		if (ret != null) {
			int[] pts = calculatePts(ret);
//			m_pointsValue.setText(String.valueOf(pts[0]));
			m_myPointsAdapter.setPointsItem(ret, pts[0], pts[1]);
		}
	}

	@Override
	public void onMyAchievementsResult(MyAchievementItem[] ret) {
		if (ret != null) {
			m_myPointsAdapter.setAchievementData(ret);
			m_achievementData = ret;
		}
	}

	/**
	 * calculate current pts and pts in all
	 *
	 * @param ret
	 * @return two values: [pts, sumPts]
	 */
	private int[] calculatePts(MyPointsItem[] ret) {
		int pt = 0, sumPt = 0;
		for (MyPointsItem item : ret) {
			if (item.isFinished()) {
				pt += item.getCounts() * item.getPoints();
			} else {
				pt += item.getCurCounts() * item.getPoints();
			}
			sumPt += item.getCounts() * item.getPoints();
		}
		return new int[]{pt, sumPt};
	}

	class MyPointsAdapter extends MVPAdapter<MyPointsItem, MVPViewHolder> {
		private static final int ITEM_TEXT_TITLE = 0;
		private static final int ITEM_POINTS = 1;
		private static final int ITEM_SEPARATOR_TEXT = 2;
		private static final int ITEM_LINE_SIMPLE = 3;
		private int m_pointsItemCount = 0;
		private MyPointsItem m_title = new MyPointsItem();
		private MyPointsItem m_separator1 = new MyPointsItem();
		private MyPointsItem m_simpleItem1 = new MyPointsItem();
		private MyPointsItem m_separator2 = new MyPointsItem();
		private MyPointsItem m_simpleItem2 = new MyPointsItem();
		private ArrayList<MyAchievementItem> myAchievementItems;

		/**
		 * @param ctx
		 */
		public MyPointsAdapter(Context ctx) {
			super(ctx);
			m_title.setName(getString(R.string.my_points_summary_default));
			m_adapterData = new ArrayList<>(Arrays.asList(m_title, m_separator1, m_simpleItem1, m_separator2, m_simpleItem2));
			m_pointsItemCount = 0;
			m_simpleItem2.setName(getString(R.string.pt_rule));
			m_simpleItem2.setCode(getString(R.string.pt_rule_description));
		}

		public int getItemViewType(int position) {
			int size = m_adapterData.size() - 5;
			if (position == 0) {
				return ITEM_TEXT_TITLE;
			} else if (position <= size) {
				return ITEM_POINTS;
			} else if (position == size + 1
			           || position == size + 3) {
				return ITEM_SEPARATOR_TEXT;
			} else {
				return ITEM_LINE_SIMPLE;
			}
		}

		@Override
		public MVPViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			switch (viewType) {
				case ITEM_TEXT_TITLE:
					return new PtsTitleHolder(getInflated(parent, R.layout.list_item_simple_text));
				case ITEM_POINTS:
					return new PointsRecordHolder(getInflated(parent, R.layout.list_item_points_record));
				case ITEM_SEPARATOR_TEXT:
					return new SeparatorHolder(getInflated(parent, R.layout.list_item_separator_txt));
				case ITEM_LINE_SIMPLE:
					return new SimpleLineHolder(getInflated(parent, R.layout.list_item_line_simple));
				default:
					return new PtsTitleHolder(getInflated(parent, R.layout.list_item_simple_text));
			}
		}

		public void setPointsItem(MyPointsItem[] ret, int currentPts, int sumPts) {
			if (ret != null) {
				m_adapterData = new ArrayList<>(Arrays.asList(m_title, m_separator1, m_simpleItem1, m_separator2, m_simpleItem2));
				m_adapterData.addAll(1, Arrays.asList(ret));
				m_title.setName(String.format(getString(R.string.my_points_summary), currentPts, sumPts));
				m_pointsItemCount = ret.length;
			} else {
				m_title.setName(getString(R.string.my_points_summary_default));
				m_adapterData = new ArrayList<>(Arrays.asList(m_title, m_separator1, m_simpleItem1, m_separator2, m_simpleItem2));
				m_pointsItemCount = 0;
			}
			notifyDataSetChanged();
		}

		public void setAchievementData(MyAchievementItem[] ret) {
			myAchievementItems = new ArrayList<>(Arrays.asList(ret));
			int[] pts = calculateAchievements(ret);
			m_separator1.setName(String.format(getString(R.string.achievement_summary), pts[1]));
			m_simpleItem1.setName(getString(R.string.achievement_finished));
			m_simpleItem1.setCode(String.format(getString(R.string.achievement_count_finished), pts[0]));
			notifyItemRangeChanged(m_pointsItemCount + 1, 2);
		}

		public ArrayList<MyAchievementItem> getAchievementData() {
			return myAchievementItems != null ? myAchievementItems : null;
		}

		private int[] calculateAchievements(MyAchievementItem[] ret) {
			int finished = 0, sumPt = 0;
			for (MyAchievementItem item : ret) {
				if (item.isFinished()) {
					finished++;
				}
				sumPt += item.getPoints();
			}
			return new int[]{finished, sumPt};
		}

		public boolean isPointItem(int position) {
			return position >= 1 && position <= m_pointsItemCount;
		}

		public boolean isAchievementItem(int position) {
			return position == m_pointsItemCount + 2;
		}

		public boolean isPtRule(int position) {
			return position == m_pointsItemCount + 4;
		}
	}

	class PtsTitleHolder extends MVPViewHolder<MyPointsItem> {

		@BindView(R2.id.simple_text)
		TextView m_txt;

		public PtsTitleHolder(View itemView) {
			super(itemView);
		}

		@Override
		public void bindData(int position, MyPointsItem data) {
			m_txt.setText(data.getName());
		}
	}

	class SeparatorHolder extends MVPViewHolder<MyPointsItem> {

		@BindView(R2.id.text)
		TextView m_txt;

		public SeparatorHolder(View itemView) {
			super(itemView);
		}

		@Override
		public void bindData(int position, MyPointsItem data) {
			m_txt.setText(data.getName());
		}
	}

	class PointsRecordHolder extends MVPViewHolder<MyPointsItem> {
		@BindView(R2.id.item_name_view)
		TextView m_nameTitle;
		@BindView(R2.id.item_pt_description_view)
		TextView m_description;
		@BindView(R2.id.item_value_view)
		TextView m_currentCount;
		@BindView(R2.id.item_forward_btn)
		ImageView m_btnForward;

		public PointsRecordHolder(View itemView) {
			super(itemView);
		}

		@Override
		public void bindData(int position, MyPointsItem data) {
			m_btnForward.setVisibility(View.VISIBLE);
			m_nameTitle.setText(data.getName());
			m_description.setText(String.format(getString(R.string.pt_description), data.getPoints(), data.getPeriod(), data.getCounts()));
			if (data.isFinished()) {
				m_currentCount.setText(getString(R.string.pt_is_finished));
				m_currentCount.setTextColor(getResources().getColor(R.color.blue_purple));
			} else {
				m_currentCount.setText(String.format(getString(R.string.pt_count_finished), data.getCurCounts()));
				m_currentCount.setTextColor(getResources().getColor(R.color.grayscale3));
			}
		}
	}

	class SimpleLineHolder extends MVPViewHolder<MyPointsItem> {
		@BindView(R2.id.item_name)
		TextView m_itemName;
		@BindView(R2.id.btn_forward)
		ImageView m_btnForward;
		@BindView(R2.id.item_description)
		TextView m_description;

		public SimpleLineHolder(View itemView) {
			super(itemView);
		}

		@Override
		public void bindData(int position, MyPointsItem data) {
			m_itemName.setText(data.getName());
			m_description.setText(data.getCode());
		}
	}
}
