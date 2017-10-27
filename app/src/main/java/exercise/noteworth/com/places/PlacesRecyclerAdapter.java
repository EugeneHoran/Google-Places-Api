package exercise.noteworth.com.places;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import exercise.noteworth.com.databinding.RecyclerRestaurantItemBinding;
import exercise.noteworth.com.model.Result;
import exercise.noteworth.com.util.ImageUtils;

public class PlacesRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Result> resultList = new ArrayList<>();
    private PlaceRecyclerInterface listener;

    interface PlaceRecyclerInterface {
        void onPlaceClicked(String placeId, View view);
    }

    public void setListener(PlaceRecyclerInterface listener) {
        this.listener = listener;
    }

    public void setItems(List<Result> results) {
        this.resultList.clear();
        this.resultList.addAll(results);
        notifyDataSetChanged();
    }

    public void addItems(List<Result> results) {
        this.resultList.addAll(results);
        notifyItemRangeInserted(resultList.size(), results.size());
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
            if (listener != null) {
                listener.onPlaceClicked(result.getPlaceId(), binding.imageView);
            }
        }
    }
}
