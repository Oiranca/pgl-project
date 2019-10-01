package com.oiranca.pglproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity{

    Button loginButton;
    TextView sign, forgot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    sign = (TextView) findViewById(R.id.text_sign);
    sign.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent signIntent = new Intent(getApplicationContext(),ActivitySignUp.class);
            startActivity(signIntent);


        }
    });

    forgot = (TextView)findViewById(R.id.text_forgot);
    forgot.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent forgotIntent = new Intent(getApplicationContext(),ActivityForgot.class);
            startActivity(forgotIntent);
        }
    });

    loginButton=(Button)findViewById(R.id.button_login);
    loginButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FragmentManager provicionalFrag = getSupportFragmentManager();
            DialogApp alerta = new DialogApp();
            alerta.show(provicionalFrag,"Alerta");
        }
    });





    }



}
