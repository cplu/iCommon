package com.windfindtech.icommon.fragment.pts;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.windfindtech.icommon.R;
import com.windfindtech.icommon.R2;
import com.windfindtech.icommon.jsondata.points.RankItem;
import com.windfindtech.icommon.util.ResUtil;
import com.windfindtech.icommon.util.Utils;
import com.windfindtech.icommon.view.CircleImageDrawable;
import com.windfindtech.icommon.view.DynamicImageView;
import com.windfindtech.icommon.view.DynamicSizeListener;
import com.windfindtech.icommon.view.recycler.MVPAdapter;
import com.windfindtech.icommon.view.recycler.MVPViewHolder;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;

/**
 * Created by py on 2016/11/4.
 */

class HorizontalRecyclerViewAdapter extends MVPAdapter<RankItem, MVPViewHolder> {


    private static final int ITEM_TYPE_AVATAR = 0;
    private static final int ITEM_TYPE_RANK = 1;
    private static final int ITEM_TYPE_SPACE = 2;
    private final Context m_ctx;
    private RankItem m_myRankData;
    private Bitmap m_bitmap;
    private RankItem m_spaceItem1 =new RankItem();
    private RankItem m_spaceItem2=new RankItem();
//    private OnItemSelectedListener m_onItemSelectedListener;


    /**
     * @param ctx
     */
    public HorizontalRecyclerViewAdapter(Context ctx) {
        super(ctx);
        this.m_ctx = ctx;
        m_spaceItem1.setName("");
        m_spaceItem2.setName("");
        m_adapterData=new ArrayList<>(Arrays.asList(m_spaceItem1,m_spaceItem2));

    }

    @Override
    public int getItemViewType(int position) {
        if (m_myRankData != null && position == m_myRankData.getLevel()) {
            return ITEM_TYPE_AVATAR;
        } else if (position == 0 || position == m_adapterData.size() - 1) {
            return ITEM_TYPE_SPACE;
        } else {
            return ITEM_TYPE_RANK;
        }
    }

    @Override
    public MVPViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case ITEM_TYPE_AVATAR:
                return new AvatarItemHolder(getInflated(parent, R.layout.layout_horizontal_avatar_item));
            case ITEM_TYPE_RANK:
                return new RankItemHolder(getInflated(parent, R.layout.layout_horizontal_rank_item));
            case ITEM_TYPE_SPACE:
                return new SpaceItemHolder(getInflated(parent, R.layout.layout_horizontal_space_item));
            default:
                return new SpaceItemHolder(getInflated(parent, R.layout.layout_horizontal_space_item));
        }
    }



    public void setAvatarBitmap(Bitmap bitmap) {
        this.m_bitmap = bitmap;
    }

    @Override
    public int getItemCount() {
        return m_adapterData != null ? m_adapterData.size() : 0;
    }

    public void setMyRankItem(RankItem ret) {
        this.m_myRankData = ret;
        notifyDataSetChanged();
    }

    public void setRankData(RankItem[] ret) {
        if (ret != null) {
            m_adapterData = new ArrayList<>();
            m_adapterData.add(0,m_spaceItem1);
            m_adapterData.addAll(1,Arrays.asList(ret));
            m_adapterData.add(ret.length+1,m_spaceItem2);
        } else {
            m_adapterData = new ArrayList<>(Arrays.asList(m_spaceItem1, m_spaceItem2));
        }
        notifyDataSetChanged();
    }

//    public void setOnItemSelectedListener(OnItemSelectedListener onItemSelectedListener) {
//        this.m_onItemSelectedListener=onItemSelectedListener;
//    }
//
//    public interface OnItemSelectedListener{
//        void onItemSelect(View view,int position);
//    }

    class AvatarItemHolder extends MVPViewHolder<RankItem> {
        @BindView(R2.id.my_rank_avatar)
        DynamicImageView m_avatarImage;
        @BindView(R2.id.my_rank_level_img)
        ImageView m_levelImg;
        @BindView(R2.id.my_rank_level_txt)
        TextView m_leveltxt;


        public AvatarItemHolder(View itemView) {
            super(itemView);
            resetChildViewWidth(itemView);

        }

//        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void bindData(final int position, RankItem data) {
            if (m_bitmap != null) {
                m_avatarImage.setImageDrawable(new CircleImageDrawable(m_ctx, m_bitmap)
                        .setBorderColor(R.color.default_white));
            }
            m_avatarImage.setSizeListener(new DynamicSizeListener() {
                @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
                @Override
                public void onSizeChanged(int w, int h, int oldw, int oldh) {
                    if (w > 0 && h > 0) {
                        int levelImg_width = m_levelImg.getMeasuredWidth();
                        m_levelImg.setPivotX(levelImg_width / 2);
                        m_levelImg.setTranslationX(levelImg_width / 2);
                    }
                }
            });

            m_leveltxt.setText(data.getName());
            m_leveltxt.setTextColor(m_ctx.getResources().getColor(ResUtil.getRankLevelTextColorId(data.getLevel())));
            m_levelImg.setImageDrawable(ResUtil.getDrawableResByLevel(data.getLevel()));
        }
    }



    class RankItemHolder extends MVPViewHolder<RankItem> {
        @BindView(R2.id.rank_level_img)
        ImageView m_levelImg;
        @BindView(R2.id.rank_level_txt)
        TextView m_leveltxt;


        public RankItemHolder(View itemView) {
            super(itemView);
            resetChildViewWidth(itemView);
        }

        @Override
        public void bindData(int position, RankItem data) {
            m_leveltxt.setText(data.getName());
            m_levelImg.setImageDrawable(ResUtil.getGrayDrawableResByLevel(data.getLevel()));
        }

    }

    class SpaceItemHolder extends MVPViewHolder<RankItem> {


        public SpaceItemHolder(View itemView) {
            super(itemView);
            resetChildViewWidth(itemView);
        }

        @Override
        public void bindData(int position, RankItem data) {

        }

    }

    private void resetChildViewWidth(View itemView) {
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,  ViewGroup.LayoutParams.WRAP_CONTENT);
        params.width = Utils.s_deviceWidth / 3;
        itemView.setLayoutParams(params);
    }

}


