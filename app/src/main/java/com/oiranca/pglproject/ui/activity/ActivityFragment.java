package com.oiranca.pglproject.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.oiranca.pglproject.R;
import com.oiranca.pglproject.ui.entidades.Admin;

import java.util.ArrayList;

public class ActivityFragment extends Fragment {

    private ActivityViewModel activityViewModel;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ArrayAdapter<String> comAdp;
    String date;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        activityViewModel = ViewModelProviders.of(this).get(ActivityViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_activity, container, false);
        final Spinner spinnerFam = root.findViewById(R.id.spinnerFamily);
        final Spinner spinnerWork = root.findViewById(R.id.spinnerWork);
        final CalendarView calendarView = root.findViewById(R.id.calendarAssig);


        Intent idUser = getActivity().getIntent();
        Bundle user = idUser.getExtras();
        String emailUser;

        emailUser = user.getString("Admin");

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Family-" + emailUser.replace(".", "-"));
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> nameFamily = new ArrayList<>();

                for (DataSnapshot name : dataSnapshot.getChildren()) {
                    String namesAd = name.child("name").getValue(String.class);
                    String nameFm = name.child("nameF").getValue(String.class);
                    if (nameFm != null) {
                        nameFamily.add(nameFm);
                    }
                    if ((namesAd != null)) {
                        nameFamily.add(namesAd);
                    }
                }

                comAdp = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, nameFamily);
                spinnerFam.setAdapter(comAdp);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        spinnerFam.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final String famSelect = spinnerFam.getSelectedItem().toString();

                calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                    @Override
                    public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                        int correction = 1 + month;
                        date = dayOfMonth + "-" + correction + "-" + year;

                        spinnerWork.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                final String workSelect = spinnerWork.getSelectedItem().toString();
                                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        String email;


                                        for (DataSnapshot work : dataSnapshot.getChildren()) {
                                            String namesAd = work.child("name").getValue(String.class);
                                            String namesFam = work.child("nameF").getValue(String.class);


                                            if (namesAd != null) {
                                                if (namesAd.contains(famSelect)) {
                                                    email = work.child("email").getValue(String.class);
                                                    String emailRemplace = email.replace(".", "-");

                                                    databaseReference.child(emailRemplace).child("Work-" + emailRemplace).child(date).child(workSelect).child("completed").setValue("no");

                                                    Toast.makeText(getContext(), email + workSelect + date, Toast.LENGTH_LONG).show();
                                                }
                                            }

                                            if (namesFam != null) {
                                                if (namesFam.contains(famSelect)) {
                                                    email = work.child("emailF").getValue(String.class);
                                                    Toast.makeText(getContext(), email + workSelect + date, Toast.LENGTH_LONG).show();
                                                }
                                            }


                                        }
                                    }


                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });


                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                    }
                });


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        final TextView textView = root.findViewById(R.id.home);
        activityViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

                textView.setText(s);
            }
        });


        return root;
    }
}