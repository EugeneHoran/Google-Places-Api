package exercise.noteworth.com.places;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import exercise.noteworth.com.databinding.RecyclerRestaurantItemBinding;
import exercise.noteworth.com.model.Result;
import exercise.noteworth.com.place.details.DetailsActivity;
import exercise.noteworth.com.util.Common;
import exercise.noteworth.com.util.ImageUtils;

public class PlacesRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Result> resultList = new ArrayList<>();

    void setItems(List<Result> results) {
        this.resultList.clear();
        this.resultList.addAll(results);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return resultList.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        return new ViewHolderPlace(RecyclerRestaurantItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        ViewHolderPlace mHolder = (ViewHolderPlace) holder;
        mHolder.bindView();
        holder.itemView.setTag(this);
    }

    public class ViewHolderPlace extends RecyclerView.ViewHolder {
        private RecyclerRestaurantItemBinding binding;
        private Result result;

        private ViewHolderPlace(RecyclerRestaurantItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        private void bindView() {
            result = resultList.get(getAdapterPosition());
            binding.setHolder(this);
            binding.setPhotoReference(ImageUtils.getPhotoRef(result));
            binding.setResult(result);
        }

        @SuppressWarnings("unused")
        public void onPlaceClicked(View view) {
            Intent intentPlaceDetails = new Intent(view.getContext(), DetailsActivity.class);
            Bundle bundlePlaceDetails = new Bundle();
            bundlePlaceDetails.putString(Common.ARG_PLACE_ID, result.getPlaceId());
            intentPlaceDetails.putExtras(bundlePlaceDetails);
            view.getContext().startActivity(intentPlaceDetails);
        }
    }
}
