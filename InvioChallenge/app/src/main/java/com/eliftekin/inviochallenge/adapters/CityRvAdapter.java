package com.eliftekin.inviochallenge.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.eliftekin.inviochallenge.R;
import com.eliftekin.inviochallenge.database.FavoritesEntity;
import com.eliftekin.inviochallenge.databinding.ListItemCityBinding;
import com.eliftekin.inviochallenge.listeners.CityClickListener;
import com.eliftekin.inviochallenge.model.DataList;
import com.eliftekin.inviochallenge.utils.CityDiffUtil;

import java.util.List;
import java.util.Set;

public class CityRvAdapter extends ListAdapter<DataList, CityRvAdapter.ViewHolder> {

    private Set<Integer> expandedPositions;

    private List<FavoritesEntity> favoritesList; //inner rv adaptöre iletilecek
    private final CityClickListener listener; //inner rv adaptörüne iletilecek

    public CityRvAdapter(List<FavoritesEntity> favoritesList, CityClickListener listener) {
        super(new CityDiffUtil());

        this.favoritesList = favoritesList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ListItemCityBinding binding = ListItemCityBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DataList data = getItem(position);
        holder.binding.cityName.setText(data.getCity());

        setNestedRv(data, holder);
        checkIcon(data, holder); //+ ikonu kontrolü
        checkExpandState(holder, position); //expand collapse durumu kontrolü

        holder.binding.expand.setOnClickListener(v -> {
            listener.onExpandClickListener(position);
        });
        holder.binding.cityMap.setOnClickListener(v -> {
            listener.onCityMapClick(data);
        });

    }

    //favoriler listesinin güncellenmesi
    public void updateFavoritesList(List<FavoritesEntity> favoritesList) {
        this.favoritesList = favoritesList;
        notifyDataSetChanged();
    }

    //expand edilmiş kart pozisyonlarını tutar
    public void setExpandedPositions(Set<Integer> positions) {
        this.expandedPositions = positions;
    }

    private void checkExpandState(ViewHolder holder, int position) {
        if (expandedPositions.contains(position)) {
            holder.binding.locationRv.setVisibility(View.VISIBLE);
            holder.binding.expand.setImageResource(R.drawable.icon_collapse);
        } else {
            holder.binding.locationRv.setVisibility(View.GONE);
            holder.binding.expand.setImageResource(R.drawable.icon_expand);
        }
    }

    //inner rv kurulur
    private void setNestedRv(DataList data, ViewHolder holder) {
        InnerRvAdapter innerRvAdapter = new InnerRvAdapter(data.getLocationsLists(), favoritesList, listener);
        holder.binding.locationRv.setAdapter(innerRvAdapter);
    }

    private void checkIcon(DataList data, ViewHolder holder) {
        if (data.getLocationsLists().isEmpty())
            holder.binding.expand.setVisibility(View.GONE);
        else
            holder.binding.expand.setVisibility(View.VISIBLE);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        ListItemCityBinding binding;

        public ViewHolder(ListItemCityBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}

