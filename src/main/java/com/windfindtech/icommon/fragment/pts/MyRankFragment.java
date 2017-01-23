package com.windfindtech.icommon.fragment.pts;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
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
import com.windfindtech.icommon.fragment.management.WrappedFragment;
import com.windfindtech.icommon.jsondata.points.MyRankItem;
import com.windfindtech.icommon.jsondata.points.RankItem;
import com.windfindtech.icommon.mvp.presenter.navigate.MyRankPresenter;
import com.windfindtech.icommon.mvp.view.navigate.IMyRankView;
import com.windfindtech.icommon.util.ResUtil;
import com.windfindtech.icommon.util.Utils;
import com.windfindtech.icommon.view.recycler.MVPAdapter;
import com.windfindtech.icommon.view.recycler.MVPViewHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import butterknife.BindView;


/**
 * Created by py on 2016/8/23.
 */
public class MyRankFragment extends WrappedFragment<IMyRankView, MyRankPresenter> implements IMyRankView {


	@BindView(R2.id.my_rank_back)
	ImageView m_naviBack;
	@BindView(R2.id.recycler_my_rank)
	RecyclerView m_recyclerView;

	@BindView(R2.id.my_rank_des_txt)
	TextView m_myRankDesTxt;
	@BindView(R2.id.horizontal_recycler_view)
	RecyclerView m_horizontalRecyclerView;

	private MyRankAdapter m_myRankAdapter;
	private HorizontalRecyclerViewAdapter m_horizontalAdapter;
	private LinearLayoutManager m_linearLayoutManager;
	private MyRankItem m_myRankItem;
	private View m_rootView;


	@Override
	protected MyRankPresenter createPresenter() {
		return new MyRankPresenter();
	}

	public MyRankFragment() {
		setPageName("MyRankFragment");
	}

//    @Override
//    protected int getNotificationType() {
//        return MType.WS_RECEIVER;
//    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		m_rootView = inflater.inflate(R.layout.fragment_my_rank, container, false);
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
		m_recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
		m_myRankAdapter = new MyRankAdapter(getContext());
		m_recyclerView.setAdapter(m_myRankAdapter);
		m_horizontalAdapter = new HorizontalRecyclerViewAdapter(getContext());
		m_linearLayoutManager = new LinearLayoutManager(getActivity());
		m_linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
		m_horizontalRecyclerView.setLayoutManager(m_linearLayoutManager);
		m_horizontalRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
			//            @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
			@Override
			public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
				super.onScrollStateChanged(recyclerView, newState);
				double scrollOffset = recyclerView.computeHorizontalScrollOffset();
				double childViewWidth = Utils.s_deviceWidth / 3;
				double scrollPostion = scrollOffset / childViewWidth;
				int idlePosition = (int) (scrollOffset / childViewWidth);

				if (newState == RecyclerView.SCROLL_STATE_IDLE) {
					RankItem item = m_horizontalAdapter.getData(idlePosition + 1);
					if (idlePosition < scrollPostion - 0.5) {
						smoothScrollToPosition(idlePosition + 1, item);
					} else if (idlePosition > scrollPostion - 0.5) {
						smoothScrollToPosition(idlePosition, item);
					}
				}
			}

			@Override
			public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
				super.onScrolled(recyclerView, dx, dy);
			}
		});

		m_horizontalRecyclerView.setAdapter(m_horizontalAdapter);

		m_myRankItem = (MyRankItem) getParcelableParams();
		if (m_myRankItem != null && m_myRankItem.getLevel() != null) {
			setMyRankData(m_myRankItem.getLevel());
		}
		m_presenter.retrieveRankData();
		onAvatar(DrawableManager.instance().getAssetBitmap("avatar_waiting_icon.jpg"));

	}

	/**
	 * 滑动头像时,让光标顺滑停留在相应的位置.
	 * 而recyclerview.smoothScrollToPostion(int position)方法只能是选中item不在屏幕内时才能调用起作用
	 *
	 * @param idlePosition
	 * @param item
	 */
	private void smoothScrollToPosition(int idlePosition, RankItem item) {
		int firstItemPosition = m_linearLayoutManager.findFirstVisibleItemPosition();
		int lastItemPosition = m_linearLayoutManager.findLastVisibleItemPosition();
		if (idlePosition <= lastItemPosition) {
			int left = m_horizontalRecyclerView.getChildAt(idlePosition - firstItemPosition).getLeft();
			m_horizontalRecyclerView.smoothScrollBy(left, 0);
			m_myRankDesTxt.setText(
				item != null ? String.format(getString(R.string.prefer_surf_internet), item.getSpeed()) : getString(R.string.rank_undefined));
		}
	}

