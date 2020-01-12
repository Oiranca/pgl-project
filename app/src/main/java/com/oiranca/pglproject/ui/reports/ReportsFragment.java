package com.oiranca.pglproject.ui.reports;


import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import android.widget.TableLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import androidx.lifecycle.ViewModelProviders;

import com.oiranca.pglproject.R;

import java.util.ArrayList;


public class ReportsFragment extends Fragment {

    private ReportsViewModel reportsViewModel;
    private TableView tablaDyn, tabHead;
    private String[] header = {"Fecha", "Nombre", "Actividad", "Realizado"};
    private ArrayList<String> item;
    private String[] datos;
    private String date;


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


        CalendarView calenReport = root.findViewById(R.id.calendarReport);
        calenReport.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                int correction = 1 + month;
                date = dayOfMonth + "." + correction + "." + year;

                item = new ArrayList<String>();
                item.add(date);
                item.add("Casilla [" + "i" + ", 0]");
                item.add("Casilla.. [" + "i" + ", 1]");
                item.add("Casilla.. [" + "i" + ", 2]");


                tablaDyn.addFile(item);
            }
        });


        return root;
    }


}