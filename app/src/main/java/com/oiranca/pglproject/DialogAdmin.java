package com.oiranca.pglproject;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class DialogAdmin extends AppCompatActivity {

    TextView title;
    Button cancel,acept;
    EditText email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_admin);
        title=(TextView)findViewById(R.id.textDialog);
        cancel=(Button)findViewById(R.id.buttonCancel);
        acept=(Button)findViewById(R.id.buttonAcep);
        email=(EditText)findViewById(R.id.editDAdmin);


    }
}
