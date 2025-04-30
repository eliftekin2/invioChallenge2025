package com.eliftekin.inviochallenge.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.eliftekin.inviochallenge.R;
import com.eliftekin.inviochallenge.viewmodel.SharedViewModel;

public class SplashFragment extends Fragment {

    SharedViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_splash, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //ortak vm
        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        //ilk sayfayı çeker
        viewModel.fetchFirstPage();

        viewModel.dataList.observe(getViewLifecycleOwner(), data -> {
            if (data != null && !data.isEmpty()) {
                navigateToHomePage();
            }
        });

        //hata durumunda ekranda kalır
        viewModel.errorMessage.observe(getViewLifecycleOwner(), error -> {
            Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
        });
    }

    private void navigateToHomePage() {
        new Handler().postDelayed(() -> {
            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_splashFragment_to_homeFragment);
        }, 3000);
    }
}