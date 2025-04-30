package com.eliftekin.inviochallenge.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eliftekin.inviochallenge.database.FavoritesEntity;
import com.eliftekin.inviochallenge.databinding.ListItemFavoritesBinding;
import com.eliftekin.inviochallenge.listeners.FavoritesListener;

import java.util.List;

public class FavoritesRvAdapter extends RecyclerView.Adapter<FavoritesRvAdapter.ViewHolder> {

    private List<FavoritesEntity> favorites;
    private FavoritesListener listener;

    public FavoritesRvAdapter(List<FavoritesEntity> favorites, FavoritesListener listener) {
        this.favorites = favorites;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ListItemFavoritesBinding binding = ListItemFavoritesBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FavoritesEntity favorite = favorites.get(position);
        holder.binding.locationName.setText(favorite.getName());

        holder.itemView.setOnClickListener(v -> {
            listener.onItemClick(favorite);
        });

        holder.binding.buttonFav.setOnClickListener(v -> {
            listener.onFavRemoveClick(favorite);
        });
    }

    @Override
    public int getItemCount() {
        return favorites.size();
    }

    public void updateRv(List<FavoritesEntity> newList) {
        favorites.clear();
        favorites.addAll(newList);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ListItemFavoritesBinding binding;

        public ViewHolder(ListItemFavoritesBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
