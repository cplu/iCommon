package com.windfindtech.icommon.mvp.presenter.pts;

import com.windfindtech.icommon.mvp.presenter.IFragmentPresenter;
import com.windfindtech.icommon.mvp.view.pts.IPointsRulesView;

/**
 * Created by py on 2016/8/23.
 */
public class PointsRulesPresenter extends IFragmentPresenter<IPointsRulesView> {
    @Override
    protected IPointsRulesView createDummy() {
        return IPointsRulesView.dummy;
    }
}
