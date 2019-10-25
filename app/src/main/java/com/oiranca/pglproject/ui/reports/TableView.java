package com.oiranca.pglproject.ui.reports;

import android.content.Context;
import android.graphics.Typeface;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

public class TableView {

    private TableLayout tableLayout;
    private Context context;
    private String[] header;
    private ArrayList<String[]> data;
    private TableRow tableRow;
    private TextView txtCell;
    private int indexC, indexR;

    private int firstColor;


    public TableView(TableLayout tableLayout, Context context) {

        this.tableLayout = tableLayout;
        this.context = context;

    }


    public void setHeader(String[] header) {
        this.header = header;
       createHeader();
    }


    public void setData(ArrayList<String[]> data) {
        this.data = data;

        createDataTable();
    }

    private void newRow() {

        tableRow = new TableRow(context);

    }

    private void newCell() {

        txtCell = new TextView(context);
        txtCell.setGravity(Gravity.CENTER);
        txtCell.setTextSize(16);


    }

    private void createHeader() {
        indexC = 0;
        newRow();

        while (indexC < header.length) {
            newCell();
            txtCell.setText(header[indexC++]);
            txtCell.setWidth(0);
            txtCell.setHeight(60);
            tableRow.addView(txtCell, newTableRowParams());

        }

        tableLayout.addView(tableRow);
    }

    private void createDataTable() {
        String info;

        for (indexR = 1; indexR <= header.length; indexR++) {
            newRow();

            for (indexC = 0; indexC < header.length; indexC++) {
                newCell();
                String[] row = data.get(indexR - 1);
                info = (indexC < row.length) ? row[indexC] : "";
                txtCell.setText(info);
                txtCell.setWidth(0);
                txtCell.setHeight(60);
                tableRow.addView(txtCell, newTableRowParams());

            }

            tableLayout.addView(tableRow);

        }


    }

    public void addItems(String[] item) {

        String info;
        data.add(item);
        indexC = 0;
        newRow();
        while (indexC < header.length) {
            newCell();
            info = (indexC < item.length) ? item[indexC++] : "";
            txtCell.setText(info);
            tableRow.addView(txtCell, newTableRowParams());
        }
        tableLayout.addView(tableRow, data.size() - 1);
        reColoring();



    }

    public void backgroundHeader(int color) {

        indexC = 0;
        newRow();
        while (indexC < header.length) {
            txtCell = getCell(0, indexC++);
            txtCell.setBackgroundColor(color);
            txtCell.setTypeface(null, Typeface.BOLD);


        }


    }


    public void backgroundData(int firstColor) {

        for (indexR = 1; indexR <= header.length; indexR++) {


            for (indexC = 0; indexC < header.length; indexC++) {
                txtCell = getCell(indexR, indexC);
                txtCell.setBackgroundColor(firstColor);


            }


        }

        this.firstColor = firstColor;


    }

    public void reColoring() {

        indexC = 0;

        while (indexC < header.length) {
            txtCell = getCell(data.size() - 1, indexC++);
            txtCell.setBackgroundColor(firstColor);

        }


    }

    public void lineColor(int color){
        indexR=0;
        while (indexR<data.size()){
            getRow(indexR++).setBackgroundColor(color);

        }

    }

    private TableRow getRow(int index) {
        return (TableRow) tableLayout.getChildAt(index);


    }

    private TextView getCell(int rowIndex, int columsIndex) {

        tableRow = getRow(rowIndex);
        return (TextView) tableRow.getChildAt(columsIndex);


    }

    private TableRow.LayoutParams newTableRowParams() {

        TableRow.LayoutParams params = new TableRow.LayoutParams();
        params.setMargins(2, 2, 2, 2);
        params.weight = 2;
        return params;

    }
}

