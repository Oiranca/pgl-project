package com.oiranca.pglproject.ui.myactivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.CheckBox;
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
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.oiranca.pglproject.R;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class MyActivityFragment extends Fragment {


    private String actadm = null;
    private CheckBox chkMy, chkMybis;
    private DatabaseReference databaseReference;
    private String emailUser;
    private String emailFam;
    private String keyValue;
    private String[] homeWorks;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MyActivityViewModel myActivityViewModel = ViewModelProviders.of(this).get(MyActivityViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_my_activity, container, false);
        final TextView textView = root.findViewById(R.id.text_slideshow);
        myActivityViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText("Poner el Nombre del Usuario");
            }
        });

        Intent idUser = Objects.requireNonNull(getActivity()).getIntent();
        Bundle user = idUser.getExtras();
        assert user != null;
        emailUser = user.getString("Admin");

        Intent idFam = Objects.requireNonNull(getActivity()).getIntent();
        Bundle userF = idFam.getExtras();
        assert userF != null;
        emailFam = user.getString("Family");


        chkMy = root.findViewById(R.id.checkBoxAdm);
        chkMybis = root.findViewById(R.id.checkBoxAdmBis);

        CalendarView calendar = root.findViewById(R.id.calendarMactivity);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        datePresent();
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                int correction = 1 + month;

                String day = Integer.toString(dayOfMonth);
                String m = Integer.toString(correction);
                String years = Integer.toString(year);
                actadm = day + "/" + m + "/" + years;

                if (emailUser != null) {
                    chkMy.setText(getString(R.string.first_activity) + emailUser);
                    chkMybis.setText(getString(R.string.second_activity) + emailUser);

                } else {
                    if (emailFam != null) {

                        chkMy.setText(getString(R.string.first_activity) + emailFam);
                        chkMybis.setText(getString(R.string.second_activity) + emailFam);
                    }
                }


            }
        });


        return root;
    }


    private void datePresent() {

        SimpleDateFormat fechForm = new SimpleDateFormat("dd-M-yyyy", Locale.getDefault());
        Date fechaHoy = new Date();
        final String present = fechForm.format(fechaHoy);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String emailRemp = null;
                String emailRmpF = null;
                if (emailUser != null) {
                    emailRemp = emailUser.replace(".", "-");
                } else {
                    if (emailFam != null) {
                        emailRmpF = emailFam.replace(".", "-");

                    }
                }


                for (DataSnapshot homeW : dataSnapshot.getChildren()) {

                    keyValue = homeW.getKey();
                    if (keyValue != null) {
                        String emailComp = null;
                        String emailCompF = null;
                        if (emailRemp != null) {
                            emailComp = dataSnapshot.child(keyValue).child(emailRemp).child("email").getValue(String.class);
                        } else {
                            if (emailRmpF != null) {
                                emailCompF = dataSnapshot.child(keyValue).child(emailRmpF).child("emailF").getValue(String.class);
                            }
                        }


                        if (emailComp != null) {
                            if (emailComp.contains(emailUser)) {
                                GenericTypeIndicator<Map<String, Object>> genericTypeIndicator = new GenericTypeIndicator<Map<String, Object>>() {
                                };
                                Map<String, Object> workDay = dataSnapshot.child(keyValue).child(emailRemp).
                                        child("Work-" + emailRemp).child(present).getValue(genericTypeIndicator);


                                if (!workDay.keySet().isEmpty()) {
                                    homeWorks = workDay.keySet().toArray(new String[0]);

                                    if (homeWorks.length > 0) {
                                        for (int i = 0; i < homeWorks.length; i++) {
                                            if (i == 0) {
                                                chkMy.setText(homeWorks[i]);
                                            } else {
                                                chkMybis.setText(homeWorks[i]);
                                            }
                                        }


                                    } else {
                                        chkMy.setText("Dia libre");
                                        chkMybis.setText("Dia libre");
                                    }

                                }


                            }

                        } else {
                            if (emailCompF != null) {
                                if (emailCompF.contains(emailFam)) {
                                    GenericTypeIndicator<Map<String, Object>> genericTypeIndicator = new GenericTypeIndicator<Map<String, Object>>() {
                                    };
                                    Map<String, Object> workDay = dataSnapshot.child(keyValue).child(emailRmpF).
                                            child("Work-" + emailRmpF).child(present).getValue(genericTypeIndicator);


                                    if (!workDay.keySet().isEmpty()) {
                                        homeWorks = workDay.keySet().toArray(new String[0]);

                                        if (homeWorks.length > 0) {
                                            for (int i = 0; i < homeWorks.length; i++) {
                                                if (i == 0) {
                                                    chkMy.setText(homeWorks[i]);
                                                } else {
                                                    chkMybis.setText(homeWorks[i]);
                                                }
                                            }


                                        } else {
                                            chkMy.setText("Dia libre");
                                            chkMybis.setText("Dia libre");
                                        }

                                    }


                                }
                            }

                        }


                    }


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

}