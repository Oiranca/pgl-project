package com.oiranca.pglproject.ui.reports;


import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import android.widget.TableLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import androidx.lifecycle.ViewModelProviders;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.oiranca.pglproject.R;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;


public class ReportsFragment extends Fragment {

    private ReportsViewModel reportsViewModel;
    private TableView tablaDyn, tabHead;
    private String[] header = {"Fecha", "Nombre", "Actividad", "Realizado"};
    private ArrayList<String> item;
    private String[] datos;
    private String date;
    private DatabaseReference databaseReference;
    private String emailUser;
    private String emailFam;
    private String keyValue;
    private String[] homeWorks;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        reportsViewModel =
                ViewModelProviders.of(this).get(ReportsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_reports, container, false);
        TableLayout tableLayout = (TableLayout) root.findViewById(R.id.tableReports);
        tablaDyn = new TableView(tableLayout, getContext());
        TableLayout headTable = (TableLayout) root.findViewById(R.id.headTable);
        tabHead = new TableView(headTable, getContext());
        tabHead.addHead(header);
        Intent idUser = Objects.requireNonNull(getActivity()).getIntent();
        Bundle user = idUser.getExtras();
        assert user != null;
        emailUser = user.getString("Admin");

        Intent idFam = Objects.requireNonNull(getActivity()).getIntent();
        Bundle userF = idFam.getExtras();
        assert userF != null;
        emailFam = user.getString("Family");


        CalendarView calenReport = root.findViewById(R.id.calendarReport);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        calenReport.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, final int year, int month, final int dayOfMonth) {
                final int correction = 1 + month;

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

                                        String emailData[];
                                        date = dayOfMonth + "-" + correction + "-" + year;
                                        GenericTypeIndicator<Map<String, Object>> genericTypeIndicator = new GenericTypeIndicator<Map<String, Object>>() {
                                        };

                                        Map<String, Object> data = dataSnapshot.child(keyValue).getValue(genericTypeIndicator);

                                        emailData = data.keySet().toArray(new String[0]);

                                        if (emailData.length > 0) {
                                            for (int d = 0; d < emailData.length; d++) {
                                                String nameAd = dataSnapshot.child(keyValue).child(emailData[d]).child("name").getValue(String.class);
                                                String nameF = dataSnapshot.child(keyValue).child(emailData[d]).child("nameF").getValue(String.class);

                                                if (nameAd != null) {

                                                    Map<String, Object> workDay = dataSnapshot.child(keyValue).child(emailData[d]).
                                                            child("Work-" + emailData[d]).child(date).getValue(genericTypeIndicator);
                                                    if (workDay == null) {
                                                        item = new ArrayList<String>();
                                                        item.add(date);
                                                        item.add(nameAd);
                                                        item.add("Sin tarea");
                                                        item.add("Sin tarea");


                                                        tablaDyn.addFile(item);
                                                    } else {


                                                        homeWorks = workDay.keySet().toArray(new String[0]);

                                                        if (homeWorks.length > 0) {
                                                            for (int i = 0; i < homeWorks.length; i++) {
                                                                item = new ArrayList<String>();
                                                                String completed = dataSnapshot.child(keyValue).child(emailData[d])
                                                                        .child("Work-" + emailData[d]).child(date).child(homeWorks[i])
                                                                        .child("completed").getValue(String.class);


                                                                item.add(date);
                                                                item.add(nameAd);
                                                                item.add(homeWorks[i]);
                                                                item.add(completed);


                                                                tablaDyn.addFile(item);
                                                                System.out.println(homeWorks[i] + " " + completed);
                                                            }


                                                        }


                                                    }




                                                } else {
                                                    if (nameF != null) {
                                                        Map<String, Object> workDay = dataSnapshot.child(keyValue).child(emailData[d]).
                                                                child("Work-" + emailData[d]).child(date).getValue(genericTypeIndicator);
                                                        if (workDay == null) {
                                                            item = new ArrayList<String>();
                                                            item.add(date);
                                                            item.add(nameF);
                                                            item.add("Sin tarea");
                                                            item.add("Sin tarea");


                                                            tablaDyn.addFile(item);
                                                        } else {


                                                            homeWorks = workDay.keySet().toArray(new String[0]);

                                                            if (homeWorks.length > 0) {
                                                                for (int i = 0; i < homeWorks.length; i++) {
                                                                    item = new ArrayList<String>();
                                                                    String completed = dataSnapshot.child(keyValue).child(emailData[d])
                                                                            .child("Work-" + emailData[d]).child(date).child(homeWorks[i])
                                                                            .child("completed").getValue(String.class);


                                                                    item.add(date);
                                                                    item.add(nameF);
                                                                    item.add(homeWorks[i]);
                                                                    item.add(completed);


                                                                    tablaDyn.addFile(item);
                                                                    System.out.println(homeWorks[i] + " " + completed);
                                                                }


                                                            }


                                                        }

                                                    }
                                                }
                                            }
                                        }


                                    }

                                } else {
                                    if (emailCompF != null) {
                                        if (emailCompF.contains(emailFam)) {
                                            date = dayOfMonth + "-" + correction + "-" + year;
                                            GenericTypeIndicator<Map<String, Object>> genericTypeIndicator = new GenericTypeIndicator<Map<String, Object>>() {
                                            };
                                            Map<String, Object> workDay = dataSnapshot.child(keyValue).child(emailRmpF).
                                                    child("Work-" + emailRmpF).child(date).getValue(genericTypeIndicator);
                                            String name = dataSnapshot.child(keyValue).child(emailRmpF).child("nameF").getValue(String.class);


                                            if (name != null) {
                                                if (workDay == null) {
                                                    item = new ArrayList<String>();
                                                    item.add(date);
                                                    item.add("Sin tarea");
                                                    item.add("Sin tarea");
                                                    item.add("Sin tarea");


                                                    tablaDyn.addFile(item);
                                                } else {


                                                    homeWorks = workDay.keySet().toArray(new String[0]);

                                                    if (homeWorks.length > 0) {
                                                        for (int i = 0; i < homeWorks.length; i++) {
                                                            item = new ArrayList<String>();
                                                            String completed = dataSnapshot.child(keyValue).child(emailRmpF)
                                                                    .child("Work-" + emailRmpF).child(date).child(homeWorks[i])
                                                                    .child("completed").getValue(String.class);


                                                            item.add(date);
                                                            item.add(name);
                                                            item.add(homeWorks[i]);
                                                            item.add(completed);


                                                            tablaDyn.addFile(item);
                                                            System.out.println(homeWorks[i] + " " + completed);
                                                        }


                                                    }


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
        });


        return root;
    }


}