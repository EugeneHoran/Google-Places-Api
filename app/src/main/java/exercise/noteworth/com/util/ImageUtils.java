package exercise.noteworth.com.util;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

import de.hdodenhof.circleimageview.CircleImageView;
import exercise.noteworth.com.BuildConfig;
import exercise.noteworth.com.R;
import exercise.noteworth.com.model.Result;

public class ImageUtils {

    private static final String PHOTO_URL = "https://maps.googleapis.com/maps/api/place/photo?photoreference=%s&key=%s&maxheight=400";

    @BindingAdapter("bind:loadImage")
    public static void loadDetailsPhoto(View imageView, String imageReference) {
        if (imageReference == null) {
            return;
        }
        String url = String.format(PHOTO_URL, imageReference, BuildConfig.PLACES_KEY);
        loadIcon(imageView.getContext(), url, imageView);
    }

    @BindingAdapter("bind:drawableLeft")
    public static void setTextDrawableLeft(TextView view, Integer drawable) {
        if (drawable == null) return;
        view.setCompoundDrawablesRelativeWithIntrinsicBounds(getDrawableMutate(view.getContext(), drawable, R.color.colorPrimary), null, null, null);
    }

    @BindingAdapter("bind:loadCircleImage")
    public static void loadCircleImage(ImageView imageView, String imageReference) {
        if (TextUtils.isEmpty(imageReference)) return;
        Glide.with(imageView.getContext())
                .load(imageReference)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView);
    }

    public static void loadIcon(Context context, String url, View imageView) {
        if (TextUtils.isEmpty(url)) return;
        Glide.with(context)
                .load(url)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into((ImageView) imageView);
    }

    public static Drawable getDrawableMutate(Context context, int resDrawable, int resColor) {
        Drawable drawableChange = ContextCompat.getDrawable(context, resDrawable);
        drawableChange.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(context, resColor), PorterDuff.Mode.SRC_ATOP));
        drawableChange.mutate();
        return drawableChange;
    }

    public static String getPhotoRef(Result result) {
        if (result == null) {
            return null;
        }
        if (result.getPhotos() == null) {
            return null;
        }
        if (result.getPhotos().get(0) == null) {
            return null;
        }
        if (result.getPhotos().get(0).getPhotoReference() == null) {
            return null;
        }
        return result.getPhotos().get(0).getPhotoReference();
    }
}
