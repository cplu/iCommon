package com.windfindtech.icommon.view.recycler;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.ButterKnife;

/**
 * Created by cplu on 2016/8/3.<br/>
 * viewholder in MVP, extending RecyclerView.ViewHolder<br/>
 * viewholder belongs to "View" in MVP<br/>
 */
public abstract class MVPViewHolder<ItemType> extends RecyclerView.ViewHolder {
	public MVPViewHolder(View itemView) {
		super(itemView);
		ButterKnife.bind(this, itemView);
	}

	/**
	 * set item data to view
	 * @param position
	 * @param data
	 */
	public abstract void bindData(int position, ItemType data);

	/**
	 * update bitmap to the view associated with the holder
	 * derived class may override to implement bitmap updating
	 * @param index     position index of the bitmap in the holder
	 * @param bitmap    bitmap to be set
	 */
	public void updateBitmap(int index, Bitmap bitmap) {

	}
}
