package com.oiranca.pglproject.ui.reportmonth;


import androidx.lifecycle.ViewModelProviders;


;
import android.content.Intent;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;


import com.github.mikephil.charting.animation.Easing;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;


import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.oiranca.pglproject.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

public class ReportMonthFragment extends Fragment {


    private DatabaseReference databaseReference;
    private ArrayAdapter<String> comAdp;
    private String famSelect;
    private String keyValue;
    private PieChart chart;

    private String day;
    private int cont = 0;
    private int contRepeat = 0;
    private String monthWork;

    private String[] monthLargeName = {"Enero", "Marzo", "Mayo", "Julio", "Agosto", "Octubre", "Diciembre"};
    private Integer[] monthLargeNumber = {1, 3, 5, 7, 8, 10, 12};

    private String[] monthShortName = {"Abril", "Junio", "Septiembre", "Noviembre"};
    private Integer[] monthShortNumber = {4, 6, 9, 11};

    private Map<Integer, String> dayWorks = new HashMap<>();

    private Map<Integer, String> monthLarge = new HashMap<>();
    private Map<Integer, String> monthShort = new HashMap<>();

    private Map<String, Object> workDayFam;
    private Map<String, Object> workDayAdm;

    private String[] homeWorks;
    private String[] possWorks;
    private ArrayList<Integer> numberRep;
    private String compName;
    private String compNameAd;
    private String emailUser;

    private GenericTypeIndicator<Map<String, Object>> genericTypeIndicator = new GenericTypeIndicator<Map<String, Object>>() {
    };


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        ReportMonthViewModel monthViewModel = ViewModelProviders.of(this).get(ReportMonthViewModel.class);
        final View root = inflater.inflate(R.layout.report_month_fragment, container, false);
        possWorks = getResources().getStringArray(R.array.works);

        final Spinner spinnerFamily = root.findViewById(R.id.spinnerFam);
        final Spinner spinnerMonth = root.findViewById(R.id.spinnerMonth);
        chart = root.findViewById(R.id.chart1);

        dataMonth();


        Intent idUser = Objects.requireNonNull(getActivity()).getIntent();
        Bundle user = idUser.getExtras();

        assert user != null;


        final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();


        if (user.getString("Admin") != null) {
            emailUser = user.getString("Admin");
            assert emailUser != null;
            databaseReference = firebaseDatabase.getReference().child("Family-" + emailUser.replace(".", "-"));
            itemSpinner(spinnerFamily);

            spinnerFamily.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    famSelect = spinnerFamily.getSelectedItem().toString();

                    spinnerMonth.setSelection(0);
                    drawColumm();

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            chargeWorkAllFamily(spinnerMonth);

        } else {

            emailUser = user.getString("Family");
            databaseReference = firebaseDatabase.getReference();

            if (user.getString("NameFam") != null) {
                String[] nameFam = new String[1];
                nameFam[0] = user.getString("NameFam");
                comAdp = new ArrayAdapter<String>(Objects.requireNonNull(getContext()), android.R.layout.simple_spinner_dropdown_item, nameFam);
                spinnerFamily.setAdapter(comAdp);
                famSelect = spinnerFamily.getSelectedItem().toString();
                spinnerFamily.setEnabled(false);

            }
            chargeOnlyFamilyMember(spinnerMonth, emailUser);
        }


