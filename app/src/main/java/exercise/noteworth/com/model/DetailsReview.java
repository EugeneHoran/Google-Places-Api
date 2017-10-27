
package exercise.noteworth.com.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DetailsReview {

    @SerializedName("author_name")
    @Expose
    public String authorName;
    @SerializedName("author_url")
    @Expose
    public String authorUrl;
    @SerializedName("language")
    @Expose
    public String language;
    @SerializedName("profile_photo_url")
    @Expose
    public String profilePhotoUrl;
    @SerializedName("rating")
    @Expose
    public Integer rating;
    @SerializedName("relative_time_description")
    @Expose
    public String relativeTimeDescription;
    @SerializedName("text")
    @Expose
    public String text;
    @SerializedName("time")
    @Expose
    public Integer time;

    public DetailsReview(String authorName, String authorUrl, String language, String profilePhotoUrl, Integer rating, String relativeTimeDescription, String text, Integer time) {
        this.authorName = authorName;
        this.authorUrl = authorUrl;
        this.language = language;
        this.profilePhotoUrl = profilePhotoUrl;
        this.rating = rating;
        this.relativeTimeDescription = relativeTimeDescription;
        this.text = text;
        this.time = time;
    }

    public DetailsReview() {
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorUrl() {
        return authorUrl;
    }

    public void setAuthorUrl(String authorUrl) {
        this.authorUrl = authorUrl;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getProfilePhotoUrl() {
        return profilePhotoUrl;
    }

    public void setProfilePhotoUrl(String profilePhotoUrl) {
        this.profilePhotoUrl = profilePhotoUrl;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getRelativeTimeDescription() {
        return relativeTimeDescription;
    }

    public void setRelativeTimeDescription(String relativeTimeDescription) {
        this.relativeTimeDescription = relativeTimeDescription;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

}
