package com.oiranca.pglproject.ui.reports;


import android.graphics.Color;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import android.widget.TableLayout;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import androidx.lifecycle.ViewModelProviders;

import com.oiranca.pglproject.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class ReportsFragment extends Fragment {

    private ReportsViewModel reportsViewModel;
    TableLayout tableLayout;
    String[] header = {"Fecha", "Nombre", "Actividad"};
    ArrayList<String[]> rows = new ArrayList<>();
    TableView tablaDyn;
    public String present, dateOthers;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        reportsViewModel =
                ViewModelProviders.of(this).get(ReportsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_reports, container, false);
        tableLayout = (TableLayout) root.findViewById(R.id.tableReports);


        CalendarView calenReport = root.findViewById(R.id.calendarReport);
        calenReport.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                dateOthers = dayOfMonth + "." + month + "." + year;
                save(dateOthers);
            }
        });

        datePresent();
        tablaDyn = new TableView(tableLayout, getContext());
        tablaDyn.setHeader(header);
        tablaDyn.setData(getClients());
        tablaDyn.backgroundHeader(Color.parseColor("#0081D4"));
        tablaDyn.backgroundData(Color.parseColor("#B2D0F0"));
        tablaDyn.lineColor(Color.parseColor("#465F85"));

        return root;
    }

    private void datePresent() {
        SimpleDateFormat fechForm = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        Date fechaHoy = new Date();
        present = fechForm.format(fechaHoy);
    }

    public void save(String dateOther) {

        String[] item = new String[]{dateOther, "Hola", "Adios"};
        tablaDyn.addItems(item);
        tablaDyn.lineColor(Color.parseColor("#465F85"));

    }

    private ArrayList<String[]> getClients() {


        rows.add(new String[]{present, "Samuel", "Limpiar"});
        rows.add(new String[]{"02.01.2000", "Pedro", "Lavar"});
        rows.add(new String[]{present, "Samuel", "Limpiar"});
        rows.add(new String[]{"02.01.2000", "Pedro", "Lavar"});

        return rows;
    }


}