package com.example.locationtrackapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.LocationRequest;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.locationtrackapp.databinding.FragmentMapBinding;
import com.example.locationtrackapp.databinding.FragmentMapsBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;


public class MapFrag extends Fragment   {

    private FragmentMapsBinding binding;

    private GoogleMap mMap;
    List<LatLng> locations;

    private EmployeeDbHelper database;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database=new EmployeeDbHelper(getContext());
        getLocationsFromDb();
      //  getLocationsFromDb();  // Fetch saved locations from the database
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding=FragmentMapsBinding.inflate(inflater,container,false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap map) {
                    mMap = map;
                    plotLocationsOnMap();
                }
            });
        }

        return binding.getRoot();
    }

    private List<LatLng> getLocationsFromDb() {
      locations = new ArrayList<>();
        Cursor cursor = database.getReadableDatabase().query(
                LocationContract.LocationEntry.TABLE_NAME,
                new String[]{LocationContract.LocationEntry.COLUMN_LATITUDE, LocationContract.LocationEntry.COLUMN_LONGITUDE},
                null, null, null, null, null);

        while (cursor.moveToNext()) {
            double latitude = cursor.getDouble(cursor.getColumnIndexOrThrow(LocationContract.LocationEntry.COLUMN_LATITUDE));
            double longitude = cursor.getDouble(cursor.getColumnIndexOrThrow(LocationContract.LocationEntry.COLUMN_LONGITUDE));
            locations.add(new LatLng(latitude, longitude));
        }
        cursor.close();
        return locations;
    }
    private void plotLocationsOnMap() {
        if (mMap == null || locations.isEmpty()) {
            return;
        }

        PolylineOptions polylineOptions = new PolylineOptions()
                .clickable(false)
                .addAll(locations);

        mMap.addPolyline(polylineOptions);

        // Optionally add markers for each location
        for (LatLng location : locations) {
            mMap.addMarker(new MarkerOptions().position(location).title("Location"));
        }

        // Move the camera to the first location (optional)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(locations.get(0), 15));


    }
}