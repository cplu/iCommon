package com.windfindtech.icommon.mvp.presenter.navigate;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.windfindtech.icommon.jsondata.points.RankItem;
import com.windfindtech.icommon.jsondata.webservice.WSErrorResponse;
import com.windfindtech.icommon.mvp.model.local.AvatarManager;
import com.windfindtech.icommon.mvp.presenter.IFragmentPresenter;
import com.windfindtech.icommon.mvp.view.navigate.IMyRankView;
import com.windfindtech.icommon.rx.RXLocalSubscriber;
import com.windfindtech.icommon.webservice.WSCallback;
import com.windfindtech.icommon.webservice.WSManager;

import rx.Subscription;

/**
 * Created by py on 2016/8/23.
 */
public class MyRankPresenter extends IFragmentPresenter<IMyRankView> {
    private Subscription m_avatarSubscription;

    @Override
    protected IMyRankView createDummy() {
        return IMyRankView.dummy;
    }


    public void  retrieveRankData() {
        WSManager.instance().doHttpGet("api/benefit/levels", RankItem[].class, new WSCallback<RankItem[]>() {
            @Override
            public void onSuccess(RankItem[] ret) {
                getView().onRankDataResult(ret);
            }

            @Override
            public void onFailed(@NonNull WSErrorResponse reason) {
                getView().onRankDataResult(null);
            }
        });
    }

    @Override
    public void attach(IMyRankView view, int notifiers) {
        super.attach(view, notifiers);
        m_avatarSubscription = AvatarManager.avatarNotifier.register(new RXLocalSubscriber<Bitmap>() {
            @Override
            public void onNext(Bitmap bitmap) {
                getView().onAvatar(bitmap);
            }
        }, false);
    }

    @Override
    public void detach() {
        AvatarManager.avatarNotifier.unregister(m_avatarSubscription);

        super.detach();
    }
}
