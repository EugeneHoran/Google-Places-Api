package exercise.noteworth.com.place.details;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import exercise.noteworth.com.databinding.RecyclerDividerBinding;
import exercise.noteworth.com.databinding.RecyclerHeaderBinding;
import exercise.noteworth.com.databinding.RecyclerImageTextBinding;
import exercise.noteworth.com.databinding.RecyclerReviewBinding;
import exercise.noteworth.com.databinding.RecyclerTextSpaceLeftBinding;
import exercise.noteworth.com.model.DetailsReview;
import exercise.noteworth.com.model.ModelTextHeader;
import exercise.noteworth.com.model.ModelTextImage;
import exercise.noteworth.com.model.ModelTextSpaceLeft;
import exercise.noteworth.com.model.ModelViewDivider;

public class DetailsRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int HOLDER_ERROR = 0;
    private static final int HOLDER_HEADER = 1;
    private static final int HOLDER_DIVIDER = 2;
    private static final int HOLDER_TEXT_IMAGE = 3;
    private static final int HOLDER_TEXT_SPACE_LEFT = 4;
    private static final int HOLDER_REVIEW = 5;
    private List<Object> objectList = new ArrayList<>();

    public void setItems(List<Object> objectList) {
        this.objectList.clear();
        this.objectList.addAll(objectList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (objectList.get(position) instanceof ModelTextHeader) {
            return HOLDER_HEADER;
        } else if (objectList.get(position) instanceof ModelViewDivider) {
            return HOLDER_DIVIDER;
        } else if (objectList.get(position) instanceof ModelTextImage) {
            return HOLDER_TEXT_IMAGE;
        } else if (objectList.get(position) instanceof ModelTextSpaceLeft) {
            return HOLDER_TEXT_SPACE_LEFT;
        } else if (objectList.get(position) instanceof DetailsReview) {
            return HOLDER_REVIEW;
        } else {
            return HOLDER_ERROR;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case HOLDER_ERROR:
                return null;
            case HOLDER_HEADER:
                return new ViewHolderHeader(RecyclerHeaderBinding.inflate(layoutInflater, parent, false));
            case HOLDER_DIVIDER:
                return new ViewHolderDivider(RecyclerDividerBinding.inflate(layoutInflater, parent, false));
            case HOLDER_TEXT_IMAGE:
                return new ViewHolderTextImage(RecyclerImageTextBinding.inflate(layoutInflater, parent, false));
            case HOLDER_TEXT_SPACE_LEFT:
                return new ViewHolderTextSpaceLeft(RecyclerTextSpaceLeftBinding.inflate(layoutInflater, parent, false));
            case HOLDER_REVIEW:
                return new ViewHolderReview(RecyclerReviewBinding.inflate(layoutInflater, parent, false));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolderHeader) {
            ViewHolderHeader mHolder = (ViewHolderHeader) holder;
            mHolder.bindItem();
        } else if (holder instanceof ViewHolderDivider) {
            ViewHolderDivider mHolder = (ViewHolderDivider) holder;
            mHolder.bindItem();
        } else if (holder instanceof ViewHolderTextImage) {
            ViewHolderTextImage mHolder = (ViewHolderTextImage) holder;
            mHolder.bindItem();
        } else if (holder instanceof ViewHolderTextSpaceLeft) {
            ViewHolderTextSpaceLeft mHolder = (ViewHolderTextSpaceLeft) holder;
            mHolder.bindItem();
        } else if (holder instanceof ViewHolderReview) {
            ViewHolderReview mHolder = (ViewHolderReview) holder;
            mHolder.bindItem();
        }
        holder.itemView.setTag(this);
    }

    @Override
    public int getItemCount() {
        return objectList.size();
    }

    /**
     * View Holders
     */
    private class ViewHolderHeader extends RecyclerView.ViewHolder {
        private RecyclerHeaderBinding binding;

        ViewHolderHeader(RecyclerHeaderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bindItem() {
            ModelTextHeader object = (ModelTextHeader) objectList.get(getAdapterPosition());
            binding.setObject(object);
        }
    }

    private class ViewHolderDivider extends RecyclerView.ViewHolder {
        private RecyclerDividerBinding binding;

        ViewHolderDivider(RecyclerDividerBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bindItem() {
            ModelViewDivider object = (ModelViewDivider) objectList.get(getAdapterPosition());
            binding.setObject(object);
            binding.executePendingBindings();
        }
    }


    private class ViewHolderTextImage extends RecyclerView.ViewHolder {
        private RecyclerImageTextBinding binding;

        ViewHolderTextImage(RecyclerImageTextBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bindItem() {
            ModelTextImage object = (ModelTextImage) objectList.get(getAdapterPosition());
            binding.setObject(object);
            binding.executePendingBindings();
        }
    }


    private class ViewHolderTextSpaceLeft extends RecyclerView.ViewHolder {
        private RecyclerTextSpaceLeftBinding binding;

        ViewHolderTextSpaceLeft(RecyclerTextSpaceLeftBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bindItem() {
            ModelTextSpaceLeft object = (ModelTextSpaceLeft) objectList.get(getAdapterPosition());
            binding.setObject(object);
            binding.executePendingBindings();
        }
    }

    private class ViewHolderReview extends RecyclerView.ViewHolder {
        private RecyclerReviewBinding binding;

        ViewHolderReview(RecyclerReviewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bindItem() {
            DetailsReview object = (DetailsReview) objectList.get(getAdapterPosition());
            binding.setObject(object);
            binding.executePendingBindings();
        }
    }
}
