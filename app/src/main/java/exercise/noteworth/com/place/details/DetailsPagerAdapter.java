package exercise.noteworth.com.place.details;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import exercise.noteworth.com.databinding.PagerPhotosBinding;

public class DetailsPagerAdapter extends PagerAdapter {

    private List<String> photoRefList;
    private LayoutInflater mLayoutInflater;

    DetailsPagerAdapter(Context context, List<String> photoRefList) {
        this.photoRefList = photoRefList;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return photoRefList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        PagerPhotosBinding binding = PagerPhotosBinding.inflate(mLayoutInflater, container, false);
        binding.setPhotoRef(photoRefList.get(position));
        container.addView(binding.getRoot());
        return binding.getRoot();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ImageView) object);
    }
}