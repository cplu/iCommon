package com.windfindtech.icommon.fragment.pts;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.windfindtech.icommon.R;
import com.windfindtech.icommon.R2;
import com.windfindtech.icommon.fragment.management.WrappedFragment;
import com.windfindtech.icommon.jsondata.points.MyAchievementItem;
import com.windfindtech.icommon.mvp.presenter.pts.MyAchievementPresenter;
import com.windfindtech.icommon.mvp.view.pts.IMyAchievementView;
import com.windfindtech.icommon.view.recycler.MVPAdapter;
import com.windfindtech.icommon.view.recycler.MVPViewHolder;
import com.windfindtech.icommon.webservice.WSManager;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;

/**
 * Created by py on 2016/8/23.
 */
public class MyAchievementFragment extends WrappedFragment<IMyAchievementView, MyAchievementPresenter> implements IMyAchievementView {

	@BindView(R2.id.recycler_my_achievement)
	RecyclerView m_recyclerView;
	private MyAchievementAdapter m_adapter;
	private View m_rootView;
	private MyAchievementItem[] m_myAchievementData;


	@Override
	protected MyAchievementPresenter createPresenter() {
		return new MyAchievementPresenter();
	}

	public MyAchievementFragment() {
		setPageName("MyAchievementFragment");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		m_rootView = inflater.inflate(R.layout.fragment_my_achievement, container, false);
		m_myAchievementData = (MyAchievementItem[]) getParcelableArrayParams();
		return m_rootView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		m_rootView.setBackgroundResource(R.drawable.bg_weather);
		GridLayoutManager m_gridManager = new GridLayoutManager(getContext(), 3);
		m_gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
			@Override
			public int getSpanSize(int position) {
				return 1;
			}
		});
		m_recyclerView.setLayoutManager(m_gridManager);
		m_adapter = new MyAchievementAdapter(getContext());
//		MyAchievementItem[] achievementData = m_presenter.getAchievementData();
		if (m_myAchievementData != null && m_myAchievementData.length > 0) {
			m_adapter.setData(m_myAchievementData, false);
		}
		m_recyclerView.setAdapter(m_adapter);
		m_adapter.setOnItemClickListener(new MVPAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(View view, int position, long id) {
				MyAchievementItem item = m_adapter.getData(position);
				if(item != null) {
					String url = item.getIntroPage();
					if(url != null) {
						m_primaryCallback.onForwardWebView(url, null);
					}
				}
			}
		});
	}

	@OnClick(R2.id.points_store_back)
	public void onNaviBack() {
		m_primaryCallback.onNaviBack();
	}

	class MyAchievementAdapter extends MVPAdapter<MyAchievementItem, MyAchievementViewHolder> {

		/**
		 * @param ctx
		 */
		public MyAchievementAdapter(Context ctx) {
			super(ctx);
		}

		@Override
		public MyAchievementViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			return new MyAchievementViewHolder(getInflated(parent, R.layout.grid_item_my_achievement));
		}

		@Override
		public Observable<Bitmap>[] getImages(int position, MyAchievementItem data) {
			return new Observable[]{
				WSManager.instance().getImageObservable(data.getIcon(), true, 256, 256)
			};
		}
	}

	class MyAchievementViewHolder extends MVPViewHolder<MyAchievementItem> {
		@BindView(R2.id.img_achievement)
		ImageView m_achieveIcon;
		@BindView(R2.id.achievement_name_txt)
		TextView m_achieveName;
		@BindView(R2.id.achievement_des_txt)
		TextView m_achieveDes;
		@BindView(R2.id.achievement_value_txt)
		TextView m_achieveValue;

		public MyAchievementViewHolder(View itemView) {
			super(itemView);
			m_achieveDes.setSelected(true);
		}

		@Override
		public void bindData(int position, MyAchievementItem data) {
			m_achieveName.setText(data.getName() != null ? data.getName() : getString(R.string.weather_undefined));
			m_achieveName.setTextColor(data.isFinished() ? getResources().getColor(R.color.default_white) : getResources().getColor(R.color.grayscale4));
			m_achieveDes.setText(data.getDescription() != null ? data.getDescription() : getString(R.string.weather_undefined));
			m_achieveValue.setText(data.isFinished() ? getString(R.string.achievement_reached) : getString(R.string.achievement_unreached));
			m_achieveValue.setTextColor(data.isFinished() ? getResources().getColor(R.color.default_white) : getResources().getColor(R.color.grayscale4));
		}

		@Override
		public void updateBitmap(int index, Bitmap bitmap) {
			if (index == 0) {
				m_achieveIcon.setImageBitmap(bitmap);
			}
		}
	}
}
