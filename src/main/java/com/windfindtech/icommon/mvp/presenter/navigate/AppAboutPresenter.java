package com.windfindtech.icommon.mvp.presenter.navigate;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.ScaleXSpan;
import android.view.View;

import com.windfindtech.icommon.mvp.view.navigate.IAppAboutView;

import org.pmw.tinylog.Logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by cplu on 2016/8/2.
 */
public class AppAboutPresenter extends INavigatePresenter<IAppAboutView> {
	@Override
	protected IAppAboutView createDummy() {
		return IAppAboutView.dummy;
	}

	/**
	 * set spannable String
	 * @param logo
	 * @return spannableString
     */
	public SpannableString getLogoSpannableString(String logo) {
		SpannableString spannableString = new SpannableString(logo);
		spannableString.setSpan(new ScaleXSpan(0.8f), 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		spannableString.setSpan(new RelativeSizeSpan(1.5f), 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		return spannableString;
	}

	public SpannableString getCompanySpannableString(String company_info, ForegroundColorSpan colorSpan) {
		SpannableString spanable_info = new SpannableString(company_info);
		Pattern pattern = Pattern.compile("[0-9]+");
		Matcher matcher = pattern.matcher(spanable_info);
		if (matcher.find()) {
			final String phone_number = matcher.group();
			int start = matcher.start();
			int end = matcher.end();
			spanable_info.setSpan(new ClickableSpan() {

				@Override
				public void onClick(View widget) {
					/// to do: make a phone call to phone_number
					Logger.debug("phone number clicked!");
					//					Intent intent = new Intent(Intent.ACTION_CALL);
					//					intent.setData(Uri.parse("tel:" + phone_number));
					//					startActivity(intent);
				}
			}, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			spanable_info.setSpan(colorSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		return spanable_info;
	}
}
