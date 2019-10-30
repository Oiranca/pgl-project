package com.oiranca.pglproject;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class ActivityForgot extends AppCompatActivity {

    EditText forgoten;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_forgot);
        Toolbar toolbar = findViewById(R.id.toolbarForgot);
        setSupportActionBar(toolbar);
        forgoten = (EditText) findViewById(R.id.textForgot);

        FloatingActionButton fab = findViewById(R.id.fabForgot);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                correct();

            }
        });


    }

    private void correct() {

        forgoten.setError(null);

        String forget = forgoten.getText().toString();

        if (TextUtils.isEmpty(forget)) {

            forgoten.setError(getString(R.string.empty));
            forgoten.requestFocus();
            return;

        } else {


            Intent fortgot = new Intent(getApplicationContext(), MainActivity.class);
            Toast.makeText(getApplicationContext(), "Se le ha enviado un password provisional", Toast.LENGTH_LONG).show();
            startActivity(fortgot);
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_back) {

            Intent back = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(back);
        }
        return true;

    }

}
