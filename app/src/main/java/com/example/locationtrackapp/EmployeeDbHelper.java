package com.example.locationtrackapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class EmployeeDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "employee.db";
    private static final int DATABASE_VERSION = 1;

    public EmployeeDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + EmployeeContract.EmployeeEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + LocationContract.LocationEntry.TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String SQL_CREATE_EMPLOYEES_TABLE = "CREATE TABLE IF NOT EXISTS " + EmployeeContract.EmployeeEntry.TABLE_NAME + " ("
                + EmployeeContract.EmployeeEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + EmployeeContract.EmployeeEntry.COLUMN_NAME_NAME + " TEXT NOT NULL, "
                + EmployeeContract.EmployeeEntry.COLUMN_NAME_ADDRESS + " TEXT, "
                + EmployeeContract.EmployeeEntry.COLUMN_NAME_DEPARTMENT + " TEXT NOT NULL);";

        sqLiteDatabase.execSQL(SQL_CREATE_EMPLOYEES_TABLE);

        String SQL_CREATE_LOCATION_TABLE = "CREATE TABLE IF NOT EXISTS " + LocationContract.LocationEntry.TABLE_NAME + " ("
                + LocationContract.LocationEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + LocationContract.LocationEntry.COLUMN_LATITUDE + " REAL NOT NULL, "
                + LocationContract.LocationEntry.COLUMN_LONGITUDE + " REAL NOT NULL, "
                + LocationContract.LocationEntry.COLUMN_TIMESTAMP + " INTEGER NOT NULL);";
        sqLiteDatabase.execSQL(SQL_CREATE_LOCATION_TABLE);
    }
}
