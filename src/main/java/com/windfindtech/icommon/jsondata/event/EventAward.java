package com.windfindtech.icommon.jsondata.event;

/**
 * Created by cplu on 2014/11/25.
 */
public class EventAward extends EventParticipationModel{
//    private boolean isWinner;
    private String prize;
    private String maskedUserName;
    //private transient Drawable[] m_drawables = new Drawable[2];

    public String getPrize() {
        return prize;
    }

    public void setPrize(String prize) {
        this.prize = prize;
    }

    public String getMaskedUserName() {
        return maskedUserName;
    }

    public void setMaskedUserName(String maskedUserName) {
        this.maskedUserName = maskedUserName;
    }

//    public void set_drawable(int index, BitmapDrawable drawable) {
//        m_drawables[index] = drawable;
//    }

//    public Drawable get_drawable(int i) {
//        return m_drawables[i];
//    }

//    @Override
//    public boolean isWinner() {
//        return isWinner;
//    }
//
//    @Override
//    public void setWinner(boolean isWinner) {
//        this.isWinner = isWinner;
//    }
}
