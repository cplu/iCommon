package com.windfindtech.icommon.jsondata.shanghai;

import android.util.Pair;

import com.windfindtech.icommon.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by cplu on 2015/5/22.
 */
public class RoadClosureData extends ISHBaseData{
    private Date time;
    private ArrayList<RoadClosureItemData> data;

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public ArrayList<RoadClosureItemData> getData() {
        return data;
    }

    public void setData(ArrayList<RoadClosureItemData> data) {
        this.data = data;
    }

    /**
     * Aggregate data item by same road and side
     */
    public void aggregateData(){
        HashMap<Pair<String, String>, RoadClosureItemData> hashedData = new HashMap<>();
        for(int i = 0; i < data.size(); i++){
            RoadClosureItemData itemData = data.get(i);
            Pair<String, String> pair_data = new Pair<>(itemData.getRoad(), itemData.getSide());
            RoadClosureItemData foundData = hashedData.get(pair_data);
            if(foundData == null){
                hashedData.put(pair_data, itemData);
            }
            else{
                String new_part = foundData.getPart() + System.getProperty("line.separator") + itemData.getPart();
	            foundData.setPart(new_part);
//                hashedData.put(pair_data, new_part);
            }
        }

        /// set data to aggregated result
        data = new ArrayList<>(hashedData.values());
    }

    @Override
    public boolean isValid() {
        return data != null && data.size() > 0;
    }

	@Override
	public boolean getBriefInfos(String[] infos) throws Exception {
		RoadClosureItemData first_item = data.get(0);
		infos[1] = first_item.getRoad();
		return true;
	}

    @Override
    public Pair<Integer, String> getTextColor() throws Exception {
        return Pair.create(R.color.default_pink, "red");
    }

    @Override
    public Pair<Integer, String> getBriefValue() throws Exception {
        RoadClosureItemData first_item = data.get(0);
        return Pair.create(0, first_item.getSide());
    }
}
