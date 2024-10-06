package com.example.locationtrackapp;

import android.provider.BaseColumns;

public class LocationContract {

    private LocationContract() {}

    public static final class LocationEntry implements BaseColumns {
        public static final String TABLE_NAME = "location";
        public static final String COLUMN_LATITUDE = "latitude";
        public static final String COLUMN_LONGITUDE = "longitude";
        public static final String COLUMN_TIMESTAMP = "timestamp";
    }
}
