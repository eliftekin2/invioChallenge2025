package com.eliftekin.inviochallenge.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.eliftekin.inviochallenge.R;
import com.eliftekin.inviochallenge.databinding.ListItemCarouselBinding;
import com.eliftekin.inviochallenge.listeners.CarouselItemListener;
import com.eliftekin.inviochallenge.model.LocationsList;

import java.util.List;

public class CarouselRvAdapter extends RecyclerView.Adapter<CarouselRvAdapter.ViewHolder>{

    private List<LocationsList> locationsLists;
    private CarouselItemListener listener;

    public CarouselRvAdapter(List<LocationsList> locationsLists, CarouselItemListener listener) {
        this.locationsLists = locationsLists;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ListItemCarouselBinding binding = ListItemCarouselBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LocationsList locationsList = locationsLists.get(position);

        holder.binding.locationName.setText(locationsList.getName());

        Glide.with(holder.itemView.getContext())
                .load(locationsList.getImgUrl())
                .placeholder(R.drawable.placeholder)
                .into(holder.binding.locationImage);

        holder.itemView.setOnClickListener(v -> {
            listener.onLocationSelected(locationsList);
        });

        holder.binding.navigateToDetails.setOnClickListener(v -> {
            listener.onDetailClicked(locationsList);
        });
    }

    @Override
    public int getItemCount() {
        return locationsLists.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ListItemCarouselBinding binding;

        public ViewHolder(ListItemCarouselBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
