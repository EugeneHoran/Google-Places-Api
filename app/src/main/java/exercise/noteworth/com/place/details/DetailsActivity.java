package exercise.noteworth.com.place.details;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import javax.annotation.Nullable;

import exercise.noteworth.com.R;
import exercise.noteworth.com.databinding.ActivityPlaceDetailsBinding;
import exercise.noteworth.com.model.Details;
import exercise.noteworth.com.util.Common;

public class DetailsActivity extends AppCompatActivity {

    private ActivityPlaceDetailsBinding binding;
    private DetailsRecyclerAdapter detailsAdapter;
    private String placeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_place_details);
        binding.fab.hide();
        setSupportActionBar(binding.toolbar);
        setTitle(null);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        if (getIntent().getExtras() != null) {
            placeId = getIntent().getExtras().getString(Common.ARG_PLACE_ID);
        }
        detailsAdapter = new DetailsRecyclerAdapter();
        binding.recyclerDetails.setAdapter(detailsAdapter);
        DetailsViewModel.Factory factory = new DetailsViewModel.Factory(placeId);
        final DetailsViewModel viewModel = ViewModelProviders
                .of(this, factory)
                .get(DetailsViewModel.class);
        binding.setModel(viewModel);
        observeViewModel(viewModel);
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Clicked", Toast.LENGTH_SHORT).show();
                binding.fab.hide();
            }
        });
    }

    /**
     * Observe Live data @ {@link DetailsViewModel}
     *
     * @param viewModel DetailsViewModel
     */
    private void observeViewModel(final DetailsViewModel viewModel) {
        viewModel.getObservableDetails().observe(this, new Observer<Details>() {
            @Override
            public void onChanged(@Nullable Details details) {
                if (details != null) {
                    initPhotosAdapter(DetailsHelper.photoList(details.getResult().getPhotos()));
                    initPlaceAdapter(DetailsHelper.detailsList(details));
                    setTitle(details.getResult().getName());
                    // Anchoring the Fab prevents animation.
                    // Manually show Fab animation.
                    binding.fab.show();
                    binding.fab.setAlpha(0f);
                    binding.fab.setScaleY(0f);
                    binding.fab.setScaleX(0f);
                    binding.fab.animate()
                            .scaleX(1f)
                            .scaleY(1f)
                            .alpha(1f)
                            .setDuration(200)
                            .start();
                }
            }
        });
    }

    /**
     * Initiate Details Adapter
     *
     * @param detailsList detailList
     */
    public void initPlaceAdapter(List<Object> detailsList) {
        detailsAdapter.setItems(detailsList);
    }

    /**
     * Initiate Photo Pager Adapter and Indicator
     *
     * @param photoList photoList
     */
    public void initPhotosAdapter(List<String> photoList) {
        binding.pager.setAdapter(new DetailsPagerAdapter(this, photoList));
        binding.indicator.setupWithViewPager(binding.pager);
    }
}
