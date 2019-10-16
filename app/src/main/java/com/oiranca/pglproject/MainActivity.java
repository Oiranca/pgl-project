package com.oiranca.pglproject;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.Navigation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import android.widget.Button;

import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button loginButton;
    TextView sign, forgot;
    EditText mail, pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mail = findViewById(R.id.plain_email);
        pass = findViewById(R.id.plain_password);


        sign = findViewById(R.id.text_sign);
        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signIntent = new Intent(getApplicationContext(), ActivitySignUp.class);
                startActivity(signIntent);

            }
        });

        forgot = findViewById(R.id.text_forgot);
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent forgotIntent = new Intent(getApplicationContext(), ActivityForgot.class);
                startActivity(forgotIntent);

            }
        });

        loginButton = findViewById(R.id.button_login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mail.getText().toString().equals("admin") & pass.getText().toString().equals("admin")) {
                    Intent admin = new Intent(getApplicationContext(), NavigationAdmin.class);

                    startActivity(admin);

                } else {

                    if (mail.getText().toString().equals("familiar") & pass.getText().toString().equals("familiar")) {
                        Intent fam = new Intent(getApplicationContext(), ActivityFam.class);
                        startActivity(fam);

                    } else {
                        Toast.makeText(getApplicationContext(), "Usuario o contraseña incorrecto", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });


    }


}
