package exercise.noteworth.com.place.details;

import java.util.ArrayList;
import java.util.List;

import exercise.noteworth.com.R;
import exercise.noteworth.com.model.Details;
import exercise.noteworth.com.model.DetailsPhoto;
import exercise.noteworth.com.model.DetailsResult;
import exercise.noteworth.com.model.ModelTextHeader;
import exercise.noteworth.com.model.ModelTextImage;
import exercise.noteworth.com.model.ModelTextSpaceLeft;
import exercise.noteworth.com.model.ModelViewDivider;
import exercise.noteworth.com.util.Common;

public class DetailsHelper {


    public static List<Object> detailsList(Details details) {
        DetailsResult result = details.getResult();
        List<Object> objectList = new ArrayList<>();
        objectList.add(new ModelTextHeader("Business Information"));
        // Handle Phone Number
        if (result.getFormattedPhoneNumber() != null) {
            objectList.add(new ModelTextImage(result.getFormattedPhoneNumber(), R.drawable.ic_call, null));
            objectList.add(new ModelViewDivider(Common.DIVIDER_LINE_NO_SPACE));
        }
        // Handle Address
        if (result.getFormattedAddress() != null) {
            objectList.add(new ModelTextImage(result.getFormattedAddress(), R.drawable.ic_location, null));
            objectList.add(new ModelViewDivider(Common.DIVIDER_LINE_NO_SPACE));
        }
        // Handle Hours
        if (result.getOpeningHours() != null) {
            // Handle Is Open
            if (result.getOpeningHours().getOpenNow() != null) {
                String openNow = result.getOpeningHours().getOpenNow() ? "Open Now" : "Closed";
                objectList.add(new ModelTextImage("Hours: " + openNow, R.drawable.ic_access_time, null));
            }
            // Handle Hour List
            if (result.getOpeningHours().getWeekdayText() != null) {
                for (int i = 0; i < result.getOpeningHours().getWeekdayText().size(); i++) {
                    String weekText = result.getOpeningHours().getWeekdayText().get(i);
                    objectList.add(new ModelTextSpaceLeft(weekText));
                }
                objectList.add(new ModelViewDivider(Common.DIVIDER_NO_LINE_WITH_SPACE));
            }
        }
        // Handle Reviews
        if (result.getReviews() != null) {
            objectList.add(new ModelTextHeader("Reviews"));
            for (int i = 0; i < result.getReviews().size(); i++) {
                objectList.add(result.getReviews().get(i));
                if (result.getReviews().size() - 1 != i) {
                    objectList.add(new ModelViewDivider(Common.DIVIDER_LINE_NO_SPACE));
                }
            }
        }
        return objectList;
    }

    public static List<String> photoList(List<DetailsPhoto> photos) {
        List<String> photoList = new ArrayList<>();
        if (photos == null) {
            return photoList;
        }
        for (int i = 0; i < photos.size(); i++) {
            DetailsPhoto photo = photos.get(i);
            photoList.add(photo.getPhotoReference());
        }
        return photoList;
    }
}
