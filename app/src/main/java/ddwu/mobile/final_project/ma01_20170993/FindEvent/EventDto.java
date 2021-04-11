package ddwu.mobile.final_project.ma01_20170993.FindEvent;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class EventDto implements Serializable, Parcelable {

    private int _id;
    private String title;
    private String addr1;
    private String contentid;
    private String contentTypeId;
    private String firstimage;
    private double mapX;
    private double mapY;
    private String tel;
    private String eventstartdate;
    private String eventenddate;

    public EventDto() {
    }

    public EventDto(EventDto e){
        this._id = e.get_id();
        this.title = e.getTitle();
        this.addr1 = e.getAddr1();
        this.contentid = e.getContentid();
        this.contentTypeId = e.getContentTypeId();
        this.firstimage = e.getFirstimage();
        this.mapX = e.getMapX();
        this.mapY = e.getMapY();
        this.tel = e.getTel();
        this.eventenddate = e.getEventenddate();
        this.eventstartdate = e.getEventstartdate();
    }

    public EventDto(Parcel in) {
        readFromParcel(in);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(_id);
        dest.writeString(title);
        dest.writeString(addr1);
        dest.writeString(contentid);
        dest.writeString(contentTypeId);
        dest.writeString(firstimage);
        dest.writeDouble(mapX);
        dest.writeDouble(mapY);
        dest.writeString(tel);
        dest.writeString(eventstartdate);
        dest.writeString(eventenddate);
    }

    private void readFromParcel(Parcel in){
        _id = in.readInt();
        title = in.readString();
        addr1 = in.readString();
        contentid = in.readString();
        contentTypeId = in.readString();
        firstimage = in.readString();
        mapX = in.readDouble();
        mapY = in.readDouble();
        tel = in.readString();
        eventstartdate = in.readString();
        eventenddate = in.readString();
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public EventDto createFromParcel(Parcel in) {
            return new EventDto(in);
        }

        public EventDto[] newArray(int size) {
            return new EventDto[size];
        }
    };
    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddr1() {
        return addr1;
    }

    public void setAddr1(String addr1) {
        this.addr1 = addr1;
    }

    public String getContentid() {
        return contentid;
    }

    public void setContentid(String contentid) {
        this.contentid = contentid;
    }

    public String getContentTypeId() { return contentTypeId; }

    public void setContentTypeId(String contentTypeId) { this.contentTypeId = contentTypeId; }

    public String getFirstimage() {
        return firstimage;
    }

    public void setFirstimage(String firstimage) {
        this.firstimage = firstimage;
    }

    public double getMapX() {
        return mapX;
    }

    public void setMapX(double mapX) {
        this.mapX = mapX;
    }

    public double getMapY() {
        return mapY;
    }

    public void setMapY(double mapY) {
        this.mapY = mapY;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getEventstartdate() {
        return eventstartdate;
    }

    public void setEventstartdate(String eventstartdate) {
        this.eventstartdate = eventstartdate;
    }

    public String getEventenddate() {
        return eventenddate;
    }

    public void setEventenddate(String eventenddate) {
        this.eventenddate = eventenddate;
    }

    @Override
    public String toString() {
        return  _id + ": " + title + " (" + addr1 + ") ";
    }
}
