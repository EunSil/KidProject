package ddwu.mobile.final_project.ma01_20170993.FindKindergarden;

import java.io.Serializable;

public class DetailKindergardenDto implements Serializable {

    private int _id;
    private String typeName;
    private String openDate;
    private String phoneNumber;
    private String homePage;
    private String spec;
    private String isCar;
    private String careRoomCnt;
    private String careRoomSize;
    private String playCnt;
    private String cctvCnt;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getOpenDate() {
        return openDate;
    }

    public void setOpenDate(String openDate) {
        this.openDate = openDate;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getHomePage() {
        return homePage;
    }

    public void setHomePage(String homePage) {
        this.homePage = homePage;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getCareRoomCnt() {
        return careRoomCnt;
    }

    public void setCareRoomCnt(String careRoomCnt) {
        this.careRoomCnt = careRoomCnt;
    }

    public String getCareRoomSize() {
        return careRoomSize;
    }

    public void setCareRoomSize(String careRoomSize) {
        this.careRoomSize = careRoomSize;
    }

    public String getPlayCnt() {
        return playCnt;
    }

    public void setPlayCnt(String playCnt) {
        this.playCnt = playCnt;
    }

    public String getCctvCnt() {
        return cctvCnt;
    }

    public void setCctvCnt(String cctvCnt) {
        this.cctvCnt = cctvCnt;
    }

    public String getIsCar() {
        return isCar;
    }

    public void setIsCar(String isCar) {
        this.isCar = isCar;
    }

    @Override
    public String toString() {
        return  _id + ": " + phoneNumber;
    }
}
