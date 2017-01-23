package com.windfindtech.icommon.view.recycler;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.pmw.tinylog.Logger;

import java.util.ArrayList;
import java.util.Arrays;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by cplu on 2016/8/3.<br/>
 * adapter in MVP, extending RecyclerView.Adapter<br/>
 * adapter belongs to "View" in MVP, it can share presenter with Fragment/Activity<br/>
 * derived classes should override {@link RecyclerView.Adapter#onCreateViewHolder(ViewGroup, int)}<br/>
 * here's a typical implementation: <br/>
 *	public AlbumItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {<br/>
 *	    return new AlbumItemHolder(getInflated(parent));<br/>
 *	}<br/>
 */
public abstract class MVPAdapter<ItemType, HolderType extends MVPViewHolder> extends RecyclerView.Adapter<HolderType> {
//	private final Class<HolderType> m_clazz;
	protected ArrayList<ItemType> m_adapterData;
//	protected int[] m_itemLayoutID;
	protected LayoutInflater m_inflater;
	protected Context m_ctx;

	/**
	 *
	 * @param ctx
	 */
	public MVPAdapter(Context ctx){
		super();
		m_ctx = ctx;
//		m_itemLayoutID = item_layout_id;
		m_inflater = LayoutInflater.from(ctx);
	}

	/**
	 * set data of adapter.
	 * @param data
	 * @param invalidate        true if notifyDataSetChanged is called
	 */
	public void setData(ItemType[] data, boolean invalidate){
		if(data != null) {
			m_adapterData = new ArrayList<>(Arrays.asList(data));
		} else {
			m_adapterData = new ArrayList<>();
		}
		if(invalidate){
			notifyDataSetChanged();
		}
	}

	/**
	 * Set data of adapter. The ownership of data is transferred to the adapter. The caller SHOULD NOT change the data anymore
	 * @param data
	 * @param invalidate        true if notifyDataSetChanged is called
	 */
	public void setData(ArrayList<ItemType> data, boolean invalidate){
		m_adapterData = data;
		if(invalidate){
			notifyDataSetChanged();
		}
	}

	public ArrayList<ItemType> getData(){
		return m_adapterData;
	}

	public ItemType getData(int position) {
		if(position >= 0 && position < getItemCount()) {
			return m_adapterData.get(position);
		}
		return null;
	}

//	public View getInflated(ViewGroup parent, int index) {
//		return m_inflater.inflate(m_itemLayoutID[index], parent, false);
//	}

	public View getInflated(ViewGroup parent, @LayoutRes int layoutID) {
		return m_inflater.inflate(layoutID, parent, false);
	}

	@Override
	public void onBindViewHolder(final HolderType holder, final int position) {
		holder.itemView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(m_onItemClickListener != null) {
					m_onItemClickListener.onItemClick(holder.itemView, position, getItemId(position));
				}
			}
		});

		ItemType data = m_adapterData.get(position);
		if(data != null) {
			holder.bindData(position, data);
			Observable<Bitmap>[] observables = getImages(position, data);
			for (int i = 0; i < observables.length; i++) {
				final int j = i;
				observables[i].subscribe(new Subscriber<Bitmap>() {
					@Override
					public void onCompleted() {

					}

					@Override
					public void onError(Throwable throwable) {

					}

					@Override
					public void onNext(Bitmap bitmap) {
						if (holder.getLayoutPosition() == position) {
							holder.updateBitmap(j, bitmap);
						} else {
							Logger.debug("MVPAdapter: not current bitmap");
						}
					}
				});
			}
		}
	}

	/**
	 * Get images for the holder at specific position. If an array of zero size is returned, no bitmaps are required for this adapter
	 * derived classes may override to return observables, one of each represents one bitmap
	 * @param position
	 * @param data
	 * @return zero or more Observables representing bitmaps
	 */
	public Observable<Bitmap>[] getImages(int position, ItemType data) {
		/// default implementation is empty
		return new Observable[0];
	}

	@Override
	public int getItemCount() {
		return m_adapterData != null ? m_adapterData.size() : 0;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	//define interface
	public interface OnItemClickListener {
		void onItemClick(View view, int position, long id);
	}

	private OnItemClickListener m_onItemClickListener = null;

	public void setOnItemClickListener(OnItemClickListener listener) {
		this.m_onItemClickListener = listener;
	}
}
