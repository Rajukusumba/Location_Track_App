package com.example.locationtrackapp;

import android.provider.BaseColumns;

public final class EmployeeContract {

    private EmployeeContract() {
    }

    public static class EmployeeEntry implements BaseColumns {
        public static final String TABLE_NAME = "emp";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_ADDRESS="address";

        public static final String COLUMN_NAME_DEPARTMENT = "Department";
    }
}
