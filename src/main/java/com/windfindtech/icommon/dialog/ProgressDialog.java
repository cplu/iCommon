package com.windfindtech.icommon.dialog;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.windfindtech.icommon.R;
import com.windfindtech.icommon.dialog.callback.ProgressDialogCallback;
import com.windfindtech.icommon.iCommon;
import com.windfindtech.icommon.rx.RXTimerTask;

import org.pmw.tinylog.Logger;

import rx.Subscriber;
import rx.Subscription;

/**
 * 进度条，
 *
 * @author cplu
 */
public class ProgressDialog extends BaseDialog {
	private static final String URI = "uri";
	private static final String DOWNLOAD_REFERENCE = "download_reference";
	//    private ProgressDialogCallback m_callback;
	private ProgressBar m_progressBar;
	private TextView m_totalSizeView;
	private TextView m_percentView;

	//    private Context m_ctx;
	private DownloadManager m_downloadManager;
	private DownloadBroadcast m_receiver;
	private Uri m_uri;
	//    private Handler m_handler;
	private String m_filePath;
	//    private boolean m_bFinished;
	private long m_reference;
	private int m_totalSize = -1;
	private DownloadManager.Query m_downloadQuery;
	private ProgressDialogCallback m_callback;
	private Subscription m_timerSubscription;
//    private int m_download_status;
	private static final String TAG = "ProgressDialog";

	public static ProgressDialog create(long id, Uri uri, FragmentManager manager) {
		ProgressDialog dialog = new ProgressDialog();
		Bundle bundle = dialog.getBaseBundle(id);
		bundle.putParcelable(URI, uri);
		dialog.setArguments(bundle);
		showDialog(manager, dialog, TAG);
		return dialog;
	}

	public static boolean isShowing(FragmentManager manager) {
		return BaseDialog.isShowing(manager, TAG);
	}

	/**
	 *
	 */
	public ProgressDialog() {

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setStyle(STYLE_NORMAL, R.style.dialog_default);
		Bundle bundle = getArguments();
		m_uri = bundle.getParcelable(URI);
		m_callback = (ProgressDialogCallback) getCallback();

		setCancelable(false);

//        m_handler = new Handler();
		m_downloadManager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);

		m_receiver = new DownloadBroadcast();

		IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
		getActivity().registerReceiver(m_receiver, filter);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
	                         @Nullable Bundle savedInstanceState) {
		m_root = super.onCreateView(inflater, container, savedInstanceState);

		getDialog().setTitle(getString(R.string.dlg_progress_title));
		m_root.setBackgroundResource(R.drawable.dialog_custom);
		m_progressBar = (ProgressBar) m_root.findViewById(R.id.progress_bar);
		m_totalSizeView = (TextView) m_root.findViewById(R.id.textview_total_size);
		m_percentView = (TextView) m_root.findViewById(R.id.textview_percent);

		Button negative_btn = (Button) m_root.findViewById(R.id.btn_negative);
		negative_btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
				m_callback.onProgressDlgCancelled(m_uniqueID, "");
			}
		});

		if (savedInstanceState != null) {
			m_reference = savedInstanceState.getLong(DOWNLOAD_REFERENCE);
		}
		if (m_reference <= 0) {

//        Uri uri = Uri.parse("http://xmp.down.sandai.net/kankan/XMPSetup_4.9.17.2314-dl.exe");
//        Uri uri = Uri.parse("http://down.ttdtweb.com/release/android/TTPod_Android_v8.0.1.apk");

			DownloadManager.Request request = new DownloadManager.Request(m_uri);
			//        request.setTitle("title");
			//        request.setDescription("m_description");
			//        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
			request.setDestinationInExternalFilesDir(getActivity(), Environment.DIRECTORY_DOWNLOADS, "app.apk");
			request.setTitle(getContext().getString(R.string.app_name));
			//        request.setDestinationUri(Uri.fromFile(new File(m_filePath)));
			m_reference = m_downloadManager.enqueue(request);
		}
		m_downloadQuery = new DownloadManager.Query();
		m_downloadQuery.setFilterById(m_reference);
//        m_handler.post(runnable);
		prepare_file();

		m_timerSubscription = RXTimerTask.createAndStart(1000, new Subscriber<Long>() {
			@Override
			public void onCompleted() {

			}

			@Override
			public void onError(Throwable throwable) {

			}

			@Override
			public void onNext(Long aLong) {
				update_progress();
			}
		});

		return m_root;
	}

	@Override
	protected int getLayoutId() {
		return R.layout.dialog_progress;
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (m_reference > 0) {
			outState.putLong(DOWNLOAD_REFERENCE, m_reference);
		}
	}

	@Override
	public void onDestroyView() {
		RXTimerTask.stop(m_timerSubscription);
//		m_downloadManager.remove(m_reference);
		m_reference = 0;
		super.onDestroyView();
	}

	@Override
	public void onDestroy() {

		if (getActivity() != null) {
			getActivity().unregisterReceiver(m_receiver);
		}
		super.onDestroy();
	}

	public class DownloadBroadcast extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction() == DownloadManager.ACTION_DOWNLOAD_COMPLETE) {
				RXTimerTask.stop(m_timerSubscription);
//                update_progress();

				try {
					int status = query_status();
					if (status == DownloadManager.STATUS_SUCCESSFUL) {
						Intent open_intent = new Intent(Intent.ACTION_VIEW);
						open_intent.setDataAndType(Uri.parse("file://" + m_filePath)
							, "application/vnd.android.package-archive");
						open_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(open_intent);
					} else {
						iCommon.showToast(getActivity(), R.string.download_app_failed);
					}
					dismiss();
				} catch (Exception e) {
					Logger.error(e);
					iCommon.showToast(getActivity(), R.string.download_app_failed);
				}
//            }
			}
		}
	}

	private void prepare_file() {
		try {
			Cursor cursor = m_downloadManager.query(m_downloadQuery);
			if (cursor.moveToFirst()) {
				int fileNameIdx =
					cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME);
				int total_size =
					cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES);
				m_filePath = cursor.getString(fileNameIdx);
				m_totalSize = cursor.getInt(total_size);

				if (m_totalSize != -1) {
					m_totalSizeView.setText(String.format("%s%.2fMB", getContext().getString(R.string.file_size_prefix),
						((float) m_totalSize) / 1024 / 1024));
				}
			}
			cursor.close();
		} catch (Exception e) {
			Logger.error(e);
		}
	}

	private void update_progress() {
		try {
			Cursor cursor = m_downloadManager.query(m_downloadQuery);
			if (cursor.moveToFirst()) {
				int download_size =
					cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR);

				int finish_size = cursor.getInt(download_size);
				if (m_totalSize == -1) {
					prepare_file();
				} else if (finish_size > 0) {
					int progress = finish_size * 100 / m_totalSize;
					m_progressBar.setProgress(progress);
					m_percentView.setText(String.format("%s%d%%", getContext().getString(R.string.file_download_progress_prefix), progress));
				}
			}
			cursor.close();
		} catch (Exception e) {
			Logger.error(e);
		}
	}

	private int query_status() {
		try {
			Cursor cursor = m_downloadManager.query(m_downloadQuery);
			int status = DownloadManager.STATUS_FAILED;
			if (cursor.moveToFirst()) {
				int statusIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
				status = cursor.getInt(statusIndex);
			}
			cursor.close();
			return status;
		} catch (Exception e) {
			Logger.error(e);
			return DownloadManager.STATUS_FAILED;
		}
	}
}
