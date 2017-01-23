package com.windfindtech.icommon.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.windfindtech.icommon.R;
import com.windfindtech.icommon.advert.AdManager;
import com.windfindtech.icommon.http.HttpCallback;
import com.windfindtech.icommon.http.HttpRetroManager;
import com.windfindtech.icommon.jsondata.advertisement.BaseAdModel;
import com.windfindtech.icommon.jsondata.advertisement.DefaultAdModel;
import com.windfindtech.icommon.jsondata.webservice.WSErrorResponse;
import com.windfindtech.icommon.rx.RXIntervalSubscriber;
import com.windfindtech.icommon.rx.RXTimerTask;
import com.windfindtech.icommon.webservice.WSCallback;
import com.windfindtech.icommon.webservice.WSManager;

import org.pmw.tinylog.Logger;

import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Created by cplu on 2016/4/7.
 */
public class AdvertActivity extends Activity {
	public static final String EXTADMODEL_KEY = "extadmodel";
	public static final String ADMODEL_KEY = "admodel";
	private RelativeLayout m_layout;
	private ImageView m_img;
	private TextView m_closeTxt;
	//	private Handler m_handler;
//	private int m_tick;
	//	private ProgressBar m_progress;
	private DefaultAdModel m_adModel;   /// model for the currently working advertisement
	private BaseAdModel m_extAdModel;   /// model for the currently working advertisement
//	private AdvertSource m_advertSource = AdvertSource.FROM_WFT;
//	private ImageView m_closeImg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
			WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_advertise);
		Intent intent = getIntent();
		if (intent != null) {
			m_adModel = (DefaultAdModel) intent.getParcelableExtra(ADMODEL_KEY);
			m_extAdModel = (BaseAdModel) intent.getParcelableExtra(EXTADMODEL_KEY);
		}
//		if (m_extAdModel == null) {
//			/// copy adModel to extAdModel to simplify logic
//			m_extAdModel = m_adModel;
//		}
		m_layout = (RelativeLayout) findViewById(R.id.activity_ad);
//		m_progress = (ProgressBar) findViewById(R.id.progress_ad);
		m_img = (ImageView) m_layout.findViewById(R.id.image_view_ad);
		m_img.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					/// start browser
					String href = m_extAdModel != null ? m_extAdModel.getHrefUrl() : m_adModel.getHrefUrl();
					if (href != null) {
						Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(href));
						browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(browserIntent);
						/// trigger click
						triggerClick();
					}
				} catch (Exception e) {
					Logger.error(e);
				}
			}
		});
//		m_img.setImageDrawable(DrawableManager.instance().getAssetImage("bg_connecting_ad.jpg"));
		m_closeTxt = (TextView) m_layout.findViewById(R.id.text_view_close);
		m_closeTxt.setText(String.format(getString(R.string.click_to_skip), System.getProperty("line.separator")));
		m_closeTxt.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
//		m_handler = new Handler();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onResume() {
		super.onResume();
		m_closeTxt.setVisibility(View.INVISIBLE);
//		m_progress.setVisibility(View.VISIBLE);
		String imgUrl;
		if (m_extAdModel != null) {
			imgUrl = m_extAdModel.getImageUrl();
		} else if (m_adModel != null) {
			imgUrl = m_adModel.getImageUrl();
		} else {
			return;
		}
		if (!TextUtils.isEmpty(imgUrl)) {
			/// this should read image from local cache because it is prepared in AdManager.prepareAdvertisement
			WSManager.instance().doGetImage(imgUrl, AdManager.IMAGE_WIDTH_LIMIT, AdManager.IMAGE_HEIGHT_LIMIT,
				new WSCallback<Bitmap>() {

					@Override
					public void onSuccess(Bitmap ret) {
						m_img.setImageBitmap(ret);
						try {
							triggerPV();
						} catch (Exception e) {
							Logger.error(e);
						}
					}

					@Override
					public void onFailed(WSErrorResponse reason) {

					}
				});
		}
//		m_tick = 5;
		RXTimerTask.createAndStart(1000, 5, new RXIntervalSubscriber() {
			@Override
			public void onNext(Long aLong) {
				if (aLong >= 2) {
					m_closeTxt.setVisibility(View.VISIBLE);
//					m_progress.setVisibility(View.INVISIBLE);
				}
				if (aLong >= 4) {
					finish();
				}
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	private void triggerPV() throws Exception {
		if (m_extAdModel != null) {
			String[] trackUrls = m_extAdModel.getPVUrl();
			if (trackUrls == null) {
				return;
			}
			for (String url : trackUrls) {
				doNotifyEvent(url, "triggerPV on " + url);
			}
		}
		if (m_adModel != null) {
			String[] trackUrls = m_adModel.getPVUrl();
			if (trackUrls == null) {
				return;
			}
			for (String url : trackUrls) {
				doNotifyEvent(url, "triggerPV on " + url);
			}
		}
	}

	private void triggerClick() throws Exception {
		if (m_extAdModel != null) {
			String[] clickUrls = m_extAdModel.getClickUrl();
			if (clickUrls == null) {
				return;
			}
			for (String url : clickUrls) {
				doNotifyEvent(url, "triggerClick on " + url);
			}
		}
		if (m_adModel != null) {
			String[] clickUrls = m_adModel.getClickUrl();
			if (clickUrls == null) {
				return;
			}
			for (String url : clickUrls) {
				doNotifyEvent(url, "triggerClick on " + url);
			}
		}
	}

	/**
	 * trigger PV or click
	 *
	 * @param url  target url
	 * @param info info to show in log (debug only)
	 */
	private void doNotifyEvent(String url, final String info) {
		if (TextUtils.isEmpty(url)) {
			return;
		}
		HttpRetroManager.instance().getRaw(url, new HttpCallback<ResponseBody>() {
			@Override
			public void onSuccess(ResponseBody ret) {
				Logger.debug(info + " success");
			}

			@Override
			public void onFailed(Response reason) {
				Logger.debug(info + " failed");
			}
		});
	}

//	private String getResolvedUrl(DefaultAdModel adModel, String toResolve) throws Exception {
//		if(adModel.getBaseUrl() != null) {
//			URI uri = new URI(adModel.getBaseUrl());
//			return uri.resolve(toResolve).toString();
//		}
//		return toResolve;
//	}
}