        return root;
    }



    private void chargeWorkAllFamily(final Spinner spinnerMonth) {
        spinnerMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
                monthWork = spinnerMonth.getSelectedItem().toString();
                numberRep = new ArrayList<>();
                numberRep.clear();
                dayWorks.clear();


                Calendar cal = Calendar.getInstance();
                final int year = cal.get(Calendar.YEAR);

                if (!monthWork.equals("Selecciona el mes")) {

                    if (!famSelect.equals("Selecciona un familiar")) {

                        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot dataFamily : dataSnapshot.getChildren()) {

                                    compName = dataFamily.child("nameF").getValue(String.class);
                                    compNameAd = dataFamily.child("name").getValue(String.class);
                                    keyValue = dataFamily.getKey();

                                    if (keyValue != null) {
                                        if (compName != null) {
                                            if (compName.contains(famSelect)) {
                                                for (Integer key : monthShort.keySet()) {


                                                    if (monthWork.equals(monthShort.get(key))) {

                                                        for (int dWork = 0; dWork < 30; dWork++) {
                                                            day = (dWork + 1) + "-" + key + "-" + year;
                                                            workDayFam = dataSnapshot.child(keyValue).
                                                                    child("Work-" + keyValue).child(day).getValue(genericTypeIndicator);

                                                            if (workDayFam != null) {
                                                                homeWorks = workDayFam.keySet().toArray(new String[0]);

                                                                for (int workF = 0; workF < homeWorks.length; workF++) {


                                                                    dayWorks.put(cont, homeWorks[workF]);
                                                                    cont++;


                                                                }
                                                            }


                                                        }
                                                        for (int i = 1; i < possWorks.length; i++) {


                                                            contRepeat = 0;
                                                            Iterator<Map.Entry<Integer, String>> it = dayWorks.entrySet().iterator();

                                                            while (it.hasNext()) {
                                                                Map.Entry<Integer, String> e = it.next();
                                                                if (possWorks[i].equals(e.getValue())) {

                                                                    cont++;
                                                                    contRepeat = cont + contRepeat;
                                                                }
                                                                cont = 0;

                                                            }
                                                            System.out.println(possWorks[i] + " se repite " + contRepeat);
                                                            numberRep.add(contRepeat);


                                                        }
                                                        setData(possWorks.length, 100, numberRep);
                                                    }
                                                }

                                                for (Integer keyL : monthLarge.keySet()) {


                                                    if (monthWork.equals(monthLarge.get(keyL))) {


                                                        for (int dWork = 0; dWork < 31; dWork++) {
                                                            day = (dWork + 1) + "-" + keyL + "-" + year;
                                                            workDayFam = dataSnapshot.child(keyValue).
                                                                    child("Work-" + keyValue).child(day).getValue(genericTypeIndicator);

                                                            if (workDayFam != null) {
                                                                homeWorks = workDayFam.keySet().toArray(new String[0]);

                                                                for (int workF = 0; workF < homeWorks.length; workF++) {


                                                                    dayWorks.put(cont, homeWorks[workF]);
                                                                    cont++;


                                                                }
                                                            }

                                                        }
                                                        for (int i = 1; i < possWorks.length; i++) {


                                                            contRepeat = 0;
                                                            Iterator<Map.Entry<Integer, String>> it = dayWorks.entrySet().iterator();

                                                            while (it.hasNext()) {
                                                                Map.Entry<Integer, String> e = it.next();
                                                                if (possWorks[i].equals(e.getValue())) {

                                                                    cont++;
                                                                    contRepeat = cont + contRepeat;
                                                                }
                                                                cont = 0;

                                                            }
                                                            System.out.println(possWorks[i] + " se repite " + contRepeat);
                                                            numberRep.add(contRepeat);


                                                        }
                                                        setData(possWorks.length, 100, numberRep);
                                                    }
                                                }


                                                if (monthWork.equals("Febrero")) {


                                                    if ((year % 4 == 0 && year % 100 != 0) || (year % 100 == 0 && year % 400 == 0)) {

                                                        for (int dWork = 0; dWork < 29; dWork++) {
                                                            day = (dWork + 1) + "-2-" + year;

                                                            workDayFam = dataSnapshot.child(keyValue).
                                                                    child("Work-" + keyValue).child(day).getValue(genericTypeIndicator);
                                                            if (workDayFam != null) {
                                                                homeWorks = workDayFam.keySet().toArray(new String[0]);

                                                                for (int workF = 0; workF < homeWorks.length; workF++) {


                                                                    dayWorks.put(cont, homeWorks[workF]);
                                                                    cont++;


                                                                }

                                                            }
                                                        }

                                                        for (int i = 1; i < possWorks.length; i++) {


                                                            contRepeat = 0;
                                                            Iterator<Map.Entry<Integer, String>> it = dayWorks.entrySet().iterator();

                                                            while (it.hasNext()) {
                                                                Map.Entry<Integer, String> e = it.next();
                                                                if (possWorks[i].equals(e.getValue())) {

                                                                    cont++;
                                                                    contRepeat = cont + contRepeat;
                                                                }
                                                                cont = 0;

                                                            }
                                                            System.out.println(possWorks[i] + " se repite " + contRepeat);
                                                            numberRep.add(contRepeat);


                                                        }

                                                        setData(possWorks.length, 100, numberRep);


                                                    } else {
                                                        for (int dWork = 0; dWork < 28; dWork++) {
                                                            day = (dWork + 1) + "-2-" + year;
                                                            workDayFam = dataSnapshot.child(keyValue).
                                                                    child("Work-" + keyValue).child(day).getValue(genericTypeIndicator);

                                                            if (workDayFam != null) {
                                                                homeWorks = workDayFam.keySet().toArray(new String[0]);

                                                                for (int workF = 0; workF < homeWorks.length; workF++) {


                                                                    dayWorks.put(cont, homeWorks[workF]);
                                                                    cont++;


                                                                }
                                                            }
                                                        }
                                                        for (int i = 1; i < possWorks.length; i++) {


                                                            contRepeat = 0;
                                                            Iterator<Map.Entry<Integer, String>> it = dayWorks.entrySet().iterator();

                                                            while (it.hasNext()) {
                                                                Map.Entry<Integer, String> e = it.next();
                                                                if (possWorks[i].equals(e.getValue())) {

                                                                    cont++;
                                                                    contRepeat = cont + contRepeat;
                                                                }
                                                                cont = 0;


                                                            }
                                                            System.out.println(possWorks[i] + " se repite " + contRepeat);
                                                            numberRep.add(contRepeat);


                                                        }

                                                        setData(possWorks.length, 100, numberRep);
                                                    }

                                                }
                                            }
                                        }

                                    }

                                }
                                if (compName == null) {


                                      if ( emailUser != null) {
                                          compName = dataSnapshot.child(emailUser.replace(".", "-")).child("name").getValue(String.class);
                                          if (compName != null) {

                                              if (compName.contains(famSelect)) {
                                                  for (Integer key : monthShort.keySet()) {


                                                      if (monthWork.equals(monthShort.get(key))) {

                                                          for (int dWork = 0; dWork < 30; dWork++) {
                                                              day = (dWork + 1) + "-" + key + "-" + year;
                                                              workDayFam = dataSnapshot.child(emailUser.replace(".", "-")).
                                                                      child("Work-" + emailUser.replace(".", "-")).child(day).getValue(genericTypeIndicator);

                                                              if (workDayFam != null) {
                                                                  homeWorks = workDayFam.keySet().toArray(new String[0]);

                                                                  for (int workF = 0; workF < homeWorks.length; workF++) {


                                                                      dayWorks.put(cont, homeWorks[workF]);
                                                                      cont++;


                                                                  }
                                                              }


                                                          }
                                                          for (int i = 1; i < possWorks.length; i++) {


                                                              contRepeat = 0;
                                                              Iterator<Map.Entry<Integer, String>> it = dayWorks.entrySet().iterator();

                                                              while (it.hasNext()) {
                                                                  Map.Entry<Integer, String> e = it.next();
                                                                  if (possWorks[i].equals(e.getValue())) {

                                                                      cont++;
                                                                      contRepeat = cont + contRepeat;
                                                                  }
                                                                  cont = 0;

                                                              }
                                                              numberRep.add(contRepeat);


                                                          }
                                                          setData(possWorks.length, 100, numberRep);
                                                      }
                                                  }

                                                  for (Integer keyL : monthLarge.keySet()) {


                                                      if (monthWork.equals(monthLarge.get(keyL))) {


                                                          for (int dWork = 0; dWork < 31; dWork++) {
                                                              day = (dWork + 1) + "-" + keyL + "-" + year;
                                                              workDayFam = dataSnapshot.child(emailUser.replace(".", "-")).
                                                                      child("Work-" + emailUser.replace(".", "-")).child(day).getValue(genericTypeIndicator);

                                                              if (workDayFam != null) {
                                                                  homeWorks = workDayFam.keySet().toArray(new String[0]);

                                                                  for (int workF = 0; workF < homeWorks.length; workF++) {


                                                                      dayWorks.put(cont, homeWorks[workF]);
                                                                      cont++;


                                                                  }
                                                              }

                                                          }
                                                          for (int i = 1; i < possWorks.length; i++) {


                                                              contRepeat = 0;
                                                              Iterator<Map.Entry<Integer, String>> it = dayWorks.entrySet().iterator();

                                                              while (it.hasNext()) {
                                                                  Map.Entry<Integer, String> e = it.next();
                                                                  if (possWorks[i].equals(e.getValue())) {

                                                                      cont++;
                                                                      contRepeat = cont + contRepeat;
                                                                  }
                                                                  cont = 0;

                                                              }

                                                              numberRep.add(contRepeat);


                                                          }
                                                          setData(possWorks.length, 100, numberRep);
                                                      }
                                                  }


                                                  if (monthWork.equals("Febrero")) {


                                                      if ((year % 4 == 0 && year % 100 != 0) || (year % 100 == 0 && year % 400 == 0)) {

                                                          for (int dWork = 0; dWork < 29; dWork++) {
                                                              day = (dWork + 1) + "-2-" + year;

                                                              workDayFam = dataSnapshot.child(emailUser.replace(".", "-")).
                                                                      child("Work-" + emailUser.replace(".", "-")).child(day).getValue(genericTypeIndicator);
                                                              if (workDayFam != null) {
                                                                  homeWorks = workDayFam.keySet().toArray(new String[0]);

                                                                  for (int workF = 0; workF < homeWorks.length; workF++) {


                                                                      dayWorks.put(cont, homeWorks[workF]);
                                                                      cont++;


                                                                  }

                                                              }
                                                          }

                                                          for (int i = 1; i < possWorks.length; i++) {


                                                              contRepeat = 0;
                                                              Iterator<Map.Entry<Integer, String>> it = dayWorks.entrySet().iterator();

                                                              while (it.hasNext()) {
                                                                  Map.Entry<Integer, String> e = it.next();
                                                                  if (possWorks[i].equals(e.getValue())) {

                                                                      cont++;
                                                                      contRepeat = cont + contRepeat;
                                                                  }
                                                                  cont = 0;

                                                              }
                                                              System.out.println(possWorks[i] + " se repite " + contRepeat);
                                                              numberRep.add(contRepeat);


                                                          }

                                                          setData(possWorks.length, 100, numberRep);


                                                      } else {
                                                          for (int dWork = 0; dWork < 28; dWork++) {
                                                              day = (dWork + 1) + "-2-" + year;
                                                              workDayFam = dataSnapshot.child(emailUser.replace(".", "-")).
                                                                      child("Work-" + emailUser.replace(".", "-")).child(day).getValue(genericTypeIndicator);

                                                              if (workDayFam != null) {
                                                                  homeWorks = workDayFam.keySet().toArray(new String[0]);

                                                                  for (int workF = 0; workF < homeWorks.length; workF++) {


                                                                      dayWorks.put(cont, homeWorks[workF]);
                                                                      cont++;


                                                                  }
                                                              }
                                                          }
                                                          for (int i = 1; i < possWorks.length; i++) {


                                                              contRepeat = 0;
                                                              Iterator<Map.Entry<Integer, String>> it = dayWorks.entrySet().iterator();

                                                              while (it.hasNext()) {
                                                                  Map.Entry<Integer, String> e = it.next();
                                                                  if (possWorks[i].equals(e.getValue())) {

                                                                      cont++;
                                                                      contRepeat = cont + contRepeat;
                                                                  }
                                                                  cont = 0;


                                                              }

                                                              numberRep.add(contRepeat);


                                                          }

                                                          setData(possWorks.length, 100, numberRep);
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


                    } else {
                        Toast.makeText(getContext(), "No ha seleccionado el familiar", Toast.LENGTH_SHORT).show();
                        spinnerMonth.setSelection(0);
                    }

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void chargeOnlyFamilyMember(@NonNull final Spinner spinnerMonth, final String emailUser) {
        spinnerMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                monthWork = spinnerMonth.getSelectedItem().toString();
                numberRep = new ArrayList<>();
                numberRep.clear();
                dayWorks.clear();


                Calendar cal = Calendar.getInstance();
                final int year = cal.get(Calendar.YEAR);


                if (!monthWork.equals("Selecciona el mes") && !famSelect.equals("Selecciona un familiar")) {

                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot nameFamily : dataSnapshot.getChildren()) {
                                String keyFamily = nameFamily.getKey(); //Key Family-.......

                                if (keyFamily != null && emailUser != null) {
                                    compName = dataSnapshot.child(keyFamily).child(emailUser.replace(".", "-")).child("nameF").getValue(String.class);
                                    if (compName != null) {

                                        if (compName.contains(famSelect)) {
                                            for (Integer key : monthShort.keySet()) {


                                                if (monthWork.equals(monthShort.get(key))) {

                                                    for (int dWork = 0; dWork < 30; dWork++) {
                                                        day = (dWork + 1) + "-" + key + "-" + year;
                                                        workDayFam = nameFamily.child(emailUser.replace(".", "-")).
                                                                child("Work-" + emailUser.replace(".", "-")).child(day).getValue(genericTypeIndicator);

                                                        if (workDayFam != null) {
                                                            homeWorks = workDayFam.keySet().toArray(new String[0]);

                                                            for (int workF = 0; workF < homeWorks.length; workF++) {


                                                                dayWorks.put(cont, homeWorks[workF]);
                                                                cont++;


                                                            }
                                                        }


                                                    }
                                                    for (int i = 1; i < possWorks.length; i++) {


                                                        contRepeat = 0;
                                                        Iterator<Map.Entry<Integer, String>> it = dayWorks.entrySet().iterator();

                                                        while (it.hasNext()) {
                                                            Map.Entry<Integer, String> e = it.next();
                                                            if (possWorks[i].equals(e.getValue())) {

                                                                cont++;
                                                                contRepeat = cont + contRepeat;
                                                            }
                                                            cont = 0;

                                                        }
                                                        System.out.println(possWorks[i] + " se repite " + contRepeat);
                                                        numberRep.add(contRepeat);


                                                    }
                                                    setData(possWorks.length, 100, numberRep);
                                                }
                                            }

                                            for (Integer keyL : monthLarge.keySet()) {


                                                if (monthWork.equals(monthLarge.get(keyL))) {


                                                    for (int dWork = 0; dWork < 31; dWork++) {
                                                        day = (dWork + 1) + "-" + keyL + "-" + year;
                                                        workDayFam = nameFamily.child(emailUser.replace(".", "-")).
                                                                child("Work-" + emailUser.replace(".", "-")).child(day).getValue(genericTypeIndicator);

                                                        if (workDayFam != null) {
                                                            homeWorks = workDayFam.keySet().toArray(new String[0]);

                                                            for (int workF = 0; workF < homeWorks.length; workF++) {


                                                                dayWorks.put(cont, homeWorks[workF]);
                                                                cont++;


                                                            }
                                                        }

                                                    }
                                                    for (int i = 1; i < possWorks.length; i++) {


                                                        contRepeat = 0;
                                                        Iterator<Map.Entry<Integer, String>> it = dayWorks.entrySet().iterator();

                                                        while (it.hasNext()) {
                                                            Map.Entry<Integer, String> e = it.next();
                                                            if (possWorks[i].equals(e.getValue())) {

                                                                cont++;
                                                                contRepeat = cont + contRepeat;
                                                            }
                                                            cont = 0;

                                                        }
                                                        System.out.println(possWorks[i] + " se repite " + contRepeat);
                                                        numberRep.add(contRepeat);


                                                    }
                                                    setData(possWorks.length, 100, numberRep);
                                                }
                                            }


                                            if (monthWork.equals("Febrero")) {


                                                if ((year % 4 == 0 && year % 100 != 0) || (year % 100 == 0 && year % 400 == 0)) {

                                                    for (int dWork = 0; dWork < 29; dWork++) {
                                                        day = (dWork + 1) + "-2-" + year;

                                                        workDayFam = nameFamily.child(emailUser.replace(".", "-")).
                                                                child("Work-" + emailUser.replace(".", "-")).child(day).getValue(genericTypeIndicator);
                                                        if (workDayFam != null) {
                                                            homeWorks = workDayFam.keySet().toArray(new String[0]);

                                                            for (int workF = 0; workF < homeWorks.length; workF++) {


                                                                dayWorks.put(cont, homeWorks[workF]);
                                                                cont++;


                                                            }

                                                        }
                                                    }

                                                    for (int i = 1; i < possWorks.length; i++) {


                                                        contRepeat = 0;
                                                        Iterator<Map.Entry<Integer, String>> it = dayWorks.entrySet().iterator();

                                                        while (it.hasNext()) {
                                                            Map.Entry<Integer, String> e = it.next();
                                                            if (possWorks[i].equals(e.getValue())) {

                                                                cont++;
                                                                contRepeat = cont + contRepeat;
                                                            }
                                                            cont = 0;

                                                        }
                                                        System.out.println(possWorks[i] + " se repite " + contRepeat);
                                                        numberRep.add(contRepeat);


                                                    }

                                                    setData(possWorks.length, 100, numberRep);


                                                } else {
                                                    for (int dWork = 0; dWork < 28; dWork++) {
                                                        day = (dWork + 1) + "-2-" + year;
                                                        workDayFam = nameFamily.child(emailUser.replace(".", "-")).
                                                                child("Work-" + emailUser.replace(".", "-")).child(day).getValue(genericTypeIndicator);

                                                        if (workDayFam != null) {
                                                            homeWorks = workDayFam.keySet().toArray(new String[0]);

                                                            for (int workF = 0; workF < homeWorks.length; workF++) {


                                                                dayWorks.put(cont, homeWorks[workF]);
                                                                cont++;


                                                            }
                                                        }
                                                    }
                                                    for (int i = 1; i < possWorks.length; i++) {


                                                        contRepeat = 0;
                                                        Iterator<Map.Entry<Integer, String>> it = dayWorks.entrySet().iterator();

                                                        while (it.hasNext()) {
                                                            Map.Entry<Integer, String> e = it.next();
                                                            if (possWorks[i].equals(e.getValue())) {

                                                                cont++;
                                                                contRepeat = cont + contRepeat;
                                                            }
                                                            cont = 0;


                                                        }
                                                        System.out.println(possWorks[i] + " se repite " + contRepeat);
                                                        numberRep.add(contRepeat);


                                                    }

                                                    setData(possWorks.length, 100, numberRep);
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

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void drawColumm() {

        chart.clear();
        chart.invalidate();
        chart.getDescription().setEnabled(false);


        chart.setExtraOffsets(5, 10, 5, 5);

        chart.setDragDecelerationFrictionCoef(0.95f);


        chart.setExtraOffsets(20.f, 0.f, 20.f, 0.f);

        chart.setDrawHoleEnabled(true);
        chart.setHoleColor(Color.TRANSPARENT);

        chart.setTransparentCircleColor(Color.TRANSPARENT);
        chart.setTransparentCircleAlpha(110);

        chart.setHoleRadius(58f);
        chart.setTransparentCircleRadius(61f);


        chart.setRotationAngle(0);

        chart.setRotationEnabled(false);
        chart.setHighlightPerTapEnabled(true);


        chart.animateY(1400, Easing.EaseInOutQuad);

        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setYOffset(10f);
        l.setDrawInside(false);
        l.setEnabled(true);
        l.setTextColor(Color.WHITE);
        l.setTextSize(12f);
        l.setTypeface(Typeface.SANS_SERIF);
    }


    private void setData(int count, float range, ArrayList<Integer> numberRep) {

        drawColumm();
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.clear();


        for (int i = 0; i < numberRep.size(); i++) {
            if (numberRep.get(i) != null && numberRep.get(i) != 0) {

                if (!possWorks[i].equals("Selecciona una tarea")){
                    entries.add(new PieEntry((float) (numberRep.get(i)), possWorks[i]));
                }

            }

        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);


        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<>();
        colors.clear();

        for (int c : ColorTemplate.MATERIAL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);


        dataSet.setValueLinePart1OffsetPercentage(80.f);
        dataSet.setValueLinePart1Length(0.2f);
        dataSet.setValueLinePart2Length(0.4f);


        dataSet.setYValuePosition(PieDataSet.ValuePosition.INSIDE_SLICE);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(12f);
        data.setValueTextColor(Color.BLACK);
        data.setValueTypeface(Typeface.DEFAULT_BOLD);
        chart.setData(data);

        //
        chart.highlightValues(null);

        chart.invalidate();
    }

    private void dataMonth() {
        for (int dateLarge = 0; dateLarge < monthLargeName.length; dateLarge++) {

            monthLarge.put(monthLargeNumber[dateLarge], monthLargeName[dateLarge]);


        }

        for (int dateShort = 0; dateShort < monthShortName.length; dateShort++) {

            monthShort.put(monthShortNumber[dateShort], monthShortName[dateShort]);


        }
    }


    private void itemSpinner(final Spinner spinnerFamily) {
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
                comAdp = new ArrayAdapter<String>(Objects.requireNonNull(getContext()), android.R.layout.simple_spinner_dropdown_item, nameFamily);
                spinnerFamily.setAdapter(comAdp);


            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}

