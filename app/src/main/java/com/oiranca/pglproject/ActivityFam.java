package com.oiranca.pglproject;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.Toast;

public class ActivityFam extends AppCompatActivity {
     String activFam = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fam);
        Toolbar toolbar = findViewById(R.id.toolbarFam);
        setSupportActionBar(toolbar);

        final CheckBox chkFirst = findViewById(R.id.checkBoxFam);
        final CheckBox chkBis = findViewById(R.id.checkBoxFamBis);


        // Recoger fecha del calendar
        CalendarView calenFam = findViewById(R.id.calendarViewFam);
        calenFam.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                String day = Integer.toString(dayOfMonth);
                String m = Integer.toString(month);
                String years = Integer.toString(year);
                activFam = day+"/"+m+"/"+years;

                chkFirst.setText(getString(R.string.first_activity)+activFam);
                chkBis.setText(getString(R.string.second_activity)+activFam);

                Toast.makeText(getApplicationContext(),activFam, Toast.LENGTH_SHORT).show();
            }
        });


        FloatingActionButton fab = findViewById(R.id.fabFam);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_admin, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_back) {

            Intent back = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(back);
        }

        if (id == R.id.action_settings) {

            Toast.makeText(getApplicationContext(), "Pendiente de configurar", Toast.LENGTH_SHORT).show();
        }
        return true;

    }
}
