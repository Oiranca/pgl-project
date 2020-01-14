package com.oiranca.pglproject;


import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.oiranca.pglproject.ui.entidades.Admin;
import com.oiranca.pglproject.ui.entidades.Family;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.UUID;
import java.util.Vector;

public class ActivitySignUp extends AppCompatActivity {
    EditText name;
    EditText surname;
    EditText email;
    EditText pass;
    EditText rPass;
    EditText emailAdmin;
    private int op = 1;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;


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
        emailAdmin = (EditText) findViewById(R.id.emailSignAdmin);
        option();


        FloatingActionButton okFab = findViewById(R.id.signOk);
        okFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                correct();

            }
        });


    }

    private void option() {

        Intent idAd = this.getIntent();
        Bundle admin = idAd.getExtras();
        boolean selected = admin.getBoolean("administrator");


        if (!selected) {
            emailAdmin.setVisibility(View.INVISIBLE);
            op = 0;
            Toast.makeText(getApplicationContext(), "Es Administrador", Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(getApplicationContext(), "Es Familiar", Toast.LENGTH_LONG).show();
        }
    }

    private void correct() {

        name.setError(null);
        surname.setError(null);
        email.setError(null);
        pass.setError(null);
        rPass.setError(null);
        emailAdmin.setError(null);


        String nameText = name.getText().toString();
        String surnameText = surname.getText().toString();
        final String emailText = email.getText().toString();
        String passText = pass.getText().toString();
        String rpText = rPass.getText().toString();
        final String emailAdText = emailAdmin.getText().toString();


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

                        // Create Administrator in FireBase

                        if (op == 0) {
                            emailAdmin.setVisibility(View.INVISIBLE);
                            String rpassw = rPass.getText().toString();
                            final Admin ad = new Admin();


                            ad.setIdAdm(UUID.randomUUID().toString());
                            ad.setName(nameText);
                            ad.setSurname(surnameText);
                            ad.setEmail(emailText);
                            ad.setRange("Admin");

                            if (rpassw.equals(passText)) {


                                ad.setPass(passText);
                                firebaseDatabase = FirebaseDatabase.getInstance();
                                databaseReference = firebaseDatabase.getReference();

                                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                                        String email = dataSnapshot.child("Family-" + emailText.replace(".", "-"))
                                                .child(emailText.replace(".", "-")).child("email").getValue(String.class);
                                        if (email != null) {
                                            if (email.contains(emailText)) {
                                                Toast.makeText(getApplicationContext(), "Ya existe administrador con ese email", Toast.LENGTH_SHORT).show();

                                            }
                                        } else {

                                            databaseReference.child("Family-" + emailText.replace(".", "-")).child(emailText.replace(".", "-")).setValue(ad);
                                            Toast.makeText(getApplicationContext(), "Se le ha enviado E-mail de confirmación ", Toast.LENGTH_LONG).show();
                                            backLogin();
                                        }

                                    }


                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        Log.w(null, "Failed to read value", databaseError.toException());
                                    }
                                });


                            } else {
                                Toast.makeText(getApplicationContext(), "Constraseñas diferentes ", Toast.LENGTH_LONG).show();
                            }

                        } else {

                            if (op == 1) {

                                if (emailAdText.isEmpty()) {


                                    emailAdmin.setError(getString(R.string.empty));
                                    emailAdmin.requestFocus();
                                    return;

                                } else {
                                    String rpassw = rPass.getText().toString();


                                    if (rpassw.equals(passText)) {

                                        final Family fm = new Family();

                                        fm.setIdFam(UUID.randomUUID().toString());
                                        fm.setNameF(nameText);
                                        fm.setSurnameF(surnameText);
                                        fm.setEmailF(emailText);

                                        fm.setPassF(passText);
                                        fm.setRangeF("Family");
                                        firebaseDatabase = FirebaseDatabase.getInstance();
                                        databaseReference = firebaseDatabase.getReference().child("Family-" + emailAdText.replace(".", "-"));
                                        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                                                String email = dataSnapshot.child(emailAdText.replace(".", "-")).child("email").getValue(String.class);
                                                if (email != null) {
                                                    if (email.contains(emailAdText)) {
                                                        email = dataSnapshot.child(emailText.replace(".", "-")).child("emailF").getValue(String.class);
                                                        if (email != null) {
                                                            if (email.contains(emailText)) {
                                                                Toast.makeText(getApplicationContext(), "Familiar ya existe ", Toast.LENGTH_LONG).show();
                                                            }
                                                        }else {
                                                            databaseReference.child(emailText.replace(".", "-")).setValue(fm);
                                                            Toast.makeText(getApplicationContext(), "Se le ha enviado E-mail de confirmación ", Toast.LENGTH_LONG).show();
                                                            backLogin();
                                                        }
                                                    }
                                                } else {


                                                    Toast.makeText(getApplicationContext(), "El administrador no existe ", Toast.LENGTH_LONG).show();

                                                }



                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                                Log.w(null, "Failed to read value", databaseError.toException());
                                            }
                                        });


                                    } else {
                                        Toast.makeText(getApplicationContext(), "Constraseñas diferentes ", Toast.LENGTH_LONG).show();
                                    }
                                }


                            }
                        }

                    }
                }
            }
        }

        clean();
    }

    private void backLogin() {
        Intent login = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(login);
    }

    private void clean() {
        name.setText("");
        surname.setText("");
        email.setText("");
        pass.setText("");
        rPass.setText("");
        emailAdmin.setText("");
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

            backLogin();
        }
        return true;

    }


}
