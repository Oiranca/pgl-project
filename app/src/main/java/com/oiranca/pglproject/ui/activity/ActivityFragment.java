package com.oiranca.pglproject.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.oiranca.pglproject.R;

import java.util.ArrayList;

public class ActivityFragment extends Fragment {

    private DatabaseReference databaseReference;
    private ArrayAdapter<String> comAdp;
    private String date;
    private String famSelect;
    private String workSelect;
    private String emailRemplace;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        ActivityViewModel activityViewModel = ViewModelProviders.of(this).get(ActivityViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_activity, container, false);
        final Spinner spinnerFam = root.findViewById(R.id.spinnerFamily);
        final Spinner spinnerWork = root.findViewById(R.id.spinnerWork);
        final CalendarView calendarView = root.findViewById(R.id.calendarAssig);
        final FloatingActionButton floatButton = root.findViewById(R.id.floatMyAct);


        Intent idUser = getActivity().getIntent();
        Bundle user = idUser.getExtras();
        String emailUser;
        emailUser = user.getString("Admin");

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Family-" + emailUser.replace(".", "-"));

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> nameFamily = new ArrayList<>();
                nameFamily.add("Selecciona un familiar");
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

                famSelect = spinnerFam.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                int correction = 1 + month;
                date = dayOfMonth + "-" + correction + "-" + year;


            }
        });

        spinnerWork.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                workSelect = spinnerWork.getSelectedItem().toString();
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String email;


                        for (DataSnapshot work : dataSnapshot.getChildren()) {
                            String namesAd = work.child("name").getValue(String.class);
                            String namesFam = work.child("nameF").getValue(String.class);


                            if (namesAd != null) {
                                if (namesAd.contains(famSelect) && !famSelect.equals("Selecciona un familiar") && !workSelect.equals("Selecciona una tarea")) {
                                    email = work.child("email").getValue(String.class);
                                    emailRemplace = email.replace(".", "-");
                                    if (date == null) {
                                        Toast.makeText(getContext(), "Tiene que elegir el día primero", Toast.LENGTH_LONG).show();
                                    } else {
                                        databaseReference.child(emailRemplace).child("Work-" + emailRemplace).child(date).child(workSelect).child("completed").setValue("no");
                                    }
                                }
                            }

                            if (namesFam != null) {
                                if (namesFam.contains(famSelect) && !famSelect.equals("Selecciona un familiar") && !workSelect.equals("Selecciona una tarea")) {

                                    email = work.child("emailF").getValue(String.class);
                                    emailRemplace = email.replace(".", "-");
                                    floatButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            databaseReference.child(emailRemplace).child("Work-" + emailRemplace).child(date).child(workSelect).child("completed").setValue("no");


                                        }
                                    });

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