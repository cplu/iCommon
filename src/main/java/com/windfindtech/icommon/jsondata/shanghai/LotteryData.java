package com.windfindtech.icommon.jsondata.shanghai;

import android.content.Context;
import android.util.Pair;

import com.google.gson.annotations.SerializedName;
import com.windfindtech.icommon.R;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by cplu on 2015/5/22.
 */
public class LotteryData extends ISHBaseData{
    private Date time;
    private LotteryItemData ssq;
    private LotteryItemData qlc;
    @SerializedName("3D")
    private LotteryItemData _3D;
    private LotteryItemData dlt;
    private LotteryItemData pl3;
    private LotteryItemData pl5;
    private LotteryItemData qxc;

    public LotteryItemData getQxc() {
        return qxc;
    }

    public void setQxc(LotteryItemData qxc) {
        this.qxc = qxc;
    }

    public LotteryItemData getDlt() {
        return dlt;
    }

    public void setDlt(LotteryItemData dlt) {
        this.dlt = dlt;
    }

    public LotteryItemData getPl3() {
        return pl3;
    }

    public void setPl3(LotteryItemData pl3) {
        this.pl3 = pl3;
    }

    public LotteryItemData getPl5() {
        return pl5;
    }

    public void setPl5(LotteryItemData pl5) {
        this.pl5 = pl5;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public LotteryItemData getSsq() {
        return ssq;
    }

    public void setSsq(LotteryItemData ssq) {
        this.ssq = ssq;
    }

    public LotteryItemData getQlc() {
        return qlc;
    }

    public void setQlc(LotteryItemData qlc) {
        this.qlc = qlc;
    }

    public LotteryItemData get3D() {
        return _3D;
    }

    public void set3D(LotteryItemData _3D) {
        this._3D = _3D;
    }

    public ArrayList<LotteryItemData> getAllLotteryItemData(){
        ArrayList<LotteryItemData> lottery_datas = new ArrayList<LotteryItemData>();
        if (ssq!=null) lottery_datas.add(ssq);
        if (qlc!=null) lottery_datas.add(qlc);
        if (dlt!=null) lottery_datas.add(dlt);
        if (pl3!=null) lottery_datas.add(pl3);
        if (pl5!=null) lottery_datas.add(pl5);
        if (qxc!=null) lottery_datas.add(qxc);
        if (_3D!=null) lottery_datas.add(_3D);
        return lottery_datas;
    }

    @Override
    public boolean isValid() {
        return ssq != null || qlc != null || _3D != null;
    }

    @Override
    public boolean getBriefInfos(String[] infos) throws Exception {
        String name = ssq != null ? ssq.getName() : null;
        infos[1] = name;
        return true;
    }

    @Override
    public Pair<Integer, String> getTextColor() throws Exception{
        return Pair.create(R.color.default_orange, "orange");
    }

    @Override
    public Pair<Integer, String> getBriefValue() throws Exception {
        String date_str = ssq != null ? ssq.getDate() : null;
        if(date_str != null){
            int index = date_str.indexOf("-");
            if(index >= 0){
                date_str = date_str.substring(index + 1);
            }
        }
        return Pair.create(0, date_str);
    }
}
