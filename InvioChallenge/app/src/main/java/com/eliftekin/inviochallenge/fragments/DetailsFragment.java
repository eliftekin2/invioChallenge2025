package com.eliftekin.inviochallenge.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.eliftekin.inviochallenge.R;
import com.eliftekin.inviochallenge.database.FavoritesEntity;
import com.eliftekin.inviochallenge.databinding.FragmentDetailsBinding;
import com.eliftekin.inviochallenge.model.LocationsList;
import com.eliftekin.inviochallenge.viewmodel.SharedViewModel;

import java.util.ArrayList;
import java.util.List;

public class DetailsFragment extends Fragment {
    FragmentDetailsBinding binding;
    LocationsList locationsList;

    SharedViewModel viewModel;

    List<FavoritesEntity> favorites = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //viewbinding
        binding = FragmentDetailsBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        viewModel.getFavorites().observe(getViewLifecycleOwner(), favoritesEntities -> {
            favorites = favoritesEntities;
            checkButtonState();
        });

        setDetails();

        //buton işlemleri
        binding.buttonFav.setOnClickListener(v -> {
            addOrRemoveFavorite();
        });
        binding.buttonBack.setOnClickListener(this::navigateBack);
        binding.locationMap.setOnClickListener(this::navigateToMap);

    }

    private void navigateBack(View v) {
        NavController navController = Navigation.findNavController(v);
        navController.popBackStack();
    }

    private void addOrRemoveFavorite() {
        String location_name = binding.locationName.getText().toString();

        if (checkIfFavorite()){
            viewModel.removeFavorite(location_name);
            checkButtonState();
        }
        else {
            FavoritesEntity favorites = new FavoritesEntity(
                    locationsList.getName(),
                    locationsList.getDescription(),
                    locationsList.getImgUrl(),
                    locationsList.getCoordinates().getLat(),
                    locationsList.getCoordinates().getLng());
            viewModel.insertFavorite(favorites);
            checkButtonState();
        }
    }

    private void navigateToMap(View v) {
        NavController navController = Navigation.findNavController(v);

        DetailsFragmentDirections.ActionDetailsFragmentToLocationMapFragment action =
                DetailsFragmentDirections.actionDetailsFragmentToLocationMapFragment(locationsList);

        navController.navigate(action);
    }

    private boolean checkIfFavorite() {
        boolean isFav = false;

        for(FavoritesEntity f : favorites){
            if (f.getName().equals(binding.locationName.getText().toString())) {
                isFav = true;
                break;
            }
        }
        return isFav;
    }

    private void checkButtonState() {
        if(checkIfFavorite())
            binding.buttonFav.setImageResource(R.drawable.favorites);
        else
            binding.buttonFav.setImageResource(R.drawable.favorites_border);
    }

    private void setDetails() {
        DetailsFragmentArgs args = DetailsFragmentArgs.fromBundle(getArguments());
        locationsList = args.getLocationDetail();

        binding.locationName.setText(locationsList.getName());
        binding.locationDescription.setText(locationsList.getDescription());

        Glide.with(this)
                .load(locationsList.getImgUrl())
                .placeholder(R.drawable.placeholder)
                .into(binding.locationImg);

    }
}