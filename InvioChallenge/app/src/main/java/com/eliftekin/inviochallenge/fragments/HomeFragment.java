package com.eliftekin.inviochallenge.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eliftekin.inviochallenge.R;
import com.eliftekin.inviochallenge.adapters.CityRvAdapter;
import com.eliftekin.inviochallenge.database.FavoritesEntity;
import com.eliftekin.inviochallenge.databinding.FragmentHomeBinding;
import com.eliftekin.inviochallenge.listeners.CityClickListener;
import com.eliftekin.inviochallenge.model.DataList;
import com.eliftekin.inviochallenge.model.LocationsList;
import com.eliftekin.inviochallenge.viewmodel.SharedViewModel;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements CityClickListener {

    private FragmentHomeBinding binding;

    SharedViewModel viewModel;
    RecyclerView recyclerView;
    CityRvAdapter rvAdapter;

    List<FavoritesEntity> favorites = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //viewbinding
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initRecyclerView();
        setRvScroll();

        //ortak vm
        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        viewModel.dataList.observe(getViewLifecycleOwner(), dataLists -> {
            //şehir listesi güncellendiğinde recyclerview güncellenir
            rvAdapter.submitList(new ArrayList<>(dataLists));
        });

        viewModel.getFavorites().observe(getViewLifecycleOwner(), this::checkIfFavorite);

        viewModel.expandedPositions.observe(getViewLifecycleOwner(), positions -> {
            rvAdapter.setExpandedPositions(positions);
            rvAdapter.notifyDataSetChanged();
        });

        //buton işlemleri
        binding.favButton.setOnClickListener(this::navigateToFavPage);
        binding.buttonCollapseAll.setOnClickListener(v -> {
            viewModel.collapseAll();
        });
    }

    //favori liste kontrolü
    private void checkIfFavorite(List<FavoritesEntity> favoritesEntities) {
        favorites = favoritesEntities;
        rvAdapter.updateFavoritesList(favoritesEntities);
    }

    private void navigateToFavPage(View v) {
        Navigation.findNavController(v).navigate(R.id.action_homeFragment_to_favoritesFragment);
    }

    //lazy loading
    private void setRvScroll() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                //son eleman kontrolü
                if (manager != null && manager.findLastVisibleItemPosition() == rvAdapter.getItemCount()-1)
                    viewModel.fetchNextPage();
            }
        });
    }

    //recyclerview kurulur
    private void initRecyclerView() {
        recyclerView = binding.cityRv;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        rvAdapter = new CityRvAdapter(favorites, this); //boş liste
        recyclerView.setAdapter(rvAdapter);
    }

    @Override
    public void onCityMapClick(DataList data) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("cityDetail", data);
        Navigation.findNavController(binding.getRoot()).navigate(R.id.action_homeFragment_to_cityMapFragment, bundle);

    }

    @Override
    public void onDetailsClick(LocationsList locationsList) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("locationDetail", locationsList);
        Navigation.findNavController(binding.getRoot()).navigate(R.id.action_homeFragment_to_detailsFragment, bundle);
    }

    @Override
    public void onIconClickListener(int position, LocationsList locationsList) {
        boolean isFav = isFavorited(locationsList.getName());

        if(isFav)
            viewModel.removeFavorite(locationsList.getName()); //favorilerdeyse silinir
        else{
            FavoritesEntity favorites = new FavoritesEntity(
                    locationsList.getName(),
                    locationsList.getDescription(),
                    locationsList.getImgUrl(),
                    locationsList.getCoordinates().getLat(),
                    locationsList.getCoordinates().getLng()
            );
            viewModel.insertFavorite(favorites); //değilse favorilere eklenir
        }
        rvAdapter.notifyItemChanged(position);
    }

    @Override
    public void onExpandClickListener(int position) {
        viewModel.togglePosition(position);
    }

    //favorilerde mi değil mi kontrolü
    public boolean isFavorited(String cityName) {
        if (favorites != null) {
            for (FavoritesEntity f : favorites) {
                if (f.getName().equals(cityName)) return true;
            }
        }
        return false;
    }
}