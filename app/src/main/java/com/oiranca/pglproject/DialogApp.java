package com.oiranca.pglproject;

import android.app.AlertDialog;
import android.app.Dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

public class DialogApp extends DialogFragment {

    @Override
    public Dialog onCreateDialog (Bundle saveInstanceState){

        AlertDialog.Builder provicional = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        provicional.setView(inflater.inflate(R.layout.dialog, null));
        provicional.setTitle("Email del administrador").setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Toast.makeText(getContext(),"Boton Aceptar",Toast.LENGTH_LONG).show();

              /*  Intent adminIntent = new Intent(getActivity(),NavigationAdmin.class);
                startActivity(adminIntent);*/

            }
        }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getContext(),"Boton Cancelar",Toast.LENGTH_LONG).show();

               // Intent famIntent = new Intent(getActivity(),TabFamily.class);
              //  startActivity(famIntent);

            }
        });
        return provicional.create();

    }


}
