package com.oiranca.pglproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

/*Esta clase fue creada para un alerdialog personalizado
* y la usamos en el MainActivity para seleccionar si es
* administrador o familiar*/

public class DialogAdmin extends AppCompatActivity {

    TextView title;
    RadioButton admin, family;
    Button cancel, acept;
    boolean check;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_admin);
        admin = (RadioButton) findViewById(R.id.radioAdminSign);
        family = (RadioButton) findViewById(R.id.radioFamSign);
        title = (TextView) findViewById(R.id.textDialog);
        cancel = (Button) findViewById(R.id.buttonCancel);
        acept = (Button) findViewById(R.id.buttonAcep);

        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                family.setChecked(false);
            }
        });

        family.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                admin.setChecked(false);
            }
        });


        acept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (admin.isChecked()) {

                    Intent back = new Intent(getApplicationContext(), ActivitySignUp.class);
                    back.putExtra("administrator", check);
                    startActivity(back);

                } else {
                    if (family.isChecked()){

                        Intent back = new Intent(getApplicationContext(), ActivitySignUp.class);
                        check = true;
                        back.putExtra("administrator", check);
                        startActivity(back);
                    }else{
                        Toast.makeText(getApplicationContext(),"Debe elegir una opción",Toast.LENGTH_SHORT).show();

                    }


                }


            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
