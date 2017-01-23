package com.windfindtech.icommon.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.windfindtech.icommon.R;
import com.windfindtech.icommon.jsondata.webservice.RatingCheck;

import org.pmw.tinylog.Logger;

/**
 * Created by py on 2015/12/1.
 */
public class WebMapDialog extends BaseDialog {

	private static final String RATING_MODEL = "rating_model";
	private static final String WIDTH = "width";
	private static final String HEIGHT = "height";
	private WebView m_webMap;
//	private WFTLocation m_location;
//	private String m_siteName;
	private RatingCheck m_ratingModel;
	private int m_width;
	private int m_height;

	public static WebMapDialog create(long id, RatingCheck model, int width, int height, FragmentManager manager) {
		WebMapDialog dialog = new WebMapDialog();
		Bundle bundle = dialog.getBaseBundle(id);
		bundle.putParcelable(RATING_MODEL, model);
		bundle.putInt(WIDTH, width);
		bundle.putInt(HEIGHT, height);
		dialog.setArguments(bundle);
		showDialog(manager, dialog, "WebMapDialog");
		return dialog;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setStyle(STYLE_NO_TITLE, R.style.dialog_notitlebar_transparent_slowanimation);
		Bundle bundle = getArguments();
		m_ratingModel = bundle.getParcelable(RATING_MODEL);
		m_width = bundle.getInt(WIDTH);
		m_height = bundle.getInt(HEIGHT);
//		m_callback = (RatingCallback) getCallback();
	}

	@Override
	public void onStart() {
		super.onStart();
		getDialog().getWindow().setLayout(m_width, m_height);
	}

	@Override
	protected int getLayoutId() {
		return R.layout.dialog_map;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
	                         @Nullable Bundle savedInstanceState) {
		m_root = super.onCreateView(inflater, container, savedInstanceState);
		ImageView m_ivCancel = (ImageView) m_root.findViewById(R.id.map_iv_cancel);
		m_ivCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		m_webMap = (WebView) m_root.findViewById(R.id.webmap);
		initWebMap();
		return m_root;
	}

	private void initWebMap() {

		m_webMap.setWebViewClient(new WebViewClient()); /// avoid jumping to system browser

		try {
			WebSettings settings = m_webMap.getSettings();
			settings.setJavaScriptEnabled(true);
			settings.setDomStorageEnabled(true);
			settings.setAllowFileAccess(true);
			settings.setBuiltInZoomControls(false);
			settings.setUseWideViewPort(true);
//			settings.setUserAgentString("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.86 Safari/537.36");

			String url = String.format("file:///android_asset/html/amap.html?lon=%s&lat=%s&description=%s",
				m_ratingModel.getLocation().getLongitude(), m_ratingModel.getLocation().getLatitude(), m_ratingModel.getName());
			m_webMap.loadUrl(url);
		}
		catch (Exception e){
			Logger.error(e);
		}
	}
}
