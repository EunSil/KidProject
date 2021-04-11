package ddwu.mobile.final_project.ma01_20170993.FindEvent;

import java.io.Serializable;

public class EventDetailDto implements Serializable {

    private int _id;
    private String title;
    private String image = null;
    private String imageLink;
    private String addr;
    private String place;
    private String tel;
    private String time;
    private String home;
    private String age;
    private String contentId;
    private String contentTypeId;
    private String eventstartdate;
    private String eventenddate;

    public EventDetailDto() {
    }

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

    public String getImage() { return image; }

    public void setImage(String image) { this.image = image; }

    public String getImageLink() { return imageLink; }

    public void setImageLink(String imageLink) { this.imageLink = imageLink; }

    public String getAddr() { return addr; }

    public void setAddr(String addr) { this.addr = addr; }

    public String getPlace() { return place; }

    public void setPlace(String place) { this.place = place; }

    public String getTel() { return tel; }

    public void setTel(String tel) { this.tel = tel; }

    public String getTime() { return time; }

    public void setTime(String time) { this.time = time; }

    public String getHome() { return home; }

    public void setHome(String home) { this.home = home; }

    public String getAge() { return age; }

    public void setAge(String age) { this.age = age; }

    public String getContentId() { return contentId; }

    public void setContentId(String contentId) { this.contentId = contentId; }

    public String getContentTypeId() { return contentTypeId; }

    public void setContentTypeId(String contentTypeId) { this.contentTypeId = contentTypeId; }

    public String getEventstartdate() { return eventstartdate; }

    public void setEventstartdate(String eventstartdate) { this.eventstartdate = eventstartdate; }

    public String getEventenddate() { return eventenddate; }

    public void setEventenddate(String eventenddate) { this.eventenddate = eventenddate; }

    @Override
    public String toString() {
        return  _id + ": " + title + " (" + addr + ") ";
    }
}
