package exercise.noteworth.com.model;

import android.os.Parcel;
import android.os.Parcelable;

import exercise.noteworth.com.util.Common;

public class ModelViewDivider implements Parcelable {
    private String fillerBreak = "break";

    private boolean showLine;
    private boolean showSpace;


    public ModelViewDivider(String fillerBreak) {
        this.fillerBreak = fillerBreak;
    }

    public boolean getLineVisibility() {
        if (getFillerBreak().equalsIgnoreCase(Common.DIVIDER_LINE_NO_SPACE)) {
            showLine = true;
        } else if (getFillerBreak().equalsIgnoreCase(Common.DIVIDER_LINE_WITH_SPACE)) {
            showLine = true;
        } else if (getFillerBreak().equalsIgnoreCase(Common.DIVIDER_NO_LINE_WITH_SPACE)) {
            showLine = false;
        } else {
            showLine = true;
        }
        return showLine;
    }

    public boolean getSpaceVisibility() {
        if (getFillerBreak().equalsIgnoreCase(Common.DIVIDER_LINE_NO_SPACE)) {
            showSpace = false;
        } else if (getFillerBreak().equalsIgnoreCase(Common.DIVIDER_LINE_WITH_SPACE)) {
            showSpace = true;
        } else if (getFillerBreak().equalsIgnoreCase(Common.DIVIDER_NO_LINE_WITH_SPACE)) {
            showSpace = true;
        } else {
            showSpace = true;
        }
        return showSpace;
    }


    public void setShowLine(boolean showLine) {
        this.showLine = showLine;
    }

    public boolean isShowSpace() {
        return showSpace;
    }

    public void setShowSpace(boolean showSpace) {
        this.showSpace = showSpace;
    }

    public String getFillerBreak() {
        return fillerBreak;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.fillerBreak);
    }

    protected ModelViewDivider(Parcel in) {
        this.fillerBreak = in.readString();
    }

    public static final Creator<ModelViewDivider> CREATOR = new Creator<ModelViewDivider>() {
        @Override
        public ModelViewDivider createFromParcel(Parcel source) {
            return new ModelViewDivider(source);
        }

        @Override
        public ModelViewDivider[] newArray(int size) {
            return new ModelViewDivider[size];
        }
    };
}
