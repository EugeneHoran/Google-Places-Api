
package exercise.noteworth.com.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DetailsGeometry {

    @SerializedName("location")
    @Expose
    private DetailsLocation location;

    public DetailsLocation getLocation() {
        return location;
    }

    public void setLocation(DetailsLocation location) {
        this.location = location;
    }

}
