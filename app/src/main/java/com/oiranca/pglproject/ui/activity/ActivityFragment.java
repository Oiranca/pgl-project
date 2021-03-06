package com.oiranca.pglproject.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.oiranca.pglproject.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class ActivityFragment extends Fragment {

    private DatabaseReference databaseReference;
    private ArrayAdapter<String> comAdp;
    private String date;
    private String famSelect;
    private String workSelect;
    private String emailRemplace;
    private String namesAd;
    private String namesFam;
    private Map<String, Object> workDay;

    /*En este frágmento vamos a seleccionar un día en el calendario, un familiar y una actividad
     * para poder asignarsela a dicho usuairo */

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        ActivityViewModel activityViewModel = ViewModelProviders.of(this).get(ActivityViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_activity, container, false);
        final Spinner spinnerFam = root.findViewById(R.id.spinnerFamily);
        final Spinner spinnerWork = root.findViewById(R.id.spinnerWork);
        final CalendarView calendarView = root.findViewById(R.id.calendarAssig);
        final FloatingActionButton floatButton = root.findViewById(R.id.floatMyAct);

        disableLastDate(calendarView);


        Intent idUser = Objects.requireNonNull(getActivity()).getIntent();
        Bundle user = idUser.getExtras();
        String emailUser;
        assert user != null;
        emailUser = user.getString("Admin");

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        if (emailUser != null) {
            databaseReference = firebaseDatabase.getReference().child("Family-" + emailUser.replace(".", "-"));
        }


        dataSpinner(spinnerFam);

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
                // Se le suma 1 al mes porque el calendarview empieza en el mes 0 que equivale a Enero
                int correction = 1 + month;


                date = dayOfMonth + "-" + correction + "-" + year;


            }
        });

        spinnerWork.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                workSelect = spinnerWork.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        floatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*Creamos un Alerdialog para saber si la tarea asignar al usuario es la correcta
                * si aceptamos dicha tarea con dicho usuario se le asignara en Firebase
                * y se pondrá como no realizada hasta que el usuario la haga*/

                AlertDialog.Builder workFamily = new AlertDialog.Builder(getContext());

                workFamily.setMessage("¿Desea que " + famSelect + " haga la terea " + workSelect + "?").setTitle("Tareas de Casa");

                final SimpleDateFormat fechForm = new SimpleDateFormat("d-M-yyyy", Locale.getDefault());
                final Date fechaHoy = new Date();

                if (date == null) {
                    date = fechForm.format(fechaHoy);
                }
                final GenericTypeIndicator<Map<String, Object>> genericTypeIndicator = new GenericTypeIndicator<Map<String, Object>>() {
                };


                workFamily.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String email;


                                for (DataSnapshot work : dataSnapshot.getChildren()) {
                                    namesAd = work.child("name").getValue(String.class);
                                    namesFam = work.child("nameF").getValue(String.class);


                                    if (namesAd != null) {
                                        if (namesAd.contains(famSelect) && !famSelect.equals("Selecciona un familiar") && !workSelect.equals("Selecciona una tarea") && date != null) {
                                            email = work.child("email").getValue(String.class);
                                            assert email != null;
                                            emailRemplace = email.replace(".", "-");

                                            workDay = dataSnapshot.child(emailRemplace).
                                                    child("Work-" + emailRemplace).child(date).getValue(genericTypeIndicator);


                                            if (workDay == null) {
                                                databaseReference.child(emailRemplace).child("Work-" + emailRemplace).child(date).child(workSelect).child("completed").setValue("no");
                                                spinnerFam.setSelection(0);
                                                spinnerWork.setSelection(0);
                                            } else {
                                                if (workDay.size() < 2) {

                                                    databaseReference.child(emailRemplace).child("Work-" + emailRemplace).child(date).child(workSelect).child("completed").setValue("no");
                                                    spinnerFam.setSelection(0);
                                                    spinnerWork.setSelection(0);

                                                } else {

                                                    Toast.makeText(getContext(), "Maximo de tareas del día asignadas", Toast.LENGTH_SHORT).show();
                                                }
                                            }


                                        }
                                        if (namesAd.isEmpty() || famSelect.equals("Selecciona un familiar") || workSelect.equals("Selecciona una tarea") || date == null) {
                                            Toast.makeText(getContext(), "Le ha faltado seleccionar algún dato", Toast.LENGTH_LONG).show();
                                        }

                                    }
                                    if (namesFam != null) {
                                        if (namesFam.contains(famSelect) && !famSelect.equals("Selecciona un familiar") && !workSelect.equals("Selecciona una tarea") && date != null) {

                                            email = work.child("emailF").getValue(String.class);
                                            if (email != null) {
                                                emailRemplace = email.replace(".", "-");

                                                workDay = dataSnapshot.child(emailRemplace).
                                                        child("Work-" + emailRemplace).child(date).getValue(genericTypeIndicator);


                                                if (workDay == null) {


                                                    databaseReference.child(emailRemplace).child("Work-" + emailRemplace).child(date).child(workSelect).child("completed").setValue("no");
                                                    spinnerFam.setSelection(0);
                                                    spinnerWork.setSelection(0);
                                                } else {
                                                    if (workDay.size() < 2) {

                                                        databaseReference.child(emailRemplace).child("Work-" + emailRemplace).child(date).child(workSelect).child("completed").setValue("no");
                                                        spinnerFam.setSelection(0);
                                                        spinnerWork.setSelection(0);
                                                    } else {
                                                        Toast.makeText(getContext(), "Maximo de tareas del día asignadas", Toast.LENGTH_SHORT).show();
                                                    }
                                                }


                                            }


                                        } else {
                                            if (namesFam.isEmpty() || famSelect.equals("Selecciona un familiar") || workSelect.equals("Selecciona una tarea") || date == null) {
                                                Toast.makeText(getContext(), "Le ha faltado seleccionar algún dato", Toast.LENGTH_LONG).show();
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
                workFamily.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        spinnerFam.setSelection(0);
                        spinnerWork.setSelection(0);
                    }
                });


                workFamily.show();
            }

        });


        return root;
    }


    //El metodo dataSpinner nos permite cargar los usuarios de la base datos correspondiente a ese administrador

    private void dataSpinner(final Spinner spinnerFam) {
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
                spinnerFam.setAdapter(comAdp);


            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void disableLastDate(CalendarView calendar) {

        Date fechaHoy = new Date();
        calendar.setMinDate(fechaHoy.getTime());
    }
}