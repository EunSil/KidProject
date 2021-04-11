package ddwu.mobile.final_project.ma01_20170993.FindKindergarden;

import android.text.Html;
import android.text.Spanned;

import java.io.Serializable;

public class KindergardenDto implements Serializable {

    private int _id;
    private String name;
    private String address;
    private String fixedNumber = "-";
    private String presentNumber = "-";
    private String district;
    private String stcode;
    private double latitude;
    private double longitude;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFixedNumber() {
        return fixedNumber;
    }

    public void setFixedNumber(String fixedNumber) {
        this.fixedNumber = fixedNumber;
    }

    public String getPresentNumber() {
        return presentNumber;
    }

    public void setPresentNumber(String presentNumber) {
        this.presentNumber = presentNumber;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getName() {
        Spanned spanned = Html.fromHtml(name);
        return spanned.toString();
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStcode() {
        return stcode;
    }

    public void setStcode(String stcode) {
        this.stcode = stcode;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return  _id + ": " + name + " (" + address + ") ";
    }
}
