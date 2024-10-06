package com.example.locationtrackapp;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.locationtrackapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    //    private EmployeeAdapter employeeAdapter;
//    private EmployeeDbHelper dbHelper;
    private Fragment employefrag;
    private Fragment mappingFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        clicks();

    }

    private void clicks() {
        binding.btnAddEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                employefrag = new Employefrag();
                replaceFragment(employefrag);
                binding.ll1.setVisibility(View.GONE);
            }
        });
        binding.btnViewEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mappingFrag=new MappingFrag();
                replaceFragment(mappingFrag);
                binding.ll1.setVisibility(View.GONE);
            }
        });
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        ((FragmentTransaction) fragmentTransaction).add(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(String.valueOf(false));
        fragmentTransaction.commit();
    }
}