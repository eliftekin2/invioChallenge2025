package com.eliftekin.inviochallenge.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eliftekin.inviochallenge.R;
import com.eliftekin.inviochallenge.database.FavoritesEntity;
import com.eliftekin.inviochallenge.databinding.ListItemLocationsBinding;
import com.eliftekin.inviochallenge.listeners.CityClickListener;
import com.eliftekin.inviochallenge.model.LocationsList;

import java.util.List;


public class InnerRvAdapter extends RecyclerView.Adapter<InnerRvAdapter.ViewHolder> {

    private List<LocationsList> locationsList;
    private List<FavoritesEntity> favoritesList;
    private CityClickListener listener;

    public InnerRvAdapter(List<LocationsList> locationsList, List<FavoritesEntity> favoritesList, CityClickListener listener) {
        this.locationsList = locationsList;
        this.favoritesList = favoritesList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ListItemLocationsBinding binding = ListItemLocationsBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LocationsList list = locationsList.get(position);
        holder.binding.locationName.setText(list.getName());

        checkFavState(holder, list);

        //tıklama işlemleri
        holder.itemView.setOnClickListener(view -> {
            listener.onDetailsClick(list);
        });
        holder.binding.favButton.setOnClickListener(view -> {
            listener.onIconClickListener(position, list);
        });

    }

    private void checkFavState(ViewHolder holder, LocationsList list) {
        boolean isFavorited = false;

        for(FavoritesEntity favorites : favoritesList){
            if (favorites.getName().equals(list.getName())){
                isFavorited = true;
                break;
            }
        }
        setFavIcon(holder, isFavorited);
    }

    private void setFavIcon(ViewHolder holder, boolean isFavorited) {
        if (isFavorited)
            holder.binding.favButton.setImageResource(R.drawable.icon_fav);
        else
            holder.binding.favButton.setImageResource(R.drawable.icon_fav_border);
    }

    @Override
    public int getItemCount() {
        return locationsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ListItemLocationsBinding binding;

        public ViewHolder(ListItemLocationsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
