package com.windfindtech.icommon.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.windfindtech.icommon.R;
import com.windfindtech.icommon.dialog.callback.RatingCallback;
import com.windfindtech.icommon.jsondata.site.RatingResult;
import com.windfindtech.icommon.jsondata.webservice.BaseResponse;
import com.windfindtech.icommon.jsondata.webservice.RatingCheck;
import com.windfindtech.icommon.jsondata.webservice.WSErrorResponse;
import com.windfindtech.icommon.view.adapter.CustomAdapter;
import com.windfindtech.icommon.webservice.WSCallback;
import com.windfindtech.icommon.webservice.WSManager;

import java.util.ArrayList;

/**
 * Created by cplu on 2015/10/20.
 */
public class RatingDialog extends BaseDialog{
	private static final String RATING_MODEL = "rating_model";
	//	private final RatingCallback m_callback;
	private RatingCheck m_ratingModel;
//	private TextView m_siteNameTxt;
	private GridView m_gridRatingChoices;
	private RatingChoiceAdapter m_ratingChoiceAdapter;
	private RatingBar m_ratingBar;
	private EditText m_detailOthers;
//	private static final String SP_FILE_NAME = "rating_strings";
//	private static final String RATING_KEY = "rating_key";
	private static final float RATING_SCORE_LOW_LIMIT = 3.0f;   /// score greater than 3.0f is considered high and do not need additional details
	private Button m_btnSubmit;
	private CheckBox m_noMoreRating;
	private TextView m_siteLocationTxt;
	private TextView m_siteTimeTxt;
	private RelativeLayout m_rl_rating_location;
	private ImageView m_cancel;
//	private RelativeLayout m_rootView;
//	private WebMapDialog m_mapDialog;
	private RatingCallback m_callback;
//	private WebView m_webMap;
	//	private TextView m_cancel;

	public static RatingDialog create(long id, RatingCheck model, FragmentManager manager) {
		RatingDialog dialog = new RatingDialog();
		Bundle bundle = dialog.getBaseBundle(id);
		bundle.putParcelable(RATING_MODEL, model);
		dialog.setArguments(bundle);
		showDialog(manager, dialog, "RatingDialog");
		return dialog;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setStyle(STYLE_NO_TITLE, R.style.dialog_notitlebar_transparent);
		Bundle bundle = getArguments();
		m_ratingModel = bundle.getParcelable(RATING_MODEL);
		m_callback = (RatingCallback) getCallback();
	}

	@Override
	protected int getLayoutId() {
		return R.layout.dialog_rating;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
	                         @Nullable Bundle savedInstanceState) {
		m_root = super.onCreateView(inflater, container, savedInstanceState);
		m_siteLocationTxt = (TextView)m_root.findViewById(R.id.tv_last_location);
		m_siteTimeTxt = (TextView)m_root.findViewById(R.id.tv_last_time);
		m_rl_rating_location = (RelativeLayout) m_root.findViewById(R.id.rl_rating);
		/** 点击当前的相对布局，跳到相应的地图界面*/
		m_rl_rating_location.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				m_callback.onShowMap(m_uniqueID, m_ratingModel, m_root.getWidth(), m_root.getHeight());
			}
		});
//		m_rl_rating_location.setOnTouchListener(new View.OnTouchListener() {
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//				m_callback.onShowMap(m_uniqueID, m_ratingModel, m_root.getWidth(), m_root.getHeight());
//				return false;
//			}
//		});
//		m_webMap = (WebView) findViewById(R.id.webmap);//地图的webview
		m_cancel = (ImageView) m_root.findViewById(R.id.rating_cancel);
		m_cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		setSiteNameTxt();
		m_ratingBar = (RatingBar) m_root.findViewById(R.id.rating_value_bar);
		m_gridRatingChoices = (GridView)m_root.findViewById(R.id.gridview_rating_choices);
		m_ratingChoiceAdapter = new RatingChoiceAdapter(getContext());
		m_gridRatingChoices.setAdapter(m_ratingChoiceAdapter);
		update_rating_choices();
		m_detailOthers = (EditText)m_root.findViewById(R.id.rating_detail_others);
		m_btnSubmit = (Button)m_root.findViewById(R.id.rating_submit);
		m_btnSubmit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
				checkScoreAndSubmit();
			}
		});
		m_noMoreRating = (CheckBox)m_root.findViewById(R.id.checkbox_no_rating_in_two_weeks);
