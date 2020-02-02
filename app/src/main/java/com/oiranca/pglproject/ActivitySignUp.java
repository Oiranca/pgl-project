package com.oiranca.pglproject;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import com.google.firebase.database.ValueEventListener;
import com.oiranca.pglproject.ui.entidades.Admin;
import com.oiranca.pglproject.ui.entidades.Family;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import java.util.UUID;


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
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int CAMERA_PERMISSION = 100;
    static final int WRITE_PERMISSION = 101;
    static final int READ_PERMISSION = 102;


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
        final FloatingActionButton cameraFloat = (FloatingActionButton) findViewById(R.id.floatingCamera);
        option();


        FloatingActionButton okFab = findViewById(R.id.signOk);
        okFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                correct();

            }
        });


        cameraFloat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCameraHardware(getApplicationContext());
                checkPermission();


            }
        });

    }

    private void option() {

        Intent idAd = this.getIntent();
        Bundle admin = idAd.getExtras();
        assert admin != null;
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
                                            if (email.equals(emailText)) {
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
                                                    if (email.contains(emailAdText) && !email.equals(emailText)) {
                                                        email = dataSnapshot.child(emailText.replace(".", "-")).child("emailF").getValue(String.class);
                                                        if (email != null) {
                                                            if (email.equals(emailText)) {
                                                                Toast.makeText(getApplicationContext(), "Familiar ya existe ", Toast.LENGTH_LONG).show();
                                                            }
                                                        } else {
                                                            databaseReference.child(emailText.replace(".", "-")).setValue(fm);
                                                            Toast.makeText(getApplicationContext(), "Se le ha enviado E-mail de confirmación ", Toast.LENGTH_LONG).show();
                                                            backLogin();
                                                        }
                                                    } else {
                                                        Toast.makeText(getApplicationContext(), "Email del familiar y del administrador son el mismo", Toast.LENGTH_LONG).show();

                                                    }
                                                } else {


                                                    Toast.makeText(getApplicationContext(), "El administrador no existe o email erroneo", Toast.LENGTH_LONG).show();

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

    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            System.out.println("Detecta las Cámaras");
            return true;
        } else {
            System.out.println("No detecta las cmámaras");
            return false;
        }
    }

    public void checkPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!=PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},CAMERA_PERMISSION);


        }

        if (ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},WRITE_PERMISSION);

        }else{
            takePhoto();
        }


    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CAMERA_PERMISSION: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    checkPermission();

                } else {
                    Toast.makeText(getApplicationContext(), "Permiso camara denegado", Toast.LENGTH_SHORT).show();
                }

            }
            break;

            case WRITE_PERMISSION: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    takePhoto();

                } else {
                    Toast.makeText(getApplicationContext(), "Permiso guardado denegado", Toast.LENGTH_SHORT).show();
                }

            }
            break;


        }
    }

    private void takePhoto() {

        if (ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            Toast.makeText(getApplicationContext(), "Permiso lectura denegado", Toast.LENGTH_SHORT).show();

        }else{
            System.out.println("Permiso de lectura garantizado");

            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }


    }


}
