package com.eliftekin.inviochallenge.fragments;

import static com.eliftekin.inviochallenge.utils.location.LocationRouteUtil.createRoute;
import static com.eliftekin.inviochallenge.utils.location.LocationSettingUtil.enableGps;
import static com.eliftekin.inviochallenge.utils.location.LocationUtil.enableUserLocation;
import static com.eliftekin.inviochallenge.utils.location.LocationUtil.getUserLocation;
import static com.eliftekin.inviochallenge.utils.location.LocationUtil.isGpsEnabled;
import static com.eliftekin.inviochallenge.utils.location.LocationUtil.zoomToUserLocation;
import static com.eliftekin.inviochallenge.utils.location.MapUtil.bitmapDescriptor;
import static com.eliftekin.inviochallenge.utils.location.MapUtil.setMapFragment;
import static com.eliftekin.inviochallenge.utils.location.PermissionUtil.checkLocationPermission;
import static com.eliftekin.inviochallenge.utils.location.PermissionUtil.showAlert;

import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.eliftekin.inviochallenge.R;
import com.eliftekin.inviochallenge.databinding.FragmentLocationMapBinding;
import com.eliftekin.inviochallenge.model.LocationsList;
import com.eliftekin.inviochallenge.utils.location.LocationUtil;
import com.eliftekin.inviochallenge.viewmodel.LocationMapViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class LocationMapFragment extends Fragment implements OnMapReadyCallback {
    FragmentLocationMapBinding binding;
    LocationMapViewModel viewModel;

    LocationsList locationsList;

    double latitude;
    double longitude;

    GoogleMap mMap;

    private ActivityResultLauncher<String> requestPermissionLauncher;
    private FusedLocationProviderClient locationClient;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLocationMapBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getArgs();
        initViewModel();

        initLocationPermission();
        setMapFragment(this, this, R.id.map_fragment);

        binding.buttonBack.setOnClickListener(v -> {
            navigateBack(v);
        });

        binding.getDirections.setOnClickListener(v -> {
            getDirections();
        });

        binding.userLocation.setOnClickListener(v -> {
            zoomToUserLocation(
                    this,
                    requireContext(),
                    locationClient,
                    mMap,
                    () -> showAlert(this, requestPermissionLauncher),
                    () -> enableGps(requireContext())
            );
        });
    }

    private void initLocationPermission() {
        locationClient = LocationServices.getFusedLocationProviderClient(requireContext());

        requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        zoomToUserLocation(
                                this,
                                requireContext(),
                                locationClient,
                                mMap,
                                () -> showAlert(this, requestPermissionLauncher),
                                () -> enableGps(requireContext())
                        );
                    } else {
                        showAlert(this, requestPermissionLauncher);
                    }
                }
        );

        if (checkLocationPermission(requireContext())) {
            if (isGpsEnabled(requireContext())) {
                enableUserLocation(requireContext(), mMap);
            } else {
                enableGps(requireContext()); // GPS kapalıysa kullanıcıyı yönlendir
            }
        } else {
            showAlert(this, requestPermissionLauncher); // izin yoksa alert göster
        }
    }

    private void setDetails(LocationsList locationsList) {
        longitude = locationsList.getCoordinates().getLng();
        latitude = locationsList.getCoordinates().getLat();

        binding.locationName.setText(locationsList.getName());
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(requireActivity()).get(LocationMapViewModel.class);
        viewModel.setLocation(locationsList);
        viewModel.location.observe(getViewLifecycleOwner(), this::setDetails);
    }

    //gelen veriyi alır
    private void getArgs() {
        LocationMapFragmentArgs args = LocationMapFragmentArgs.fromBundle(getArguments());
        locationsList = args.getLocationDetail();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        LatLng location = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions()
                .position(location)
                .icon(bitmapDescriptor(requireContext(), R.drawable.icon_location_star)));

        mMap.moveCamera(com.google.android.gms.maps.CameraUpdateFactory.newLatLngZoom(location, 15));

        enableUserLocation(requireContext(), mMap);

    }

    //bir önceki sayfaya gider
    private void navigateBack(View v) {
        NavController navController = Navigation.findNavController(v);
        navController.popBackStack();
    }

    //seçilen konuma rota oluşturur
    private void getDirections() {
        getUserLocation(requireContext(), locationClient, new LocationUtil.LocationCallback() {
            @Override
            public void onLocationReceived(double userLat, double userLng) {
                createRoute(requireContext(), userLat, userLng, latitude, longitude);
            }
        });
    }

}