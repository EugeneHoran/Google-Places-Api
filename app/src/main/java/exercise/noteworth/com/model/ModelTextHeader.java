package exercise.noteworth.com.model;


import android.os.Parcel;
import android.os.Parcelable;

public class ModelTextHeader {
    private String headerTitle;

    public ModelTextHeader() {
    }

    public ModelTextHeader(String headerTitle) {
        this.headerTitle = headerTitle;
    }

    public String getHeaderTitle() {
        return headerTitle;
    }

    public void setHeaderTitle(String headerTitle) {
        this.headerTitle = headerTitle;
    }

}
