package com.windfindtech.icommon.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.windfindtech.icommon.R;
import com.windfindtech.icommon.R2;
import com.windfindtech.icommon.dialog.callback.ChoiceCallback;
import com.windfindtech.icommon.jsondata.enumtype.Gender;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author raise 2015.6.24
 */
public class GenderChoiceDialog extends BaseDialog {

	private static final String TITLE = "title";
	private String m_header;
//	private ChoiceCallback listener;
	@BindView(R2.id.layout_male)
	LinearLayout m_maleBtn;
	@BindView(R2.id.layout_female)
	LinearLayout m_femaleBtn;
	@BindView(R2.id.dlg_header_hint)
	TextView m_headerTxt;
	private ChoiceCallback m_callback;

	public static GenderChoiceDialog create(long id, String title, FragmentManager manager) {
		GenderChoiceDialog dialog = new GenderChoiceDialog();
		Bundle bundle = dialog.getBaseBundle(id);
		bundle.putString(TITLE, title);
		dialog.setArguments(bundle);
		showDialog(manager, dialog, "GenderChoiceDialog");
		return dialog;
	}

	public GenderChoiceDialog() {

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setStyle(STYLE_NO_TITLE, R.style.dialog_notitlebar_transparent);
		Bundle bundle = getArguments();
		m_header = bundle.getString(TITLE);
		m_callback = (ChoiceCallback) getCallback();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
	                         @Nullable Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);

		m_headerTxt.setText(m_header);
		return m_root;
	}

	@Override
	protected int getLayoutId() {
		return R.layout.dialog_choice;
	}

	@OnClick(R2.id.layout_male)
	public void onMaleClick() {
		dismiss();
		m_callback.onChoiceResult(m_uniqueID, true, Gender.Male.ordinal());
	}

	@OnClick(R2.id.layout_female)
	public void onFemaleClick() {
		dismiss();
		m_callback.onChoiceResult(m_uniqueID, true, Gender.Female.ordinal());
	}
}
