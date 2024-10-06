package com.example.locationtrackapp;

import android.content.ContentValues;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;

import android.os.Build;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.locationtrackapp.databinding.FragmentMappingBinding;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.Priority;




import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.Task;

import android.os.Looper;
import android.util.Log;


public class MappingFrag extends Fragment {

    private FragmentMappingBinding binding;
   public MainActivity activity;
    Fragment mapFrag;

    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private boolean isTracking = false;
    private SQLiteDatabase database;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentMappingBinding.inflate(inflater, container, false);

        EmployeeDbHelper dbHelper = new EmployeeDbHelper(getContext());
        database = dbHelper.getWritableDatabase();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
             locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY)
                     .setWaitForAccurateLocation(false)// Every 60 seconds
                    .setMinUpdateIntervalMillis(30000)  // Minimum interval (30 seconds)
                    .setMaxUpdateDelayMillis(120000)    // Maximum delay (2 minutes)
                    .build();
        }else{
            locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(60000);        // Every 60 seconds
            locationRequest.setFastestInterval(30000);
        }


        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        SettingsClient client = LocationServices.getSettingsClient(getContext());
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        task.addOnSuccessListener(locationSettingsResponse -> {
            // All location settings are satisfied
            startLocationUpdates();
        }).addOnFailureListener(e -> {
            if (e instanceof ResolvableApiException) {

                try {
                    ResolvableApiException resolvable = (ResolvableApiException) e;
                    resolvable.startResolutionForResult(getActivity(), 101);
                } catch (IntentSender.SendIntentException sendEx) {

                }
            }
        });

        binding.btnShowTrackingRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                         activity = (MainActivity) getActivity();
         mapFrag= new MapsFragment();
        activity.replaceFragment(mapFrag);
            }
        });


        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    location.getLatitude();
                    location.getLatitude();
                    Log.d("latitude", "onLocationResult: "+location.getLatitude());
                    saveLocationToDb(location);
                }
            }
        };


        binding.btnStartTracking.setOnClickListener(v -> {
            if (!isTracking) {
                if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 100);
                } else {
                    startLocationUpdates();
                }

                binding.btnStartTracking.setText("Stop Tracking");
            } else {
                stopLocationUpdates();
                binding.btnStartTracking.setText("Start Tracking");
            }
            isTracking = !isTracking;
        });


        return binding.getRoot();
    }

    private void startLocationUpdates() {
//        if (ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
//            return;
//        }

        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;

        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
                .addOnSuccessListener(aVoid -> {
                    Log.d("Location", "Location updates started");
                })
                .addOnFailureListener(e -> {
                    Log.e("Location", "Failed to start location updates: ");
                });
    }

    private void stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    private void saveLocationToDb(Location location) {
        ContentValues values = new ContentValues();
        values.put(LocationContract.LocationEntry.COLUMN_LATITUDE, location.getLatitude());
        values.put(LocationContract.LocationEntry.COLUMN_LONGITUDE, location.getLongitude());
        values.put(LocationContract.LocationEntry.COLUMN_TIMESTAMP, System.currentTimeMillis());

        database.insert(LocationContract.LocationEntry.TABLE_NAME, null, values);
    }


}