package exercise.noteworth.com.util;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import exercise.noteworth.com.R;

public class Helper {


    /**
     * Check to see if device version is compatible
     *
     * @return Min 21 Version
     */
    public static boolean isL() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    /**
     * Calculate listview size
     */
    public static void getListViewSize(ListView myListView) {
        ListAdapter myListAdapter = myListView.getAdapter();
        if (myListAdapter == null) {
            //do nothing return null
            return;
        }
        //set listAdapter in loop for getting final size
        int totalHeight = 0;
        for (int size = 0; size < myListAdapter.getCount(); size++) {
            View listItem = myListAdapter.getView(size, null, myListView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        //setting listview item in adapter
        ViewGroup.LayoutParams params = myListView.getLayoutParams();
        params.height = totalHeight + (myListView.getDividerHeight() * (myListAdapter.getCount() - 1));
        myListView.setLayoutParams(params);
    }

    public static String getSortBy(Integer position) {
        if (position == null) {
            return null;
        }
        switch (position) {
            case 0:
                return "prominence";
            case 1:
                return "distance";
            default:
                return "prominence";
        }
    }

    public static String getRating(Integer rating) {
        if (rating == null) return null;
        switch (rating) {
            case 0:
                return "Free";
            case 1:
                return "$";
            case 2:
                return "$$";
            case 3:
                return "$$$";
            case 4:
                return "$$$$";
            default:
                return "";
        }
    }

    /**
     * Distance Filter
     * Meters list converted manually
     */
    private static Double[] metersList = {
            1609.344, // 1 Mile
            8046.72,  // 5 Miles
            16093.44, // 10 Miles
            32186.88, // 20 Miles
            80467.2   // 50 Miles
    };

    /**
     * Convert Miles to Meters
     * Places Api radius is measured in meters
     *
     * @param position Distance Spinner Position
     * @return Meters based on distance in miles
     */
    public static Double getMeters(Integer position) {
        if (position == null) {
            return null;
        }
        return metersList[position];
    }

    /**
     * Keyword List
     * <p>
     * Typically I would reference xml array list but it requires (some context).getResources()
     * which can cause issues while testing
     */
    private static String[] keywordList = {
            "Japanese",
            "Pizza",
            "Mexican",
            "Chinese",
            "Italian",
            "Thai",
            "Seafood",
            "Burgers",
            "Korean",
            "Vegetarian",
            "Sushi",
            "Sandwiches",
            "American",
            "Breakfast"
    };

    /**
     * Get Keyword based on view position
     * <p>
     * Removed Context from presenter to prevent testing issues
     *
     * @param position Keyword Single Selector List Position
     * @return keywordList[listSelectedPosition]
     */
    public static String getKeyword(Integer position) {
        if (position == null || position == -1) {
            return null;
        }
//        String[] keywordList = context.getResources().getStringArray(R.array.spinner_keyword);
        return keywordList[position];
    }

    public static Integer getPriceRange(Integer position) {
        if (position == null || position == -1) {
            return null;
        }
        return position + 1;
    }

    /**
     * Format Filter ModelTextHeader String
     *
     * @param context     activity
     * @param sortByPos   Filter Pos Spinner
     * @param distancePos Distance Pos Spinner
     * @param pricePos    Price Pos Switch
     * @param keywordPos  Keyword Pos Single Selector List
     * @return Filter string formatted
     */
    public static String getHeaderString(Context context, Integer sortByPos, Integer distancePos, Integer pricePos, Integer keywordPos) {
        String[] sortList = context.getResources().getStringArray(R.array.spinner_filter);
        String[] distanceList = context.getResources().getStringArray(R.array.spinner_distance);
        String[] priceList = context.getResources().getStringArray(R.array.switch_money);
        String[] keywordList = context.getResources().getStringArray(R.array.spinner_keyword);
        String filter = sortList[sortByPos];
        String distance = "";
        if (sortByPos != 1) {
            distance = ", " + distanceList[distancePos];
        }
        String price = "";
        if (pricePos != -1) {
            price = ", " + priceList[pricePos];
        }
        String keyword = "";
        if (keywordPos != -1) {
            keyword = ", " + keywordList[keywordPos];
        }
        return "Filtered by: " + filter + distance + price + keyword;
    }

}
