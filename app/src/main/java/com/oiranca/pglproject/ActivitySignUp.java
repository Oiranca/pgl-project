package com.oiranca.pglproject;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Toast;

public class ActivitySignUp extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton backFab = findViewById(R.id.signBack);
        backFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Intent backMenu = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(backMenu);
            }
        });

        FloatingActionButton okFab = findViewById(R.id.signOk);
        okFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent fortgot = new Intent(getApplicationContext(),MainActivity.class);
                Toast.makeText(getApplicationContext(),"Registrado Correctamente",Toast.LENGTH_LONG).show();
                startActivity(fortgot);

            }
        });
    }

}
