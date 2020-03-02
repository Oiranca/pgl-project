package com.oiranca.pglproject.ui.family;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.oiranca.pglproject.R;

import java.util.ArrayList;
import java.util.Objects;


public class DeleteFamily extends Fragment {

    private DatabaseReference databaseReference;
    private ArrayAdapter<String> comAdp;
    private String famSelect;
    private String keyValue;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DeleteFamilyViewModel mViewModel = ViewModelProviders.of(this).get(DeleteFamilyViewModel.class);
        View root = inflater.inflate(R.layout.delete_family_fragment, container, false);
     ;


        final Spinner spinnerDel = root.findViewById(R.id.spinnerDelete);
        final TextView textName = root.findViewById(R.id.deleteNameF);
        final TextView textSurname = root.findViewById(R.id.deleteSurname);
        final TextView textEmailD = root.findViewById(R.id.deleteEmailF);
        final FloatingActionButton floatDelete = root.findViewById(R.id.floatDelete);

        Intent idUser = Objects.requireNonNull(getActivity()).getIntent();
        Bundle user = idUser.getExtras();
        String emailUser;
        assert user != null;
        emailUser = user.getString("Admin");

        final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        if (emailUser != null) {
            databaseReference = firebaseDatabase.getReference().child("Family-" + emailUser.replace(".", "-"));
        }


        itemSpinner(spinnerDel);

        /* Método que busca el familiar seleccionado en el spinner y lo carga en los editext que
        * los bloqueo para que no puedan modificarlos*/

        spinnerDel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                famSelect = spinnerDel.getSelectedItem().toString();

                if (!famSelect.equals("Selecciona un familiar")) {

                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot dataFamily : dataSnapshot.getChildren()) {
                                String compName = dataFamily.child("nameF").getValue(String.class);

                                if (compName != null) {
                                    if (compName.contains(famSelect)) {
                                        keyValue = dataFamily.getKey();
                                        if (keyValue!=null){
                                            String emailFamily = dataSnapshot.child(keyValue).child("emailF").getValue(String.class);
                                            String surFamily = dataSnapshot.child(keyValue).child("surnameF").getValue(String.class);
                                            textName.setText(famSelect);
                                            textSurname.setText(surFamily);
                                            textEmailD.setText(emailFamily);
                                            textName.setTextColor(Color.BLACK);
                                            textSurname.setTextColor(Color.BLACK);
                                            textEmailD.setTextColor(Color.BLACK);
                                        }

                                    }
                                }

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                    /*Una vez seleccionado el usuario a borrar le damos al boton delete y slata un alerdialog para advertinor si estamos
                    * seguros de borrarlo, al seleccionar si, lo busca y lo borrar*/
                    floatDelete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertDialog.Builder delteB = new AlertDialog.Builder(getContext());

                            delteB.setMessage("¿Desea borrar a " + famSelect + "?").setTitle("Borrando Familiar");


                            delteB.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            for (DataSnapshot deleteF : dataSnapshot.getChildren()) {
                                                String compD = deleteF.child("nameF").getValue(String.class);

                                                if (compD != null) {
                                                    if (compD.contains(famSelect)) {
                                                        keyValue = deleteF.getKey();

                                                        if (keyValue!=null){

                                                            databaseReference.child(keyValue).removeValue();
                                                            itemSpinner(spinnerDel);


                                                        }
                                                    }
                                                }

                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }


                                    });

                                }

                            });




                            delteB.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    spinnerDel.setSelection(0);

                                }
                            });

                            AlertDialog dialog = delteB.show();


                        }
                    });



                } else {
                    textName.setText("Seleccione un familiar");
                    textSurname.setText("Seleccione un familiar");
                    textEmailD.setText("Seleccione un familiar");
                    textName.setTextColor(Color.GRAY);
                    textSurname.setTextColor(Color.GRAY);
                    textEmailD.setTextColor(Color.GRAY);

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        return root;
    }

    private void itemSpinner(final Spinner spinnerDel) {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> nameFamily = new ArrayList<>();
                nameFamily.add("Selecciona un familiar");
                for (DataSnapshot name : dataSnapshot.getChildren()) {
                    String nameFm = name.child("nameF").getValue(String.class);
                    if (nameFm != null) {
                        nameFamily.add(nameFm);
                    }

                }
              comAdp = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, nameFamily);
                spinnerDel.setAdapter(comAdp);


            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
