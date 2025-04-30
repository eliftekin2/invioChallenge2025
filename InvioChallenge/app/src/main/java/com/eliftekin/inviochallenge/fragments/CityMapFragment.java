package com.eliftekin.inviochallenge.fragments;

import static com.eliftekin.inviochallenge.utils.location.LocationSettingUtil.enableGps;
import static com.eliftekin.inviochallenge.utils.location.LocationUtil.enableUserLocation;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.eliftekin.inviochallenge.R;
import com.eliftekin.inviochallenge.adapters.CarouselRvAdapter;
import com.eliftekin.inviochallenge.databinding.FragmentCityMapBinding;
import com.eliftekin.inviochallenge.listeners.CarouselItemListener;
import com.eliftekin.inviochallenge.model.DataList;
import com.eliftekin.inviochallenge.model.LocationsList;
import com.eliftekin.inviochallenge.viewmodel.CityMapViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class CityMapFragment extends Fragment implements OnMapReadyCallback {
    private FragmentCityMapBinding binding;

    CityMapViewModel viewModel;
    RecyclerView recyclerView;
    CarouselRvAdapter rvAdapter;

    DataList cityList;
    List<LocationsList> locationsList;

    double latitude;
    double longitude;

    //kullanıcının konumu için
    double userLat;
    double userLng;

    GoogleMap mMap;

    private FusedLocationProviderClient locationClient;
    private ActivityResultLauncher<String> requestPermissionLauncher;
    
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCityMapBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getArgs();
        initViewModel();

        initLocationPermission();
        setMapFragment(this, this, R.id.map_fragment);

        binding.buttonBack.setOnClickListener(this::navigateBack);
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

    //gelen veriyi alır
    private void getArgs() {
        CityMapFragmentArgs args = CityMapFragmentArgs.fromBundle(getArguments());
        cityList = args.getCityDetail();
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(requireActivity()).get(CityMapViewModel.class);
        viewModel.setCity(cityList);

        viewModel.city.observe(getViewLifecycleOwner(), this::setDetails);
        viewModel.location.observe(getViewLifecycleOwner(), locationsLists -> {
            locationsList = locationsLists;
            initRecyclerView(locationsList);
        });
    }

    private void setDetails(DataList dataList) {
        binding.cityName.setText(dataList.getCity());
    }

    private void initRecyclerView(List<LocationsList> locationsList) {
        recyclerView = binding.carouselRv;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        rvAdapter = new CarouselRvAdapter(locationsList, new CarouselItemListener() {
            @Override
            public void onLocationSelected(LocationsList location) {
                setMarkers(location);
            }

            @Override
            public void onDetailClicked(LocationsList location) {
                navigateToDetails(location);
            }
        });
        recyclerView.setAdapter(rvAdapter);

    }

    private void setMarkers(LocationsList location) {
        //kullanıcının seçtiği konum
        LatLng latLng = new LatLng(location.getCoordinates().getLat(), location.getCoordinates().getLng());

        if (mMap != null) {
            //seçilen konuma odaklanır
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));

            mMap.clear();
            for (LocationsList loc : locationsList) {
                //şehirdeki tüm konumlara marker eklenir
                LatLng position = new LatLng(loc.getCoordinates().getLat(), loc.getCoordinates().getLng());

                MarkerOptions markerOptions = new MarkerOptions()
                        .position(position)
                        .title(loc.getName());

                if (loc.equals(location)) {
                    //seçilen konumun marker ikonunu değiştirir
                    markerOptions.icon(bitmapDescriptor(requireContext(), R.drawable.icon_location_star));
                }
                mMap.addMarker(markerOptions);
            }
        }
    }

    //tıklanılan karttaki konumun detay sayfasına yönlendirir
    private void navigateToDetails(LocationsList location) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("locationDetail", location);

        Navigation.findNavController(requireView())
                .navigate(R.id.action_cityMapFragment_to_detailsFragment, bundle);
    }

    //bir önceki sayfaya gider
    private void navigateBack(View v) {
        NavController navController = Navigation.findNavController(v);
        navController.popBackStack();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        enableUserLocation(requireContext(), mMap);

        for (int i = 0; i < locationsList.size(); i++) {
            LocationsList locations = locationsList.get(i);

            latitude = locations.getCoordinates().getLat();
            longitude = locations.getCoordinates().getLng();

            LatLng latLng = new LatLng(latitude, longitude);

            MarkerOptions markerOptions = new MarkerOptions()
                    .position(latLng)
                    .title(locations.getName());

            //ilk konumun marker rengini değiştirir
            if (i == 0) {
                mMap.addMarker(markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
            } else {
                mMap.addMarker(markerOptions);
            }
        }
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


}



