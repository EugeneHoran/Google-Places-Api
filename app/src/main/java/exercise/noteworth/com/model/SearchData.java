package exercise.noteworth.com.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class SearchData extends RealmObject {
    @PrimaryKey
    public String locationName;
    public Double locationLatitude;
    public Double locationLongitude;

    public SearchData() {
    }

    public SearchData(String locationName, Double locationLatitude, Double locationLongitude) {
        this.locationName = locationName;
        this.locationLatitude = locationLatitude;
        this.locationLongitude = locationLongitude;
    }

    public String subtitleFormatted() {
        return "near " + locationName;
    }

    public String locationFormatted() {
        return locationLatitude + "," + locationLongitude;
    }
}