//	@OnClick(R2.id.rank_rules_txt)
//	public void onRankRulesClick() {
//		m_primaryCallback.onForward(TagUser.FRG_POINTS_RULES);
//	}

	@Override
	public void onAvatar(Bitmap bitmap) {
		try {
			if (m_horizontalAdapter != null) {
				m_horizontalAdapter.setAvatarBitmap(bitmap);
			}
		} catch (Exception e) {
			return;
		}
	}

	@Override
	public void onRankDataResult(RankItem[] ret) {
		if (ret != null) {
//			m_myRankAdapter.setData(ret, true);
			m_myRankAdapter.setMyRankItem(ret, true);
			m_horizontalAdapter.setRankData(ret);
		}
		if (m_myRankItem != null) {
			m_horizontalRecyclerView.smoothScrollToPosition(
				m_myRankItem.getLevel().getLevel() + 1);//smoothScrollToPosition方法是顺滑到position可见的那一条,而要想要此item居中显示,就得滑到它的下一条.
		}
	}


	private void setMyRankData(RankItem ret) {
		if (m_horizontalAdapter != null) {
			m_horizontalAdapter.setMyRankItem(ret);
		}
	}

	class MyRankAdapter extends MVPAdapter<RankItem, MVPViewHolder> {
		private static final int ITEM_TEXT_SIMPLE = 0;
		private static final int ITEM_MY_RANK = 1;
		private RankItem m_simpleText = new RankItem();

		/**
		 * @param ctx
		 */
		public MyRankAdapter(Context ctx) {
			super(ctx);
			m_simpleText.setName(getString(R.string.rights_description));
			m_adapterData = new ArrayList<>(Collections.singletonList(m_simpleText));
		}

		@Override
		public int getItemViewType(int position) {
			int size=m_adapterData.size()-2;
			if (position<=size){
				return ITEM_MY_RANK;
			}else{
				return ITEM_TEXT_SIMPLE;
			}
		}

		@Override
		public MVPViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			switch (viewType) {
				case ITEM_TEXT_SIMPLE:
					return new SimpleTextHolder(getInflated(parent,R.layout.list_item_simple_text_transparent_bg));
				case ITEM_MY_RANK:
					return new MyRankViewHolder(getInflated(parent, R.layout.my_rank_recycler_item));
				default:
					return new SimpleTextHolder(getInflated(parent,R.layout.list_item_simple_text_transparent_bg));
			}
		}

		public void setMyRankItem(RankItem[] ret, boolean invalidate) {
			if (ret != null) {
				m_adapterData = new ArrayList<>(Collections.singletonList(m_simpleText));
				m_adapterData.addAll(0, Arrays.asList(ret));
			} else {
				m_simpleText.setName(getString(R.string.rights_description));
				m_adapterData = new ArrayList<>(Collections.singletonList(m_simpleText));
			}
			if (invalidate) {
				notifyDataSetChanged();
			}
		}

	}

	class MyRankViewHolder extends MVPViewHolder<RankItem> {
		@BindView(R2.id.recycler_item_level_img)
		ImageView m_levelImg;
		@BindView(R2.id.recycler_item_name_view)
		TextView m_nameTitle;
		@BindView(R2.id.recycler_item_rights_view)
		TextView m_rights;
		@BindView(R2.id.recycler_item_login_site_count)
		TextView m_siteCount;
		@BindView(R2.id.recycler_item_login_count)
		TextView m_count;

		public MyRankViewHolder(View itemView) {
			super(itemView);
		}

		@Override
		public void bindData(int position, RankItem data) {
			if (data != null) {
				m_nameTitle.setText(data.getName() != null ? data.getName() : getString(R.string.aqi_average_undefined));
				m_levelImg.setImageDrawable(ResUtil.getDrawableResByLevel(data.getLevel()));
				m_nameTitle.setBackgroundResource(ResUtil.getRankLevelBackgroundID(data.getLevel()));
				m_rights.setText(data.getSpeed() != null ? data.getSpeed() : getString(R.string.rank_undefined));
				m_siteCount.setText(
					data.getCountLoginSitesInMonth() != -1 ? String.valueOf(data.getCountLoginSitesInMonth()) : getString(R.string.rank_undefined));
				m_count.setText(data.getCountLoginsInMonth() != null ? data.getCountLoginsInMonth() : getString(R.string.rank_undefined));
			}
		}
	}

	class SimpleTextHolder extends MVPViewHolder<RankItem> {
		@BindView(R2.id.simple_text)
		TextView m_description;

		public SimpleTextHolder(View itemView) {
			super(itemView);
		}

		@Override
		public void bindData(int position, RankItem data) {
			m_description.setText(data.getName());
		}
	}

}




