package com.oiranca.pglproject.ui.reports;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.oiranca.pglproject.R;

public class ReportsFragment extends Fragment {

    private ReportsViewModel reportsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        reportsViewModel =
                ViewModelProviders.of(this).get(ReportsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_reports, container, false);



// Aquí pondríamos los datos de las actividades en la base de datos al pulsar el calendario


        CalendarView calenReport = root.findViewById(R.id.calendarReport);
        calenReport.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {


                Toast.makeText(getContext(),"Cargar la tabla con: "+dayOfMonth+"/"+month+"/"+year,Toast.LENGTH_SHORT).show();



            }
        });



        return root;
    }
}