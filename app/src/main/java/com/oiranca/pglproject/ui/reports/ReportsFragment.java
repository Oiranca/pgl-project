package com.oiranca.pglproject.ui.reports;

import android.app.DatePickerDialog;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.media.RatingCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;

import com.oiranca.pglproject.R;

import java.util.ArrayList;

public class ReportsFragment extends Fragment {

    private ReportsViewModel reportsViewModel;
    TableLayout tableLayout;
    String[] header = {"Fecha", "Nombre", "Actividad"};
    ArrayList<String[]> rows = new ArrayList<>();


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        reportsViewModel =
                ViewModelProviders.of(this).get(ReportsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_reports, container, false);
        tableLayout = (TableLayout) root.findViewById(R.id.tableReports);

        TableView tablaDyn = new TableView(tableLayout, getContext());
        tablaDyn.setHeader(header);
        tablaDyn.setData(getClients());
        tablaDyn.backgroundHeader(Color.parseColor("#0081D4"));
        tablaDyn.backgroundData(Color.parseColor("#B2D0F0"));


// Aquí pondríamos los datos de las actividades en la base de datos al pulsar el calendario


        CalendarView calenReport = root.findViewById(R.id.calendarReport);

        calenReport.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {


                Toast.makeText(getContext(), "Cargar la tabla con: " + dayOfMonth + "/" + month + "/" + year, Toast.LENGTH_SHORT).show();


            }
        });


        return root;
    }



    private ArrayList<String[]> getClients() {

        rows.add(new String[]{"01.01.2000", "Samuel", "Limpiar"});
        rows.add(new String[]{"02.01.2000", "Pedro", "Lavar"});
        rows.add(new String[]{"03.01.2000", "Juan", "Perro"});
        rows.add(new String[]{"04.01.2000", "Jose", "Secar"});
        return rows;
    }

}