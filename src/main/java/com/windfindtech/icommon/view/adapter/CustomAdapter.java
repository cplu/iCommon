package com.windfindtech.icommon.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.windfindtech.icommon.R;
import com.windfindtech.icommon.view.recycler.MVPAdapter;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by cplu on 2015/8/28.
 * Generic implementation of Adapter for ListView.
 * Adapters should extend this class and implement {@link #initHolder(View)} and {@link #setHolderData}
 * 2016/08/03
 *  consider using {@link MVPAdapter} instead
 */
public abstract class CustomAdapter<ItemType, HolderType> extends BaseAdapter{
    protected ArrayList<ItemType> m_adapterData;
    protected int m_itemLayoutID;
    protected LayoutInflater m_inflater;
    protected Context m_ctx;

    /**
     *
     * @param ctx
     * @param item_layout_id        layout id for every item in this adapter
     */
    public CustomAdapter(Context ctx, int item_layout_id){
        m_itemLayoutID = item_layout_id;
        m_inflater = LayoutInflater.from(ctx);
        m_ctx = ctx;
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

    @Override
    public int getCount() {
        return m_adapterData != null ? m_adapterData.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return (m_adapterData != null && position < m_adapterData.size()) ? m_adapterData.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HolderType holder;
        if (convertView == null) {
            convertView = m_inflater.inflate(m_itemLayoutID, parent, false);
            holder = initHolder(convertView);
            convertView.setTag(holder);
        }
        else {
            holder = (HolderType) convertView.getTag();
        }

        String tag = "CustomAdapter" + position;
        convertView.setTag(R.id.event_list_item_id, tag);

        ItemType item = (ItemType)getItem(position);
        if(item != null){
            try {
                setHolderData(position, convertView, holder, item, tag);
            }
            catch (Exception e){

            }
        }

        return convertView;
    }

    /**
     * Initialize and return a valid holder.
     * A simple and general implementation is:
     *  HolderType holder = new HolderType();
     *  holder.xxxView = convertView.findViewById(......);
     *  ...
     *  return holder;
     * @param convertView
     * @return
     */
    protected abstract HolderType initHolder(View convertView);

    /**
     * Set data in holder according to <code>item</code>
     * @param position
     * @param convertView
     * @param holder
     * @param item
     * @param tag
     */
    protected abstract void setHolderData(int position, View convertView, HolderType holder, ItemType item, String tag);

    /**
     * Check if convertView has a tag equals to <code>tag</code>
     * This is usually used in a callback generated in {@link #setHolderData(int, View, Object, Object, String)}
     *  to check if convertView has the same tag as it is given when {@link #setHolderData(int, View, Object, Object, String)} is called
     * @param convertView
     * @param tag
     * @return
     */
    protected boolean checkTag(View convertView, String tag){
        String current_tag = (String)convertView.getTag(R.id.event_list_item_id);
        return current_tag.equals(tag);
    }

    protected String getTagForCheck(View convertView) {
        return (String)convertView.getTag(R.id.event_list_item_id);
    }
}
