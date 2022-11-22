package com.example.cscb07_tut5_group1.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.io.Console;

import com.example.cscb07_tut5_group1.databinding.FragmentHomeBinding;

// DatabaseReference
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

// Firebase
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.ValueEventListener;

// R
import com.example.cscb07_tut5_group1.R;

// DatabaseReference.getChildren()
import com.google.firebase.database.ChildEventListener;

// Console.log()
import android.util.Log;

// getValues()
import com.google.firebase.database.GenericTypeIndicator;

// getValue()
import com.google.firebase.database.DataSnapshot;

// mDatabase
import com.example.cscb07_tut5_group1.ui.home.HomeFragment;

// Task
import com.google.android.gms.tasks.Task;

// OnCompleteListener()
import com.google.android.gms.tasks.OnCompleteListener;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        DatabaseReference ref = FirebaseDatabase
                .getInstance("https://cscb07-project-tut5-grou-f0436-default-rtdb.firebaseio.com/")
                .getReference();
        ref.child("test").setValue("Hello World");

        System.out.println(ref.child("test").getKey());

        final TextView textView = binding.appName;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.test_database_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference mDatabase;
                mDatabase = FirebaseDatabase
                        .getInstance("https://cscb07-project-tut5-grou-f0436-default-rtdb.firebaseio.com/")
                        .getReference();
                // DatabaseReference ref = FirebaseDatabase
                //         .getInstance("https://cscb07-project-tut5-grou-f0436-default-rtdb.firebaseio.com/")
                //         .getReference();

                mDatabase.child("STUDENT").child("COURSE").get()
                        .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                if (!task.isSuccessful()) {
                                    Log.e("firebase", "Error getting data", task.getException());
                                } else {
                                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                                }
                            }
                        });
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}