package com.example.locationtrackapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.locationtrackapp.databinding.FragmentEmployefragBinding;
import com.example.locationtrackapp.databinding.FragmentEmpolyeDetailsBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class EmpolyeDetails extends Fragment {

  private FragmentEmpolyeDetailsBinding binding;

    private EmployeeAdapter employeeAdapter;
    private EmployeeDbHelper dbHelper;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding=FragmentEmpolyeDetailsBinding.inflate(inflater,container,false);

        dbHelper = new EmployeeDbHelper(getContext());


        binding.rcv.setLayoutManager(new LinearLayoutManager(getContext()));
        employeeAdapter = new EmployeeAdapter(loadEmployeesFromDatabase());
        binding.rcv.setAdapter(employeeAdapter);

        return binding.getRoot();
    }
    private List<Employee> loadEmployeesFromDatabase() {
        List<Employee> employeeList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                EmployeeContract.EmployeeEntry.COLUMN_NAME_NAME,
                EmployeeContract.EmployeeEntry.COLUMN_NAME_DEPARTMENT
        };

        Cursor cursor = db.query(
                EmployeeContract.EmployeeEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow(EmployeeContract.EmployeeEntry.COLUMN_NAME_NAME));
            String department = cursor.getString(cursor.getColumnIndexOrThrow(EmployeeContract.EmployeeEntry.COLUMN_NAME_DEPARTMENT));

            employeeList.add(new Employee(name, department));
        }
        cursor.close();

        return employeeList;
    }
}