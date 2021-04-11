package ddwu.mobile.final_project.ma01_20170993.MemoryRegistration;

public class MemoryDto {
    private long id;
    private String title;
    private String date;
    private String memo;
    private String image = null;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return id + ". " + title + " - " + date +  " - " + memo + " (" + image + ")";
    }
}
