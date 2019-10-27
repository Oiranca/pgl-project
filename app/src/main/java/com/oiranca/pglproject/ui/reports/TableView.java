package com.oiranca.pglproject.ui.reports;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.oiranca.pglproject.R;

import java.util.ArrayList;

public class TableView {
    private TableLayout table;
    private TableRow nRow;
    private ArrayList<TableRow> rows;
    private Resources rs;
    private Context context;
    private TextView textView;
    private int indexR, indeC;


    public TableView(TableLayout table, Context context) {
        this.table = table;
        this.context = context;
        rs = this.context.getResources();
        indexR = indeC = 0;
        rows = new ArrayList<TableRow>();
    }


    public void addHead(String[] head) {


        nRow = new TableRow(context);
        indeC = head.length;

        for (int i = 0; i < head.length; i++) {
            textView = new TextView(context);
            textView.setText(head[i]);
            textView.setGravity(Gravity.CENTER_HORIZONTAL);
            textView.setTextSize(18);
            textView.setTextColor(Color.WHITE);
            textView.setBackgroundResource(R.drawable.heade_table);


            nRow.addView(textView, newTableRowParams());
        }

        table.addView(nRow);
        rows.add(nRow);

        indexR++;
    }

    public void agregarFilaTabla(ArrayList<String> elementos) {


        nRow = new TableRow(context);


        for (int i = 0; i < elementos.size(); i++) {
            textView = new TextView(context);
            textView.setText(String.valueOf(elementos.get(i)));
            textView.setGravity(Gravity.CENTER_HORIZONTAL);
            textView.setTextSize(16);
            textView.setBackgroundResource(R.drawable.cell_table);

            textView.setWidth(0);
            textView.setHeight(60);
            nRow.addView(textView, newTableRowParams());
        }

        table.addView(nRow);
        rows.add(nRow);

        indexR++;
    }

    private TableRow.LayoutParams newTableRowParams() {

        TableRow.LayoutParams params = new TableRow.LayoutParams();
        params.setMargins(1, 1, 1, 1);
        params.weight = 1;
        return params;

    }


}

