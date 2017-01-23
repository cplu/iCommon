package com.windfindtech.icommon.mvp.presenter.pts;

import com.windfindtech.icommon.mvp.presenter.IFragmentPresenter;
import com.windfindtech.icommon.mvp.view.pts.IPointsStoreView;

/**
 * Created by py on 2016/8/23.
 */
public class PointsStorePresenter extends IFragmentPresenter<IPointsStoreView> {
    @Override
    protected IPointsStoreView createDummy() {
        return IPointsStoreView.dummy;
    }
}
