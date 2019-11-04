package com.oiranca.pglproject;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.oiranca.pglproject.ui.conection.BaseCreate;
import com.oiranca.pglproject.ui.conection.HelperBase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class ActivitySignUp extends AppCompatActivity {
    EditText name;
    EditText surname;
    EditText email;
    EditText pass;
    EditText rPass;
    RadioButton checkedAdmin;
    RadioButton checkedFam;
    HelperBase dbHelp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_sign_up);
        Toolbar toolbar = findViewById(R.id.toolbarSign);
        setSupportActionBar(toolbar);


        name = (EditText) findViewById(R.id.name_sign);
        surname = (EditText) findViewById(R.id.surname_sign);
        email = (EditText) findViewById(R.id.email_sign);
        pass = (EditText) findViewById(R.id.pass_sign);
        rPass = (EditText) findViewById(R.id.repeat_sign);
        checkedAdmin = (RadioButton) findViewById(R.id.radioAdmin);
        checkedFam = (RadioButton) findViewById(R.id.radioFam);


        checkedFam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent admini = new Intent(getApplicationContext(), DialogAdmin.class);
                startActivity(admini);
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
                        if (checkedAdmin.isChecked()) {
                            dbHelp = new HelperBase(getApplicationContext(), "HomeWork.db", null, 1);
                            SQLiteDatabase db = dbHelp.getWritableDatabase();
                            ContentValues values = new ContentValues();

                            values.put(BaseCreate.COLUMN_NAME, nameText);
                            values.put(BaseCreate.COLUMN_SURNAME, surnameText);
                            values.put(BaseCreate.COLUMN_MAIL, emailText);
                            values.put(BaseCreate.COLUMN_PASS, passText);
                            values.put(BaseCreate.COLUMN_RANGE, "Administrador");

                            long rId = db.insert(BaseCreate.NAME_TABLE, BaseCreate.ID, values);

                            Toast.makeText(getApplicationContext(), "Se le ha enviado E-mail de confirmación " + rId, Toast.LENGTH_LONG).show();

                            db.close();
                        } else {

                            if (checkedFam.isChecked()) {

                            } else {
                                Toast.makeText(getApplicationContext(), getString(R.string.checked), Toast.LENGTH_LONG).show();
                            }
                        }

                    }
                }
            }
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
