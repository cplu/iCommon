package com.windfindtech.icommon.mvp.model.local;

import com.google.gson.Gson;
import com.windfindtech.icommon.gson.GsonUtil;
import com.windfindtech.icommon.iCommon;
import com.windfindtech.icommon.jsondata.enumtype.EventType;
import com.windfindtech.icommon.jsondata.event.EventList;
import com.windfindtech.icommon.jsondata.event.EventModel;
import com.windfindtech.icommon.jsondata.event.EventPair;
import com.windfindtech.icommon.mvp.model.MNotifierManager;
import com.windfindtech.icommon.retrofit.StreamParser;
import com.windfindtech.icommon.webservice.WSManager;

import java.io.InputStreamReader;
import java.util.Arrays;

import rx.Observable;

/**
 * make sure all members should be run on ui thread
 * Created by cplu on 2014/11/21.
 */
public class EventManager extends MNotifierManager<EventPair[]>{
    private static final int TIMEOUT = 30*60*1000;  // 30 minutes timeout

	public static EventManager eventPromotion = new EventManager(EventType.Promotion);
	private final EventType m_type;

	@SuppressWarnings("unchecked")
    public EventManager(final EventType type){
	    /// fetch all events by specific type
	    super();
	    m_type = type;
    }

	@Override
	public Observable<EventPair[]> getNetworkObservable() {
		return WSManager.instance().httpGet(String.format("api/activities/%s?deviceOs=%s", m_type.toString(), iCommon.DEVICE_OS),
				EventPair[].class, new StreamParser<EventPair[]>() {
					@Override
					public EventPair[] parse(InputStreamReader reader) {
						Gson gson = GsonUtil.getGson();
						EventList list = gson.fromJson(reader, EventList.class);

						if (list != null && list.getData() != null) {
							EventModel[] models = list.getData();
							Arrays.sort(models);
							if (models.length > 0) {
								EventPair[] pairs = new EventPair[models.length];
								for (int i = 0; i < models.length; i++) {
									pairs[i] = new EventPair();
									pairs[i].set_model(models[i]);
								}
								return pairs;
							}
						}
						return null;
					}
				}, TIMEOUT, true);
	}
}
