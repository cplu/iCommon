package com.windfindtech.icommon.mvp.presenter.pts;

import com.windfindtech.icommon.jsondata.points.MyAchievementItem;
import com.windfindtech.icommon.mvp.presenter.IFragmentPresenter;
import com.windfindtech.icommon.mvp.view.pts.IMyAchievementView;

/**
 * Created by py on 2016/8/23.
 */
public class MyAchievementPresenter extends IFragmentPresenter<IMyAchievementView> {
    @Override
    protected IMyAchievementView createDummy() {
        return IMyAchievementView.dummy;
    }

    public MyAchievementItem[] getAchievementData() {
        MyAchievementItem item1=new MyAchievementItem(null,"上网达人","连续登陆100分钟","已达成");
        MyAchievementItem item2=new MyAchievementItem(null,"蹭网狂人","累计使用10天","已达成");
        MyAchievementItem item3=new MyAchievementItem(null,"分享天使","累计分享30次","已达成");
        MyAchievementItem item4=new MyAchievementItem(null,"省钱能手","累计兑换10000分","已达成");
        MyAchievementItem item5=new MyAchievementItem(null,"一颗赛艇","不间断续命+1s","未达成");
        MyAchievementItem item6=new MyAchievementItem(null,"蛤蛤蛤蛤","苟利国家生死以,岂因祸福避趋之","未达成");
        MyAchievementItem[] datas=new MyAchievementItem[]{item1,item2,item3,item4,item5,item6};
        return datas;
     }
}
