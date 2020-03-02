package com.oiranca.pglproject.ui.myactivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.oiranca.pglproject.R;

import java.text.SimpleDateFormat;
import java.util.Date;
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
    private String completed;
    private String emailRemp = null;
    private String emailRmpF = null;
    private String day, m, years;
    private FloatingActionButton calendarInsert;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MyActivityViewModel myActivityViewModel = ViewModelProviders.of(this).get(MyActivityViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_my_activity, container, false);
        calendarInsert = root.findViewById(R.id.floatCalendar);


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

        disableLastDate(calendar);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        datePresent();

        calendarInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                workInCalendar();
            }
        });


        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                int correction = 1 + month;

                day = Integer.toString(dayOfMonth);
                m = Integer.toString(correction);
                years = Integer.toString(year);
                actadm = day + "-" + m + "-" + years;

                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        extractData(dataSnapshot);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }
        });


        chkMy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (chkMy.isChecked()) {


                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if (emailUser != null) {
                                emailRemp = emailUser.replace(".", "-");
                            } else {
                                if (emailFam != null) {
                                    emailRmpF = emailFam.replace(".", "-");

                                }
                            }

                            for (DataSnapshot clickBox : dataSnapshot.getChildren()) {
                                keyValue = clickBox.getKey();

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
                                            final GenericTypeIndicator<Map<String, Object>> genericTypeIndicator = new GenericTypeIndicator<Map<String, Object>>() {
                                            };
                                            Map<String, Object> workDay = dataSnapshot.child(keyValue).child(emailRemp).
                                                    child("Work-" + emailRemp).child(actadm).getValue(genericTypeIndicator);


                                            if (workDay != null) {


                                                homeWorks = workDay.keySet().toArray(new String[0]);

                                                if (homeWorks.length > 0) {

                                                    for (int i = 0; i < homeWorks.length; i++) {
                                                        final int index = i;
                                                        if (index == 0) {

                                                            completed = dataSnapshot.child(keyValue).child(emailRemp).
                                                                    child("Work-" + emailRemp).child(actadm).child(homeWorks[i]).
                                                                    child("completed").getValue(String.class);
                                                            if (completed != null) {
                                                                if (completed.toLowerCase().contains("no")) {

                                                                    AlertDialog.Builder workFamily = new AlertDialog.Builder(getContext());

                                                                    workFamily.setMessage("¿Desea que marcar la tarea como realizada?").setTitle("Tareas realizadas");

                                                                    workFamily.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(DialogInterface dialog, int which) {
                                                                            databaseReference.child(keyValue).child(emailRemp).
                                                                                    child("Work-" + emailRemp).child(actadm).child(homeWorks[index]).
                                                                                    child("completed").setValue("si");
                                                                            chkMy.setClickable(false);
                                                                        }
                                                                    });


                                                                    workFamily.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(DialogInterface dialog, int which) {
                                                                            Toast.makeText(getContext(), "No se ha asignado la tarea", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    });


                                                                    workFamily.show();


                                                                } else {
                                                                    chkMy.setClickable(false);
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

                                                GenericTypeIndicator<Map<String, Object>> genericTypeIndicator = new GenericTypeIndicator<Map<String, Object>>() {
                                                };
                                                Map<String, Object> workDay = dataSnapshot.child(keyValue).child(emailRmpF).
                                                        child("Work-" + emailRmpF).child(actadm).getValue(genericTypeIndicator);


                                                if (workDay != null) {


                                                    homeWorks = workDay.keySet().toArray(new String[0]);

                                                    if (homeWorks.length > 0) {
                                                        for (int i = 0; i < homeWorks.length; i++) {
                                                            final int indexF = i;
                                                            if (indexF == 0) {


                                                                completed = dataSnapshot.child(keyValue).child(emailRmpF).
                                                                        child("Work-" + emailRmpF).child(actadm).child(homeWorks[i]).
                                                                        child("completed").getValue(String.class);
                                                                if (completed != null) {
                                                                    if (completed.toLowerCase().contains("no")) {


                                                                        AlertDialog.Builder workFamily = new AlertDialog.Builder(getContext());

                                                                        workFamily.setMessage("¿Desea que marcar la tarea como realizada?").setTitle("Tareas realizadas");

                                                                        workFamily.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                                                            @Override
                                                                            public void onClick(DialogInterface dialog, int which) {
                                                                                databaseReference.child(keyValue).child(emailRmpF).
                                                                                        child("Work-" + emailRmpF).child(actadm).child(homeWorks[indexF]).
                                                                                        child("completed").setValue("si");
                                                                                chkMy.setClickable(false);
                                                                            }
                                                                        });


                                                                        workFamily.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                                            @Override
                                                                            public void onClick(DialogInterface dialog, int which) {
                                                                                Toast.makeText(getContext(), "No se ha asignado la tarea", Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        });


                                                                        workFamily.show();


                                                                    } else {
                                                                        chkMy.setClickable(false);
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


                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                }
            }
        });

        chkMybis.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (chkMybis.isChecked()) {
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if (emailUser != null) {
                                emailRemp = emailUser.replace(".", "-");
                            } else {
                                if (emailFam != null) {
                                    emailRmpF = emailFam.replace(".", "-");

                                }
                            }

                            for (DataSnapshot clickBox : dataSnapshot.getChildren()) {
                                keyValue = clickBox.getKey();

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
                                                    child("Work-" + emailRemp).child(actadm).getValue(genericTypeIndicator);


                                            if (workDay != null) {


                                                homeWorks = workDay.keySet().toArray(new String[0]);

                                                if (homeWorks.length > 0) {
                                                    for (int i = 0; i < homeWorks.length; i++) {
                                                        final int index = i;
                                                        if (index > 0) {


                                                            completed = dataSnapshot.child(keyValue).child(emailRemp).
                                                                    child("Work-" + emailRemp).child(actadm).child(homeWorks[i]).
                                                                    child("completed").getValue(String.class);
                                                            if (completed != null) {
                                                                if (completed.toLowerCase().contains("no")) {

                                                                    AlertDialog.Builder workFamily = new AlertDialog.Builder(getContext());

                                                                    workFamily.setMessage("¿Desea que marcar la tarea como realizada?").setTitle("Tareas realizadas");

                                                                    workFamily.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(DialogInterface dialog, int which) {
                                                                            databaseReference.child(keyValue).child(emailRemp).
                                                                                    child("Work-" + emailRemp).child(actadm).child(homeWorks[index]).
                                                                                    child("completed").setValue("si");
                                                                            chkMybis.setClickable(false);
                                                                        }
                                                                    });


                                                                    workFamily.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(DialogInterface dialog, int which) {
                                                                            Toast.makeText(getContext(), "No se ha asignado la tarea", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    });


                                                                    workFamily.show();

                                                                } else {
                                                                    chkMybis.setClickable(false);
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

                                                GenericTypeIndicator<Map<String, Object>> genericTypeIndicator = new GenericTypeIndicator<Map<String, Object>>() {
                                                };
                                                Map<String, Object> workDay = dataSnapshot.child(keyValue).child(emailRmpF).
                                                        child("Work-" + emailRmpF).child(actadm).getValue(genericTypeIndicator);


                                                if (workDay != null) {


                                                    homeWorks = workDay.keySet().toArray(new String[0]);

                                                    if (homeWorks.length > 0) {
                                                        for (int i = 0; i < homeWorks.length; i++) {
                                                            final int indexf = i;
                                                            if (indexf > 0) {


                                                                completed = dataSnapshot.child(keyValue).child(emailRmpF).
                                                                        child("Work-" + emailRmpF).child(actadm).child(homeWorks[i]).
                                                                        child("completed").getValue(String.class);
                                                                if (completed != null) {
                                                                    if (completed.toLowerCase().contains("no")) {
                                                                        AlertDialog.Builder workFamily = new AlertDialog.Builder(getContext());

                                                                        workFamily.setMessage("¿Desea que marcar la tarea como realizada?").setTitle("Tareas realizadas");

                                                                        workFamily.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                                                            @Override
                                                                            public void onClick(DialogInterface dialog, int which) {

                                                                                databaseReference.child(keyValue).child(emailRmpF).
                                                                                        child("Work-" + emailRmpF).child(actadm).child(homeWorks[indexf]).
                                                                                        child("completed").setValue("si");
                                                                                chkMybis.setClickable(false);
                                                                            }
                                                                        });


                                                                        workFamily.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                                            @Override
                                                                            public void onClick(DialogInterface dialog, int which) {
                                                                                Toast.makeText(getContext(), "No se ha asignado la tarea", Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        });


                                                                        AlertDialog dialog = workFamily.show();

                                                                    } else {
                                                                        chkMybis.setClickable(false);
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


                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
            }
        });


        return root;
    }

    private void disableLastDate(CalendarView calendar) {
        SimpleDateFormat fechForm = new SimpleDateFormat("dd-M-yyyy", Locale.getDefault());
        Date fechaHoy = new Date();
        calendar.setMinDate(fechaHoy.getTime());
    }

    private void datePresent() {

        SimpleDateFormat fechForm = new SimpleDateFormat("dd-M-yyyy", Locale.getDefault());
        Date fechaHoy = new Date();
        actadm = fechForm.format(fechaHoy);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                extractData(dataSnapshot);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void workInCalendar() {


        if (actadm != null) {


            assert chkMy != null;
            if (chkMy.getText().equals("Dia libre") || !chkMy.getText().equals("Sin tarea") && !chkMy.isChecked()) {
                Intent calIntent = new Intent(Intent.ACTION_INSERT);
                calIntent.setType("vnd.android.cursor.item/event");
                calIntent.putExtra(CalendarContract.Events.TITLE, chkMy.getText().toString());
                calIntent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true);


                startActivity(calIntent);

                System.out.println("Pasa por el primer check");

            }
            assert chkMybis != null;
            if (chkMybis.getText().equals("Dia libre") || !chkMybis.getText().equals("Sin tarea") && !chkMybis.isChecked()) {
                Intent calIntent = new Intent(Intent.ACTION_INSERT);
                calIntent.setType("vnd.android.cursor.item/event");
                calIntent.putExtra(CalendarContract.Events.TITLE, chkMybis.getText().toString());
                calIntent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true);


                startActivity(calIntent);

                System.out.println("Pasa por el primer check");

            }

        } else {
            Toast.makeText(getContext(), "No ha seleccionado la fecha", Toast.LENGTH_SHORT).show();
        }


    }

    private void extractData(@NonNull DataSnapshot dataSnapshot) {
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
                                child("Work-" + emailRemp).child(actadm).getValue(genericTypeIndicator);


                        if (workDay == null) {
                            chkMy.setText("Dia libre");
                            chkMybis.setText("Dia libre");
                            if (chkMy.getText().toString().equals("Dia libre") | chkMy.getText().toString().equals("Dia libre")) {

                                chkMy.setClickable(false);
                                chkMy.setChecked(false);
                                chkMybis.setClickable(false);
                                chkMybis.setChecked(false);

                            } else {
                                chkMy.setClickable(false);

                                chkMybis.setClickable(false);

                            }
                        } else {


                            homeWorks = workDay.keySet().toArray(new String[0]);

                            if (homeWorks.length > 0) {
                                for (int i = 0; i < homeWorks.length; i++) {
                                    if (i == 0) {
                                        chkMy.setText(homeWorks[i]);

                                        completed = dataSnapshot.child(keyValue).child(emailRemp).
                                                child("Work-" + emailRemp).child(actadm).child(homeWorks[i]).
                                                child("completed").getValue(String.class);
                                        if (completed != null) {
                                            if (completed.toLowerCase().contains("si")) {
                                                chkMy.setChecked(true);
                                                chkMy.setClickable(false);
                                            } else {

                                                chkMy.setChecked(false);
                                                chkMy.setClickable(true);
                                            }
                                        }

                                    } else {
                                        chkMybis.setText(homeWorks[i]);
                                        completed = dataSnapshot.child(keyValue).child(emailRemp).
                                                child("Work-" + emailRemp).child(actadm).child(homeWorks[i]).
                                                child("completed").getValue(String.class);

                                        if (completed != null) {
                                            if (completed.toLowerCase().contains("si")) {
                                                chkMybis.setChecked(true);
                                                chkMybis.setClickable(false);
                                            } else {
                                                chkMybis.setChecked(false);
                                                chkMybis.setClickable(true);
                                            }
                                        }


                                    }
                                    if (homeWorks.length < 2) {
                                        chkMybis.setText("Sin tarea");
                                        if (chkMy.getText().toString().equals("Sin tarea")) {
                                            chkMy.setClickable(false);
                                            chkMy.setChecked(false);

                                        } else {
                                            chkMy.setClickable(true);
                                        }
                                        if (chkMybis.getText().toString().equals("Sin tarea")) {
                                            chkMybis.setChecked(false);
                                            chkMybis.setClickable(false);
                                        } else {
                                            chkMybis.setClickable(true);
                                        }
                                    }
                                }


                            }


                        }


                    }

                } else {
                    if (emailCompF != null) {
                        if (emailCompF.contains(emailFam)) {
                            GenericTypeIndicator<Map<String, Object>> genericTypeIndicator = new GenericTypeIndicator<Map<String, Object>>() {
                            };
                            Map<String, Object> workDay = dataSnapshot.child(keyValue).child(emailRmpF).
                                    child("Work-" + emailRmpF).child(actadm).getValue(genericTypeIndicator);

                            if (workDay == null) {
                                chkMy.setText("Dia libre");
                                chkMybis.setText("Dia libre");
                                if (chkMy.getText().toString().equals("Dia libre") | chkMy.getText().toString().equals("Dia libre")) {

                                    chkMy.setClickable(false);
                                    chkMy.setChecked(false);
                                    chkMybis.setClickable(false);
                                    chkMybis.setChecked(false);

                                } else {
                                    chkMy.setClickable(false);

                                    chkMybis.setClickable(false);

                                }
                            } else {


                                homeWorks = workDay.keySet().toArray(new String[0]);

                                if (homeWorks.length > 0) {

                                    for (int i = 0; i < homeWorks.length; i++) {
                                        if (i == 0) {
                                            chkMy.setText(homeWorks[i]);

                                            completed = dataSnapshot.child(keyValue).child(emailRmpF).
                                                    child("Work-" + emailRmpF).child(actadm).child(homeWorks[i]).
                                                    child("completed").getValue(String.class);
                                            if (completed != null) {
                                                if (completed.toLowerCase().contains("si")) {
                                                    chkMy.setChecked(true);
                                                    chkMy.setClickable(false);
                                                } else {
                                                    chkMy.setChecked(false);
                                                    chkMy.setClickable(true);
                                                }
                                            }

                                        } else {
                                            chkMybis.setText(homeWorks[i]);
                                            completed = dataSnapshot.child(keyValue).child(emailRmpF).
                                                    child("Work-" + emailRmpF).child(actadm).child(homeWorks[i]).
                                                    child("completed").getValue(String.class);

                                            if (completed != null) {
                                                if (completed.toLowerCase().contains("si")) {
                                                    chkMybis.setChecked(true);
                                                    chkMybis.setClickable(false);
                                                } else {
                                                    chkMybis.setChecked(false);
                                                    chkMybis.setClickable(true);
                                                }
                                            }


                                        }
                                        if (homeWorks.length < 2) {
                                            chkMybis.setText("Sin tarea");
                                            if (chkMy.getText().toString().equals("Sin tarea")) {
                                                chkMy.setClickable(false);
                                                chkMy.setChecked(false);

                                            } else {
                                                chkMy.setClickable(true);
                                            }
                                            if (chkMybis.getText().toString().equals("Sin tarea")) {
                                                chkMybis.setChecked(false);
                                                chkMybis.setClickable(false);
                                            } else {
                                                chkMybis.setClickable(true);
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
    }


}