package com.windfindtech.icommon.fragment.pts;
/**
 * Created by py on 2016/8/31.
 */

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.windfindtech.icommon.R;
import com.windfindtech.icommon.R2;
import com.windfindtech.icommon.jsondata.points.PointsRecordItem;
import com.windfindtech.icommon.view.recycler.MVPAdapter;
import com.windfindtech.icommon.view.recycler.MVPViewHolder;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;

public class PointsRecordsAdapter extends MVPAdapter<PointsRecordItem, PointsRecordsHolder> {


    /**
     * @param ctx
     */
    public PointsRecordsAdapter(Context ctx) {
        super(ctx);
    }


    @Override
    public PointsRecordsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PointsRecordsHolder(getInflated(parent, R.layout.list_item_points_record));
    }
}

class PointsRecordsHolder extends MVPViewHolder<PointsRecordItem> {

    @BindView(R2.id.item_name_view)
    TextView m_incomeTitle;

    @BindView(R2.id.item_pt_description_view)
    TextView m_incomeTime;

    @BindView(R2.id.item_value_view)
    TextView m_incomeValue;


    public PointsRecordsHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void bindData(int position, PointsRecordItem data) {
        m_incomeTitle.setText(data.getDescription() != null ? data.getDescription() : "");
        m_incomeTime.setText(data.getTime() != null ? parseDateToString(data.getTime()) : "");
        if ("income".equals(data.getDirection())) {
            m_incomeValue.setText(data.getAmount() != null ? "+" + data.getAmount() : "");
        } else if ("outcome".equals(data.getDirection())) {
            m_incomeValue.setText(data.getAmount() != null ? "-" + data.getAmount() : "");
        }
    }

    private String parseDateToString(Date time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String parseTime = simpleDateFormat.format(time);
        return parseTime;
    }


}