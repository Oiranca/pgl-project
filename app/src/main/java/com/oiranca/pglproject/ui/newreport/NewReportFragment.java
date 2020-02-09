package com.oiranca.pglproject.ui.newreport;

import androidx.lifecycle.ViewModelProviders;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.oiranca.pglproject.R;
import com.oiranca.pglproject.ui.reportmonth.ReportMonthViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class NewReportFragment extends Fragment {

    private NewReportViewModel mViewModel;

    public static NewReportFragment newInstance() {
        return new NewReportFragment();
    }

    private DatabaseReference databaseReference;
    private ArrayAdapter<String> comAdp;
    private String famSelect;
    private String keyValue;
    private String mailNoWork;
    private String nameNoWork;
    private String nameNoWorkBis;

    private CheckBox checkRep, checkRepBis;
    private EditText dateText;
    private String calendarDay = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ReportMonthViewModel monthViewModel = ViewModelProviders.of(this).get(ReportMonthViewModel.class);
        View root = inflater.inflate(R.layout.new_report_fragment, container, false);
        final Spinner spinnerReports = root.findViewById(R.id.snreReport);
        DatePicker dateNReport = root.findViewById(R.id.dateNewReport);
        dateNReport.setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);

        dateText = root.findViewById(R.id.textNReport);

        checkRep = root.findViewById(R.id.checkBoxReport);
        checkRepBis = root.findViewById(R.id.checkBisNReport);

        FloatingActionButton sendMail = root.findViewById(R.id.buttonSendMail);


        Intent idUser = Objects.requireNonNull(getActivity()).getIntent();
        Bundle user = idUser.getExtras();
        String emailUser;
        assert user != null;
        emailUser = user.getString("Admin");

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        if (emailUser != null) {
            databaseReference = firebaseDatabase.getReference().child("Family-" + emailUser.replace(".", "-"));


        }


        itemSpinner(spinnerReports);
        datePresent();

        spinnerReports.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                famSelect = spinnerReports.getSelectedItem().toString();

                extractData();


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            dateNReport.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    int correction = 1 + monthOfYear;

                    String day = Integer.toString(dayOfMonth);
                    String m = Integer.toString(correction);
                    String years = Integer.toString(year);
                    calendarDay = day + "-" + m + "-" + years;
                    dateText.setText("Fecha : " + calendarDay);
                    extractData();


                }
            });
        }


        sendMail.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                sendData();
            }
        });


        return root;
    }

    @SuppressLint("IntentReset")
    private void sendData() {
        if (nameNoWork == null & nameNoWorkBis == null) {
            Toast.makeText(getContext(),"Todas la tareas hechas", Toast.LENGTH_SHORT).show();
        } else {
            String[] TO = {mailNoWork};
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setData(Uri.parse("mailto:"));
            emailIntent.setType("text/plain");
            emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);


            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Tareas sin hacer del día: " + calendarDay);

            if (nameNoWork != null & nameNoWorkBis != null) {
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Tareas sin hacer:\n\n " + "1- " + nameNoWork + "\n\n 2- " + nameNoWorkBis);
            } else {
                if (nameNoWork != null) {
                    emailIntent.putExtra(Intent.EXTRA_TEXT, "Tareas sin hacer:\n\n " + "1- " + nameNoWork);
                } else {
                    if (nameNoWorkBis != null) {
                        emailIntent.putExtra(Intent.EXTRA_TEXT, "Tareas sin hacer:\n\n " + "1- " + nameNoWorkBis);
                    }
                }
            }


            try {
                startActivity(Intent.createChooser(emailIntent, "Enviar email..."));

            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(getContext(),
                        "No tienes clientes de email instalados.", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void extractData() {
        if (!famSelect.equals("Selecciona un familiar")) {

            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot worksReports : dataSnapshot.getChildren()) {

                        keyValue = worksReports.getKey();
                        if (keyValue != null) {
                            String nameF = dataSnapshot.child(keyValue).child("nameF").getValue(String.class);
                            String nameAd = dataSnapshot.child(keyValue).child("name").getValue(String.class);

                            if (nameAd != null) {
                                if (nameAd.equals(famSelect)) {
                                    baseData(dataSnapshot);
                                }


                            } else {
                                if (nameF != null) {
                                    if (nameF.equals(famSelect)) {

                                        baseData(dataSnapshot);
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

            checkRep.setText("Selecciona una familiar");
            checkRepBis.setText("Selecciona una familiar");
            checkRep.setChecked(false);
            checkRepBis.setChecked(false);

        }
    }

    private void baseData(@NonNull DataSnapshot dataSnapshot) {

        mailNoWork = keyValue.replace("-", ".");

        GenericTypeIndicator<Map<String, Object>> genericTypeIndicator = new GenericTypeIndicator<Map<String, Object>>() {
        };
        Map<String, Object> workDay = dataSnapshot.child(keyValue).
                child("Work-" + keyValue).child(calendarDay).getValue(genericTypeIndicator);

        if (workDay != null) {

            String[] homeWorks = workDay.keySet().toArray(new String[0]);

            if (homeWorks.length > 0) {

                for (int works = 0; works < homeWorks.length; works++) {
                    String completedFirst;
                    if (works > 0) {


                        checkRep.setText(homeWorks[works - 1]);
                        checkRepBis.setText(homeWorks[works]);

                        completedFirst = dataSnapshot.child(keyValue).
                                child("Work-" + keyValue).child(calendarDay).child(homeWorks[works - 1]).child("completed").getValue(String.class);

                        String completedSecond = dataSnapshot.child(keyValue).
                                child("Work-" + keyValue).child(calendarDay).child(homeWorks[works]).child("completed").getValue(String.class);


                        assert completedFirst != null;
                        if (completedFirst.toLowerCase().equals("si")) {

                            checkRep.setChecked(true);


                        } else {
                            checkRep.setChecked(false);

                            nameNoWork = homeWorks[works - 1];


                        }

                        assert completedSecond != null;
                        if (completedSecond.toLowerCase().equals("si")) {
                            checkRepBis.setChecked(true);


                        } else {

                            checkRepBis.setChecked(false);
                            nameNoWorkBis = homeWorks[works];
                        }


                    } else {
                        checkRep.setText(homeWorks[works]);
                        checkRepBis.setText("Sin tarea");
                        completedFirst = dataSnapshot.child(keyValue).
                                child("Work-" + keyValue).child(calendarDay).child(homeWorks[works]).child("completed").getValue(String.class);
                        if (completedFirst.toLowerCase().equals("si")) {

                            checkRep.setChecked(true);
                            checkRepBis.setChecked(false);


                        } else {
                            checkRep.setChecked(false);

                            checkRepBis.setChecked(false);
                        }

                    }

                }

            }
        } else {
            checkRep.setText("Día libre");
            checkRepBis.setText("Día libre");
            checkRep.setChecked(false);
            checkRepBis.setChecked(false);
        }
    }


    private void itemSpinner(final Spinner spinneReports) {
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
                spinneReports.setAdapter(comAdp);


            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void datePresent() {

        SimpleDateFormat fechForm = new SimpleDateFormat("d-M-yyyy", Locale.getDefault());
        Date fechaHoy = new Date();
        calendarDay = fechForm.format(fechaHoy);
        dateText.setText("Fecha : " + calendarDay);


    }


}
