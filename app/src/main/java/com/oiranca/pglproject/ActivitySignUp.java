package com.oiranca.pglproject;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class ActivitySignUp extends AppCompatActivity {
    EditText name;
    EditText surname;
    EditText email;
    EditText pass;
    EditText rPass;
    RadioGroup group;
    RadioButton checkedAdmin;
    RadioButton checkedFam;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        name = (EditText) findViewById(R.id.name_sign);
        surname = (EditText) findViewById(R.id.surname_sign);
        email = (EditText) findViewById(R.id.email_sign);
        pass = (EditText) findViewById(R.id.pass_sign);
        rPass = (EditText) findViewById(R.id.repeat_sign);
        checkedAdmin = (RadioButton) findViewById(R.id.radioAdmin);
        checkedFam = (RadioButton) findViewById(R.id.radioFam);

        FloatingActionButton backFab = findViewById(R.id.signBack);
        backFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Intent backMenu = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(backMenu);
            }
        });

        FloatingActionButton okFab = findViewById(R.id.signOk);
        okFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                correct();

            }
        });


    }

    private void correct() {

        name.setError(null);
        surname.setError(null);
        email.setError(null);
        pass.setError(null);
        rPass.setError(null);
        checkedAdmin.setError(null);


        String nameText = name.getText().toString();
        String surnameText = surname.getText().toString();
        String emailText = email.getText().toString();
        String passText = pass.getText().toString();
        String rpText = rPass.getText().toString();

        if (TextUtils.isEmpty(nameText)) {

            name.setError(getString(R.string.empty));
            name.requestFocus();
            return;

        } else {

            if (TextUtils.isEmpty(surnameText)) {

                surname.setError(getString(R.string.empty));
                surname.requestFocus();
                return;

            } else {
                if (TextUtils.isEmpty(emailText)) {

                    email.setError(getString(R.string.empty));
                    email.requestFocus();
                    return;

                } else {
                    if (TextUtils.isEmpty(passText)) {

                        pass.setError(getString(R.string.empty));
                        pass.requestFocus();
                        return;

                    } else {

                        if (TextUtils.isEmpty(rpText)) {

                            rPass.setError(getString(R.string.empty));
                            rPass.requestFocus();
                            return;
                        }
                        if (checkedAdmin.isChecked() || checkedFam.isChecked()) {
                            Intent fortgot = new Intent(getApplicationContext(), MainActivity.class);
                            Toast.makeText(getApplicationContext(), "Registrado Correctamente", Toast.LENGTH_LONG).show();
                            startActivity(fortgot);

                        } else {
                            Toast.makeText(getApplicationContext(), getString(R.string.checked), Toast.LENGTH_LONG).show();

                        }

                    }
                }
            }
        }


    }

}