//		m_cancel = (TextView)findViewById(R.id.rating_cancel);
//		m_cancel.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				dismiss();
//			}
//		});

		m_ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
				if (fromUser) {
					if (rating > RATING_SCORE_LOW_LIMIT) {
						m_gridRatingChoices.setVisibility(View.GONE);
//						m_detailOthers.setVisibility(View.GONE);
					}
					else {
						m_gridRatingChoices.setVisibility(View.VISIBLE);
//						m_detailOthers.setVisibility(View.VISIBLE);
					}
				}
			}
		});

		return m_root;
	}

	private void setSiteNameTxt(){
		if(m_siteLocationTxt != null && m_siteTimeTxt!=null && m_ratingModel != null){
			if(m_ratingModel.getLastLoginStr() != null) {
				try {
					/*m_siteLocationTxt.setText(String.format(getContext().getString(R.string.rating_site_name_hint_with_time),
							m_ratingModel.getLastLoginStr(), m_ratingModel.getName()));*/
					m_siteLocationTxt.setText(String.format(getContext().getString(R.string.location),m_ratingModel.getName()));//TODO py修改
					m_siteTimeTxt.setText(String.format(getContext().getString(R.string.time),m_ratingModel.getLastLoginStr()));
				}
				catch (Exception e){
				}
			}
			else{
//				m_siteNameTxt.setText(String.format(getContext().getString(R.string.rating_site_name_hint), m_ratingModel.getName()));
				m_siteLocationTxt.setText(String.format(getContext().getString(R.string.location),m_ratingModel.getName()));
				m_siteTimeTxt.setText(R.string.null_in_string_format);
			}
		}
	}

	public void checkScoreAndSubmit() {
		RatingResult ratingResult = new RatingResult();
		float rating = m_ratingBar.getRating();
		ratingResult.setRank((int)rating );
		ratingResult.setSiteCode(m_ratingModel != null ? m_ratingModel.getCode() : null);
		if (rating <= RATING_SCORE_LOW_LIMIT){
			if(m_ratingChoiceAdapter.getLastChecked()) {
				ratingResult.setRemarks(m_detailOthers.getText().toString());
			}
			ratingResult.setReasons(m_ratingChoiceAdapter.getReasons());
		}
		/// send rating result
		WSManager.instance().doSendRatingResult(ratingResult, new WSCallback<BaseResponse>() {
			@Override
			public void onSuccess(BaseResponse ret) {
				m_callback.onRatingFinished(m_uniqueID, true);
			}

			@Override
			public void onFailed(WSErrorResponse reason) {
				m_callback.onRatingFinished(m_uniqueID, false);
			}
		});
		if(m_noMoreRating.isChecked()){
			/// send no more ranking in two weeks
			WSManager.instance().doSendNoMoreRanking(new WSCallback<BaseResponse>() {
				@Override
				public void onSuccess(BaseResponse ret) {

				}

				@Override
				public void onFailed(WSErrorResponse reason) {

				}
			});
		}
	}

	private void update_rating_choices() {
		WSManager.instance().doGetRatingReasons(new WSCallback<String[]>() {
			@Override
			public void onSuccess(String[] ret) {
				if (isAdded()) {
					ArrayList<RatingDetail> ratingDetails = new ArrayList<>();
					for (String detailStr : ret) {
						RatingDetail ratingDetail = new RatingDetail();
						ratingDetail.m_bChecked = false;
						ratingDetail.m_description = detailStr;
						ratingDetails.add(ratingDetail);
					}
					/// add "其它"
					RatingDetail ratingDetail = new RatingDetail();
					ratingDetail.m_bChecked = false;
					ratingDetail.m_description = getContext().getString(R.string.site_rating_others);
					ratingDetails.add(ratingDetail);
					/// set GridView height
					RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) m_gridRatingChoices.getLayoutParams();
					int item_height = getContext().getResources().getDimensionPixelSize(R.dimen.ui_preferred_subtitle_height);
					int choice_count = ratingDetails.size();
					params.height = item_height * (choice_count / 2 + choice_count % 2);
					m_gridRatingChoices.setLayoutParams(params);
					m_ratingChoiceAdapter.setData(ratingDetails, true);
				}
			}

			@Override
			public void onFailed(WSErrorResponse reason) {

			}
		});
	}

	private class RatingChoiceHolder{
		public CheckBox m_checkBox;
	}

	private class RatingDetail{
		public String m_description;
		public boolean m_bChecked;
	}

	private class RatingChoiceAdapter extends CustomAdapter<RatingDetail, RatingChoiceHolder>{

		/**
		 * @param ctx
		 */
		public RatingChoiceAdapter(Context ctx) {
			super(ctx, R.layout.list_item_rating_choice);
//			m_adapterData = new ArrayList<String>();
		}

		@Override
		protected RatingChoiceHolder initHolder(View convertView) {
			RatingChoiceHolder holder = new RatingChoiceHolder();
			holder.m_checkBox = (CheckBox) convertView.findViewById(R.id.checkbox_rating_choice);
			return holder;
		}

		@Override
		protected void setHolderData(final int position, View convertView, RatingChoiceHolder holder, final RatingDetail item, String tag) {
			holder.m_checkBox.setText(item.m_description);
			holder.m_checkBox.setChecked(item.m_bChecked);
			if(is_last_item(position)){
				showDetailDescription(item.m_bChecked);
			}
			holder.m_checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					item.m_bChecked = isChecked;
					if(is_last_item(position)){
						showDetailDescription(item.m_bChecked);
					}
				}
			});
		}

		private boolean is_last_item(int position){
			int count = getCount();
			if(count > 0){
				return position == count - 1;
			}
			else{
				return false;
			}
		}

		public ArrayList<String> getReasons() {
			ArrayList<String> results = new ArrayList<String>();
			for(int i = 0; i < getCount() - 1; i++){
				RatingDetail ratingDetail = (RatingDetail)getItem(i);
				if(ratingDetail != null && ratingDetail.m_bChecked){
					results.add(ratingDetail.m_description);
				}
			}
			return results;
		}

		public boolean getLastChecked() {
			int count = getCount();
			if(count > 0) {
				RatingDetail ratingDetail = (RatingDetail) getItem(count - 1);
				if(ratingDetail != null){
					return ratingDetail.m_bChecked;
				}
			}
			return false;
		}
	}

	/**
	 * show/hide EditText (User Input)
	 * @param to_show
	 * @return
	 */
	public void showDetailDescription(boolean to_show){
		if(to_show) {
			m_detailOthers.setVisibility(View.VISIBLE);
		}
		else {
			m_detailOthers.setVisibility(View.GONE);
		}
	}
}
