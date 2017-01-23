package com.windfindtech.icommon.fragment.navigate;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.windfindtech.icommon.R;
import com.windfindtech.icommon.R2;
import com.windfindtech.icommon.dialog.ConfirmDialog;
import com.windfindtech.icommon.dialog.callback.ConfirmCallback;
import com.windfindtech.icommon.iCommon;
import com.windfindtech.icommon.jsondata.webservice.BaseResponse;
import com.windfindtech.icommon.jsondata.webservice.WSErrorResponse;
import com.windfindtech.icommon.logger.WFTLogger;
import com.windfindtech.icommon.mvp.presenter.navigate.AppFeedbackPresenter;
import com.windfindtech.icommon.mvp.view.navigate.IAppFeedbackView;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 个人中心-->用户反馈
 */
public class AppFeedbackFragment extends NavigateFragment<IAppFeedbackView, AppFeedbackPresenter>
	implements IAppFeedbackView, ConfirmCallback {

	private static final int DLG_ERROR_REPORT = 1;
	//    private ConfirmDialog m_confirmDlg;
	@BindView(R2.id.feedback_content_edit)
	EditText m_editText;
	@BindView(R2.id.feedback_submit_btn)
	Button m_feedbackSubmitBtn;
	@BindView(R2.id.error_report_submit_btn)
	Button m_errorReportBtn;
	@BindView(R2.id.error_report_edit)
	EditText m_errorEditText;

	public AppFeedbackFragment() {
		setPageName("UserFeedBackView");
	}

	@Override
	protected AppFeedbackPresenter createPresenter() {
		return new AppFeedbackPresenter();
	}

	@Override
	protected int getLayoutID() {
		return R.layout.fragment_feedback;
	}

	@Override
	protected int getTitleID() {
		return R.string.title_feedback;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

	}

	@Override
	protected NavListAdapter createNavAdapter() {
		return null;
	}

	@OnClick(R2.id.feedback_submit_btn)
	public void onFeedbackSubmitBtnClick() {
		String info = m_editText.getText().toString();
		int min_length = 2;
		if (info.length() >= min_length) {
			m_presenter.submitFeedback(info);
		} else {
			iCommon.showToast(getActivity(), R.string.feedback_not_valid, String.valueOf(min_length));
		}
	}

	@OnClick(R2.id.error_report_submit_btn)
	public void onErrorSubmitBtn() {
		ConfirmDialog.create(DLG_ERROR_REPORT, getActivity().getString(R.string.confirm_send_log), getChildFragmentManager());
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	@Override
	public void onSubmitFeedbackSuccess(BaseResponse ret) {
		m_editText.setText("");
		iCommon.showToast(getActivity(), R.string.feedback_submitted);
	}

	@Override
	public void onSubmitFeedbackFailed(WSErrorResponse reason) {
		iCommon.showToast(getActivity(), R.string.feedback_failed);
	}

	@Override
	public void onSendErrorReportSuccess(BaseResponse ret) {
		iCommon.showToast(getActivity(), R.string.error_log_submitted);
		m_errorReportBtn.setEnabled(true);
	}

	@Override
	public void onSendErrorReportFailed(WSErrorResponse reason) {
		iCommon.showToast(getActivity(), R.string.error_log_submit_failed);
		m_errorReportBtn.setEnabled(true);
	}

	@Override
	public void onConfirmResult(long id, boolean isOK) {
		if (id == DLG_ERROR_REPORT) {
			if (isOK) {
				final File log_file = WFTLogger.getLatestLogFile();
				if (log_file != null) {
					m_errorReportBtn.setEnabled(false);
					m_presenter.sendErrorReport(log_file, m_errorEditText.getText().toString());
				}
			} else {
				m_errorReportBtn.setEnabled(true);
			}
		}
	}

//    class FeedbackStruct{
//        private String log;
//        private String phone_number;
//
//        public FeedbackStruct(String log, String phone_number){
//            this.log = log;
//            this.phone_number = phone_number;
//        }
//    }
}
