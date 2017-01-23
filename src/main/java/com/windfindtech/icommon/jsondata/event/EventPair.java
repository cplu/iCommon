package com.windfindtech.icommon.jsondata.event;

import com.windfindtech.icommon.jsondata.enumtype.EventStatus;
import com.windfindtech.icommon.jsondata.enumtype.EventType;
import com.windfindtech.icommon.jsondata.webservice.WSErrorResponse;
import com.windfindtech.icommon.webservice.WSCallback;
import com.windfindtech.icommon.webservice.WSManager;

import org.pmw.tinylog.Logger;

/**
 * Created by cplu on 2015/7/16.
 * match EventModel to EventDetail
 */
public class EventPair {
    private static final int DETAIL_FETCHER_TIMEOUT = 5*60*1000;

//    private enum Status{
//        NoDetail,       // no detail means
//        Inprogress,
//        Detailed
//    }
//
//    private enum ImageStatus{
//        NoImage,       // no image
//        Inprogress,
//        Ready
//    }

    private EventModel m_model;     // event basic information
	private EventDetail m_detail;
	private EventImage[] m_images;

//    private EventDetail m_detail;   // event detail information
//    private Status m_status;        /// current status of fetching EventDetail
//    private ImageStatus m_image_status; /// current status of fetching images
//    private EventDetailObserver m_observable;
//    private ResponseTimeoutChecker<EventDetail> m_detailChecker;
//    private ResponseTimeoutChecker<EventImage[]> m_imageListChecker;

    /**
     * how many images are contained in each kind of event
     */
//    public static final int[] EVENT_DRAWABLE_COUNT = {
//            2,      // sweepstakes
//            1,      // check-in
//            1,      // promotion
//    };

//    /**
//     * size limit for each image of each EventType
//     * the order is as {w1, h1, w2, h2, ...} for each EventType, where wi/hi is the size limit for image i
//     */
//    private static final int[][] DRAWABLE_SIZE_LIMIT = {
//            {320, 320, 640, 640},   // sweepstakes
//            {640, 640},             // check-in
//            {640, 640}              // promotion
//    };

    public EventPair(){
        m_model = null;
        m_detail = null;
	    m_images = null;
//        m_status = Status.NoDetail;
//        m_image_status = ImageStatus.NoImage;
//        m_observable = new EventDetailObserver();
//        m_detailChecker = new ResponseTimeoutChecker<EventDetail>(DETAIL_FETCHER_TIMEOUT, EventDetail.class);
//        m_imageListChecker = new ResponseTimeoutChecker<EventImage[]>(0, EventImage[].class);
    }

    public void set_model(EventModel model){
        m_model = model;
    }

    /**
     * fetch detail of m_model
     */
    public void fetchDetail(final WSCallback<EventDetail> callback){
        if(m_model == null){
            Logger.warn("EventPair fetchDetail with model null");
            return;
        }
        final EventType event_type = m_model.getType();
        String url = String.format("api/activities/%s/%s", event_type.toString(), m_model.getId());
        WSManager.instance().doHttpGet(url, EventDetail.class, null, DETAIL_FETCHER_TIMEOUT, true, new WSCallback<EventDetail>() {
	        @Override
	        public void onSuccess(EventDetail ret) {
		        m_detail = ret;
		        callback.onSuccess(ret);
	        }

	        @Override
	        public void onFailed(WSErrorResponse reason) {
		        callback.onFailed(reason);
	        }
        });
//        m_detailChecker.doHttpGetInWebservice(url, callback);
    }

//    public void reset() {
//        m_model = null;
//        m_detail = null;
//    }

//    private class ImageCallbackWrapper{
//        private int m_current_image_cnt;
//        private int m_limit_image_cnt;
////        private EventType m_type;
////        private EventCategory m_category;
//        private boolean m_success = true;
//
//        public ImageCallbackWrapper(int limit){
//            m_limit_image_cnt = limit;
//            m_current_image_cnt = 0;
////            m_category = category;
//        }
//
//        public void onSuccess(){
//            m_current_image_cnt++;
//            if(m_current_image_cnt >= m_limit_image_cnt){
////                m_image_status = ImageStatus.Ready;
//                logger.debug("EventPair images fetched, observer count " + m_observable.countObservers());
//                m_observable.setChanged();
//                m_observable.notifyObservers(EventPair.this);
//            }
//        }
//
//        public void onFailed(){
//            if(m_success) {
//                m_success = false;
////                m_image_status = ImageStatus.NoImage;
//            }
//        }
//    }

    /**
     * fetch images related to this event
     */
    public void getImageList(final EventDetail detail, final WSCallback<EventImage[]> callback) {
        if(detail == null){
            Logger.warn("getImageList: detail is null");
            return;
        }
        final EventType event_Type = detail.getType();
        String url = String.format("api/activities/%s/%s/images", event_Type.toString(), detail.getId());
	    WSManager.instance().doHttpGet(url, EventImage[].class, new WSCallback<EventImage[]>() {
		    @Override
		    public void onSuccess(EventImage[] ret) {
			    m_images = ret;
			    callback.onSuccess(ret);
		    }

		    @Override
		    public void onFailed(WSErrorResponse reason) {
			    callback.onFailed(reason);
		    }
	    });
    }

    public EventModel getModel() {
        return m_model;
    }

    public EventStatus getStatus() {
        return m_model != null ? m_model.getStatus() : null;
    }

    public EventDetail getDetail() {
        return m_detail;
    }

	public void setDetail(EventDetail detail) {
		m_detail = detail;
	}
}
