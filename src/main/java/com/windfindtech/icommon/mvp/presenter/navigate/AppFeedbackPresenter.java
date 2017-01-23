package com.windfindtech.icommon.mvp.presenter.navigate;

import com.windfindtech.icommon.error.ErrorSubmit;
import com.windfindtech.icommon.jsondata.webservice.BaseResponse;
import com.windfindtech.icommon.jsondata.webservice.FeedbackInfo;
import com.windfindtech.icommon.jsondata.webservice.WSErrorResponse;
import com.windfindtech.icommon.mvp.view.navigate.IAppFeedbackView;
import com.windfindtech.icommon.webservice.WSCallback;
import com.windfindtech.icommon.webservice.WSManager;

import org.pmw.tinylog.Logger;

import java.io.File;

/**
 * Created by cplu on 2016/8/2.
 */
public class AppFeedbackPresenter extends INavigatePresenter<IAppFeedbackView> {
	@Override
	protected IAppFeedbackView createDummy() {
		return IAppFeedbackView.dummy;
	}

	/**
	 * submit the feedback to ws
	 *
	 * @param info
	 */
	public void submitFeedback(String info) {
		FeedbackInfo feedbackInfo = new FeedbackInfo();
		feedbackInfo.setContent(info);
		WSManager.instance().doSubmitFeedback(feedbackInfo, new WSCallback<BaseResponse>() {
			@Override
			public void onSuccess(BaseResponse ret) {
				getView().onSubmitFeedbackSuccess(ret);
			}

			@Override
			public void onFailed(WSErrorResponse reason) {
				getView().onSubmitFeedbackFailed(reason);
			}
		});
	}

	/**
	 * send the error report to the webservice
	 *
	 * @param log_file
	 */
	public void sendErrorReport(File log_file, String userFeedback) {
		ErrorSubmit.sendAppLogs(log_file, userFeedback, new WSCallback<BaseResponse>() {
			@Override
			public void onSuccess(BaseResponse ret) {
				Logger.info("[sendAppLogs] onSuccess - ret : " + ret);
				getView().onSendErrorReportSuccess(ret);
			}

			@Override
			public void onFailed(WSErrorResponse reason) {
				Logger.info("[sendAppLogs] onFailure - statusCode " + reason.getStatusCode() + " reason: " + reason.getMessage());
				getView().onSendErrorReportFailed(reason);
			}
		});
	}
}
