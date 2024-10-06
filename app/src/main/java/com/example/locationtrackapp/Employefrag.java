package com.example.locationtrackapp;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.locationtrackapp.databinding.FragmentEmployefragBinding;


public class Employefrag extends Fragment {

 private FragmentEmployefragBinding binding;
    private EmployeeDbHelper dbHelper;
    String department;
    SQLiteDatabase db;
    Fragment frag;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentEmployefragBinding.inflate(inflater,container,false);

        dbHelper = new EmployeeDbHelper(getContext());
        db = dbHelper.getWritableDatabase();
        setupSpinner();
        frag=new EmpolyeDetails();
        binding.btnDiscard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDetails(frag);

            }
        });


        binding.btnSave.setOnClickListener(v -> saveEmployee());
        return binding.getRoot();
    }

    private void showDetails(Fragment frg) {

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        ((FragmentTransaction) fragmentTransaction).replace(R.id.fragment_container, frg);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void saveEmployee() {
        String name = binding.etName.getText().toString();
        String address = binding.editTextAddress.toString();


      //   department = binding.spinnerDepartment.toString();


        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(address) || TextUtils.isEmpty(department)) {

            if(address.isEmpty()){
                binding.textInputLayoutAddress.setError("Field cannot be empty");
            }else{
                binding.textInputLayoutAddress.setError(null);
            }



             if(name.isEmpty()){
                binding.textInputLayoutName.setError("Field cannot be empty");
            } else{
                 binding.textInputLayoutName.setError(null);

             }
             if (department.isEmpty()) {
                Toast.makeText(getContext(), "All fields are required", Toast.LENGTH_SHORT).show();
            }
            Toast.makeText(getContext(), "All fields are required", Toast.LENGTH_SHORT).show();

        }else{
            db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(EmployeeContract.EmployeeEntry.COLUMN_NAME_NAME, name);
            values.put(EmployeeContract.EmployeeEntry.COLUMN_NAME_ADDRESS, address);
            values.put(EmployeeContract.EmployeeEntry.COLUMN_NAME_DEPARTMENT, department);

            long newRowId = db.insert(EmployeeContract.EmployeeEntry.TABLE_NAME, null, values);
            if (newRowId != -1) {
                Toast.makeText(getContext(), "Employee saved", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Error saving employee", Toast.LENGTH_SHORT).show();
            }
        }



    }
    private void setupSpinner() {

        String[] departments = {"Select Department", "Developer", "Tester"};


        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_spinner_item,
                departments
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerDepartment.setAdapter(adapter);


        binding.spinnerDepartment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                department = (String) parent.getItemAtPosition(position);


                if (position > 0) { // Ignore the "Select Department" option
                    Toast.makeText(getActivity(), "Selected: " + department, Toast.LENGTH_SHORT).show();
                } else {
                    department = "";  // Clear if "Select Department" is chosen
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }
}