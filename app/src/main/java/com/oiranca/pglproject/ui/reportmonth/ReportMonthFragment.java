package com.oiranca.pglproject.ui.reportmonth;


import androidx.lifecycle.ViewModelProviders;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.support.v4.media.RatingCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.oiranca.pglproject.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;


public class ReportMonthFragment extends Fragment {


    private DatabaseReference databaseReference;
    private ArrayAdapter<String> comAdp;
    private String famSelect;
    private String keyValue;


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        ReportMonthViewModel monthViewModel = ViewModelProviders.of(this).get(ReportMonthViewModel.class);
        View root = inflater.inflate(R.layout.report_month_fragment, container, false);
        final Spinner spinnerMonth = root.findViewById(R.id.spinnerMonth);


        final EditText textMonth = root.findViewById(R.id.textMonth);

        final EditText textMonthBis = root.findViewById(R.id.textMonthBis);


        Intent idUser = Objects.requireNonNull(getActivity()).getIntent();
        Bundle user = idUser.getExtras();
        String emailUser;
        assert user != null;
        emailUser = user.getString("Admin");

        final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        if (emailUser != null) {
            databaseReference = firebaseDatabase.getReference().child("Family-" + emailUser.replace(".", "-"));
        }


        itemSpinner(spinnerMonth);

        spinnerMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                famSelect = spinnerMonth.getSelectedItem().toString();

                if (!famSelect.equals("Selecciona un familiar")) {

                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot dataFamily : dataSnapshot.getChildren()) {
                                String compName = dataFamily.child("nameF").getValue(String.class);

                                if (compName != null) {
                                    if (compName.contains(famSelect)) {
                                        keyValue = dataFamily.getKey();
                                        if (keyValue != null) {
                                            String emailFamily = dataSnapshot.child(keyValue).child("emailF").getValue(String.class);
                                            String surFamily = dataSnapshot.child(keyValue).child("surnameF").getValue(String.class);

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

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        textMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentDate = Calendar.getInstance();
                final int year = mcurrentDate.get(Calendar.YEAR);
                final int month = mcurrentDate.get(Calendar.MONTH);
                final int day = mcurrentDate.get(Calendar.DAY_OF_MONTH);



                DatePickerDialog mDatePicker = new DatePickerDialog(Objects.requireNonNull(getContext()),R.style.Theme_Custom_Dialog, new DatePickerDialog.OnDateSetListener() {
                    @SuppressLint("SetTextI18n")
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        String firstDay = String.valueOf(selectedday);
                        String firstMonth = String.valueOf(selectedmonth);
                        String firstYear = String.valueOf(selectedyear);
                        textMonth.setText("Fecha de inicio : "+firstDay + "-" + firstMonth + "-" + firstYear);
                    }
                }, year, month, day);

                mDatePicker.setTitle("Seleciona la fecha incio");
                mDatePicker.show();
            }

        });

        textMonthBis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentDateDis = Calendar.getInstance();
                int yearBis = mcurrentDateDis.get(Calendar.YEAR);
                int monthBis = mcurrentDateDis.get(Calendar.MONTH);
                int dayBis = mcurrentDateDis.get(Calendar.DAY_OF_MONTH);


                final DatePickerDialog mDatePickerBis = new DatePickerDialog(Objects.requireNonNull(getContext()),R.style.Theme_Custom_Dialog, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyearBis, int selectedmonthBis, int selecteddayBis) {

                        String firstDayBis = String.valueOf(selecteddayBis);
                        String firstMonthBis = String.valueOf(selectedmonthBis);
                        String firstYearBis = String.valueOf(selectedyearBis);
                        textMonthBis.setText("Fecha de fin : "+firstDayBis + "-" + firstMonthBis + "-" + firstYearBis);
                    }
                }, yearBis, monthBis, dayBis);

                mDatePickerBis.setTitle("Selecciona la fecha fin");
                mDatePickerBis.show();
            }
        });







        return root;
    }

    private void itemSpinner(final Spinner spinnerMonth) {
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
                spinnerMonth.setAdapter(comAdp);


            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
