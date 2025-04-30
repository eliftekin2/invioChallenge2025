package com.eliftekin.inviochallenge.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eliftekin.inviochallenge.R;
import com.eliftekin.inviochallenge.adapters.FavoritesRvAdapter;
import com.eliftekin.inviochallenge.database.FavoritesEntity;
import com.eliftekin.inviochallenge.databinding.FragmentFavoritesBinding;
import com.eliftekin.inviochallenge.listeners.FavoritesListener;
import com.eliftekin.inviochallenge.viewmodel.FavoritesViewModel;

import java.util.ArrayList;
import java.util.List;

public class FavoritesFragment extends Fragment implements FavoritesListener {
    FragmentFavoritesBinding binding;

    FavoritesViewModel viewModel;

    RecyclerView recyclerView;
    FavoritesRvAdapter rvAdapter;
    List<FavoritesEntity> favorites = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initRecyclerView();

        viewModel = new ViewModelProvider(requireActivity()).get(FavoritesViewModel.class);
        viewModel.getFavorites().observe(getViewLifecycleOwner(), favoritesEntities -> {
            favorites = favoritesEntities;
            rvAdapter.updateRv(favorites);

            checkIfEmpty(favorites);
        });

        binding.buttonBack.setOnClickListener(this::navigateToBack);

    }

    private void navigateToBack(View v) {
        NavController navController = Navigation.findNavController(v);
        navController.popBackStack();
    }

    private void checkIfEmpty(List<FavoritesEntity> favorites) {
        if (favorites.isEmpty())
            binding.warningText.setVisibility(View.VISIBLE);
        else
            binding.warningText.setVisibility(View.GONE);
    }

    private void initRecyclerView() {
        recyclerView = binding.favoritesRv;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        rvAdapter = new FavoritesRvAdapter(new ArrayList<>(), this);
        recyclerView.setAdapter(rvAdapter);
    }

    @Override
    public void onItemClick(FavoritesEntity favorites) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("locationDetail", favorites.toLocationsList());
        NavController navController = Navigation.findNavController(binding.getRoot());
        navController.navigate(R.id.action_favoritesFragment_to_detailsFragment, bundle);
    }

    @Override
    public void onFavRemoveClick(FavoritesEntity favorites) {
        viewModel.removeFavorite(favorites.getName());

    }
}