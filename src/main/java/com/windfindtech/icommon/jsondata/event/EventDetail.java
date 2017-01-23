package com.windfindtech.icommon.jsondata.event;


/**
 * Created by cplu on 2014/11/25.
 */
public class EventDetail extends EventModel{
    private String userId;
    private int points;
    private boolean participated;
    private EventParticipationModel[] history;
    private String href;
    /// the following parameters are only used locally
//    private int m_drawable_counts = 0;
//    private transient Drawable[] m_drawables = new Drawable[2];
    //private transient EventAward m_award;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isParticipated() {
        return participated;
    }

    public void setParticipated(boolean participated) {
        this.participated = participated;
    }

    public EventParticipationModel[] getHistory() {
        return history;
    }

    public void setHistory(EventParticipationModel[] history) {
        this.history = history;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

//    public void set_image(int index, BitmapDrawable drawable) {
//        m_drawables[index] = drawable;
//    }

//    public void set_award(EventAward award) {
//        m_award = award;
//    }

//    public void set_drawable_count(int count){
//        m_drawable_counts = count;
//    }
//
//    public boolean is_ready() {
//        for(int i = 0; i < m_drawable_counts; i++){
//            if(m_drawables[i] == null){
//                return false;
//            }
//        }
//        return true;
//    }
//
//    public Drawable get_drawable(int i) {
//        return m_drawables[i];
//    }


//    public EventAward get_award() {
//        return m_award;
//    }
}
